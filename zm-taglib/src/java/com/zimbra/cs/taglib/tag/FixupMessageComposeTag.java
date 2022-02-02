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

import com.zimbra.cs.taglib.bean.ZMessageBean;
import com.zimbra.cs.taglib.bean.ZMessageComposeBean;
import com.zimbra.cs.taglib.bean.ZMimePartBean;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.List;

public class FixupMessageComposeTag extends ZimbraSimpleTag {


    private ZMessageComposeBean mCompose;
    private ZMessageBean mMessage;
    private boolean mNewAttachments;


    public void setMessage(ZMessageBean message) { mMessage = message; }

    public void setCompose(ZMessageComposeBean compose) { mCompose = compose; }

    public void setNewattachments(boolean newAttachments) { mNewAttachments = newAttachments; }

    public void doTag() throws JspException, IOException {
        List<ZMimePartBean> attachments = mMessage.getAttachments();
        mCompose.setOrignalAttachments(attachments);
        if (mNewAttachments) {
            for (ZMimePartBean part : attachments) {
                mCompose.setCheckedAttachmentName(part.getPartName(),(part.getContentId() == null || part.getContentId().equals("") ? "true" : part.getContentId()));
            }
        }
    }
}
