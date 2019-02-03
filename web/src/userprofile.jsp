<%@page import="encryption.CryptoConverter"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.util.Base64"%>
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
        <title>Messages - User's Profile</title>
        <link rel="stylesheet" href="../css/mystyles.css">
    </head>
    <body>
        <%
            if (session.getAttribute("user") == null
                    || session.getAttribute("user") != "user") {
                response.sendRedirect("login.jsp");
            }

            String message = "";
            String messagePwd = "";
            String messagePhoto = "";
            boolean changePwd = false;
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("WebMessagesPU");
            WebuserJpaController wjc = new WebuserJpaController(emf);
            Webuser user = wjc.findWebuser(((Webuser) session.getAttribute("userObject")).getUserid());

            byte[] photo = {};
            String photoPic = new String();
            if (user.getPhoto() != null) {
                photo = user.getPhoto();
                byte[] encodeBase64 = Base64.getEncoder().encode(photo);
                photoPic = new String(encodeBase64, "UTF-8");
            }
            String fname = user.getFname();
            String lname = user.getLname();
            String email = user.getEmail();
            String oldpassword = "";
            String newpassword1 = "";
            String newpassword2 = "";

            // If the logged in user clicks the save button in the second form
            if (request.getMethod().equals("POST")
                    && request.getParameter("savedata") != null) {

                fname = request.getParameter("fname");
                lname = request.getParameter("lname");
                email = request.getParameter("email");
                oldpassword = request.getParameter("oldpassword");
                newpassword1 = request.getParameter("newpassword1");
                newpassword2 = request.getParameter("newpassword2");

                // If oldpassword input field has been filled in
                // and oldpassword input field value is equal to database presently stored password
                // and newpassword1, newpassword2 input fields have been filled in
                // and newpassword1, newpassword2 input field values are equal
                if (!oldpassword.equals("")
                        && oldpassword.equals(CryptoConverter.decrypt(user.getUserpwd()))
                        && !newpassword1.equals("")
                        && !newpassword2.equals("")
                        && newpassword2.equals(newpassword1)) {
                    changePwd = true;
                }
                // If oldpassword input field has been filled in
                // but oldpassword input field value is NOT equal to database presently stored password
                // and newpassword1, newpassword2 input fields have been filled in
                // and newpassword1, newpassword2 input field values are equal
                if (!oldpassword.equals("")
                        && !oldpassword.equals(CryptoConverter.decrypt(user.getUserpwd()))
                        && !newpassword1.equals("")
                        && !newpassword2.equals("")
                        && newpassword2.equals(newpassword1)) {
                    messagePwd = "<h4>Wrong present password. Please try again!</h4>";
                }
                // If oldpassword input field has been filled in
                // and oldpassword input field value is equal to database presently stored password
                // and newpassword1, newpassword2 input fields have been filled in
                // but newpassword1, newpassword2 input field values are NOT equal
                if (!oldpassword.equals("")
                        && oldpassword.equals(CryptoConverter.decrypt(user.getUserpwd()))
                        && !newpassword1.equals("")
                        && !newpassword2.equals("")
                        && !newpassword2.equals(newpassword1)) {
                    messagePwd = "<h4>New passwords do not match! Please try again!</h4>";
                }
                if (!oldpassword.equals("")
                        && oldpassword.equals(CryptoConverter.decrypt(user.getUserpwd()))
                        && (newpassword1.equals("") || newpassword2.equals(""))) {
                    messagePwd = "<h4>New passwords needed to be filled in! Please try again!</h4>";
                }
                // In any case store all other data 
                // and then clear values for oldpassword, newpassword1 and newpassword2
                user.setFname(fname);
                user.setLname(lname);
                user.setEmail(email);
                if (changePwd) {
                    user.setUserpwd(CryptoConverter.encrypt(newpassword2));
                    messagePwd = "<h4>New password succesfully updated!</h4>";
                }
                wjc.edit(user);
                oldpassword = "";
                newpassword1 = "";
                newpassword2 = "";
                message = "<h4>User succesfully Updated!</h4>";
                // Fetch the newly updated user
                user = wjc.findWebuser(user.getUserid());
                // Close the emf
                emf.close();
            }

            // If the logged in user clicks the save button in the first form with the photo
            if (request.getMethod().equals("POST")
                    && request.getContentType() != null) {

                // Verify the content type
                String contentType = request.getContentType();
                String saveFile = "";

                if (contentType.indexOf("multipart/form-data") >= 0) {
                    DataInputStream in = new DataInputStream(request.getInputStream());
                    int formDataLength = request.getContentLength();
                    byte dataBytes[] = new byte[formDataLength];
                    int byteRead = 0;
                    int totalBytesRead = 0;
                    while (totalBytesRead < formDataLength) {
                        byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                        totalBytesRead += byteRead;
                    }
                    String file = new String(dataBytes);
                    saveFile = file.substring(file.indexOf("filename=\"") + 10);
                    saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
                    saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
                            saveFile.indexOf("\""));
                    int lastIndex = contentType.lastIndexOf("=");
                    String boundary = contentType.substring(lastIndex + 1,
                            contentType.length());
                    int pos;
                    pos = file.indexOf("filename=\"");
                    pos = file.indexOf("\n", pos) + 1;
                    pos = file.indexOf("\n", pos) + 1;
                    pos = file.indexOf("\n", pos) + 1;
                    int boundaryLocation = file.indexOf(boundary, pos) - 4;
                    int startPos = ((file.substring(0, pos)).getBytes()).length;
                    int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
                    File ff = new File(saveFile);
                    FileOutputStream fileOut = new FileOutputStream(ff);
                    fileOut.write(dataBytes, startPos, (endPos - startPos));
                    fileOut.flush();
                    fileOut.close();

                    File f = new File(saveFile);
                    user.setPhoto(Files.readAllBytes(f.toPath()));
                    wjc.edit(user);
                    messagePhoto = "<h4>User Photo succesfully Updated!</h4>";
                    // Fetch the newly stored photo
                    photo = user.getPhoto();
                    byte[] encodeBase64 = Base64.getEncoder().encode(photo);
                    photoPic = new String(encodeBase64, "UTF-8");
                    // Close the emf
                    emf.close();
                }
            }
        %>
        <h1>Your Profile</h1>
        <h3>Here you can make changes to your personal data</h3>
        <%
            out.print("<h3>" + user + "</h3>");
            if (!messagePwd.equals("")) {
                out.print(messagePwd);
            } else {
                out.print(message);
            }
            if (!messagePhoto.equals("")) {
                out.print(messagePhoto);
            }
        %>
        <h3>Your Photo</h3>
        <p><img src="data:image/jpeg;base64,<%=photoPic%>" width="200px"/></p>
        <form action="userprofile.jsp" method="post" enctype="multipart/form-data">
            <input type="hidden" name="savephoto" value="true">
            <label for="file">Choose your Photo and click 'Save' (only PNG or JPEG files)</label>
            <input type="file" name="file"  size="50" accept="image/png, image/jpeg">
            <input type="submit" value="Save" >
        </form>
        <hr>
        <form action="userprofile.jsp" method="post" >
            <input type="hidden" name="savedata" value="true">
            <input type="text" name="fname" id="fname" value="<%=fname%>" placeholder="Enter your First name" autocomplete="off" required><br>
            <input type="text" name="lname" id="lname" value="<%=lname%>" placeholder="Enter your Last name" autocomplete="off" required><br>
            <input type="email" name="email" id="email" value="<%=email%>" placeholder="Enter your E-Mail" autocomplete="off" required><br>
            <input type="text" value="<%=user.getUsername() %>" disabled="true"><br>
            <input type="password" name="oldpassword" value="<%=oldpassword%>" id="oldpassword" placeholder="Enter your Present password"><br>
            <input type="password" name="newpassword1" value="<%=newpassword1%>" id="newpassword1" placeholder="Choose your New password"><br>
            <input type="password" name="newpassword2" value="<%=newpassword2%>" id="newpassword2" placeholder="Enter again your New password"><br>
            <br>
            <input type="submit" value="Save">
            <input type="reset" value="Clear"><br>
        </form>
        <ul>
            <li><a href="messages.jsp">User Messages</a></li>
            <li><a href="../index.jsp">Home</a></li>
        </ul>
        <hr>
        <h4>Copyright My Name 2019</h4>
    </body>
</html>