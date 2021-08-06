<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="managers">

	<h2>Manager Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${manager.name}" /></b></td>
		</tr>
		<tr>
			<th>Email</th>
			<td><c:out value="${manager.email}" /></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${manager.address}" /></td>
		</tr>
	</table>

	<spring:url value="{managerId}/edit" var="editUrl">
		<spring:param name="managerId" value="${manager.id}" />
	</spring:url>
	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit
		Manager</a>

</petclinic:layout>
