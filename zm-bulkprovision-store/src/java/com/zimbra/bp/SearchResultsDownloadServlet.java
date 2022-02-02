package com.zimbra.bp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.account.AuthToken;
import com.zimbra.cs.extension.ExtensionHttpHandler;
import com.zimbra.cs.extension.ZimbraExtension;
import com.zimbra.cs.servlet.ZimbraServlet;

public class SearchResultsDownloadServlet extends ExtensionHttpHandler {
    public static final String HANDLER_NAME = "search_results_download";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            // check the auth token
            AuthToken authToken = ZimbraServlet.getAdminAuthTokenFromCookie(req, resp);
            if (authToken == null || !AuthToken.isAnyAdmin(authToken)) {
                sendError(resp, HttpServletResponse.SC_FORBIDDEN, "Auth failed");
                return;
            }

            ZimbraLog.extensions.debug("Received a download request for search results");

            String query = req.getParameter("q");
            String domain = req.getParameter("domain");
            String types = req.getParameter("types");
            if(types == null) {
                //DirectorySearch won't work without types, so make it required
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "'types' parameter is required");
                return;
            }
            resp.setHeader("Expires", "Tue, 24 Jan 2000 20:46:50 GMT");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/x-download");
            resp.setHeader("Content-Disposition", "attachment; filename=search_result.csv");
            SearchResults.writeSearchResultOutputStream(resp.getOutputStream(), query, domain, types, authToken);
        } catch (Exception e) {
            ZimbraLog.extensions.error(e);
            return;
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }

    @Override
    public String getPath() {
        return super.getPath() + "/" + HANDLER_NAME;
    }

    @Override
    public void init(ZimbraExtension ext) throws ServiceException {
        super.init(ext);
        ZimbraLog.extensions.info("Handler at " + getPath() + " starting up");
    }

    @Override
    public void destroy() {
        ZimbraLog.extensions.info("Handler at " + getPath() + " shutting down");
    }

    private void sendError(HttpServletResponse resp, int sc, String msg) throws IOException {
        ZimbraLog.extensions.error(msg);
        resp.sendError(sc, msg);
    }
}
