<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="employees">

	<h2>Employee Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${employee.name}" /></b></td>
		</tr>
		<tr>
			<th>Email</th>
			<td><c:out value="${employee.email}" /></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${employee.address}" /></td>
		</tr>
	</table>

	<spring:url value="{employeeId}/edit" var="editUrl">
		<spring:param name="employeeId" value="${employee.id}" />
	</spring:url>
	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit
		Employee</a>

</petclinic:layout>
