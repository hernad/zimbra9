/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import com.google.common.base.Strings;
import com.zimbra.client.ZContactHit;
import com.zimbra.client.ZEmailAddress;

public class ZContactHitBean extends ZSearchHitBean {

    private final ZContactHit mHit;

    public ZContactHitBean(ZContactHit hit) {
        super(hit, HitType.contact);
        mHit = hit;
    }

    public String getFlags() { return mHit.getFlags(); }

    public boolean getHasFlags() { return mHit.hasFlags(); }

    public boolean getIsFlagged() { return mHit.isFlagged(); }

    public boolean getHasTags() { return mHit.hasTags(); }

    public boolean getIsGroup() { return mHit.isGroup(); }

    public String getTagIds() { return mHit.getTagIds(); }

    public String getFolderId() { return mHit.getFolderId(); }

    public String getRevision() { return mHit.getRevision(); }

    public String getFileAsStr() { return mHit.getFileAsStr(); }

    public String getEmail() { return mHit.getEmail(); }

    public String getEmail2() { return mHit.getEmail2(); }

    public String getEmail3() { return mHit.getEmail3(); }

    public String getWorkEmail1() { return mHit.getWorkEmail1(); }

    public String getWorkEmail2() { return mHit.getWorkEmail2(); }

    public String getWorkEmail3() { return mHit.getWorkEmail3(); }

    public String getType() { return mHit.getType(); }

    public String getDlist() { return mHit.getDlist(); }

    @Override
    public String toString() { return mHit.toString(); }

    public String getFullName() { return mHit.getFullName(); }
    public String getFileAs() { return mHit.getFileAs(); }
    public String getNickname() { return mHit.getNickname(); }
    public String getNamePrefix() { return mHit.getNamePrefix(); }
    public String getFirstName() { return mHit.getFirstName(); }
    public String getPhoneticFirstName() { return mHit.getPhoneticFirstName(); }
    public String getMiddleName() { return mHit.getMiddleName(); }
    public String getMaidenName() { return mHit.getMaidenName(); }
    public String getLastName() { return mHit.getLastName(); }
    public String getPhoneticLastName() { return mHit.getPhoneticLastName(); }
    public String getNameSuffix() { return mHit.getNameSuffix(); }
    public String getCompany() { return mHit.getCompany(); }
    public String getPhoneticCompany() { return mHit.getPhoneticCompany(); }

    /**
     * @return time in msecs
     */
    public long getMetaDataChangedDate() { return mHit.getMetaDataChangedDate(); }

    /**
     * @return first email from email/2/3 that is set, or an empty string
     */
    public String getDisplayEmail() {
        if (getIsGroup())
            return getDlist();
        else if (getEmail() != null && getEmail().length() > 0)
            return getEmail();
        else if (getEmail2() != null && getEmail2().length() > 0)
            return getEmail2();
        else if (getEmail3() != null && getEmail3().length() > 0)
            return getEmail3();
        else
            return "";
    }

    /**
     *
     * @return the "full" email address suitable for inserting into a To/Cc/Bcc header
     */
    public String getFullAddress() {
        if (getIsGroup()) {
            return getDlist();
        } else {
            /* Used to use FileAsStr but Bug 97900 suggests the default "Last, First" form wasn't appropriate for
             * Japanese - where the "," should be omitted. We actually use that form in FullName (and the Ajax
             * client currently uses that).  Also:
             *     http://www.slipstick.com/outlook/contacts/bulk-change-outlook-contacts-email-display-name-format/
             * has this to say:
             *     The default email display name in the address book used to be "Full Name (Email1)",
             *     "Full Name (Email2)", "Full Name (Email3)" but beginning with Outlook 2002,
             *     it was changed to "Full Name (email address)"
             * all of which implies that FullName is more appropriate if Outlook compatibility is desired.
             */
            String commentPart = getFullName();
            if (Strings.isNullOrEmpty(commentPart)) {
                commentPart = Strings.nullToEmpty(getFileAsStr());
            }
            return new ZEmailAddress(getDisplayEmail(), null, commentPart,
                            ZEmailAddress.EMAIL_TYPE_TO).getFullAddress();
        }
    }

    public String getImage() {
        if (getIsGroup())
            return "contacts/ImgGroup.png";
        else
            return "contacts/ImgContact.png";
    }

    public String getImageAltKey() {
        if (getIsGroup())
            return "ALT_CONTACT_GROUP";
        else
            return "ALT_CONTACT_CONTACT";
    }
}
