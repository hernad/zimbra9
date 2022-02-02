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

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.memcached.MemcachedMap;
import com.zimbra.common.util.memcached.MemcachedSerializer;
import com.zimbra.common.util.memcached.ZimbraMemcachedClient;
import com.zimbra.cs.taglib.ZJspSession;

public class RouteCache {

    private static RouteCache sTheInstance = new RouteCache();
    private MemcachedMap<RouteCacheKey, String> mMemcachedLookup;

    public static RouteCache getInstance() { return sTheInstance; }

    RouteCache() {
        ZimbraMemcachedClient memcachedClient = MemcachedConnector.getClient();
        RouteSerializer serializer = new RouteSerializer();
        mMemcachedLookup = new MemcachedMap<RouteCacheKey, String>(memcachedClient, serializer, false);
    }

    private static class RouteSerializer implements MemcachedSerializer<String> {
        @Override
        public Object serialize(String value) {
            return value;
        }

        @Override
        public String deserialize(Object obj) throws ServiceException {
            return (String) obj;
        }
    }

    public String get(String accountId) throws ServiceException {
        RouteCacheKey key = new RouteCacheKey(ZJspSession.isProtocolModeHttps() ?  "https" : "http", accountId);
        return mMemcachedLookup.get(key);
    }

    public void put(String accountId, String route) throws ServiceException {
        RouteCacheKey key = new RouteCacheKey(ZJspSession.isProtocolModeHttps() ?  "https" : "http", accountId);
        mMemcachedLookup.put(key, route);
    }
}
