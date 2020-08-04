package br.erp.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


@Path("SessionUtils")
public class SessionUtils extends HttpServlet {
    @Context
    HttpServletRequest request;

    @Path("/isAdmin")
    @POST
    @Produces("*/*")
    public Response isAdmin(){

        HttpSession session = ((HttpServletRequest) request).getSession();
        Object admin = session.getAttribute("admin");
        String adminConvert = admin.toString();
        UtilRest UR = new UtilRest();
        return UR.buildResponse(Boolean.parseBoolean(adminConvert));

    }
    @Path("/getName")
    @POST
    @Produces("*/*")
    public Response getName(){

        HttpSession session = ((HttpServletRequest) request).getSession();
        Object name = session.getAttribute("name");
        String nameConverted = name.toString();
        UtilRest UR = new UtilRest();
        return UR.buildResponse(nameConverted);

    }

}
