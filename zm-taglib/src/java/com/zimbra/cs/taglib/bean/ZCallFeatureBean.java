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

import com.zimbra.client.ZCallFeature;

public class ZCallFeatureBean {
    private ZCallFeature mFeature;

    public ZCallFeatureBean(ZCallFeature feature) {
        mFeature = feature;
    }

    public void setIsActive(boolean isActive) {
        mFeature.setIsActive(isActive);
    }

    public boolean getIsActive() {
		return mFeature.getIsActive();
	}

    public boolean getIsSubscribed() {
		return mFeature.getIsSubscribed();
	}

    public String getName() {
		return mFeature.getName();
	}

    protected ZCallFeature getFeature() {
        return mFeature;
    }
}
