/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

public class NextPrevItemBean {
    private int mPrevIndex;
    private int mPrevOffset;
    private int mNextIndex;
    private int mNextOffset;
    private boolean mHasNext;
    private boolean mHasPrev;

    public NextPrevItemBean(boolean hasPrev, int prevIndex, int prevOffset, boolean hasNext, int nextIndex, int nextOffset) {
        mHasPrev = hasPrev;
        mPrevIndex = prevIndex;
        mPrevOffset = prevOffset;
        mHasNext = hasNext;
        mNextIndex = nextIndex;
        mNextOffset = nextOffset;
    }

    public boolean getHasPrev() {
        return mHasPrev;
    }

    public int getPrevIndex() {
        return mPrevIndex;
    }

    public int getPrevOffset() {
        return mPrevOffset;
    }

    public boolean getHasNext() {
        return mHasNext;
    }
    
    public int getNextIndex() {
        return mNextIndex;
    }

    public int getNextOffset() {
        return mNextOffset;
    }
}
