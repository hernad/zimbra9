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
package com.zimbra.cs.taglib.tag;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZFolderBean;
import com.zimbra.client.ZFolder;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZSearchFolder;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ForEachFolderTag extends ZimbraSimpleTag {
    
    private String mVar;
    private Map mExpanded = null;
    private String mParentId;
    private ZFolderBean mParentFolder;
    private boolean mSkipRoot = true;
    private boolean mSkipSystem = false;
    private boolean mSkipTopSearch = false;
    private boolean mSkipTrash = false;

    public void setParentid(String parentId) { this.mParentId = parentId != null && parentId.length() ==0 ? null : parentId; }
    public void setParentfolder(ZFolderBean folder) { this.mParentFolder = folder; }
    public void setVar(String var) { this.mVar = var; }
    public void setSkiproot(boolean skiproot) { this.mSkipRoot = skiproot; }
    public void setSkipsystem(boolean skipsystem) { this.mSkipSystem = skipsystem; }
    public void setSkiptopsearch(boolean skiptopsearch) { this.mSkipTopSearch = skiptopsearch; }
    public void setExpanded(Map expanded) { mExpanded = expanded; }
    public void setSkiptrash(boolean skipTrash) { this.mSkipTrash = skipTrash; }

    public void doTag() throws JspException, IOException {
        JspFragment body = getJspBody();
        if (body == null) return;
        
        try {
            ZMailbox mbox = getMailbox();
            JspContext jctxt = getJspContext();
            ZFolder folder;
            if (mParentId != null) {
                folder = mbox.getFolderById(mParentId);
            } else if (mParentFolder != null) {
                folder = mParentFolder.folderObject();
            } else {
                folder = mbox.getUserRoot();
            }
            handleFolder(folder, body, jctxt, mSkipRoot, mSkipSystem);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }    
    
    private void handleFolder(ZFolder folder, JspFragment body, JspContext jctxt, boolean skip, boolean skipsystem)
    throws ServiceException, JspException, IOException {
        if (folder == null)
            return;

        if (skipsystem && folder.isSystemFolder() && !folder.getId().equals(ZFolder.ID_USER_ROOT))
            return;

        if (mSkipTopSearch && (folder instanceof ZSearchFolder) && folder.getParentId().equals(ZFolder.ID_USER_ROOT))
            return;

        if (mSkipTrash && folder.getId().equals(ZFolder.ID_TRASH)) 
            return;

        if (!skip) {
            jctxt.setAttribute(mVar, new ZFolderBean(folder));
            body.invoke(null);
        }

        if (mExpanded != null && !folder.getSubFolders().isEmpty()) {
            String state = (String) mExpanded.get(folder.getId());
            if (state != null && state.equals("collapse"))
                return;
        }

        List<ZFolder> subfolders = folder.getSubFolders();
        Collections.sort(subfolders);
        for (ZFolder subfolder : subfolders) {
            if (subfolder != null)
                handleFolder(subfolder, body, jctxt, false, skipsystem);
        }
    }
}