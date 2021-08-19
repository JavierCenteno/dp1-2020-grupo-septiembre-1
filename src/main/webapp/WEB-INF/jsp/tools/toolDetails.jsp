<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="tools">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Tool Information</h2>

	<a href="/buildings/${tool.building.id}/tools">Go to the list of tools of building</a>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${tool.name}" /></b></td>
		</tr>
	</table>

</petclinic:layout>
