<%@ page import="static java.util.Collections.list" %>
<%@ page import="static java.util.stream.Collectors.toMap" %>
<%@ page import="static java.util.function.Function.identity" %>
<%@ page import="java.util.*" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Map headers = new HashMap();
    Enumeration names = request.getHeaderNames();
    while (names.hasMoreElements()) {
        String name = (String) names.nextElement();
        Enumeration<String> values = request.getHeaders(name);
        List valueList = new ArrayList();
        while (values.hasMoreElements()) {
            valueList.add(values.nextElement());
        }
        headers.put(name, valueList);
    }

    Map attributes = new HashMap();
    Enumeration attributeNames = request.getAttributeNames();
    while (attributeNames.hasMoreElements()) {
        String name = (String) attributeNames.nextElement();
        attributes.put(name, request.getAttribute(name));
    }
%>
<html>
<head>
    <title>Request Dumper</title>
</head>

<body>

<h1>Request Dumper</h1>

<h2>Request Details</h2>
<table>
    <tr>
        <td>RemoteAdr</td>
        <td><%=request.getRemoteAddr()%>
        </td>
    </tr>
    <tr>
        <td>RemoteHost</td>
        <td><%=request.getRemoteHost()%>
        </td>
    </tr>
    <tr>
        <td>RemotePort</td>
        <td><%=request.getRemotePort()%>
        </td>
    </tr>
    <tr>
        <td>LocalAdr</td>
        <td><%=request.getLocalAddr()%>
        </td>
    </tr>
    <tr>
        <td>LocalHost</td>
        <td><%=request.getLocalName()%>
        </td>
    </tr>
    <tr>
        <td>LocalPort</td>
        <td><%=request.getLocalPort()%>
        </td>
    </tr>
    <tr>
        <td>ServerName</td>
        <td><%=request.getServerName()%>
        </td>
    </tr>
    <tr>
        <td>ServerPort</td>
        <td><%=request.getServerPort()%>
        </td>
    </tr>
    <tr>
        <td>Scheme</td>
        <td><%=request.getScheme()%>
        </td>
    </tr>
    <tr>
        <td>isSecure</td>
        <td><%=request.isSecure()%>
        </td>
    </tr>
</table>


<h2>Request Headers</h2>
<table>
    <%
        for (Object entry : headers.entrySet()) {
    %>
    <tr>
        <td><%=((Map.Entry) entry).getKey()%>
        </td>
        <td><%=((Map.Entry) entry).getValue()%>
        </td>
    </tr>
    <%
        }
    %>
</table>

<h2>Request Attributes</h2>
<table>
    <%
        for (Object entry : attributes.entrySet()) {
    %>
    <tr>
        <td><%=((Map.Entry) entry).getKey()%>
        </td>
        <td><%=((Map.Entry) entry).getValue()%>
        </td>
    </tr>
    <%
        }
    %>
</table>

<hr/>

<ul>
    <li><a href="redirect1.jsp">Test redirect page 1</a></li>
    <li><a href="redirect2.jsp">Test redirect page 2</a></li>
</ul>

</body>
</html>
