/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2010, 2011, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.cs.taglib.tag;

import com.zimbra.client.ZDomain;
import com.zimbra.client.ZSoapProvisioning;
import com.zimbra.common.account.Key;
import com.zimbra.common.localconfig.LC;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.common.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class GetFavIconTag extends ZimbraSimpleTag {

    private String var;
    private HttpServletRequest request;

    public void setVar(String var) {
         this.var = var;
   }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void doTag() throws JspException, IOException {
        try {
            String soapUri = LC.zimbra_admin_service_scheme.value() + LC.zimbra_zmprov_default_soap_server.value() +':' +
                               LC.zimbra_admin_service_port.intValue() + AdminConstants.ADMIN_SERVICE_URI;

            ZSoapProvisioning provisioning = new ZSoapProvisioning();
            provisioning.soapSetURI(soapUri);

            String serverName = this.request.getParameter("customerDomain");

            if (serverName == null) {
                serverName = HttpUtil.getVirtualHost(this.request);
            }

			// get info
            ZDomain info = provisioning.getDomainInfo(Key.DomainBy.virtualHostname, serverName);
            if (info != null) {
                String favicon = info.getSkinFavicon();
                getJspContext().setAttribute(this.var, favicon, PageContext.REQUEST_SCOPE);
            } else {
                ZimbraLog.webclient.debug("unable to get domain info");
            }
        }
        catch (Exception e) {
                ZimbraLog.webclient.debug("error getting favicon:", e);
        }
   }

} // class GetFavIconTag
