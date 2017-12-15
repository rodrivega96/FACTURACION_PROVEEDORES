caApp.registerCtrl('autorizacionFileController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		$routeParams, tableFactory, $filter, download, utilidadesConversion,
		utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.locationPath = '/autorizacion-admin';

	$scope.backFileAdm = function() {
		$location.path($scope.locationPath);
	};

	var tableData = [];
	var sendId = {
		'id' : $routeParams.facId
	};
	webServices.getWSResponseGet($http, 'archivoFactura',
			'getAllArchivoFactura', sendId, $cookieStore, function(data,
					status, headers, config) {
				if (data.status == 0) {
					tableData = data.headerDTO.listArchivoHeader;
					$scope.tableFile = tableFactory.getTableFilterData(
							tableData, ngTableParams, $filter, $scope.file,
							$scope);
				}
			});

	$scope.download = function($file) {
		$scope.pdfUrl = null;
		$scope.showPDF = false;
		var sendId = {
			'id' : $file.id
		};
		webServices.getWSResponseGet($http, 'archivo', 'downloadArchivo',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						if (utilidades.isPDF($file.name)) {
							if (data.dto != null) {
								$scope.pdfName = $file.name;
								$scope.scroll = 0;
								$scope.pdfUrl = utilidadesConversion
										.base64ToUint8Array(data.dto.content);
								$scope.showPDF = true;
								$scope.showModalPDF = true;
							} else {
								$scope.pdfUrl = null;
								$scope.showPDF = false;
							}
						} else {
							download.downloadFile(data.dto.content, $file.name,
									$file.type);
						}
					}
				});
	};

	$scope.closeFindModalPDF = function() {
		$scope.showModalPDF = false;
	};

	$scope.showModalPDF = false;

});

caApp.registerCtrl('fileConsultController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, $routeParams,
		tableFactory, $filter, download, utilidades, utilidadesConversion) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.locationPath = '/factura-consult-admin';
	
	$scope.backFileAdm = function() {
		$location.path($scope.locationPath);
	};

	var tableData = [];
	var sendId = {
		'id' : $routeParams.facId
	};
	webServices.getWSResponseGet($http, 'archivoFactura',
			'getAllArchivoFactura', sendId, $cookieStore, function(data,
					status, headers, config) {
				if (data.status == 0) {
					tableData = data.headerDTO.listArchivoHeader;
					$scope.tableFile = tableFactory.getTableFilterData(
							tableData, ngTableParams, $filter, $scope.file,
							$scope);
				}
			});

	$scope.download = function($file) {
		$scope.pdfUrl = null;
		$scope.showPDF = false;
		var sendId = {
			'id' : $file.id
		};
		webServices.getWSResponseGet($http, 'archivo', 'downloadArchivo',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						if (utilidades.isPDF($file.name)) {
							if (data.dto != null) {
								$scope.pdfName = $file.name;
								$scope.scroll = 0;
								$scope.pdfUrl = utilidadesConversion
										.base64ToUint8Array(data.dto.content);
								$scope.showPDF = true;
								$scope.showModalPDF = true;
							} else {
								$scope.pdfUrl = null;
								$scope.showPDF = false;
							}
						} else {
							download.downloadFile(data.dto.content, $file.name,
									$file.type);
						}
					}
				});
	};

	$scope.closeFindModalPDF = function() {
		$scope.showModalPDF = false;
	};

});