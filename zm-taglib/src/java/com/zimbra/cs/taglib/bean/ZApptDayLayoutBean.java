/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2009, 2010, 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class ZApptDayLayoutBean {

    private long mStartTime;
    private long mEndTime;
    private int mDay;
    private int mNumDays;
    private String mFolderId;
    private static final long MSECS_PER_MINUTE = 1000*60;
    private static final long MSECS_PER_HOUR = MSECS_PER_MINUTE * 60;
    private static final long MSECS_PER_DAY = MSECS_PER_HOUR * 24;

    public static final String PSTATUS_DECLINED = "DE";

    List<ZAppointmentHit> mAllday; // all all-day appts in this range
    List<ZAppointmentHit> mAppts;  // all non-day appts in this range
    ZAppointmentHit mEarliestAppt; // non-allday appt with earliest start time
    ZAppointmentHit mLatestAppt;   // non-allday appt with latest end time
    List<List<ZAppointmentHit>> mColumns;

    public ZAppointmentHit getEarliestAppt() { return mEarliestAppt; }
    public ZAppointmentHit getLatestAppt() { return mLatestAppt; }
    public long getStartTime() { return mStartTime; }
    public long getEndTime() { return mEndTime; }

    public List<ZAppointmentHit> getAllDayAppts() { return mAllday; }
    public List<List<ZAppointmentHit>> getColumns() { return mColumns; }
    public int getDay() { return mDay; }

    public int getMaxColumns() {
        return mColumns.size();
    }

    public int getWidth() {
        return (int)(100.0/mNumDays);
    }

    public ZApptDayLayoutBean(List<ZAppointmentHit> appts, Calendar startCal, int day, int numDays, String folderId, long msecsIncr, boolean isShowDeclined) {
        mAllday = new ArrayList<ZAppointmentHit>();
        mAppts = new ArrayList<ZAppointmentHit>();
        mStartTime = startCal.getTimeInMillis();
        mEndTime = BeanUtils.addDay(startCal, 1).getTimeInMillis();
//        mStartTime = dayStartTime;
//        mEndTime = mStartTime + MSECS_PER_DAY;
        mDay = day;
        mNumDays = numDays;
        mFolderId = folderId;

        for (ZAppointmentHit appt : appts) {
            if (appt.isInRange(mStartTime, mEndTime)
                    && (mFolderId == null || mFolderId.equals(appt.getFolderId()))
                    && (appt.getParticipantStatus() == null || (appt.getParticipantStatus() != null
                                                                && ((!appt.getParticipantStatus().equals(PSTATUS_DECLINED))
                                                                    || (appt.getParticipantStatus().equals(PSTATUS_DECLINED) && isShowDeclined))))) {
                if (appt.isAllDay())
                    mAllday.add(appt);
                else {
                    mAppts.add(appt);
                    // keep track of earliest and latest
                    if ((mEarliestAppt == null || appt.getStartTime() < mEarliestAppt.getStartTime()))
                        mEarliestAppt = appt;
                    if ((mLatestAppt == null || appt.getEndTime() > mLatestAppt.getEndTime()))
                        mLatestAppt = appt;
                }
            }
        }
        computeOverlapInfo(msecsIncr);
    }

    public String getFolderId() {
        return mFolderId;
    }
    
    private void computeOverlapInfo(long msecsIncr) {
        mColumns = new ArrayList<List<ZAppointmentHit>>();
        mColumns.add(new ArrayList<ZAppointmentHit>());
        for (ZAppointmentHit appt : mAppts) {
            boolean overlap = false;
            for (List<ZAppointmentHit> col : mColumns) {
                overlap = false;
                for (ZAppointmentHit currentAppt : col) {
                    overlap = appt.isOverLapping(currentAppt, msecsIncr);
                    if (overlap) break;
                }
                if (!overlap) {
                    col.add(appt);
                    break;
                }
            }
            // if we got through all columns with overlap, add one
            if (overlap) {
                List<ZAppointmentHit> newCol = new ArrayList<ZAppointmentHit>();
                newCol.add(appt);
                mColumns.add(newCol);
            }
        }
    }
}
