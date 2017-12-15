caApp.controller('fileAdminController', function($http, $scope, $location,
		$filter, ngTableParams, $routeParams, breadcrumbs, $cookieStore,
		webServices, tableFactory, download, normalizeString) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.showTable = true;
	$scope.backFileAdm = function() {
		window.history.back();
	};
	$scope.fileTypes = getFileTypes();
	$scope.fileType = null;

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
						var tipos = getFileTypes();
						$scope.tableFile = tableFactory.getTableFilterData(
								tableData, ngTableParams, $filter, $scope.file,
								$scope);
					}
					$scope.showTable = true;
				});
	};

	$scope.saveFile = function() {
		if ($scope.fileType == null) {
			alertify.error("Debe seleccionar un tipo de archivo");
		} else if ($scope.file == undefined) {
			alertify.error("Debe seleccionar un archivo");
		} else if ($scope.file.size > sizeMax) {
			var sizeFile = sizeMax / (1024 * 1024);
			alertify.error("El archivo seleccionado es mayor a " + sizeFile
					+ " MB");
		} else if ($scope.fileType == 0
				&& $scope.file.type != "application/pdf") {
			alertify.error("Solo se pueden subir archivos PDF "
					+ "para el tipo factura");
		} else {
			var fileName = normalizeString.stringReplace($scope.file.name);
			webServices.getWSResponsePostFile($http, 'archivoFactura',
					'uploadFile', $scope.file, fileName, $scope.fileType,
					$routeParams.facId, false, $cookieStore, function(data,
							status, headers, config) {
						if (data.status == 0) {
							alertify.success("El archivo se"
									+ " cargo con exito");
							$scope.loadFiles();
							$scope.fileType = null;
						} else if (data.status == 3) {
							alertify.error("El archivo ya existe");
							$scope.loadFiles();
						} else if (data.status == 4) {
							$scope.forceUpdate(fileName);
						} else if (data.status == 5) {
							alertify.error("No se puede guardar "
									+ "el archivo tipo factura. "
									+ "Hay un workflow en proceso");
							$scope.loadFiles();
						} else {
							alertify.error("Error cargando " + " el archivo");
							$scope.loadFiles();
						}
					});
			$scope.showTable = false;
		}
	};

	$scope.forceUpdate = function($fileName) {
		alertify.confirm("Ya existe un archivo tipo PDF, desea reemplazarlo?",
				function(e) {
					if (e) {
						webServices.getWSResponsePostFile($http,
								'archivoFactura', 'uploadFile', $scope.file,
								$fileName, $scope.fileType, $routeParams.facId,
								true, $cookieStore, function(data, status,
										headers, config) {
									if (data.status == 0) {
										alertify.success("El archivo se"
												+ " reemplazo con exito");
										$scope.loadFiles();
										$scope.fileType = null;
									} else if (data.status == 3) {
										alertify.error("El archivo ya existe");
										$scope.loadFiles();
									} else {
										alertify.error("Error cargando "
												+ " el archivo");
										$scope.loadFiles();
									}
								});
					} else {
						$scope.loadFiles();
					}
				});
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
										+ "eliminar el archivo: "
										+ data.message);
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
