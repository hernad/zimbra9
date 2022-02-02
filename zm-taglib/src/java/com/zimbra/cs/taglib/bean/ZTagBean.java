/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.client.ZTag;

public class ZTagBean {

    private ZTag mTag;
    
    public ZTagBean(ZTag tag){
        mTag = tag;
    }

    public String getId() { return mTag.getId(); }

    public String getName() { return mTag.getName(); }

    public int getUnreadCount() { return mTag.getUnreadCount(); }
    
    public boolean getHasUnread() { return getUnreadCount() > 0; }

    public String getColor() { return mTag.getColor().name(); }
    
    public String getImage() {
        switch(mTag.getColor()) {
        case blue:
            return "zimbra/ImgTagBlue.png";
        case cyan:
            return "zimbra/ImgTagCyan.png";
        case green:
            return "zimbra/ImgTagGreen.png";
        case purple: 
            return "zimbra/ImgTagPurple.png";
        case red:
            return "zimbra/ImgTagRed.png";
        case yellow: 
            return "zimbra/ImgTagYellow.png";
        case orange:
        case defaultColor:
        default:
            return "zimbra/ImgTagOrange.png";
        }
    }
    
    public String getMiniImage() {
        switch(mTag.getColor()) {
        case blue:
            return "zimbra/ImgTagBlue.png";
        case cyan:
            return "zimbra/ImgTagCyan.png";
        case green:
            return "zimbra/ImgTagGreen.png";
        case purple: 
            return "zimbra/ImgTagPurple.png";
        case red:
            return "zimbra/ImgTagRed.png";
        case yellow: 
            return "zimbra/ImgTagYellow.png";
        case orange:
        case defaultColor:
        default:
            return "zimbra/ImgTagOrange.png";
        }
    }

}
