/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2008, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.tag.briefcase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.fileupload.FileItem;

import com.zimbra.client.ZMailbox;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZMessageBean;
import com.zimbra.cs.taglib.bean.ZMessageComposeBean;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;

public class SaveBriefcaseTag extends ZimbraSimpleTag {

    private String mVar;

    private ZMessageComposeBean mCompose;
    private ZMessageBean mMessage;
    private String mFolderId;

    public void setCompose(ZMessageComposeBean compose) { mCompose = compose; }
    public void setMessage(ZMessageBean message) { mMessage = message; }

    public void setFolderId(String folderId) { mFolderId = folderId; }
    public void setVar(String var) { this.mVar = var; }

    @Override
    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        PageContext pc = (PageContext) jctxt;

        try {

            ZMailbox mbox = getMailbox();

            if (mCompose != null && mCompose.getHasFileItems()) {
                List<FileItem> mFileItems = mCompose.getFileItems();
                int num = 0;
                for (FileItem item : mFileItems) {
                    if (item.getSize() > 0) num++;
                }
                String[] briefIds = new String[num];
                int i=0;
                try {
                    for (FileItem item : mFileItems) {
                        if (item.getSize() > 0) {
                            Map<String, byte[]> attachment = new HashMap<String, byte[]>();
                            attachment.put(item.getName(), item.get());
                            String attachmentUploadId = mbox.uploadAttachments(attachment,
                                1000 * 60);
                            briefIds[i++] = mbox.createDocument(mFolderId, item.getName(),
                                attachmentUploadId);
                        }
                    }
                } finally {
                    for (FileItem item : mFileItems) {
                        try {
                            item.delete();
                        } catch (Exception e) {
                            /* TODO: need logging infra */ }
                    }
                }

                jctxt.setAttribute(mVar, briefIds, PageContext.PAGE_SCOPE);
            }
        } catch (ServiceException e) {
            throw new JspTagException(e.getMessage(), e);
        }

    }

}
