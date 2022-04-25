<%@ page import="premerge.PremergeConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
  <td colspan="2">
    <em>This step provides Merge Trains functionality. Now it is implemented for GitHub.</em>
  </td>
</tr>
<tr>
  <th>
    <label for="<%= PremergeConstants.GITHUB_ACCESS_TOKEN %>>">GitHub Access Token:<l:star/></label>
  </th>
  <td>
    <props:passwordProperty name="<%= PremergeConstants.GITHUB_ACCESS_TOKEN %>" className="longField"></props:passwordProperty>
    <span class="error" id="error_<%= PremergeConstants.GITHUB_ACCESS_TOKEN %>"></span>
  </td>
</tr>
<tr>
  <th>
    <label for="<%= PremergeConstants.TEAMCITY_ACCESS_TOKEN %>>">TeamCity Access Token:<l:star/></label>
  </th>
  <td>
    <props:passwordProperty name="<%= PremergeConstants.TEAMCITY_ACCESS_TOKEN %>" className="longField"></props:passwordProperty>
    <span class="error" id="error_<%= PremergeConstants.TEAMCITY_ACCESS_TOKEN %>"></span>
  </td>
</tr>
