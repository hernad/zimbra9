/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.client.ZContact;
import com.zimbra.client.ZFeatures;
import com.zimbra.client.ZFilterRule;
import com.zimbra.client.ZFolder;
import com.zimbra.client.ZGetInfoResult;
import com.zimbra.client.ZIdentity;
import com.zimbra.client.ZLicenses;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZPrefs;
import com.zimbra.client.ZSignature;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.auth.ZAuthToken;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class ZMailboxBean {

    private ZMailbox mMbox;
    
    public ZMailboxBean(ZMailbox mbox) {
        mMbox = mbox;
    }
    
    public String getName() throws ServiceException { return mMbox.getName(); }
    
    public long getSize() throws ServiceException { return mMbox.getSize(); }
       
    private ZFolderBean getFolderBeanById(String id) throws ServiceException {
        ZFolder folder = mMbox.getFolderById(id);
        return folder == null ? null : new ZFolderBean(folder);
    }

    public ZMailbox getMailbox() { return mMbox; }

    public String getNoOp() throws ServiceException { mMbox.noOp(); return "";}
    
    public ZFolderBean getInbox() throws ServiceException { return getFolderBeanById(ZFolder.ID_INBOX); }

    public ZFolderBean getChats() throws ServiceException { return getFolderBeanById(ZFolder.ID_CHATS); }

    public ZFolderBean getTrash() throws ServiceException { return getFolderBeanById(ZFolder.ID_TRASH); }
    
    public ZFolderBean getSpam() throws ServiceException { return getFolderBeanById(ZFolder.ID_SPAM); }
    
    public ZFolderBean getSent() throws ServiceException { return getFolderBeanById(ZFolder.ID_SENT); }
    
    public ZFolderBean getDrafts() throws ServiceException { return getFolderBeanById(ZFolder.ID_DRAFTS); }
    
    public ZFolderBean getCalendar() throws ServiceException { return getFolderBeanById(ZFolder.ID_CALENDAR); }

    public ZFolderBean getTasks() throws ServiceException { return getFolderBeanById(ZFolder.ID_TASKS); }

    public ZFolderBean getBriefcase() throws ServiceException { return getFolderBeanById(ZFolder.ID_BRIEFCASE); }
    
    public ZFolderBean getContacts() throws ServiceException { return getFolderBeanById(ZFolder.ID_CONTACTS); }
    
    public ZFolderBean getAutoContacts() throws ServiceException { return getFolderBeanById(ZFolder.ID_AUTO_CONTACTS); }

    public double getBase64ToNormalRatio() throws ServiceException { return ZFolder.BASE64_TO_NORMAL_RATIO; }

    public Map<String, List<String>> getAttrs() throws ServiceException { return mMbox.getAccountInfo(false).getAttrs(); }

    public ZGetInfoResult getAccountInfo() throws ServiceException { return mMbox.getAccountInfo(false); }

    public ZGetInfoResult getAccountInfoReload() throws ServiceException { return mMbox.getAccountInfo(true); }

    public List<ZSignature> getSignatures() throws ServiceException { return mMbox.getAccountInfo(false).getSignatures(); }

    public ZPrefs getPrefs() throws ServiceException { return mMbox.getPrefs(); }
    
    public ZFeatures getFeatures() throws ServiceException { return mMbox.getFeatures(); }

    public ZLicenses getLicenses() throws ServiceException { return mMbox.getLicenses(); }

    public Boolean getAdminDelegated() throws ServiceException { return mMbox.getAccountInfo(false).getAdminDelegated(); }

    public boolean getHasTags() throws ServiceException { return mMbox.hasTags(); }

    public List<String> getAvailableSkins() throws ServiceException { return mMbox.getAvailableSkins(); }

    public List<String> getAvailableLocales() throws ServiceException { return mMbox.getAvailableLocales(); }
    
    public List<ZIdentity> getIdentities()  throws ServiceException { return mMbox.getIdentities(); }

    public List<ZFilterRule> getFilterRules() throws ServiceException { return mMbox.getIncomingFilterRules().getRules(); }

    public List<ZFilterRule> getFilterRulesReload() throws ServiceException { return mMbox.getIncomingFilterRules(true).getRules(); }

    public URI getRestURI(String relativePath) throws ServiceException { return mMbox.getRestURI(relativePath); } 

    public ZIdentity getDefaultIdentity() throws ServiceException {
        for (ZIdentity identity : mMbox.getAccountInfo(false).getIdentities()) {
            if (identity.getIsDefault()) return identity;
        }
        return null;
    }

	public ZContactBean getMyCard() throws ServiceException {
		ZContact myCard = mMbox.getMyCard();
		return myCard == null ? null : new ZContactBean(myCard);
	}

    public String getCsrfToken() throws ServiceException { return mMbox.getCsrfToken(); }

    public ZAuthToken getAuthToken() throws ServiceException { return mMbox.getAuthToken(); }
}
