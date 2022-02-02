/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2013, 2014, 2016 Synacor, Inc.
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

package com.zimbra.cs.taglib.tag;

import com.zimbra.common.auth.ZAuthToken;
import com.zimbra.common.soap.Element;
import com.zimbra.common.soap.SoapHttpTransport;
import com.zimbra.common.soap.SoapProtocol;
import com.zimbra.common.soap.SoapTransport;
import com.zimbra.common.soap.SoapTransport.DebugListener;

public class TagUtil {

    public static class JsonDebugListener implements DebugListener {
        Element env;
        public void sendSoapMessage(Element envelope) {}
        public void receiveSoapMessage(Element envelope) {env = envelope; }
        public Element getEnvelope(){ return env; }
    }
    
    public static SoapTransport newJsonTransport(String url, String remoteAddr, ZAuthToken authToken, DebugListener debug) {
        return newJsonTransport(url, remoteAddr, authToken, null, debug);
    }

    public static SoapTransport newJsonTransport(String url, String remoteAddr, ZAuthToken authToken, String csrfToken, DebugListener debug) {
        SoapTransport transport = new SoapHttpTransport(url);
        transport.setClientIp(remoteAddr);
        transport.setAuthToken(authToken);
        transport.setCsrfToken(csrfToken);
        transport.setRequestProtocol(SoapProtocol.SoapJS);
        transport.setResponseProtocol(SoapProtocol.SoapJS);
        transport.setDebugListener(debug);
        return transport;
    }
}
