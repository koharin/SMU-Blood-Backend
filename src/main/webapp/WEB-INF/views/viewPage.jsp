<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
Welcome, ${user.nickname} <br>
userId: ${user.userId} <br>
password: ${user.password} <br>
nickname: ${user.nickname} <br> 
<a href="save">save</a>
</body>
</html>