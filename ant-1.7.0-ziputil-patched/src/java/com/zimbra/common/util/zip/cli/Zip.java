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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import com.zimbra.common.util.zip.ZipEntry;
import com.zimbra.common.util.zip.ZipOutputStream;

public class Zip {

    private static void usage() {
        System.err.println("Usage: Zip [-0] [--zip64] <zip file> <input file/dir> [<input file/dir> ...]");
        System.err.println("Creates a zip file containing the listed input files and directories");
        System.err.println("Input directories are added recursively.");
        System.err.println("-0:      store files without compression (files are deflated by default)");
        System.err.println("--zip64: force zip64 mode for central directory information");
        System.exit(1);
    }

    public static long computeCRC32(File file) throws IOException {
        byte buf[] = new byte[32 * 1024];
        CRC32 crc = new CRC32();
        crc.reset();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                crc.update(buf, 0, bytesRead);
            }
            return crc.getValue();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {}
            }
        }
    }

    private ZipOutputStream zos;
    private String basePath;
    private boolean compressed;
    private byte[] copybuf = new byte[64 * 1024];
    private long numAdded;

    private String getRelativePath(File file) {
        String abs = file.getAbsolutePath();
        if (abs.startsWith(basePath))
            return abs.substring(basePath.length());
        else
            return abs;
    }

    public void addFile(File file) throws IOException {
        String name = getRelativePath(file);
        System.out.println("adding " + name);
        ZipEntry zentry = new ZipEntry(name);
        zentry.setMethod(compressed ? ZipEntry.DEFLATED : ZipEntry.STORED);
        if (zentry.getMethod() == ZipEntry.STORED) {
            long fileLen = file.length();
            zentry.setSize(fileLen);
            zentry.setCompressedSize(fileLen);
            zentry.setCrc(computeCRC32(file));
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            zos.putNextEntry(zentry);
            if (is != null) {
                int byteRead;
                while ((byteRead = is.read(copybuf)) != -1) {
                    zos.write(copybuf, 0, byteRead);
                }
            }
            zos.closeEntry();
            ++numAdded;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
    }

    public void addDirectory(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    addFile(file);
                } else if (file.isDirectory()) {
                    String name = file.getName();
                    if (!name.equals(".") && !name.equals(".."))
                        addDirectory(file);
                }
            }
        }
    }

    public void finish() throws IOException {
        zos.close();
        System.out.format("%d files added\n", numAdded);
    }

    public Zip(File zipFile, File workingDir, boolean compressed, boolean forceZip64) throws IOException {
        zos = new ZipOutputStream(zipFile, workingDir);
        zos.forceZip64(forceZip64);
        this.compressed = compressed;
        basePath = workingDir.getAbsolutePath();
        if (!basePath.endsWith("/"))
            basePath = basePath + "/";
    }

    public static void main(String[] args) throws Exception {
        boolean compressed = true;
        boolean zip64 = false;
        String zipFname = null;
        int firstItemIndex = args.length;
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-0")) {
                compressed = false;
            } else if (arg.equals("--zip64")) {
                zip64 = true;
            } else {
                zipFname = args[i];
                firstItemIndex = i + 1;
                break;
            }
        }
        if (zipFname == null || firstItemIndex >= args.length)
            usage();

        File cwd = new File(System.getProperty("user.dir"));
        Zip zip = new Zip(new File(zipFname), cwd, compressed, zip64);
        for (int i = firstItemIndex; i < args.length; ++i) {
            File item = new File(args[i]);
            if (item.isFile())
                zip.addFile(item);
            else if (item.isDirectory())
                zip.addDirectory(item);
            else
                System.err.println("Skipping " + args[i] + "; neither file nor directory");
        }
        zip.finish();
    }
}
