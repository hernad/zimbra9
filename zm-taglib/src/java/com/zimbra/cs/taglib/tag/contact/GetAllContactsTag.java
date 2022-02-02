/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.tag.contact;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZContactBean;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZContact;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class GetAllContactsTag extends ZimbraSimpleTag {

    private String mVar;
    private String mFolderId;
    private String mContactIds;
    private boolean mSync;

    public void setVar(String var) { this.mVar = var; }
    public void setFolderId(String folderid) { this.mFolderId = folderid; }
    public void setContactIds(String ids) { this.mContactIds = ids; }
    public void setSync(boolean sync) { this.mSync = sync; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        try {
            ZMailbox mbox = getMailbox();
            List<ZContactBean> contactBeanList = new ArrayList<ZContactBean>();
            List<ZContact> contactList = mbox.getContactsForFolder(mFolderId, mContactIds, null, false, null);
            for(ZContact contact: contactList) {
              contactBeanList.add(new ZContactBean(contact));
            }
            jctxt.setAttribute(mVar, contactBeanList,  PageContext.PAGE_SCOPE);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
