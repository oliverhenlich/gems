<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    out.println("Using request.getScheme() <br/>");

    if (!request.getScheme().equalsIgnoreCase("https")){
        response.sendRedirect("https://"  +
                request.getServerName() +
                request.getRequestURI() +
                "?redirected=true");
    }
    else if (request.getParameter("redirected") != null && request.getParameter("redirected").equals("true")){
        out.println("Here after being re-directed to a https url.");
    }
    else {
        out.println("Already secure.");
    }
%>



