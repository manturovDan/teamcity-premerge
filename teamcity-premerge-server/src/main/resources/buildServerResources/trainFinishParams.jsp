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
    <ul>
      <li>This step provides Merge Trains functionality. Merge Trains consisit of Build Steps of two types:
        <i>Merge train (start)</i> and <i>Merge train (finish)</i>. Selected step is the finish step. In this step result of this build and
        previous builds execution is checked and build is reruned without some merge requests by necesserity.
      </li>
      <li>
        You should also create <i>Merge train (start)</i> Build Step to achieve complete Merge Trains functionality.
      </li>
      <li>Please choose your Git-hosting type and set corresponding credentials. And set TeamCity Access Token for build rerunning.</li>
    </ul>
  </td>
</tr>
<l:settingsGroup title="Git Server" />
<tr>
  <th><label for="providerType">Git-hosting type:<l:star/></label></th>
  <td>
    <props:selectProperty name="providerType" className="longfield">
      <props:option value="">&lt;Select Git-hosting type&gt;</props:option>
      <props:option value="github"><c:out value="GitHub"/></props:option>
    </props:selectProperty>
    <span class="error" id="error_providerType"></span>
  </td>
</tr>
<tr>
  <th>
    <label for="<%= PremergeConstants.ACCESS_TOKEN %>>">GitHub Access Token:<l:star/></label>
  </th>
  <td>
    <props:passwordProperty name="<%= PremergeConstants.ACCESS_TOKEN %>" className="longField"></props:passwordProperty>
    <span class="error" id="error_<%= PremergeConstants.ACCESS_TOKEN %>"></span>
  </td>
</tr>
<l:settingsGroup title="TeamCity Server access" />
<tr>
  <th>
    <label for="<%= PremergeConstants.TEAMCITY_ACCESS_TOKEN %>>">TeamCity Access Token:<l:star/></label>
  </th>
  <td>
    <props:passwordProperty name="<%= PremergeConstants.TEAMCITY_ACCESS_TOKEN %>" className="longField"></props:passwordProperty>
    <span class="error" id="error_<%= PremergeConstants.TEAMCITY_ACCESS_TOKEN %>"></span>
  </td>
</tr>
