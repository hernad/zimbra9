/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.zimbra.common.util.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipException;

/**
 * Reimplementation of {@link java.util.zip.ZipOutputStream
 * java.util.zip.ZipOutputStream} that does handle the extended
 * functionality of this package, especially internal/external file
 * attributes and extra fields with different layouts for local file
 * data and central directory entries.
 *
 * <p>This class will try to use {@link java.io.RandomAccessFile
 * RandomAccessFile} when you know that the output is going to go to a
 * file.</p>
 *
 * <p>If RandomAccessFile cannot be used, this implementation will use
 * a Data Descriptor to store size and CRC information for {@link
 * #DEFLATED DEFLATED} entries, this means, you don't need to
 * calculate them yourself.  Unfortunately this is not possible for
 * the {@link #STORED STORED} method, here setting the CRC and
 * uncompressed size information is required before {@link
 * #putNextEntry putNextEntry} can be called.</p>
 *
 */
public class ZipOutputStream extends FilterOutputStream {

    /**
     * Compression method for deflated entries.
     *
     * @since 1.1
     */
    public static final int DEFLATED = java.util.zip.ZipEntry.DEFLATED;

    /**
     * Default compression level for deflated entries.
     *
     * @since Ant 1.7
     */
    public static final int DEFAULT_COMPRESSION = Deflater.DEFAULT_COMPRESSION;

    /**
     * Compression method for stored entries.
     *
     * @since 1.1
     */
    public static final int STORED = java.util.zip.ZipEntry.STORED;

    /**
     * Current entry.
     *
     * @since 1.1
     */
    private ZipEntry entry;

    /**
     * The file comment.
     *
     * @since 1.1
     */
    private String comment = "";

    /**
     * Compression level for next entry.
     *
     * @since 1.1
     */
    private int level = DEFAULT_COMPRESSION;

    /**
     * Has the compression level changed when compared to the last
     * entry?
     *
     * @since 1.5
     */
    private boolean hasCompressionLevelChanged = false;

    /**
     * Default compression method for next entry.
     *
     * @since 1.1
     */
    private int method = java.util.zip.ZipEntry.DEFLATED;

    /**
     * Number of entries written so far.
     */
    private long numEntries = 0;

    /**
     * CRC instance to avoid parsing DEFLATED data twice.
     *
     * @since 1.1
     */
    private CRC32 crc = new CRC32();

    /**
     * Count the bytes written to out.
     *
     * @since 1.1
     */
    private long written = 0;

    /**
     * Data for local header data
     *
     * @since 1.1
     */
    private long dataStart = 0;

    /**
     * Offset for CRC entry in the local file header data for the
     * current entry starts here.
     *
     * @since 1.15
     */
    private long localDataStart = 0;

    /**
     * Start of central directory.
     *
     * @since 1.1
     */
    private long cdOffset = 0;

    /**
     * Start of Zip64 end of central directory record
     */
    private long z64eocdOffset = 0;

    /**
     * Length of central directory.
     *
     * @since 1.1
     */
    private long cdLength = 0;

    private boolean forceZip64;

    /**
     * Helper, a 0 as ZipShort.
     *
     * @since 1.1
     */
    private static final byte[] ZERO = {0, 0};

    /**
     * Helper, a 0 as ZipLong.
     *
     * @since 1.1
     */
    private static final byte[] LZERO = {0, 0, 0, 0};

    /**
     * Holds the offset of the LFH start for the current entry.
     */
    private long lfhOffset = 0;

    /**
     * The encoding to use for filenames and the file comment.
     *
     * <p>For a list of possible values see <a
     * href="http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html">http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</a>.
     * Defaults to the platform's default character encoding.</p>
     *
     * @since 1.3
     */
    private String encoding = null;
    private boolean usingUtf8 = false;

    // CheckStyle:VisibilityModifier OFF - bc

    /**
     * This Deflater object is used for output.
     *
     * <p>This attribute is only protected to provide a level of API
     * backwards compatibility.  This class used to extend {@link
     * java.util.zip.DeflaterOutputStream DeflaterOutputStream} up to
     * Revision 1.13.</p>
     *
     * @since 1.14
     */
    protected Deflater def = new Deflater(level, true);

