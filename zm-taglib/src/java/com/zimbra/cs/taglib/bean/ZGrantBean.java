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

import com.zimbra.client.ZGrant;

/**
 * Created by IntelliJ IDEA.
 * User: akanjila
 * Date: Nov 20, 2007
 * Time: 2:40:03 PM
 */
public class ZGrantBean {

    private String mArgs;
    private String mGranteeName;
    private String mGranteeId;
    private ZGrant.GranteeType mGranteeType;
    private String mPermissions;

    public String getArgs() {
        return mArgs;
    }

    public void setArgs(String mArgs) {
        this.mArgs = mArgs;
    }

    public String getGranteeName() {
        return mGranteeName;
    }

    public void setGranteeName(String mGranteeName) {
        this.mGranteeName = mGranteeName;
    }

    public String getGranteeId() {
        return mGranteeId;
    }

    public void setGranteeId(String mGranteeId) {
        this.mGranteeId = mGranteeId;
    }

    public ZGrant.GranteeType getGranteeType() {
        return mGranteeType;
    }

    public void setGranteeType(ZGrant.GranteeType mGranteeType) {
        this.mGranteeType = mGranteeType;
    }

    public String getPermissions() {
        return mPermissions;
    }

    public void setPermissions(String mPermissions) {
        this.mPermissions = mPermissions;
    }
}
