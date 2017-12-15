caApp.registerCtrl('facturaFormEditController', function($scope, $location,
		$http, $routeParams, breadcrumbs, $cookieStore, webServices,
		ngTableParams, tableFactory, FModel) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.locationPath = '/factura-admin';

	$scope.factura = FModel.newFactura();
	$scope.wizardMode = false;
	$scope.wizard = function(value) {
		$scope.wizardMode = value;
	};
	$scope.backFacAdm = function() {
		$location.path($scope.locationPath);
	};

	$scope.tipos = FModel.getTipos();

	$scope.wfOptions = FModel.getWorkflowOptions();

	$scope.estadosFactura = FModel.getEstadosFactura();

	var sendId = {
		'id' : $routeParams.facId
	};

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.factura = data.factura;
			var tableData = [];
			tableData = data.factura.asientos;
			$scope.tableAsientos = tableFactory.getTableData(tableData,
					ngTableParams, $scope);
		} else if (data.status == 3) {
			alertify.error("ya eliminada");
			$location.path($scope.locationPath);
		} else {
			alertify.error("Error cargando la factura: " + data.message);
		}
	};

	webServices.getWSResponseGet($http, 'factura', 'getFactura', sendId,
			$cookieStore, callbackFuntion);

	$scope.saveFac = function() {
		webServices.getWSResponsePost($http, 'factura', 'saveFactura',
				$scope.factura, $cookieStore, function(data, status, headers,
						config) {
					if (data.status == 0) {
						alertify.success("La factura se "
								+ "actualizo correctamente");
						if ($scope.wizardMode == true) {
							$location.path("/factura-admin/file-form/"
									+ data.factura.id);
						} else {
							$location.path("/factura-admin");

						}
					} else {
						alertify.error("Error guardando la factura: "
								+ data.message);
					}
				});
	};

});

caApp.registerCtrl('facturaFormVerController', function($scope, $location, $http,
		$routeParams, breadcrumbs, $cookieStore, webServices, ngTableParams,
		tableFactory, FModel) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.locationPath = '/factura-consult-admin';
	
	$scope.factura = FModel.newFactura();

	$scope.backFacAdm = function() {
		$location.path($scope.locationPath);
	};

	$scope.tipos = FModel.getTipos();
	$scope.wfOptions = FModel.getWorkflowOptions();
	$scope.estadosFactura = FModel.getEstadosFactura();

	var sendId = {
		'id' : $routeParams.facId
	};

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.factura = data.factura;
			$scope.factura.canSave = false;
			var tableData = [];
			tableData = data.factura.asientos;
			$scope.tableAsientos = tableFactory.getTableData(tableData,
					ngTableParams, $scope);
		} else if (data.status == 3) {
			alertify.error("ya eliminada");
			$location.path($scope.locationPath);
		} else {
			alertify.error("Error cargando la factura: " + data.message);
		}
	};

	webServices.getWSResponseGet($http, 'factura', 'getFactura', sendId,
			$cookieStore, callbackFuntion);

});