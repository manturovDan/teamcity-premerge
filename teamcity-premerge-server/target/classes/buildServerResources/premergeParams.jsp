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
    <label for="<%= PremergeConstants.TARGET_BRANCH %>>">Target branch:</label>
  </th>
  <td>
    <props:textProperty name="<%= PremergeConstants.TARGET_BRANCH %>" className="longField"></props:textProperty>
    <span class="smallNote">You can configure this plugin in several ways:</span>
    <span class="smallNote">1) set target branch name in <b>Target branch</b> field (e.g. refs/head/main or main) and add build <a href = "https://www.jetbrains.com/help/teamcity/build-step-execution-conditions.html" target="_blank">condition</a> <i>teamcity.build.branch does not equal target branch (with refs/heads, e.g. refs/heads/main)</i>. In this case, all builds on commits of rest branches will run on preliminary merge commit with target branch;</span>
    <span class="smallNote">2) use Pull-request plugin to run builds on merge commits of pull requests. Set %teamcity.pullRequest.target.branch% in <b>Target branch</b> field and add <a href = "https://www.jetbrains.com/help/teamcity/build-step-execution-conditions.html" target="_blank">condition</a>  <i>teamcity.pullRequest.target.branch exists</i></span>
    <span class="error" id="error_<%= PremergeConstants.TARGET_BRANCH %>"></span>
  </td>
</tr>
