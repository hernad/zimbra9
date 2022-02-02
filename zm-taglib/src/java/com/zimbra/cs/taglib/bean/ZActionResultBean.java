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

import com.zimbra.client.ZMailbox.ZActionResult;

public class ZActionResultBean {

    private ZActionResult mResult;
    private String[] mIds;
    private int mIdCount = -1;

    public ZActionResultBean(ZActionResult result) {
        mResult = result;
    }

    public synchronized String[] getIds() {
        if (mIds == null) mIds = mResult.getIdsAsArray();
        return mIds;
    }

    public synchronized int getIdCount() {

        if (mIdCount == -1) {
            if (mIds != null) {
                mIdCount = mIds.length;
            } else {
                String ids = mResult.getIds();
                int len = ids.length();
                if (ids == null || len == 0) return 0;
                mIdCount = 1;
                for (int i=0; i < len; i++) {
                    if (ids.charAt(i) == ',') mIdCount++;
                }
            }
        }
        return mIdCount;
    }
}
