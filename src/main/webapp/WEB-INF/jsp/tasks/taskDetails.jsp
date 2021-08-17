<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="tasks">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Task Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${task.name}" /></b></td>
		</tr>
		<tr>
			<th>Income</th>
			<td><b><c:out value="${task.income}" /></b></td>
		</tr>
	</table>

</petclinic:layout>
