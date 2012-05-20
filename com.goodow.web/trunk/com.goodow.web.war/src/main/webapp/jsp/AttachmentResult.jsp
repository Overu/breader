<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>

<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  List<BlobKey> keys = (List<BlobKey>)request.getAttribute("blobKey");
%>
<html>
    <body>
    <%
      for(BlobKey key : keys) {
    %>
    <img src="/media?key=<%= key.getKeyString() %>">
    <%
      }
    %>
    </body>
</html>