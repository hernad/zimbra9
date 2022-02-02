/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

import java.util.List;
import java.util.Date;

public class ZApptRowLayoutBean {

    private List<ZApptCellLayoutBean> mCells;
    private int mRowNum;
    private long mTime;

    public ZApptRowLayoutBean(List<ZApptCellLayoutBean> cells, int rowNum, long time) {
        mCells = cells;
        mRowNum = rowNum;
        mTime = time;
    }

    public List<ZApptCellLayoutBean> getCells() {
        return mCells;
    }

    public int getRowNum() {
        return mRowNum;
    }

    public long getTime() {
        return mTime;
    }

    public Date getDate() {
        return new Date(mTime);
    }

    public long getScheduleOverlapCount() {
        int overlap = 0;
        ZApptDayLayoutBean day = null;
        for ( ZApptCellLayoutBean cell : mCells) {
            if (cell.getAppt() != null && cell.getDay() != day) {
                overlap++;
                day = cell.getDay();
            }
        }
        return overlap;
    }
}
