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
package com.zimbra.cs.taglib.ngxlookup;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.spy.memcached.HashAlgorithm;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ngxlookup.ZimbraNginxLookUpClient;

public class NginxRouteLookUpConnector {
    private static ZimbraNginxLookUpClient sTheClient = new ZimbraNginxLookUpClient();

    /**
     * Returns the one and only Nginx Lookup client object.
     * Nginx LookUp Handler Client makes a new connection to a random upstream handler.
     * Handler Client doesn't maintain persistent connections unlike Memcached Client
     * @return
     */
    public static ZimbraNginxLookUpClient getClient() {
        return sTheClient;
    }

    /**
     * Load all Nginx LookUp attributes from Web.Xml
     * @throws ServiceException
     */
    public static void startup() throws ServiceException {
        reloadConfig();
    }

    /**
     * Reload the Nginx LookUp client configuration.
     * @throws ServiceException
     */
    public static void reloadConfig() throws ServiceException {
        String[] lookUpServers = null;
        String[] upstreamMailServers = null;
        int connectTimeout = 15000;
        int retryTimeout = 60000;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            lookUpServers = ((String) envCtx.lookup("nginxLookUpHandlers")).split("\\s+");
            upstreamMailServers = ((String) envCtx.lookup("upstreamMailServers")).split("\\s+");
            connectTimeout = (Integer) envCtx.lookup("reverseProxyRouteLookupTimeout");
            retryTimeout = (Integer) envCtx.lookup("memcachedClientTimeout");
        } catch (NamingException ne) {
            //REDO to throw the error or print stack trace??
            ne.printStackTrace();
        } catch (Exception e) {
            //REDO to throw the error or print stack trace??
            e.printStackTrace();
        }
        sTheClient.setAttributes(lookUpServers, upstreamMailServers, connectTimeout, retryTimeout);
    }

    /**
     * Shutdown the memcached connection.
     * @throws ServiceException
     */
    public static void shutdown() throws ServiceException {
        sTheClient = null;
    }
}
