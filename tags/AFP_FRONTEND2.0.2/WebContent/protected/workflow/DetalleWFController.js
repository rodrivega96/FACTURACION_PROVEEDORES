caApp.controller('detalleEstadosController', function($scope, $location, $http,
		breadcrumbs, $cookieStore, $routeParams, $filter, webServices,
		tableFactory, ngTableParams, printFactory, getWorkflowTemplate) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.backAutAdm = function() {
		window.history.back();
	};

	var sendData = {
		'idFactura' : $routeParams.facId
	};

	var tableData = [];
	$scope.states = getHistoryState();

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			for (var i = 0; i < data.dto.historials.length; i++) {
				for (var j = 0; j < $scope.states.length; j++) {
					if (data.dto.historials[i].estado == $scope.states[j].id) {
						data.dto.historials[i].estado = $scope.states[j].name;
					}
				}
			}
			tableData = data.dto.historials;
			$scope.tableHistorial = tableFactory.getTableData(tableData,
					ngTableParams, $scope);
		}
	};

	webServices.getWSResponseGet($http, 'historial', 'getHistorialByFinalUser',
			sendData, $cookieStore, callbackFuntion);
	
});


