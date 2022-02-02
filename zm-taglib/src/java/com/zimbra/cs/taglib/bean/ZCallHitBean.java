/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.client.ZCallHit;
import com.zimbra.client.ZPhone;

import java.util.Date;

public class ZCallHitBean extends ZSearchHitBean {

    private ZCallHit mHit;

    public ZCallHitBean(ZCallHit hit) {
        super(hit, HitType.call);
        mHit = hit;
    }

    public String toString() { return mHit.toString(); }

    public ZPhone getCaller() { return mHit.getCaller(); }

    public ZPhone getRecipient() { return mHit.getRecipient(); }

    public String getDisplayCaller() { return mHit.getDisplayCaller(); }

	public String getDisplayRecipient() { return mHit.getDisplayRecipient(); }

    public Date getDate() { return new Date(mHit.getDate()); }

    public long getDuration() { return mHit.getDuration(); }

}
