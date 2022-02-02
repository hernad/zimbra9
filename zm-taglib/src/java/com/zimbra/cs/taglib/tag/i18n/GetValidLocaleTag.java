/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.tag.i18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import com.zimbra.common.auth.ZAuthToken;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.AccountConstants;
import com.zimbra.common.soap.Element;
import com.zimbra.common.soap.Element.XMLElement;
import com.zimbra.common.soap.SoapHttpTransport;
import com.zimbra.cs.taglib.ZJspSession;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;

public class GetValidLocaleTag extends ZimbraSimpleTag {

	private String mVar;
    private String mLocale;
    private ZAuthToken mAuthToken;
    private String mCsrfToken;
	
    public void setVar(String var) { this.mVar = var; }
	public void setLocale(String locale) { this.mLocale = locale; }
    public void setAuthtoken(ZAuthToken authToken) { this.mAuthToken = authToken; }
    public void setCsrftoken(String csrfToken) { this.mCsrfToken = csrfToken; }


    // simple tag methods

    public void doTag() throws JspException, IOException {
        JspContext ctxt = getJspContext();
        if (this.mLocale == null) {
            ctxt.setAttribute(mVar, false,  PageContext.REQUEST_SCOPE);
            return;
        }
        SoapHttpTransport transport = null;
        try {
            String soapUri = ZJspSession.getSoapURL((PageContext)ctxt);
        	transport = new SoapHttpTransport(soapUri);
     		transport.setAuthToken(mAuthToken);
     		transport.setCsrfToken(mCsrfToken);
        	XMLElement req = new XMLElement(AccountConstants.GET_AVAILABLE_LOCALES_REQUEST);
            Element resp = transport.invokeWithoutSession(req);
            List<String> locales = new ArrayList<String>();
            for (Element locale : resp.listElements(AccountConstants.E_LOCALE)) {
                String id = locale.getAttribute(AccountConstants.A_ID, null);
                if (id != null)
                	locales.add(id);
            }
            Collections.sort(locales);
            boolean isValid = false;
            for(String s : locales) {
                if (this.mLocale.toLowerCase().startsWith(s.toLowerCase())) {
                    isValid = true;
                    break;
                }
            }
            ctxt.setAttribute(mVar, isValid,  PageContext.REQUEST_SCOPE);
        }
        catch(ServiceException e) {
            throw new JspTagException(e.getMessage(), e);   
        } finally {
            if (transport != null)
                transport.shutdown();
        }
    }

}

