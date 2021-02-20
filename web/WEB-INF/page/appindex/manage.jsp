<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title></title>
    <%
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	%>
	<script>
	  var server_base_url = '<%=basePath%>';
	</script>
    <base href="${basePath}">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
</head>
<body>

</body>
<script type="text/javascript">
 var user = "${appUser}";
 console.log(user);
 if(user){
 	window.location.href="http://xunjianh5.dofuntech.cn/index.html?userId="+user.id;
 }
</script>
</html>