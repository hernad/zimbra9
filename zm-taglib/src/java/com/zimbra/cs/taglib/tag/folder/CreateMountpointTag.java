/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.tag.folder;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZFolderBean;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZFolder;
import com.zimbra.client.ZMailbox.OwnerBy;
import com.zimbra.client.ZMailbox.SharedItemBy;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class CreateMountpointTag extends ZimbraSimpleTag {

    private String mParentId;
    private String mName;
    private String mVar;
    private ZFolder.Color mColor;
    private ZFolder.View mView;
    private String mFlags;
    private OwnerBy mOwnerBy;
    private String mOwner;
    private SharedItemBy mSharedItemBy;
    private String mSharedItem;

    public void setParentid(String parentid) { mParentId = parentid; }
    public void setName(String name) { mName = name; }
    public void setFlags(String flags) { mFlags = flags; }
    public void setVar(String var) { mVar = var; }
    public void setColor(String color) throws ServiceException { mColor = ZFolder.Color.fromString(color); }
    public void setView(String view) throws ServiceException { mView = ZFolder.View.fromString(view); }
    public void setOwnerby(String ownerBy) throws ServiceException { mOwnerBy = OwnerBy.fromString(ownerBy); }
    public void setOwner(String owner) { mOwner = owner; }
    public void setShareditemby(String sharedItemBy) throws ServiceException { mSharedItemBy = SharedItemBy.fromString(sharedItemBy); }
    public void setShareditem(String sharedItem) { mSharedItem = sharedItem; }

    public void doTag() throws JspException, IOException {
        try {
            ZFolderBean result = new ZFolderBean(getMailbox().createMountpoint(
                    mParentId, mName, mView, mColor, mFlags, mOwnerBy, mOwner, mSharedItemBy, mSharedItem, false));
            getJspContext().setAttribute(mVar, result, PageContext.PAGE_SCOPE);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
