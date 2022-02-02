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
package com.zimbra.cs.taglib.bean;

import com.zimbra.client.ZTaskHit;

import java.util.Date;

public class ZTaskHitBean extends ZSearchHitBean {

    private ZTaskHit mHit;

    public ZTaskHitBean(ZTaskHit hit) {
        super(hit, HitType.task);
        mHit = hit;
    }

    public String getInviteId() { return mHit.getInviteId(); }

    public String getFlags() { return mHit.getFlags(); }

    public String getFolderId() { return mHit.getFolderId(); }

    public long getSize() { return mHit.getSize(); }

    public Date getDate() { return mHit.getStartDate(); }

    public boolean getHasAttachment() { return mHit.getHasAttachment(); }

    /**
     * @return comma-separated list of tag ids
     */
    public String getTagIds() { return mHit.getTagIds(); }

    public String getSubject() { return mHit.getName(); }

    public boolean getHasFlags() { return mHit.hasFlags(); }

    public boolean getHasTags() { return mHit.getHasTags(); }

    public boolean getIsHigh() { return "1".equals(mHit.getPriority()); }

    public boolean getIsLow() { return "9".equals(mHit.getPriority()); }

    public String getPercentComplete() { return mHit.getPercentComplete(); }

    public boolean getHasDueDate() { return mHit.getDueDateTime() != 0; }

    public long getDueDateTime() { return mHit.getDueDateTime(); }

    public Date getDueDate() { return mHit.getDueDate(); }

    public String getStatus() { return mHit.getStatus(); }
    
    public String getPriorityImage() {
        if (getIsHigh()) {
            return "tasks/ImgTaskHigh.png";
        } else if (getIsLow()) {
            return "tasks/ImgTaskLow.png";
        } else {
            return "tasks/ImgTaskNormal.gif";
        }
    }

}
