<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Goals Report</title>
</head>
<body>


<table>
	
	<tr>
		<th>Minutes</th><th>Exercise Minutes</th><th>Activity</th>
	</tr>
	<tr>
		<td>
	<div ng-controller="Reports">

		<div class="goals-container">

				<table ng-repeat="goalReport in goalReports">
					<tr>
					<td>${goalReport.goalMinutes}</td><td>${goalReport.exerciseMinutes}</td><td>${goalReport.exerciseActivity}</td>
					</tr>
				</table>
		</div>
	</div>
	</td>
	</tr>
</table>

<script type="javascript">
	function Reports($scope, $http) {
		$http.get('http://localhost:8080/EventTracker/GoalReports.json').
				success(function(data) {
					$scope.events = data;
				});
	}
</script>
</body>
</html>