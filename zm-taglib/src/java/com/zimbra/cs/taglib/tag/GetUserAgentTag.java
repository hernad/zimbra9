/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.cs.taglib.bean.ZUserAgentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class GetUserAgentTag extends ZimbraSimpleTag {

    private static final String UA_SESSION = "ZUserAgentBean.SESSION";
    private String mVar;
    private boolean mSession = true;

    public void setVar(String var) { this.mVar = var; }
    public void setSession(boolean session) { this.mSession = session; }

    public void doTag() throws JspException, IOException {
        JspContext ctxt = getJspContext();
        ctxt.setAttribute(mVar, getUserAgent(ctxt, mSession),  PageContext.REQUEST_SCOPE);
    }

    public static ZUserAgentBean getUserAgent(JspContext ctxt, boolean session) {
        PageContext pctxt = (PageContext) ctxt;
        HttpServletRequest req = (HttpServletRequest) pctxt.getRequest();
        ZUserAgentBean ua;
        if (session) {
            ua = (ZUserAgentBean) ctxt.getAttribute(UA_SESSION, PageContext.SESSION_SCOPE);
            if ( ua == null) {
                ua = new ZUserAgentBean(req.getHeader("User-Agent"));
                ctxt.setAttribute(UA_SESSION, ua,  PageContext.SESSION_SCOPE);
            }

        } else {
            ua = new ZUserAgentBean(req.getHeader("User-Agent"));
        }
        return ua;
    }
    
}
