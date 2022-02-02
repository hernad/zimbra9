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
package com.zimbra.cs.taglib.bean;

import com.zimbra.client.ZConversationHit;
import com.zimbra.client.ZEmailAddress;

import java.util.Date;
import java.util.List;

public class ZConversationHitBean extends ZSearchHitBean {

    private ZConversationHit mHit;
    
    public ZConversationHitBean(ZConversationHit hit) {
        super(hit, HitType.conversation);
        mHit = hit;
    }

    public Date getDate() { return new Date(mHit.getDate()); }
    
    public boolean getHasFlags() { return mHit.hasFlags(); }
    
    public boolean getHasMultipleTags() { return mHit.hasTags() && mHit.getTagIds().indexOf(',') != -1; }
    
    public String getTagIds() { return mHit.getTagIds(); }
    
    public boolean getHasTags() { return mHit.hasTags(); }
    
    public boolean getIsUnread() { return mHit.isUnread(); }

    public boolean getIsFlagged() { return mHit.isFlagged(); }

    public boolean getIsHighPriority() { return mHit.isHighPriority(); }

    public boolean getIsLowPriority() { return mHit.isLowPriority(); }

    public boolean getIsDraft() { return mHit.isDraft(); }

    public boolean getIsSentByMe() { return mHit.isSentByMe(); }

    public boolean getHasAttachment() { return mHit.hasAttachment(); }

    public String getSubject() { return mHit.getSubject(); }
    
    public String getFragment() { return mHit.getFragment(); }
    
    public int getMessageCount() { return mHit.getMessageCount(); }
    
    public List<String> getMatchedMessageIds() { return mHit.getMatchedMessageIds(); }
    
    public List<ZEmailAddress> getRecipients() { return mHit.getRecipients(); }
    
    public String getDisplayRecipients() { return BeanUtils.getAddrs(mHit.getRecipients()); }    
}
