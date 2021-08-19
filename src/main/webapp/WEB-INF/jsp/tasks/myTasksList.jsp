<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="myTasks">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Tasks</h2>

	<table id="tasksTable" class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Income</th>
				<th>Register work</th>
				<th>Complete task</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="task">
				<tr>
					<td><c:out value="${task.name}" /></td>
					<td><c:out value="${task.income}" /></td>
					<td><a href="/myTasks/${task.id}/workLog">Register work</a></td>
					<td>
						<form action="/myTasks/${task.id}/complete" method="post">
							<button type="submit">Complete task</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
