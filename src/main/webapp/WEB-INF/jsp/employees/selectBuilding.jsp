<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="buildings">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Select Building</h2>

	<table id="buildingsTable" class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="building">
				<tr>
					<td>
						<form:form action="/unassignedEmployees/${employeeId}/assignBuilding/${building.id}" method="post">
							<button type="submit"><c:out value="${building.name}" /></button>
						</form:form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
