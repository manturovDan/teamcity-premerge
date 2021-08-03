<%@ page import="premerge.PremergeConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
  <th>
    <label for="<%= PremergeConstants.SOURCE_BRANCHES_FILTER %>">Source branches filter:</label></th>
  <td>
    <props:textProperty name="<%= PremergeConstants.SOURCE_BRANCHES_FILTER %>" className="longField"></props:textProperty>
    <span class="smallNote"><a href="https://www.jetbrains.com/help/teamcity/branch-filter.html" target="_blank">
      Branch filter</a> of source branches
    </span>
    <span class="error" id="error_<%= PremergeConstants.SOURCE_BRANCHES_FILTER %>"></span>
  </td>
</tr>
<tr>
  <th>
    <label for="<%= PremergeConstants.TARGET_BRANCH %>>">Target branch:</label>
  </th>
  <td>
    <props:textProperty name="<%= PremergeConstants.TARGET_BRANCH %>" className="longField"></props:textProperty>
    <span class="error" id="error_<%= PremergeConstants.TARGET_BRANCH %>"></span>
  </td>
</tr>
