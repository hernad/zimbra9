/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.common.util.zip;

import java.util.zip.ZipException;

public class Zip64ExtraField implements ZipExtraField {

    public static final ZipShort HEADER_ID = new ZipShort(0x0001);

    private static final long UNSPECIFIED8 = -1L;
    private static final int UNSPECIFIED4 = -1;

    private long uncompressedSize = UNSPECIFIED8;
    private long compressedSize = UNSPECIFIED8;
    private long localHeaderOffset = UNSPECIFIED8;
    private int diskNumber = UNSPECIFIED4;

    private int parsedFieldLength;
    private long parsedLongs[];

    public Zip64ExtraField() {}

    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    private int getCDLen() {
        return
            (uncompressedSize != UNSPECIFIED8 ? 8 : 0) +
            (compressedSize != UNSPECIFIED8 ? 8 : 0) +
            (localHeaderOffset != UNSPECIFIED8 ? 8 : 0) +
            (diskNumber != UNSPECIFIED4 ? 4 : 0);
    }

    public ZipShort getCentralDirectoryLength() {
        return new ZipShort(getCDLen());
    }

    public byte[] getCentralDirectoryData() {
        byte[] data = new byte[getCDLen()];
        int offset = 0;
        if (uncompressedSize != UNSPECIFIED8) {
            System.arraycopy(ZipLong8.getBytes(uncompressedSize), 0, data, offset, 8);
            offset += 8;
        }
        if (compressedSize != UNSPECIFIED8) {
            System.arraycopy(ZipLong8.getBytes(compressedSize), 0, data, offset, 8);
            offset += 8;
        }
        if (localHeaderOffset != UNSPECIFIED8) {
            System.arraycopy(ZipLong8.getBytes(localHeaderOffset), 0, data, offset, 8);
            offset += 8;
        }
        if (diskNumber != UNSPECIFIED4)
            System.arraycopy(ZipLong8.getBytes(diskNumber), 0, data, offset, 4);
        return data;
    }

    private int getLFHLen() {
        return
            (uncompressedSize != UNSPECIFIED8 ? 8 : 0) +
            (compressedSize != UNSPECIFIED8 ? 8 : 0);
    }

    public ZipShort getLocalFileDataLength() {
        return new ZipShort(getLFHLen());
    }

    public byte[] getLocalFileDataData() {
        byte[] data = new byte[getLFHLen()];
        int offset = 0;
        if (uncompressedSize != UNSPECIFIED8) {
            System.arraycopy(ZipLong8.getBytes(uncompressedSize), 0, data, offset, 8);
            offset += 8;
        }
        if (compressedSize != UNSPECIFIED8) {
            System.arraycopy(ZipLong8.getBytes(compressedSize), 0, data, offset, 8);
            offset += 8;
        }
        return data;
    }

    public void parseFromLocalFileData(byte[] data, int offset, int length)
            throws ZipException {
        parsedFieldLength = length;
        int numLongs;
        if (length >= 24)
            numLongs = 3;
        else if (length >= 16)
            numLongs = 2;
        else if (length >= 8)
            numLongs = 1;
        else
            numLongs = 0;
        parsedLongs = new long[numLongs];
        int off = offset;
        for (int i = 0; i < numLongs; ++i) {
            parsedLongs[i] = ZipLong8.getValue(data, off);
            off += 8;
        }
        if (length % 8 >= 4)
            diskNumber = (int) ZipLong.getValue(data, off);
    }

    public void setUncompressedSize(long uncompressedSize) {
        this.uncompressedSize = uncompressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public void setLocalHeaderOffset(long localHeaderOffset) {
        this.localHeaderOffset = localHeaderOffset;
    }

    public void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    /**
     * @return disk number, or -1 if unspecified
     */
    public int getDiskNumber() { return diskNumber; }

    /**
     * @return length of the parsed extra field, in bytes
     */
    public int getParsedFieldLength() { return parsedFieldLength; }

    /**
     * The caller must depend on external information to determine which of the returned 8-byte numbers
     * map to uncompressed size, compressed size, and local header offset.
     * @return
     */
    public long[] getParsedLongs() { return parsedLongs; }
}
