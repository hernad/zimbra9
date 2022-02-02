/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.tag.msg;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZMessageBean;
import com.zimbra.cs.taglib.bean.ZMailboxBean;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZMessage;
import com.zimbra.client.ZGetMessageParams;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;

public class GetMessageTag extends ZimbraSimpleTag {
    
    private String mVar;
    private String mId;
    private boolean mMarkread;
    private boolean mWanthtml;
    private boolean mWantHtmlSet;
    private boolean mNeuterimages;
    private boolean mRaw;
    private String mPart;
    private ZMailboxBean mMailbox;
    private String mReqHdrs;
    
    public void setVar(String var) { this.mVar = var; }
    
    public void setId(String id) { this.mId = id; }    
    public void setBox(ZMailboxBean mailbox) { this.mMailbox = mailbox; }

    public void setMarkread(boolean markread) { this.mMarkread = markread; }
    public void setWanthtml(boolean wanthtml) {
        this.mWanthtml = wanthtml;
        this.mWantHtmlSet = true;
    }

    public void setRaw(boolean raw) { this.mRaw = raw; }
    public void setNeuterimages(boolean neuter) { this.mNeuterimages = neuter; }
    public void setPart(String part) { this.mPart = part; }

    public void setRequestHeaders(String reqhdrs) { this.mReqHdrs = reqhdrs; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        try {
            ZMailbox mbox = mMailbox != null ? mMailbox.getMailbox() :  getMailbox();
            boolean wantHtml = mWantHtmlSet ? mWanthtml : mbox.getPrefs().getMessageViewHtmlPreferred();
            ZGetMessageParams params = new ZGetMessageParams();
            params.setId(mId);
            params.setMarkRead(mMarkread);
            params.setWantHtml(wantHtml);
            params.setNeuterImages(mNeuterimages);
            params.setRawContent(mRaw);
            params.setPart(mPart);
            params.setReqHeaders(mReqHdrs);
            ZMessage message = mbox.getMessage(params);
            jctxt.setAttribute(mVar, new ZMessageBean(message),  PageContext.PAGE_SCOPE);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
