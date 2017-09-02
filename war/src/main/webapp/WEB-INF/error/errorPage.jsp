<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page isErrorPage = "true"%>
<%@ page import = "java.io.*" %>
<HTML>
    <HEAD>
        <TITLE> Sample - FATAL ERROR </TITLE>
        <style type="text/css">
        	body {
        		background: #eeeeee url("/Sample/javax.faces.resource/images/ui-bg_diagonals-thick_90_eeeeee_40x40.png.jsf?ln=primefaces-cupertino") 50% 50% repeat;
        	}
			#header {	
				opacity: 0.7;
				position: relative;
				width: 100%;
				height: 100%;
				background-color: #FFC9C9;
				-webkit-box-shadow: 0 8px 6px -6px black;
				   -moz-box-shadow: 0 8px 6px -6px black;
				        box-shadow: 0 8px 6px -6px black;
			}
</style>
    </head>
    <body>
    	  <div id="header">	            
	      	<h1>A Aplicação gerou um erro grave!</h1>
	      	<h3>Um e-mail com o conteúdo do erro foi enviado ao suporte.</h3>
	      	<b>Erro: </b>
       		<font style="font-weight: bold;" color="#D10000"><%= exception.getMessage() %></font>
       		<br/>
	      </div>
    </body>
</html>