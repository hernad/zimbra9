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

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.VoiceConstants;
import com.zimbra.client.ZCallFeatures;

public class ZCallFeaturesBean {

    private ZCallFeatures mFeatures;

    public ZCallFeaturesBean(ZCallFeatures features, boolean modify) {
        mFeatures = features;
    }

    public ZCallFeatures getCallFeatures() {
        return mFeatures;
    }

    public ZVoiceMailPrefsBean getVoiceMailPrefs() {
        return new ZVoiceMailPrefsBean(mFeatures.getVoiceMailPrefs());
    }

    public ZCallForwardingBean getCallForwardingAll() throws ServiceException {
        return new ZCallForwardingBean(mFeatures.getFeature(VoiceConstants.E_CALL_FORWARD));
    }
    
    public ZCallForwardingBean getCallForwardingNoAnswer() throws ServiceException {
	return new ZCallForwardingBean(mFeatures.getFeature(VoiceConstants.E_CALL_FORWARD_NO_ANSWER));
    }

    public ZSelectiveCallForwardingBean getSelectiveCallForwarding() throws ServiceException {
        return new ZSelectiveCallForwardingBean(mFeatures.getSelectiveCallForwarding());
    }
    
    public ZSelectiveCallRejectionBean getSelectiveCallRejection() throws ServiceException {
        return new ZSelectiveCallRejectionBean(mFeatures.getSelectiveCallRejection());
    }
    
    public ZCallFeatureBean getAnonymousCallRejection() throws ServiceException {
	return new ZCallFeatureBean(mFeatures.getFeature(VoiceConstants.E_ANON_CALL_REJECTION));
    }

    public boolean isEmpty() { return mFeatures.isEmpty(); }
}
