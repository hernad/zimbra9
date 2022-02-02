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

import com.zimbra.client.ZAppointmentHit;

public class ZApptCellLayoutBean {
    private boolean mIsFirst;
    private ZAppointmentHit mAppt;
    private long mRowSpan;
    private long mColSpan;
    private long mDaySpan;
    private long mWidth;
    private ZApptDayLayoutBean mDay;

    public ZApptCellLayoutBean(ZApptDayLayoutBean day) {
        mDay = day;
    }


    public ZApptDayLayoutBean getDay() {
        return mDay;
    }
    
    public boolean isIsFirst() {
        return mIsFirst;
    }

    public void setIsFirst(boolean isFirst) {
        mIsFirst = isFirst;
    }

    public ZAppointmentHit getAppt() {
        return mAppt;
    }

    public void setAppt(ZAppointmentHit appt) {
        mAppt = appt;
    }

    public long getRowSpan() {
        return mRowSpan;
    }

    public void setRowSpan(long rowSpan) {
        mRowSpan = rowSpan;
    }

    public long getColSpan() {
        return mColSpan;
    }

    public void setColSpan(long colSpan) {
        mColSpan = colSpan;
    }

    public long getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setDaySpan(long daySpan){
        mDaySpan = daySpan;
    }

    public long getDaySpan(){
        return mDaySpan;
    }

}
