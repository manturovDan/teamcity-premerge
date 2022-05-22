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
        <i>Merge train (start)</i> and <i>Merge train (finish)</i>. Selected step is the start step. In this step preliminary merge commit of
        target branch and all Merge requests source brances is created and acheckouted.
      </li>
      <li>
        You should also create <i>Merge train (finish)</i> Build Step to achieve complete Merge Trains functionality.
      </li>
      <li>Please choose your Git-hosting type and set corresponding credentials.</li>
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
      <props:option value="azure"><c:out value="Azure DevOps"/></props:option>
    </props:selectProperty>
    <span class="error" id="error_providerType"></span>
  </td>
</tr>
<tr class="github_options">
  <th>
    <label for="<%= PremergeConstants.GITHUB_ACCESS_TOKEN %>>">GitHub Access Token:<l:star/></label>
  </th>
  <td>
    <props:passwordProperty name="<%= PremergeConstants.GITHUB_ACCESS_TOKEN %>" className="longField"></props:passwordProperty>
    <span class="error" id="error_<%= PremergeConstants.GITHUB_ACCESS_TOKEN %>"></span>
  </td>
</tr>