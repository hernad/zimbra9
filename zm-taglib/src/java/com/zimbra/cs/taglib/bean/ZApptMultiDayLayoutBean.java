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

import java.util.List;

public class ZApptMultiDayLayoutBean {

    private List<ZApptRowLayoutBean> mAllDayRows;
    private List<ZApptRowLayoutBean> mRows;
    private List<ZApptDayLayoutBean> mDays;
    private List<List<ZApptRowLayoutBean>> mRowsSeperatedByDays;
    private List<List<ZApptRowLayoutBean>> mAllDayRowsSeperatedByDays;
    private int mMaxColumns;

    public ZApptMultiDayLayoutBean(List<ZApptDayLayoutBean> days, List<ZApptRowLayoutBean> allDayRows, List<ZApptRowLayoutBean> rows, List<List<ZApptRowLayoutBean>> rowsSeperatedByDays, List<List<ZApptRowLayoutBean>> allDayRowsSeperatedByDays) {
        mAllDayRows = allDayRows;
        mRows = rows;
        mDays = days;
        mMaxColumns = 0;
        mRowsSeperatedByDays = rowsSeperatedByDays;
        mAllDayRowsSeperatedByDays = allDayRowsSeperatedByDays;
        for (ZApptDayLayoutBean day : days) {
            mMaxColumns += day.getColumns().size();
        }
    }

    public List<ZApptRowLayoutBean> getAllDayRows() {
        return mAllDayRows;
    }

    public List<ZApptRowLayoutBean> getRows() {
        return mRows;
    }

    public List<List<ZApptRowLayoutBean>> getRowsSeperatedByDays() {
        return mRowsSeperatedByDays;
    }

    public List<List<ZApptRowLayoutBean>> getAllDayRowsSeperatedByDays() {
        return mAllDayRowsSeperatedByDays;
    }

    public List<ZApptDayLayoutBean> getDays() {
        return mDays;
    }

    public int getNumDays() {
        return mDays.size();
    }

    public int getMaxColumns() {
        return mMaxColumns;
    }

    public long getScheduleAlldayOverlapCount() {
        int overlap = 0;
        for ( ZApptDayLayoutBean day : mDays) {
            if (!day.getAllDayAppts().isEmpty())
                overlap++;
        }
        return overlap;
    }
}
