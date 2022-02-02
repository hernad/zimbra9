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
import com.zimbra.common.util.StringUtil;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZFolder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;

public class UpdateFolderTag extends ZimbraSimpleTag {

    private String mId;
    private ZFolder.Color mColor;
    private String mName;
    private String mParentId;
    private String mFlags;
    private String mRgb;

    public void setId(String id) { mId = id; }
    public void setName(String name) { mName = name; }
    public void setParentid(String parentId) { mParentId = parentId; }
    public void setFlags(String flags) { mFlags = flags; }
    public void setColor(String color) throws ServiceException { mColor = ZFolder.Color.fromString(color); }
    public void setRgb(String rgb) { mRgb = rgb; }

    public void doTag() throws JspException, IOException {
        try {
            getMailbox().updateFolder(
                    mId,
                    StringUtil.isNullOrEmpty(mName) ? null : mName,
                    StringUtil.isNullOrEmpty(mParentId) ? null : mParentId,
                    mColor,
                    mRgb,
                    mFlags == null ? null : mFlags,
                    null);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
