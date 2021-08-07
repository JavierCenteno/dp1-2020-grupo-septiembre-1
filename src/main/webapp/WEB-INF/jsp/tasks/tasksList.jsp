<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="tasks">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;">${error}</div>
	</c:if>

	<h2>Tasks</h2>

	<table id="tasksTable" class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Income</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="task">
				<tr>
					<td><spring:url value="/buildings/${buildingId}/tasks/{taskId}"
							var="taskUrl">
							<spring:param name="taskId" value="${task.id}" />
						</spring:url> <a href="${fn:escapeXml(taskUrl)}"><c:out
								value="${task.name}" /></a></td>
					<td><c:out value="${task.income}" /></td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
