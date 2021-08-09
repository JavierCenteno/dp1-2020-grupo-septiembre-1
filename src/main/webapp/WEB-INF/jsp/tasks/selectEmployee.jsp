<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="employees">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;">${error}</div>
	</c:if>

	<h2>Select Employee</h2>

	<table id="employeesTable" class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="employee">
				<tr>
					<td>
						<form action="/tasks/${taskId}/assignEmployee/${employee.id}" method="post">
							<input type="submit" name="select" value="${employee.name}" />
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
