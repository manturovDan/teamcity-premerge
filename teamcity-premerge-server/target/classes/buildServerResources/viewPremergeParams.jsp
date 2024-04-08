<%@ page import="jetbrains.buildServer.premerge.PremergeConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  Target Branch: <props:displayValue name="<%=PremergeConstants.TARGET_BRANCH%>" emptyValue="not specified"/>
</div>
