/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.cs.taglib.memcached;

import com.zimbra.common.util.memcached.MemcachedKey;

public class RouteCacheKey implements MemcachedKey {
    private String mKeyStr;
    private String ROUTE_TAGS= "route:proto=";

    public RouteCacheKey(String protocolMode, String accountId) {
        mKeyStr = protocolMode + ";id=" + accountId;
    }

    public boolean equals(Object other) {
        if (other instanceof RouteCacheKey) {
            RouteCacheKey otherKey = (RouteCacheKey) other;
            return mKeyStr.equals(otherKey.mKeyStr);
        }
        return false;
    }

    public int hashCode() {
        return mKeyStr.hashCode();
    }

    // MemcachedKey interface
    public String getKeyPrefix() { return ROUTE_TAGS; }
    public String getKeyValue() { return mKeyStr; }
}