    /**
     * This buffer servers as a Deflater.
     *
     * <p>This attribute is only protected to provide a level of API
     * backwards compatibility.  This class used to extend {@link
     * java.util.zip.DeflaterOutputStream DeflaterOutputStream} up to
     * Revision 1.13.</p>
     *
     * @since 1.14
     */
    protected byte[] buf = new byte[512];

    // CheckStyle:VisibilityModifier ON

    /**
     * Optional random access output.
     *
     * @since 1.14
     */
    private RandomAccessFile raf = null;

    /**
     * Temporary file for building directory so we don't have to bloat memory.
    */
    private File cdFile = null;
    private RandomAccessFile cdRaf = null;

    private boolean closed = false;

    /**
     * Creates a new ZIP OutputStream filtering the underlying stream.
     * @param out the outputstream to zip
     * @since 1.1
     * @throws IOException on error
     */
    public ZipOutputStream(OutputStream out) throws IOException {
        this(out, null);
    }

    /**
     * Creates a new ZIP OutputStream filtering the underlying stream.
     * @param out the outputstream to zip
     * @param tmpFileDir directory in which temporary file for central directory
     *                   data is to be created
     * @throws IOException on error
     */
    public ZipOutputStream(OutputStream out, File tmpFileDir) throws IOException {
        super(out);
        openDirTmpFile(tmpFileDir);
    }

    /**
     * Creates a new ZIP OutputStream writing to a File.  Will use
     * random access if possible.
     * @param file the file to zip to
     * @since 1.14
     * @throws IOException on error
     */
    public ZipOutputStream(File file) throws IOException {
        this(file, null);
    }

