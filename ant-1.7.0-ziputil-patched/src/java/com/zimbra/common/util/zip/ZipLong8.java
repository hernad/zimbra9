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

/**
 * Utility class that represents an edight byte integer with conversion
 * rules for the little endian byte order of ZIP files.
 *
 */
public class ZipLong8 implements Cloneable {

    private long value;

    /**
     * Create instance from a number.
     * @param value the long to store as a ZipLong8
     */
    public ZipLong8(long value) {
        this.value = value;
    }

    /**
     * Create instance from bytes.
     * @param bytes the bytes to store as a ZipLong8
     */
    public ZipLong8(byte[] bytes) {
        this(bytes, 0);
    }

    /**
     * Create instance from the eight bytes starting at offset.
     * @param bytes the bytes to store as a ZipLong8
     * @param offset the offset to start
     */
    public ZipLong8(byte[] bytes, int offset) {
        value = getValue(bytes, offset);
    }

    /**
     * Get value as eight bytes in little endian byte order.
     * @return value as eight bytes in little endian order
     */
    public byte[] getBytes() {
        return getBytes(value);
    }

    /**
     * Get value as Java long.
     * @return value as a long
     */
    public long getValue() {
        return value;
    }

    /**
     * Get value as eight bytes in little endian byte order.
     * @param value the value to convert
     * @return value as eight bytes in little endian byte order
     */
    public static byte[] getBytes(long value) {
        byte[] result = new byte[8];
        result[0] = (byte) ((value & 0xFFL));
        result[1] = (byte) ((value & 0xFF00L) >> 8);
        result[2] = (byte) ((value & 0xFF0000L) >> 16);
        result[3] = (byte) ((value & 0xFF000000L) >> 24);
        result[4] = (byte) ((value & 0xFF00000000L) >> 32);
        result[5] = (byte) ((value & 0xFF0000000000L) >> 40);
        result[6] = (byte) ((value & 0xFF000000000000L) >> 48);
        result[7] = (byte) ((value & 0x7F00000000000000L) >> 56);
        return result;
    }

    /**
     * Helper method to get the value as a Java long from eight bytes starting at given array offset
     * @param bytes the array of bytes
     * @param offset the offset to start
     * @return the correspondanding Java long value
     */
    public static long getValue(byte[] bytes, int offset) {
        long value = ((long) bytes[offset + 7] << 56) & 0x7F00000000000000L;
        value += ((long) bytes[offset + 6] << 48) & 0xFF000000000000L;
        value += ((long) bytes[offset + 5] << 40) & 0xFF0000000000L;
        value += ((long) bytes[offset + 4] << 32) & 0xFF00000000L;
        value += ((long) bytes[offset + 3] << 24) & 0xFF000000L;
        value += ((long) bytes[offset + 2] << 16) & 0xFF0000L;
        value += ((long) bytes[offset + 1] << 8) & 0xFF00L;
        value += ((long) bytes[offset] & 0xFFL);
        return value;
    }

    /**
     * Helper method to get the value as a Java long from an eight-byte array
     * @param bytes the array of bytes
     * @return the correspondanding Java long value
     */
    public static long getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    /**
     * Override to make two instances with same value equal.
     * @param o an object to compare
     * @return true if the objects are equal
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ZipLong8)) {
            return false;
        }
        return value == ((ZipLong8) o).getValue();
    }

    /**
     * Override to make two instances with same value equal.
     * @return the value stored in the ZipLong8
     */
    public int hashCode() {
        return (int) value;
    }
}
