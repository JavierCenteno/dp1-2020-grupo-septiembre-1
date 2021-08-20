<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="buildings">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Building Information</h2>

	<div>
		<a href="/buildings">Go to the list of buildings</a>
	</div>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${building.name}" /></b></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${building.address}" /></td>
		</tr>
		<tr>
			<th>Income</th>
			<td><c:out value="${building.income}" /></td>
		</tr>
		<tr>
			<th>Tools</th>
			<td><a href="/buildings/${building.id}/tools">View list of tools</a></td>
		</tr>
	</table>

</petclinic:layout>
