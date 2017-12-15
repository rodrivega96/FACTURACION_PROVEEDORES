caApp.controller('detalleWFController', function($scope, $location, $http,
		breadcrumbs, $cookieStore, $routeParams, $filter, webServices,
		tableFactory, ngTableParams) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.backAutAdm = function() {
		window.history.back();
	};

	var sendData = {
		'idFactura' : $routeParams.facId,
		'idUsuario' : $cookieStore.get('vatesuser').userId
	};

	var tableData = [];
	var tableDataAsientos = [];
	$scope.states = getHistoryState();

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.facHistorial = data.dto;

			for (var i = 0; i < data.dto.historials.length; i++) {
				for (var j = 0; j < $scope.states.length; j++) {
					if (data.dto.historials[i].estado == $scope.states[j].id) {
						data.dto.historials[i].estado = $scope.states[j].name;
					}
				}
			}

			tableData = data.dto.historials;
			tableDataAsientos = data.dto.asientos;
			$scope.tableHistorial = tableFactory.getTableData(tableData,
					ngTableParams, $scope);
			$scope.tableAsientos = tableFactory.getTableData(tableDataAsientos,
					ngTableParams, $scope);
		}
	};

	webServices.getWSResponseGet($http, 'historial', 'getHistorialByFinalUser',
			sendData, $cookieStore, callbackFuntion);

});
