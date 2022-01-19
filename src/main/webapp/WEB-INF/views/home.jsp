<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Sign In  
</h1>
<form:form action="signIn" modelAttribute="User">
	UserName <input type="text" name="userId"/> <br>
	Password <input type="text" name="password"/> <br>
	nickName <input type="text" name="nickname"/> <br>
<br>
<input type="submit" name="submit"/>
</form:form>
<br>
</body>
</html>
