<%-- 
    Document   : messages
    Created on : 30 Ιαν 2019, 9:38:20 μ.μ.
    Author     : nikolaos
--%>

<%@page import="controllers.WebuserJpaController"%>
<%@page import="java.util.List"%>
<%@page import="models.Webmessages"%>
<%@page import="models.Webuser"%>
<%@page import="controllers.WebmessagesJpaController"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Messages - User Messages</title>
    </head>
    <body>
        <%
            if (session.getAttribute("user") == null
                    || session.getAttribute("user") != "user") {
                response.sendRedirect("login.jsp");
            }
        %>
        <h1>User Page</h1>
        <ul>
            <li><a href="userprofile.jsp">Your Profile</a></li>
            <li><a href="../index.jsp">Home</a></li>
        </ul>
        <%
            out.print("<h3>" + session.getAttribute("userObject") + "</h3>");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("WebMessagesPU");
            WebmessagesJpaController wjc = new WebmessagesJpaController(emf);
            List<Webmessages> messages = wjc.fetchMessagesFromUser(((Webuser) session.getAttribute("userObject")).getUserid());
        %>
        <h3>Incoming Messages</h3>
        <%
            if (request.getMethod().equals("POST")
                    && request.getParameter("check").equals("table")
                    && request.getParameter("messageid") != null) {
                EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("WebMessagesPU");
                WebmessagesJpaController wjc2 = new WebmessagesJpaController(emf2);
                wjc2.destroy(Integer.parseInt(request.getParameter("messageid")));
            }
        %>
        <form action="messages.jsp" method="post">
            <input type="hidden" name="check" value="table">
            <table>
                <tr>
                    <th>FROM</th>
                    <th>MESSAGE</th>
                    <th>DATE</th>
                    <th></th>
                </tr>

                <%
                    for (Webmessages message : messages) {
                %>
                <tr>
                    <td><%=message.getFromuserid().getLname()%></td>
                    <td><%=message.getMessage()%></td>
                    <td><%=message.getDoc()%></td>
                <input type="hidden" name="messageid" value="<%=message.getMsgid()%>" >
                <td><input type="submit" value="Delete"></td>
                </tr>
                <%
                    }

                    emf.close();
                %>
            </table>
        </form>
        <hr>
        <h4>New Message</h4>
        <%
            if (request.getMethod().equals("POST")
                    && request.getParameter("check").equals("usermessage")) {
                EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("WebMessagesPU");
                WebmessagesJpaController wjc1 = new WebmessagesJpaController(emf1);
                WebuserJpaController userController = new WebuserJpaController(emf1);
                Webuser touser = userController.findWebuserByUsername(request.getParameter("user"));
                Webmessages message = new Webmessages((Webuser) session.getAttribute("userObject"),
                        touser,
                        request.getParameter("msg"));
                wjc1.create(message);
                out.print("<h4>Message successfully sent</h4>");
                emf1.close();
            }
        %>

        <form action="messages.jsp" method="post">
            <input type="hidden" name="check" value="usermessage">
            <input type="text" name="user" placeholder="Enter the user to send your message"><br>
            <textarea rows="5" cols="30" name="msg" placeholder="Enter your message"></textarea><br>
            <input type="submit" value="Send">
            <input type="reset" value="Clear">
        </form>

        <hr>
        <h4>Copyright My Name 2019</h4>
    </body>
</html>
