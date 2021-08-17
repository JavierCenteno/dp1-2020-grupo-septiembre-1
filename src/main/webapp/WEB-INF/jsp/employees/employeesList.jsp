<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="employees">

	<c:if test="${not empty error}">
		<div style="color: firebrick; font-weight: bold;"><c:out value="${error}" /></div>
	</c:if>

	<h2>Employees</h2>

	<table id="employeesTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Name</th>
				<th style="width: 200px">Email</th>
				<th style="width: 200px;">Address</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="employee">
				<tr>
					<td><spring:url value="/employees/{employeeId}"
							var="employeeUrl">
							<spring:param name="employeeId" value="${employee.id}" />
						</spring:url> <a href="${fn:escapeXml(employeeUrl)}"><c:out
								value="${employee.name}" /></a></td>
					<td><c:out value="${employee.email}" /></td>
					<td><c:out value="${employee.address}" /></td>
					<!--
                <td> 
                    <c:out value="${employee.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${employee.user.password}"/> 
                </td>
                -->
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
