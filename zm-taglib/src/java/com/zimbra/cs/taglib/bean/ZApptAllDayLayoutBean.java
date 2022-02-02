/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ZApptAllDayLayoutBean {

    long mStartTime;
    long mEndTime;
    int mNumDays;

    public static final String PSTATUS_DECLINED = "DE";

    List<ZAppointmentHit> mAllday; // all all-day appts in this range
    List<List<ZAppointmentHit>> mRows;

    public long getStartTime() { return mStartTime; }
    public long getEndTime() { return mEndTime; }

    public List<ZAppointmentHit> getAllDayAppts() { return mAllday; }
    public List<List<ZAppointmentHit>> getRows() { return mRows; }
    public Date getDate() { return new Date(mStartTime); }

    public int getNumberOfRows() {
        return mRows.size();
    }

    public ZApptAllDayLayoutBean(List<ZAppointmentHit> appts, long startTime, long endTime, int numDays, boolean scheduleMode, boolean isShowDeclined) {
        mAllday = new ArrayList<ZAppointmentHit>();
        mStartTime = startTime;
        mEndTime = endTime;
        mNumDays = numDays;

        for (ZAppointmentHit appt : appts) {
            if (appt.isAllDay() && appt.isInRange(mStartTime, mEndTime) 
                    && (!appt.getParticipantStatus().equals(PSTATUS_DECLINED) || (appt.getParticipantStatus().equals(PSTATUS_DECLINED) && isShowDeclined))) {
                mAllday.add(appt);
            }
        }
        Collections.sort(mAllday, new Comparator<ZAppointmentHit>() {
            public int compare(ZAppointmentHit a1, ZAppointmentHit a2) {
                return new Long(((a2.getEndTime() - mStartTime) - (a1.getEndTime() - mStartTime))).intValue();
            }
        });
        computeOverlapInfo(scheduleMode);
    }

    private void computeOverlapInfo(boolean scheduleMode) {
        mRows = new ArrayList<List<ZAppointmentHit>>();
        mRows.add(new ArrayList<ZAppointmentHit>());
        for (ZAppointmentHit appt : mAllday) {
            boolean overlap = false;
            for (List<ZAppointmentHit> row : mRows) {
                overlap = false;
                if (!scheduleMode) {
                    for (ZAppointmentHit currentAppt : row) {
                        overlap = appt.isOverLapping(currentAppt);
                        if (overlap) break;
                    }
                } else {
                     for (ZAppointmentHit currentAppt : row) {
                        overlap = appt.getFolderId().equals(currentAppt.getFolderId());
                        if (overlap) break;
                    }
                }
                if (!overlap) {
                    row.add(appt);
                    break;
                }
            }
            // if we got through all rows with overlap, add one
            if (overlap) {
                List<ZAppointmentHit> newRow = new ArrayList<ZAppointmentHit>();
                newRow.add(appt);
                mRows.add(newRow);
            }
        }
    }
}
