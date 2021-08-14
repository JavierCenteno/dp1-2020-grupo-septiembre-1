<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="managers">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;">${error}</div>
	</c:if>

	<h2>Managers</h2>

	<table id="managersTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Name</th>
				<th style="width: 200px">Email</th>
				<th style="width: 200px;">Address</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="manager">
				<tr>
					<td><spring:url value="/managers/{managerId}" var="managerUrl">
							<spring:param name="managerId" value="${manager.id}" />
						</spring:url> <a href="${fn:escapeXml(managerUrl)}"><c:out
								value="${manager.name}" /></a></td>
					<td><c:out value="${manager.email}" /></td>
					<td><c:out value="${manager.address}" /></td>
					<!--
                <td> 
                    <c:out value="${manager.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${manager.user.password}"/> 
                </td>
                -->
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
