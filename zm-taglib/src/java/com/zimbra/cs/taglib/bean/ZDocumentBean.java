/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.bean;

import java.util.Date;

import com.zimbra.common.mime.ContentType;
import com.zimbra.client.ZDocument;

public class ZDocumentBean {

    private final ZDocument mDoc;

    public ZDocumentBean(ZDocument doc) {
        mDoc = doc;
    }

    public Date getCreatedDate() {
        return new Date(mDoc.getCreatedDate());
    }

    public Date getModifiedDate() {
        return new Date(mDoc.getModifiedDate());
    }

    public Date getMetaDataChangedDate() {
        return new Date(mDoc.getMetaDataChangedDate());
    }

    public String getId() {
        return mDoc.getId();
    }

    public String getName() {
        return mDoc.getName();
    }

    public String getFolderId() {
        return mDoc.getFolderId();
    }

    public String getVersion() {
        return mDoc.getVersion();
    }

    public String getEditor() {
        return mDoc.getEditor();
    }

    public String getCreator() {
        return mDoc.getCreator();
    }

    public String getRestUrl() {
        return mDoc.getRestUrl();
    }

    public boolean isWiki() {
        return mDoc.isWiki();
    }

    public String getContentType() {
        String contentType = mDoc.getContentType();
        return new ContentType(contentType).getContentType();
    }

    public long getSize() {
        return mDoc.getSize();
    }

    public String getTagIds() {
        return mDoc.getTagIds();
    }

}