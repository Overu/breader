<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>

<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="java.util.List" %>
<%
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  List<String> keys = (List<String>)request.getAttribute("keys");
%>
<html>
    <body>
    <%
      for(String key : keys) {
    %>
    <img src="/media?key=<%= key %>">
    <%
      }
    %>
    </body>
</html>