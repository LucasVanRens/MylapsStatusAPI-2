package com.mylaps.mylapsstatus.controller;

import com.mylaps.mylapsstatus.model.Maintenance;
import com.mylaps.mylapsstatus.model.SystemObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mylaps.mylapsstatus.service.MaintenanceService;
import com.mylaps.mylapsstatus.service.SystemService;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ApplicationController {

    private final MaintenanceService maintenanceService;
    private final SystemService systemService;

    private final int test = 10;

    public ApplicationController(final MaintenanceService maintenanceService, final SystemService systemService) {
        this.maintenanceService = maintenanceService;
        this.systemService = systemService;
    }

    @RequestMapping(value = "/systems", method = RequestMethod.GET)
    public List<SystemObject> developerHome(final HttpServletRequest request, final HttpServletResponse response) throws SQLException, IOException {
        final List<SystemObject> systems;
        if (CookieCheck(request)) {
            systems = systemService.selectAllSystems();
        } else {
            response.setStatus(401);
            systems = null;
        }
        return systems;
    }

    @RequestMapping(value = "/systems/maintenance", method = RequestMethod.GET)
    public List<Maintenance> maintenance(final HttpServletRequest request, final HttpServletResponse response) throws SQLException, IOException {
        final List<Maintenance> systems;
        if (CookieCheck(request)) {
            systems = maintenanceService.selectAllMaintenance();
        } else {
            response.setStatus(401);
            systems = null;
        }
        return systems;
    }

    @RequestMapping(value = "/systems/maintenance/clear", method = RequestMethod.GET)
    public void maintenanceClearHistory(final HttpServletRequest request, final HttpServletResponse response) throws SQLException, IOException {
        final boolean complete;
        if (CookieCheck(request)) {
            complete = maintenanceService.clearHistory();
            if (complete) {
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
        } else {
            response.setStatus(401);
        }
    }

    @RequestMapping(value = "/systems/maintenance/history", method = RequestMethod.GET)
    public List<Maintenance> maintenanceHistory(final HttpServletRequest request, final HttpServletResponse response) throws SQLException, IOException {
        final List<Maintenance> maintenances;
        if (CookieCheck(request)) {
            maintenances = maintenanceService.selectHistoryMaintenance();
        } else {
            response.setStatus(401);
            maintenances = null;
        }
        return maintenances;
    }

    @RequestMapping(value = "/systems/{systemName}", method = RequestMethod.GET)
    public SystemObject home(@PathVariable("systemName") final String systemName, final HttpServletResponse response, final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            return systemService.oneSystem(systemName);
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(value = "/systems/{systemId}/maintenance/history", method = RequestMethod.GET)
    public List<Maintenance> singleMaintenanceHistory(@PathVariable("systemId") final int systemId, final HttpServletResponse response, final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            return maintenanceService.selectSingleHistoryMaintenance(systemId);
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(value = "/systems/{systemId}/maintenance/new", method = RequestMethod.PUT)
    public void newMaintenance(@PathVariable("systemId") final int systemId,
                               @RequestHeader(value = "startDate") final String startDate,
                               @RequestHeader(value = "endDate") final String endDate, final HttpServletResponse response,
                               final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            if (maintenanceService.newMaintenance(startDate, endDate, systemId)) {
                response.setStatus(201);
            } else {
                response.setStatus(400);
            }
        } else {
            response.setStatus(401);
        }
    }

    @RequestMapping(value = "/systems/{systemId}/maintenance", method = RequestMethod.GET)
    public Maintenance selectSingleMaintenance(@PathVariable("systemId") final int systemId, final HttpServletResponse response,
                                               final HttpServletRequest request) throws SQLException, IOException {
        final Maintenance maintenance;
        if (CookieCheck(request)) {
            maintenance = maintenanceService.selectSingleMaintenance(systemId);
        } else {
            response.setStatus(401);
            maintenance = null;
        }
        return maintenance;
    }

    @RequestMapping(value = "/systems/{systemId}/maintenance/delete", method = RequestMethod.DELETE)
    public void maintenanceDelete(@PathVariable("systemId") final int systemId, final HttpServletResponse response,
                                  final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            if (maintenanceService.deleteMaintenance(systemId)) {
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
        } else {
            response.setStatus(401);
        }
    }

    @RequestMapping(value = "/systems/{systemId}/maintenance/update", method = RequestMethod.PUT)
    public void updateMaintenance(@PathVariable("systemId") final int systemId,
                                  @RequestHeader(value = "startDate") final String startDate,
                                  @RequestHeader(value = "endDate") final String endDate, final HttpServletResponse response,
                                  final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            if (maintenanceService.updateMaintenance(startDate, endDate, systemId)) {
                response.setStatus(201);
            } else {
                response.setStatus(400);
            }
        } else {
            response.setStatus(401);
        }
    }

    @RequestMapping(value = "/systems/{systemName}/delete", method = RequestMethod.DELETE)
    public void systemDelete(@PathVariable("systemName") final String systemName, final HttpServletResponse response,
                             final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            if (systemService.deleteSystem(systemName)) {
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
        } else {
            response.setStatus(401);
        }
    }

    @RequestMapping(value = "/systems/{systemId}/status", method = RequestMethod.PUT)
    public void systemUpdate(@PathVariable final int systemId,
                             @RequestHeader(value = "systemStatus") final String systemStatus, final HttpServletResponse response,
                             final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            if (systemService.updateSystem(systemId, systemStatus)) {
                response.setStatus(201);
            } else {
                response.setStatus(400);
            }
        } else {
            response.setStatus(401);
        }
    }

    @RequestMapping(value = "/systems/new", method = RequestMethod.POST)
    public void createSystem(@RequestHeader(value = "systemName") final String systemName,
                             @RequestHeader(value = "systemStatus") final String systemStatus, final HttpServletResponse response,
                             final HttpServletRequest request) throws SQLException, IOException {
        if (CookieCheck(request)) {
            if (systemService.createNewSystem(systemName, systemStatus)) {
                response.setStatus(201);
            } else {
                response.setStatus(400);
            }
        } else {
            response.setStatus(401);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void loginCheck(@RequestHeader(value = "username") final String username,
                           @RequestHeader(value = "password") final String password, final HttpServletResponse servletResponse) throws JSONException, IOException {
        final DefaultHttpClient httpclient = new DefaultHttpClient();
        final String url = "http://stag-turntable.mylaps.lan:30090/api/v1/authenticate";
        final HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("X-Auth-Username", username);
        httpPost.addHeader("X-Auth-Password", password);

        final HttpResponse response = httpclient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            final JSONObject myJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            final String usernameJSON = myJson.getString("username");
            final String tokenJSON = myJson.getString("token");

            final Cookie cookieToken = new Cookie("access_token", URLEncoder.encode(tokenJSON, "UTF-8"));
            final Cookie cookieName = new Cookie("current_user", URLEncoder.encode(usernameJSON, "UTF-8"));
            cookieToken.setMaxAge(34560);
            cookieName.setMaxAge(34560);
            servletResponse.addCookie(cookieName);
            servletResponse.addCookie(cookieToken);
        }
    }

    private boolean CookieCheck(final HttpServletRequest request) throws IOException {
        final boolean complete;
        final Cookie[] cookies = request.getCookies();
        String token = "";
        String username = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    token = cookie.getValue();
                    token = URLDecoder.decode(token, "UTF-8");
                }
                if (cookie.getName().equals("current_user")) {
                    username = cookie.getValue();
                    username = URLDecoder.decode(username, "UTF-8");
                }
            }
        }

        final DefaultHttpClient httpclient = new DefaultHttpClient();
        final String url = "http://stag-turntable.mylaps.lan:30090/api/v1/admin/users/" + username + "/";
        final HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("X-Auth-Token", token);

        final HttpResponse response = httpclient.execute(httpGet);

        complete = response.getStatusLine().getStatusCode() == 200;
        return complete;
    }
}
