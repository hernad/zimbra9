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

package com.zimbra.common.util.zip.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;

import com.zimbra.common.util.zip.ZipEntry;
import com.zimbra.common.util.zip.ZipFile;

public class ZipCheck {

    private static long computeCRC32(InputStream is) throws IOException {
        byte buf[] = new byte[32 * 1024];
        CRC32 crc = new CRC32();
        crc.reset();
        int bytesRead;
        while ((bytesRead = is.read(buf)) != -1) {
            crc.update(buf, 0, bytesRead);
        }
        return crc.getValue();
    }

    private static void readFully(InputStream is) throws IOException {
        byte buf[] = new byte[32 * 1024];
        while (is.read(buf) != -1) {}
    }

    private static void usage() {
        System.err.println("Usage: ZipCheck <method> [--nocrc] <zip file>");
        System.err.println("Checks the integrity of a zip file");
        System.err.println("method: -c for central directory-driven, -s for sequential");
        System.err.println("--nocrc: skip crc32 checksum verification");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        boolean methodSet = false;
        boolean methodCentralDir = true;
        boolean checksum = true;
        String zipFname = null;
        int lastIndex = 0;
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-c")) {
                methodSet = true;
                methodCentralDir = true;
            } else if (arg.equals("-s")) {
                methodSet = true;
                methodCentralDir = false;
            } else if (arg.equals("--nocrc")) {
                checksum = false;
            } else {
                zipFname = arg;
                lastIndex = i;
                break;
            }
        }
        if (zipFname == null || !methodSet || lastIndex != args.length - 1)
            usage();

        if (methodCentralDir)
            checkWithCentralDirectory(zipFname, checksum);
        else
            checkSequentially(zipFname, checksum);
    }

    @SuppressWarnings("unchecked")
    private static void checkWithCentralDirectory(String fname, boolean checksum) throws Exception {
        int numEntries = 0;
        int numErrors = 0;
        ZipFile zipFile = new ZipFile(fname, "utf-8");
        Enumeration entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) entries.nextElement();
            ++numEntries;
            String name = ze.getName();
            long size = ze.getSize();
            long sizeCompressed = ze.getCompressedSize();
            long crcEntry = ze.getCrc();
            if (size == sizeCompressed)
                System.out.format("size=%8d: %s ... ", size, name);
            else
                System.out.format("size=%8d, compressed=%8d: %s ... ", size, sizeCompressed, name);
            InputStream is = zipFile.getInputStream(ze);
            long crcRecomputed;
            try {
                if (checksum) {
                    crcRecomputed = computeCRC32(is);
                } else {
                    crcRecomputed = crcEntry;
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {}
                }
            }
            if (crcRecomputed == crcEntry) {
                System.out.println("ok");
            } else {
                System.out.format("CHECKSUM ERROR: expecting 0x%8x, got 0x%8x\n", crcEntry, crcRecomputed);
                ++numErrors;
            }
        }
        System.out.format("%d entries found\n", numEntries);
        if (numErrors == 0) {
            System.out.println("zip file looks good");
        } else {
            System.out.format("Problem!  %d entries had checksum error\n", numErrors);
            System.exit(1);
        }
    }

    private static void checkSequentially(String fname, boolean checksum) throws Exception {
        int numEntries = 0;
        int numErrors = 0;
        java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(new FileInputStream(fname));
        java.util.zip.ZipEntry ze = null;
        while ((ze = zis.getNextEntry()) != null) {
            ++numEntries;
            String name = ze.getName();
            long size = ze.getSize();
            long sizeCompressed = ze.getCompressedSize();
            long crcEntry = ze.getCrc();
            long crcRecomputed;
            if (checksum) {
                crcRecomputed = computeCRC32(zis);
            } else {
                readFully(zis);
                crcRecomputed = crcEntry;
            }
            if (size == -1 || sizeCompressed == -1 || crcEntry == -1) {
                // Entry used Data Descriptor.  True sizes and crc32 are available only after reading data bytes.
                size = ze.getSize();
                sizeCompressed = ze.getCompressedSize();
                crcEntry = ze.getCrc();
            }
            if (size == sizeCompressed)
                System.out.format("size=%8d: %s ... ", size, name);
            else
                System.out.format("size=%8d, compressed=%8d: %s ... ", size, sizeCompressed, name);
            if (crcRecomputed == crcEntry) {
                System.out.println("ok");
            } else {
                System.out.format("CHECKSUM ERROR: expecting 0x%8x, got 0x%8x\n", crcEntry, crcRecomputed);
                ++numErrors;
            }
        }
        System.out.format("%d entries found\n", numEntries);
        if (numErrors == 0) {
            System.out.println("zip file looks good");
        } else {
            System.out.format("Problem!  %d entries had checksum error\n", numErrors);
            System.exit(1);
        }
    }
}
