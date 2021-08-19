<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="tools">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Tools of Building</h2>

	<div>
		<a href="/buildings/${buildingId}">Go to the building</a>
	</div>

	<div>
		<a href="/buildings/${buildingId}/tools/new">Create new tool for the building</a>
	</div>

	<table id="toolsTable" class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="tool">
				<tr>
					<td><spring:url value="/buildings/${buildingId}/tools/{toolId}"
							var="toolUrl">
							<spring:param name="toolId" value="${tool.id}" />
						</spring:url> <a href="${fn:escapeXml(toolUrl)}"><c:out
								value="${tool.name}" /></a></td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
