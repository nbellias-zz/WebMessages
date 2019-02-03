<%@page import="models.Webuser"%>
<%@page import="controllers.WebuserJpaController"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="javax.persistence.Persistence"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Messages - Login</title>
</head>
<body>
    <h1>Welcome to Messages</h1>
    <h3>Login</h3>
    <%
        String usrnm = request.getParameter("username");
        String pwd = request.getParameter("password");
            
        if(request.getMethod().equals("POST")){
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("WebMessagesPU");
            WebuserJpaController wjc = new WebuserJpaController(emf);
            
            Webuser loggedinUser = wjc.checkLogin2(usrnm, pwd);
            
            if(loggedinUser != null/*wjc.checkLogin(usrnm,pwd)*/){
                if(usrnm.equals("admin")){
                    session.setAttribute("user", "admin");
                    session.setAttribute("userObject", loggedinUser);
                    response.sendRedirect("adminpage.jsp");
                } else {
                    session.setAttribute("user", "user");
                    session.setAttribute("userObject", loggedinUser);
                    response.sendRedirect("messages.jsp");
                }
            } else {
                out.print("<h3>No such username or password exists!</h3>");
            }
            
            emf.close();
        }
    %>
    <form action="login.jsp" method="post">
        <input type="text" name="username" id="username" placeholder="Enter your username" autocomplete="off" required><br>
        <input type="password" name="password" id="password" placeholder="Enter your password" required><br>
        <input type="submit" value="Login"><br>
    </form>
    <ul>
        <li><a href="../index.jsp">Home</a></li>
    </ul>
    <hr>
    <h4>Copyright My Name 2019</h4>
</body>
</html>