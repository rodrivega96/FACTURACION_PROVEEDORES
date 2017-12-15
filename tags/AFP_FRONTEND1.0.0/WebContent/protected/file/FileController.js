caApp.controller('fileAdminController', function($http, $scope, $location,
		$filter, ngTableParams, $routeParams, breadcrumbs, $cookieStore,
		webServices, tableFactory, download, normalizeString) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.showTable = true;
	$scope.backFileAdm = function() {
		window.history.back();
	};

	var sizeMax = '';

	$scope.loadFiles = function() {
		var sendId = {
			'id' : $routeParams.facId
		};
		webServices.getWSResponseGet($http, 'archivoFactura',
				'getAllArchivoFactura', sendId, $cookieStore, function(data,
						status, headers, config) {
					if (data.status == 0) {
						sizeMax = data.headerDTO.sizeMax;
						var tableData = [];
						tableData = data.headerDTO.listArchivoHeader;
						$scope.tableFile = tableFactory.getTableFilterData(
								tableData, ngTableParams, $filter, $scope.file,
								$scope);
					}
					$scope.showTable = true;
				});
	};

	$scope.saveFile = function() {
		if ($scope.file != undefined) {
			if ($scope.file.size <= sizeMax) {
				var fileName = normalizeString.stringReplace($scope.file.name);
				webServices.getWSResponsePostFile($http, 'archivoFactura',
						'uploadFile', $scope.file, fileName,
						$routeParams.facId, $cookieStore, function(data,
								status, headers, config) {
							if (data.status == 0) {
								alertify.success("El archivo se"
										+ " cargo con exito");
								$scope.loadFiles();
							} else if (data.status == 3) {
								alertify.error("El archivo ya existe");
								$scope.loadFiles();
							} else {
								alertify.error("Error cargando "
										+ " el archivo");
								$scope.loadFiles();
							}
						});
				$scope.showTable = false;
			} else {
				var sizeFile = sizeMax / (1024 * 1024);
				alertify.error("El archivo seleccionado es mayor a " + sizeFile
						+ " MB");
			}
		} else {
			alertify.error("Debe seleccionar un archivo");
		}
	};

	$scope.download = function($file) {
		var sendId = {
			'id' : $file.id
		};
		webServices.getWSResponseGet($http, 'archivo', 'downloadArchivo',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						download.downloadFile(data.dto.content, $file.name,
								$file.type);
					}
				});
	};

	$scope.deleteFile = function($fileId) {
		alertify.confirm("Esta seguro de querer eliminar el archivo?",
				function(e) {
					if (e) {
						var sendData = {
							'idArchivoHeader' : $fileId
						};
						var callbackFuntion = function(data, status, headers,
								config) {
							if (data.status == 0) {
								alertify.success("El archivo se "
										+ "elimino correctamente");
								$scope.loadFiles();
							} else if (data.status == 1) {
								alertify.error("El archivo ya fue eliminado");
								$scope.loadFiles();
							} else {
								alertify.error("Se produjo un error al "
										+ "eliminar el archivo: "+data.message);
								$scope.loadFiles();
							}
						};
						webServices.getWSResponsePost($http, 'archivo',
								'deleteArchivo', sendData, $cookieStore,
								callbackFuntion);
						$scope.showTable = false;
					}
				});
	};

	$scope.loadFiles();

});
