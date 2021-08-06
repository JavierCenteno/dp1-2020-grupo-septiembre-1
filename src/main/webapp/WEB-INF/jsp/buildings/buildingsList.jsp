<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="buildings">
    <h2>Buildings</h2>

    <table id="buildingsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Address</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="building">
            <tr>
                <td>
                    <spring:url value="/buildings/{buildingId}" var="buildingUrl">
                        <spring:param name="buildingId" value="${building.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(buildingUrl)}"><c:out value="${building.name}"/></a>
                </td>
                <td>
                    <c:out value="${building.address}"/>
                </td>
                <td>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
