<%@page import="encryption.CryptoConverter"%>
<%@page import="java.util.Date"%>
<%@page import="models.Webuser"%>
<%@page import="controllers.WebuserJpaController"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <%
            if (request.getMethod().equals("POST")
                    && request.getParameter("newpassword2").equals(request.getParameter("newpassword1"))) {
                out.print("<meta http-equiv=\"refresh\" content=\"3;url=login.jsp\" />");
            }
        %>
        <title>Messages - Register</title>
        <link rel="stylesheet" href="../css/mystyles.css">
    </head>
    <body>
        <h1>Welcome to Messages</h1>
        <h3>Register</h3>
        <%
            String fname = "";
            String lname = "";
            String email = "";
            String username = "";
            String newpassword1 = "";
            String newpassword2 = "";

            if (request.getMethod().equals("POST")) {
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("WebMessagesPU");
                WebuserJpaController wjc = new WebuserJpaController(emf);

                fname = request.getParameter("fname");
                lname = request.getParameter("lname");
                email = request.getParameter("email");
                username = request.getParameter("username");
                newpassword1 = request.getParameter("newpassword1");
                newpassword2 = request.getParameter("newpassword2");

                if (newpassword2.equals(newpassword1)) {
                    Webuser newUser = new Webuser(username, CryptoConverter.encrypt(newpassword2), new Date(), fname, lname, email);
                    wjc.create(newUser);
                    fname = "";
                    lname = "";
                    email = "";
                    username = "";
                    newpassword1 = "";
                    newpassword2 = "";
                    out.print("<h3>Your profile succesfully created!</h3>");
                    out.print("<h4>Redirecting to Login page in 3 seconds...</h4>");
                } else {
                    out.print("<h3>Passwords do not match. Please try again!</h3>");
                }

                emf.close();
            }
        %>
        <form action="register.jsp" method="post" >
            <input type="text" name="fname" id="fname" value="<%=fname%>" placeholder="Enter your First name" autocomplete="off" required><br>
            <input type="text" name="lname" id="lname" value="<%=lname%>" placeholder="Enter your Last name" autocomplete="off" required><br>
            <input type="email" name="email" id="email" value="<%=email%>" placeholder="Enter your E-Mail" autocomplete="off" required><br>
            <input type="text" name="username" id="username" value="<%=username%>" placeholder="Choose your Username" autocomplete="off" required><br>
            <input type="password" name="newpassword1" value="<%=newpassword1%>" id="newpassword1" placeholder="Choose your password" required><br>
            <input type="password" name="newpassword2" value="<%=newpassword2%>" id="newpassword2" placeholder="Enter again your password" required><br>
            <br>
            <input type="submit" value="Save">
            <input type="reset" value="Clear"><br>
        </form>
        <ul>
            <li><a href="../index.jsp">Home</a></li>
        </ul>
        <hr>
        <h4>Copyright My Name 2019</h4>
    </body>
</html>