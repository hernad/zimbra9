/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.zimbra.client.ZMailbox;
import com.zimbra.common.service.ServiceException;

public class ZFileUploaderBean {

    private static final long DEFAULT_MAX_SIZE = 100 * 1024 * 1024;

    private boolean mIsUpload;
    private List<FileItem> mFiles;
    private Map<String,List<String>> mParamValues;
    private Map<String,String> mParams;

    @SuppressWarnings("unchecked")
    public ZFileUploaderBean(PageContext pageContext, ZMailbox mailbox) throws JspTagException, ServiceException {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        ServletFileUpload upload = getUploader();
        try {

            mIsUpload = ServletFileUpload.isMultipartContent(req);
            if (mIsUpload) {
                mParamValues = new HashMap<String, List<String>>();
                mParams = new HashMap<String, String>();
                mFiles = new ArrayList<FileItem>();
                init(pageContext, upload.parseRequest(req));
            }
        } catch (FileUploadBase.SizeLimitExceededException e) {
            // at least one file was over max allowed size
            throw new JspTagException(ZTagLibException.UPLOAD_SIZE_LIMIT_EXCEEDED("size limit exceeded", e));
        } catch (FileUploadBase.InvalidContentTypeException e) {
            // at least one file was of a type not allowed
            throw new JspTagException(ZTagLibException.UPLOAD_FAILED(e.getMessage(), e));
        } catch (FileUploadException e) {
            // parse of request failed for some other reason
            throw new JspTagException(ZTagLibException.UPLOAD_FAILED(e.getMessage(), e));
        }
    }

    private void init(PageContext pageContext, List<FileItem> items) throws ServiceException {
        for (FileItem item : items) {
            String name = item.getFieldName();
            if (!item.isFormField()) {
                if (item.getName() != null && item.getName().length() > 0) {
                    mFiles.add(item);
                    List<String> values = mParamValues.get(name);
                    if (values == null) {
                        values = new ArrayList<String>();
                        mParamValues.put(name, values);
                        mParams.put(name, item.getName());
                    }
                    values.add(item.getName());
                }
            } else {

                String value;
                try { value = item.getString("utf-8"); } catch (UnsupportedEncodingException e) { value = item.getString();}
                // normalize action params from image submits
                if (name.startsWith("action") && name.endsWith(".x")) {
                    name = name.substring(0, name.length()-2);
                }
                List<String> values = mParamValues.get(name);
                if (values == null) {
                    values = new ArrayList<String>();
                    mParamValues.put(name, values);
                    mParams.put(name, value);
                }
                values.add(value);
            }
        }
    }

    public List<FileItem> getFiles() {
        return mFiles;
    }

    public boolean hasParam(String name) { return mParamValues.get(name) != null; }

    public long getParamLong(String name, long defaultValue) {
        String v = getParam(name);
        if (v != null)
            try { return Long.parseLong(v); } catch (NumberFormatException e) {}
        return defaultValue;
    }

    public int getParamInt(String name, int defaultValue) {
        String v = getParam(name);
        if (v != null)
            try { return Integer.parseInt(v); } catch (NumberFormatException e) {}
        return defaultValue;
    }

    /**
     * Returns the value for the given param if present, otherwise null. If param has multiple values, only the
     * first is returned.
     *
      * @param name parameter name
     * @return the value for the given param.
     */
    public String getParam(String name) {
        List<String> values = mParamValues.get(name);
        return values == null ? null : values.get(0);
    }

    /**
     * Returns the value for the given param if present, otherwise null.
     *
      * @param name parameter name
     * @return the value for the given param.
     */
    public List<String> getParamValueList(String name) {
        return mParamValues.get(name);
    }

    public Map<String,List<String>> getParamValues() {
        return mParamValues;
    }

    public Map<String,String> getParams() {
        return mParams;
    }

    public boolean getIsUpload() { return mIsUpload;}

    private static ServletFileUpload getUploader() {
        // look up the maximum file size for uploads
        // TODO: get from config,
        DiskFileItemFactory dfif = new DiskFileItemFactory();
        ServletFileUpload upload;

        dfif.setSizeThreshold(32 * 1024);
        dfif.setRepository(new File(getTempDirectory()));
        upload = new ServletFileUpload(dfif);
        upload.setSizeMax(DEFAULT_MAX_SIZE);
        return upload;
    }

    private static String getTempDirectory() {
    	return System.getProperty("java.io.tmpdir", "/tmp");
    }

    public String getUploadId(ZMailbox mailbox) throws ServiceException {
        if (!mFiles.isEmpty()) {
            Map<String,byte[]> attachments = new HashMap<String,byte[]>();
            int i=0;
            for (FileItem item : mFiles) {
                attachments.put(item.getName(), item.get());
            }
            try {
                return mailbox.uploadAttachments(attachments, 1000*60); //TODO get timeout from config
            } finally {
                for (FileItem item : mFiles) {
                    try { item.delete(); } catch (Exception e) { /* TODO: need logging infra */ }
                }
            }
        }
        return null;
    }
}
