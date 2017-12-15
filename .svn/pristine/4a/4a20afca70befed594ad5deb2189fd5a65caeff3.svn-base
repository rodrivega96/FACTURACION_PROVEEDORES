caApp.registerCtrl('consultWFController', function($scope, $location, $http,
		breadcrumbs, $cookieStore, $routeParams, $filter, webServices,
		tableFactory, ngTableParams, printFactory, getWorkflowTemplate, DWFModel, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	
	$scope.backAutAdm = function() {
		$location.path(utilidades.getPath($cookieStore, true));
	};

	$scope.facturaControlerShowModalPDF = false;
	$scope.showPDF = false;
	$scope.pdfUrl = null;
	$scope.pdfName = '';
	$scope.scroll = 0;

	var sendData = {
		'idFactura' : $routeParams.facId,
		'simp' : false,
		'onlyDet' : false
	};

	var tableData = [];
	$scope.states = DWFModel.getHistoryState();
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
		} else {
			alertify.error("No se pudo cargar el detalle de Workflow");
		}
	};

	webServices.getWSResponseGet($http, 'historial', 'getHistorialByFinalUser',
			sendData, $cookieStore, callbackFuntion);

	$scope.imprimir = function() {
		$scope.dataView = getWorkflowTemplate.get($scope.facHistorial);
		$scope.pdfName = 'DetalleDeFactura.pdf';
		$scope.scroll = 0;
		$scope.pdfUrl = printFactory.printVatesReport($scope.dataView, 'p',
				'letter', 'protected/template/templatePDF.html', 'uint8array');
		$scope.showPDF = true;
		$scope.facturaControlerShowModalPDF = true;
	};

	$scope.closeFindModalPDF = function() {
		$scope.facturaControlerShowModalPDF = false;
		$scope.showPDF = false;
		$scope.pdfUrl = null;
	};

});