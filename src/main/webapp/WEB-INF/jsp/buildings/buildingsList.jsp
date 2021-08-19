<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="buildings">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Buildings</h2>

	<div>
		<a href="/buildings/new">Create new building</a>
	</div>

	<table id="buildingsTable" class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Address</th>
				<th>Tools</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="building">
				<tr>
					<td><spring:url value="/buildings/{buildingId}"
							var="buildingUrl">
							<spring:param name="buildingId" value="${building.id}" />
						</spring:url> <a href="${fn:escapeXml(buildingUrl)}"><c:out
								value="${building.name}" /></a></td>
					<td><c:out value="${building.address}" /></td>
					<td><a href="/buildings/${building.id}/tools">View list of tools</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</petclinic:layout>
