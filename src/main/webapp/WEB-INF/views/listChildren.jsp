<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url value="/j_spring_security_check" var="listChildrenURL" />

<div class="kidslist">
	<table class="listChild">
		<caption><h2>List of kids</h2></caption>
		<tr>
			<th>Kids name</th>
		</tr>
		<c:forEach var="kids" items="${listChildren}">
		<tr>
			<td><c:out value="${kids.getFirstName()} ${kids.getLastName()}" /></td>
		</tr>
        </c:forEach>
</table>
</div>

<div>
<form>
	<table class="reportTime">
		<tr>
			<td colspan="2">
				<input class="buttons2" type="submit" value="Arrival time"/>
				<input class="buttons2" type="submit" value="Departure time"/>
			</td>
		</tr>
		<tr>
			<th class="odd">Booking time</th>
			<th class="odd">Real time</th>
		</tr>
		<c:forEach var="booked" items="${listBooking}">
		<tr>

			<td><c:out value="${booked.getBookingStartTime()}"/></td>
			<td ><input type="time" value="00:00" class="timeCell"/></td>

		</tr>
	    </c:forEach>
	<tr>
		<td  colspan="2">
			<input class="buttons" type="submit" value="Apply"/>
			<input class="buttons" type="submit" value="Cancel"/>
			<input class="buttons" type="submit" value="Ok"/>
		</td>
	</tr>
</table>
</form>
</div>
