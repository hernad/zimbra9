/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.cs.taglib.ZJspSession;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZPrefs;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModifyPrefsTag extends ZimbraSimpleTag {

    protected Map<String, Object> mAttrs = new HashMap<String,Object>();
    private String mVar;
    private ZPrefs mPrefs;

    public void setVar(String var) { mVar = var; }
    
    public void doTag() throws JspException, IOException {
        try {
            JspContext jctxt = getJspContext();
            PageContext pageContext = (PageContext) jctxt;
            ZMailbox mailbox = getMailbox();
            mPrefs = mailbox.getAccountInfo(false).getPrefs();
            getJspBody().invoke(null);

            boolean update = !mAttrs.isEmpty();
            if (update) {
                mailbox.modifyPrefs(mAttrs);
                if (mAttrs.containsKey(Provisioning.A_zimbraPrefSkin)) {
                    String skin = (String)mAttrs.get(Provisioning.A_zimbraPrefSkin);
                    Cookie skinCookie = new Cookie("ZM_SKIN", skin);
                    skinCookie.setMaxAge(63072000);
                    skinCookie.setPath("/");
                    skinCookie.setSecure(ZJspSession.secureAuthTokenCookie((HttpServletRequest)pageContext.getRequest()));
                    ((HttpServletResponse)pageContext.getResponse()).addCookie(skinCookie);
                }
            }
           getJspContext().setAttribute(mVar, update, PageContext.PAGE_SCOPE);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }

    public void addPref(String name, String value) throws JspTagException {
        if (value == null) value = "";
        String currentValue = mPrefs.get(name);
        if (currentValue == null) currentValue = "";
        if (!value.equals(currentValue))
            StringUtil.addToMultiMap(mAttrs, name, value);
    }
}