    /**
     * Creates a new ZIP OutputStream writing to a File.  Will use
     * random access if possible.
     * @param file the file to zip to
     * @param tmpFileDir directory in which temporary file for central directory
     *                   data is to be created
     * @throws IOException on error
     */
    public ZipOutputStream(File file, File tmpFileDir) throws IOException {
        super(null);
        openDirTmpFile(tmpFileDir);
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.setLength(0);
        } catch (IOException e) {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException inner) {
                    // ignore
                }
                raf = null;
            }
            out = new FileOutputStream(file);
        }
    }

    public void forceZip64(boolean force) {
        forceZip64 = force;
    }

    /**
     * This method indicates whether this archive is writing to a seekable stream (i.e., to a random
     * access file).
     *
     * <p>For seekable streams, you don't need to calculate the CRC or
     * uncompressed size for {@link #STORED} entries before
     * invoking {@link #putNextEntry}.
     * @return true if seekable
     * @since 1.17
     */
    public boolean isSeekable() {
        return raf != null;
    }

    /**
     * The encoding to use for filenames and the file comment.
     *
     * <p>For a list of possible values see <a
     * href="http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html">http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</a>.
     * Defaults to the platform's default character encoding.</p>
     * @param encoding the encoding value
     * @since 1.3
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
        usingUtf8 = encoding != null && encoding.toLowerCase().equals("utf-8");
    }

    /**
     * The encoding to use for filenames and the file comment.
     *
     * @return null if using the platform's default character encoding.
     *
     * @since 1.3
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Finishes writing the contents and closes this as well as the
     * underlying stream.
     *
     * @since 1.1
     * @throws IOException on error
     */
    public void finish() throws IOException {
        closeEntry();
        cdOffset = written;
        writeOutDirTmpFile();
        cdLength = written - cdOffset;
        if (forceZip64 || numEntries >= 0xFFFF || cdOffset >= 0xFFFFFFFFL || cdLength >= 0xFFFFFFFFL) {
            z64eocdOffset = written;
            writeZip64CentralDirectoryEnd();
            writeZip64CentralDirectoryEndLocator();
        }

        writeCentralDirectoryEnd();
    }

    /**
     * Writes all necessary data for this entry.
     *
     * @since 1.1
     * @throws IOException on error
     */
    public void closeEntry() throws IOException {
        if (entry == null) {
            return;
        }

        long realCrc = crc.getValue();
        crc.reset();

        if (entry.getMethod() == DEFLATED) {
            def.finish();
            while (!def.finished()) {
                deflate();
            }

            entry.setSize(adjustToLong(def.getTotalIn()));
            entry.setCompressedSize(adjustToLong(def.getTotalOut()));
            entry.setCrc(realCrc);

            def.reset();

            written += entry.getCompressedSize();
        } else if (raf == null) {
            if (entry.getCrc() != realCrc) {
                throw new ZipException("bad CRC checksum for entry "
                                       + entry.getName() + ": "
                                       + Long.toHexString(entry.getCrc())
                                       + " instead of "
                                       + Long.toHexString(realCrc));
            }

            if (entry.getSize() != written - dataStart) {
                throw new ZipException("bad size for entry "
                                       + entry.getName() + ": "
                                       + entry.getSize()
                                       + " instead of "
                                       + (written - dataStart));
            }
        } else { /* method is STORED and we used RandomAccessFile */
            long size = written - dataStart;

            entry.setSize(size);
            entry.setCompressedSize(size);
            entry.setCrc(realCrc);
        }

        // If random access output, write the local file header containing
        // the correct CRC and compressed/uncompressed sizes
        if (raf != null) {
            long save = raf.getFilePointer();

            raf.seek(localDataStart);
            writeOut(ZipLong.getBytes(entry.getCrc()));
            writeOut(ZipLong.getBytes(entry.getCompressedSize()));
            writeOut(ZipLong.getBytes(entry.getSize()));
            raf.seek(save);
        }

        writeDataDescriptor(entry);
        writeCentralFileHeader(entry);
        numEntries++;
        entry = null;
    }

    /**
     * Begin writing next entry.
     * @param ze the entry to write
     * @since 1.1
     * @throws IOException on error
     */
    public void putNextEntry(ZipEntry ze) throws IOException {
        closeEntry();

        entry = ze;

        if (entry.getMethod() == -1) { // not specified
            entry.setMethod(method);
        }

        if (entry.getTime() == -1) { // not specified
            entry.setTime(System.currentTimeMillis());
        }

        // Size/CRC not required if RandomAccessFile is used
        if (entry.getMethod() == STORED && raf == null) {
            if (entry.getSize() == -1) {
                throw new ZipException("uncompressed size is required for"
                                       + " STORED method when not writing to a"
                                       + " file");
            }
            if (entry.getCrc() == -1) {
                throw new ZipException("crc checksum is required for STORED"
                                       + " method when not writing to a file");
            }
            entry.setCompressedSize(entry.getSize());
        }

        if (entry.getMethod() == DEFLATED && hasCompressionLevelChanged) {
            def.setLevel(level);
            hasCompressionLevelChanged = false;
        }
        writeLocalFileHeader(entry);
    }

    /**
     * Set the file comment.
     * @param comment the comment
     * @since 1.1
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the compression level for subsequent entries.
     *
     * <p>Default is Deflater.DEFAULT_COMPRESSION.</p>
     * @param level the compression level.
     * @throws IllegalArgumentException if an invalid compression level is specified.
     * @since 1.1
     */
    public void setLevel(int level) {
        if (level < Deflater.DEFAULT_COMPRESSION
            || level > Deflater.BEST_COMPRESSION) {
            throw new IllegalArgumentException(
                "Invalid compression level: " + level);
        }
        hasCompressionLevelChanged = (this.level != level);
        this.level = level;
    }

    /**
     * Sets the default compression method for subsequent entries.
     *
     * <p>Default is DEFLATED.</p>
     * @param method an <code>int</code> from java.util.zip.ZipEntry
     * @since 1.1
     */
    public void setMethod(int method) {
        this.method = method;
    }

    /**
     * Writes bytes to ZIP entry.
     * @param b the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @throws IOException on error
     */
    public void write(byte[] b, int offset, int length) throws IOException {
        if (entry.getMethod() == DEFLATED) {
            if (length > 0) {
                if (!def.finished()) {
                    def.setInput(b, offset, length);
                    while (!def.needsInput()) {
                        deflate();
                    }
                }
            }
        } else {
            writeOut(b, offset, length);
            written += length;
        }
        crc.update(b, offset, length);
    }

    /**
     * Writes a single byte to ZIP entry.
     *
     * <p>Delegates to the three arg method.</p>
     * @param b the byte to write
     * @since 1.14
     * @throws IOException on error
     */
    public void write(int b) throws IOException {
        byte[] buff = new byte[1];
        buff[0] = (byte) (b & 0xff);
        write(buff, 0, 1);
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with the stream.
     *
     * @exception  IOException  if an I/O error occurs.
     * @since 1.14
     */
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            try {
                finish();
                if (raf != null) {
                    raf.close();
                }
                if (out != null) {
                    out.close();
                }
            } finally {
                closeDirTmpFile();
            }
        }
    }

    /**
     * Flushes this output stream and forces any buffered output bytes
     * to be written out to the stream.
     *
     * @exception  IOException  if an I/O error occurs.
     * @since 1.14
     */
    public void flush() throws IOException {
        if (out != null) {
            out.flush();
        }
    }

    /*
     * Various ZIP constants
     */
    /**
     * local file header signature
     *
     * @since 1.1
     */
    protected static final byte[] LFH_SIG = ZipLong.getBytes(0X04034B50L);
    /**
     * data descriptor signature
     *
     * @since 1.1
     */
    protected static final byte[] DD_SIG = ZipLong.getBytes(0X08074B50L);
    /**
     * central file header signature
     *
     * @since 1.1
     */
    protected static final byte[] CFH_SIG = ZipLong.getBytes(0X02014B50L);
    /**
     * end of central dir signature
     *
     * @since 1.1
     */
    protected static final byte[] EOCD_SIG = ZipLong.getBytes(0X06054B50L);
    /**
     * Zip64 end of central directory record signature
     */
    protected static final byte[] ZIP64_EOCD_SIG = ZipLong.getBytes(0X06064B50L);
    /**
     * Zip64 end of central directory locator signature
     */
    protected static final byte[] ZIP64_EOCD_LOCATOR_SIG = ZipLong.getBytes(0X07064B50L);
    /**
     * minimum length of end of the central directory record
     */
    protected static final int MIN_EOCD_SIZE = 22;

    /**
     * Writes next block of compressed data to the output stream.
     * @throws IOException on error
     *
     * @since 1.14
     */
    protected final void deflate() throws IOException {
        int len = def.deflate(buf, 0, buf.length);
        if (len > 0) {
            writeOut(buf, 0, len);
        }
    }

    /**
     * Writes the local file header entry
     * @param ze the entry to write
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeLocalFileHeader(ZipEntry ze) throws IOException {
        lfhOffset = written;

        writeOut(LFH_SIG);
        written += 4;

        //store method in local variable to prevent multiple method calls
        final int zipMethod = ze.getMethod();

        long entrySize = ze.getSize();
        long entryCompressedSize = ze.getCompressedSize();
        boolean useZip64 = entrySize >= 0xFFFFFFFFL || entryCompressedSize >= 0xFFFFFFFFL;

        // version needed to extract
        // general purpose bit flag
        if (zipMethod == DEFLATED && raf == null) {
            // requires version 2 as we are going to store length info
            // in the data descriptor
            writeOut(ZipShort.getBytes(useZip64 ? 45 : 20));

            // bit3 set to signal, we use a data descriptor
            // bit11 set to signal UTF-8 filename and comment
            int flags = usingUtf8 ? 0x0808 : 0x0008;
            writeOut(ZipShort.getBytes(flags));
        } else {
            writeOut(ZipShort.getBytes(useZip64 ? 45 : 10));
            int flags = usingUtf8 ? 0x0800 : 0;
            writeOut(ZipShort.getBytes(flags));
        }
        written += 4;

        // compression method
        writeOut(ZipShort.getBytes(zipMethod));
        written += 2;

        // last mod. time and date
        writeOut(toDosTime(ze.getTime()));
        written += 4;

        // CRC
        // compressed length
        // uncompressed length
        localDataStart = written;
        if (zipMethod == DEFLATED && raf == null) {
            writeOut(LZERO);
            writeOut(LZERO);
            writeOut(LZERO);
        } else {
            writeOut(ZipLong.getBytes(ze.getCrc()));
            byte s[] = ZipLong.getBytes(entrySize < 0xFFFFFFFFL ? entrySize : 0xFFFFFFFFL);
            writeOut(s);
            writeOut(s);
        }
        written += 12;

        // file name length
        byte[] name = getBytes(ze.getName());
        writeOut(ZipShort.getBytes(name.length));
        written += 2;

        // extra field length
        if (useZip64 && entrySize != -1 && entryCompressedSize != -1) {
            Zip64ExtraField extraZip64 = new Zip64ExtraField();
            extraZip64.setUncompressedSize(entrySize);
            extraZip64.setCompressedSize(entryCompressedSize);
            ze.addExtraField(extraZip64);
        }
        byte[] extra = ze.getLocalFileDataExtra();
        writeOut(ZipShort.getBytes(extra.length));
        written += 2;

        // file name
        writeOut(name);
        written += name.length;

        // extra field
        writeOut(extra);
        written += extra.length;

        dataStart = written;
    }

    /**
     * Writes the data descriptor entry.
     * @param ze the entry to write
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeDataDescriptor(ZipEntry ze) throws IOException {
        if (ze.getMethod() != DEFLATED || raf != null) {
            return;
        }
        writeOut(DD_SIG);
        writeOut(ZipLong.getBytes(entry.getCrc()));
        writeOut(ZipLong.getBytes(entry.getCompressedSize()));
        writeOut(ZipLong.getBytes(entry.getSize()));
        written += 16;
    }

    /**
     * Writes the central file header entry.
     * @param ze the entry to write
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeCentralFileHeader(ZipEntry ze) throws IOException {
        cdRaf.write(CFH_SIG);

        // version made by
        cdRaf.write(ZipShort.getBytes((ze.getPlatform() << 8) | 45));

        long entrySize = ze.getSize();
        long entryCompressedSize = ze.getCompressedSize();
        boolean useZip64 =
            forceZip64 || entrySize >= 0xFFFFFFFFL || entryCompressedSize >= 0xFFFFFFFFL || lfhOffset >= 0xFFFFFFFFL;

        // version needed to extract
        // general purpose bit flag
        if (ze.getMethod() == DEFLATED && raf == null) {
            // requires version 2 as we are going to store length info
            // in the data descriptor
            cdRaf.write(ZipShort.getBytes(useZip64 ? 45 : 20));

            // bit3 set to signal, we use a data descriptor
            // bit11 set to signal UTF-8 filename and comment
            int flags = usingUtf8 ? 0x0808 : 0x0008;
            cdRaf.write(ZipShort.getBytes(flags));
        } else {
            cdRaf.write(ZipShort.getBytes(useZip64 ? 45 : 10));
            int flags = usingUtf8 ? 0x0800 : 0;
            cdRaf.write(ZipShort.getBytes(flags));
        }

        // compression method
        cdRaf.write(ZipShort.getBytes(ze.getMethod()));

        // last mod. time and date
        cdRaf.write(toDosTime(ze.getTime()));

        // CRC
        // compressed length
        // uncompressed length
        cdRaf.write(ZipLong.getBytes(ze.getCrc()));
        cdRaf.write(ZipLong.getBytes(entryCompressedSize < 0xFFFFFFFFL && !forceZip64 ? entryCompressedSize : 0xFFFFFFFFL));
        cdRaf.write(ZipLong.getBytes(entrySize < 0xFFFFFFFFL && !forceZip64 ? entrySize : 0xFFFFFFFFL));

        // file name length
        byte[] name = getBytes(ze.getName());
        cdRaf.write(ZipShort.getBytes(name.length));

        // extra field length
        if (useZip64) {
            Zip64ExtraField extraZip64 = new Zip64ExtraField();
            if (entrySize >= 0xFFFFFFFFL || forceZip64)
                extraZip64.setUncompressedSize(entrySize);
            if (entryCompressedSize >= 0xFFFFFFFFL || forceZip64)
                extraZip64.setCompressedSize(entryCompressedSize);
            if (lfhOffset >= 0xFFFFFFFFL || forceZip64)
                extraZip64.setLocalHeaderOffset(lfhOffset);
            ze.addExtraField(extraZip64);
        }
        byte[] extra = ze.getCentralDirectoryExtra();
        cdRaf.write(ZipShort.getBytes(extra.length));

        // file comment length
        String comm = ze.getComment();
        if (comm == null) {
            comm = "";
        }
        byte[] commentB = getBytes(comm);
        cdRaf.write(ZipShort.getBytes(commentB.length));

        // disk number start
        cdRaf.write(ZERO);

        // internal file attributes
        cdRaf.write(ZipShort.getBytes(ze.getInternalAttributes()));

        // external file attributes
        cdRaf.write(ZipLong.getBytes(ze.getExternalAttributes()));

        // relative offset of LFH
        cdRaf.write(ZipLong.getBytes(lfhOffset < 0xFFFFFFFFL && !forceZip64 ? lfhOffset : 0xFFFFFFFFL));

        // file name
        cdRaf.write(name);

        // extra field
        cdRaf.write(extra);

        // file comment
        cdRaf.write(commentB);
    }

    /**
     * Writes the Zip64 end of central directory record 
     */
    protected void writeZip64CentralDirectoryEnd() throws IOException {
        writeOut(ZIP64_EOCD_SIG);
        writeOut(ZipLong8.getBytes(44));  // size = 2 + 2 + 4 + 4 + 8 + 8 + 8 + 8
        writeOut(ZipShort.getBytes(45));  // version made by
        writeOut(ZipShort.getBytes(45));  // version needed to extract
        writeOut(LZERO);                  // disk number (4-byte 0)
        writeOut(LZERO);                  // disk number with the start of central directory (4-byte 0)
        byte[] numEntriesBytes = ZipLong8.getBytes(numEntries);
        writeOut(numEntriesBytes);        // number of entries in central directory on this disk
        writeOut(numEntriesBytes);        // total number of entries in central directory
        writeOut(ZipLong8.getBytes(cdLength));  // size of central directory
        writeOut(ZipLong8.getBytes(cdOffset));  // offset of central directory
    }

    /**
     * Writes the Zip64 end of central directory locator
     */
    protected void writeZip64CentralDirectoryEndLocator() throws IOException {
        writeOut(ZIP64_EOCD_LOCATOR_SIG);
        writeOut(LZERO);  // disk number containing start of zip64 end of central directory record
        writeOut(ZipLong8.getBytes(z64eocdOffset));  // offset of zip64 end of central directory record
        writeOut(ZipLong.getBytes(1));  // total number of disks
    }

    /**
     * Writes the &quot;End of central dir record&quot;.
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeCentralDirectoryEnd() throws IOException {
        writeOut(EOCD_SIG);

        // disk numbers
        writeOut(ZERO);
        writeOut(ZERO);

        // number of entries
        if (numEntries < 0xFFFF && !forceZip64) {
            byte[] num = ZipShort.getBytes((int) numEntries);
            writeOut(num);
            writeOut(num);
        } else {
            byte[] ffff = ZipShort.getBytes(0xFFFF);
            writeOut(ffff);
            writeOut(ffff);
        }

        // length and location of CD
        writeOut(ZipLong.getBytes(cdLength < 0xFFFFFFFFL && !forceZip64 ? cdLength : 0xFFFFFFFFL));
        writeOut(ZipLong.getBytes(cdOffset < 0xFFFFFFFFL && !forceZip64 ? cdOffset : 0xFFFFFFFFL));

        // ZIP file comment
        byte[] data = getBytes(comment);
        writeOut(ZipShort.getBytes(data.length));
        if (data.length > 0)
            writeOut(data);
    }

    /**
     * Smallest date/time ZIP can handle.
     *
     * @since 1.1
     */
    private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(0x00002100L);

    /**
     * Convert a Date object to a DOS date/time field.
     * @param time the <code>Date</code> to convert
     * @return the date as a <code>ZipLong</code>
     * @since 1.1
     */
    protected static ZipLong toDosTime(Date time) {
        return new ZipLong(toDosTime(time.getTime()));
    }

    /**
     * Convert a Date object to a DOS date/time field.
     *
     * <p>Stolen from InfoZip's <code>fileio.c</code></p>
     * @param t number of milliseconds since the epoch
     * @return the date as a byte array
     * @since 1.26
     */
    @SuppressWarnings("deprecation")
    protected static byte[] toDosTime(long t) {
        Date time = new Date(t);
        int year = time.getYear() + 1900;
        if (year < 1980) {
            return DOS_TIME_MIN;
        }
        int month = time.getMonth() + 1;
        long value =  ((year - 1980) << 25)
            |         (month << 21)
            |         (time.getDate() << 16)
            |         (time.getHours() << 11)
            |         (time.getMinutes() << 5)
            |         (time.getSeconds() >> 1);
        return ZipLong.getBytes(value);
    }

    /**
     * Retrieve the bytes for the given String in the encoding set for
     * this Stream.
     * @param name the string to get bytes from
     * @return the bytes as a byte array
     * @throws ZipException on error
     *
     * @since 1.3
     */
    protected byte[] getBytes(String name) throws ZipException {
        if (encoding == null) {
            return name.getBytes();
        } else {
            try {
                return name.getBytes(encoding);
            } catch (UnsupportedEncodingException uee) {
                throw new ZipException(uee.getMessage());
            }
        }
    }

    /**
     * Write bytes to output or random access file.
     * @param data the byte array to write
     * @throws IOException on error
     *
     * @since 1.14
     */
    protected final void writeOut(byte[] data) throws IOException {
        writeOut(data, 0, data.length);
    }

    /**
     * Write bytes to output or random access file.
     * @param data the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @throws IOException on error
     *
     * @since 1.14
     */
    protected final void writeOut(byte[] data, int offset, int length)
        throws IOException {
        if (raf != null) {
            raf.write(data, offset, length);
        } else {
            out.write(data, offset, length);
        }
    }

    /**
     * Assumes a negative integer really is a positive integer that
     * has wrapped around and re-creates the original value.
     * @param i the value to treat as unsigned int.
     * @return the unsigned int as a long.
     * @since 1.34
     */
    protected static long adjustToLong(int i) {
        if (i < 0) {
            return 2 * ((long) Integer.MAX_VALUE) + 2 + i;
        } else {
            return i;
        }
    }

    /**
     * Creates the temporary file to which the central directory data
     * is written while files are added to the zip file/stream.  The
     * temporary file is appended to the end of the zip file/stream on
     * close, and the temporary file is then deleted.
     * @param tmpFileDir directory in which temporary file for central directory
     *                   data is to be created
     * @throws IOException
     */
    private void openDirTmpFile(File tmpFileDir) throws IOException {
        cdFile = File.createTempFile("zos-", ".zip.tmp", tmpFileDir);
        cdRaf = new RandomAccessFile(cdFile, "rw");
    }

    private void closeDirTmpFile() throws IOException {
        try {
            cdRaf.close();
        } finally {
            cdFile.delete();
        }
    }

    /**
     * Write out the central directory in temporary file to the output stream/file.
     * @throws IOException
     */
    private void writeOutDirTmpFile() throws IOException {
        byte[] buf = new byte[32 * 1024];
        long len = cdRaf.getFilePointer();
        cdRaf.seek(0);
        long bytesLeft = len;
        while (bytesLeft > 0) {
            long chunkSize = Math.min((long) buf.length, bytesLeft);
            int bytesRead = cdRaf.read(buf, 0, (int) chunkSize);
            writeOut(buf, 0, bytesRead);
            bytesLeft -= (long) bytesRead;
        }
        written += len;
    }
}
