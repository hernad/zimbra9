/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.cs.taglib.bean.ZComposeUploaderBean;
import com.zimbra.common.service.ServiceException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;

public class ComposeUploaderTag extends ZimbraSimpleTag {

    private String mVar;
    private boolean mIsMobile;

    public void setVar(String var) { this.mVar = var; }
    public void setIsmobile(boolean isMobile) { mIsMobile = isMobile; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        PageContext pc = (PageContext) jctxt;
        ZComposeUploaderBean compose = (ZComposeUploaderBean) jctxt.getAttribute(mVar, PageContext.REQUEST_SCOPE);
        if (compose == null) {
            try {
                jctxt.setAttribute(mVar, new ZComposeUploaderBean(pc, getMailbox(), mIsMobile), PageContext.REQUEST_SCOPE);
            } catch (ServiceException e) {
                throw new JspTagException("compose upload failed", e);
            }
        }
    }
}
