caApp.controller('autorizacionAdminController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		normalizeString, FiltroFactura) {
	
	FiltroFactura.data = null;
	FiltroFactura.dataConsult = null;
	
	$scope.breadcrumbs = breadcrumbs;

	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.goUploadFile = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.goDetailWF = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.showTable = true;

	// Inicio metodos para las acciones
	// TODO

	$scope.goAuthorize = function($param) {
		alertify.confirm("Esta seguro de autorirar la factura?",
				function(e) {
					if (e) {
						var sendId = {
							'id' : $param,
							'estado' : 1,
							'descripcion' : ''
						};
						var callbackFuntion = function(data, status, headers,
								config) {
							if (data.status == 0) {
								alertify.success("La factura fue "
										+ "autorizada correctamente");
								$scope.loadFacturas();
							} else if (data.status == 3) {
								alertify.error("La factura fue "
										+ "autorizada correctamente, pero "
										+ "no se pudo enviar el mail.");
								$scope.loadFacturas();
							} else {

							}
						};
						webServices.getWSResponsePost($http, 'historial',
								'saveHistorial', sendId, $cookieStore,
								callbackFuntion);
					}
				});
	};

	$scope.goObserve = function($param) {
		alertify
				.prompt("Esta seguro de observar la autorización?",
						function(e, str) {
							if (e) {
								var strNew = normalizeString
										.stringReplaceCaracter(str);
								if (str == null
										|| str == ""
										|| str == "Ingresar motivo de "
												+ "observación."
										|| strNew.length < 14) {
									if(strNew.length < 14){
										alertify.error("El motivo de "
												+ "observación de la factura debe tener al menos 14 carcteres.");
									}else{
									alertify.error("Debe indicar cual "
											+ "es el motivo "
											+ "de observación "
											+ "de la factura.");
									}
									return $scope.goObserve($param);
								}
								var sendId = {
									'id' : $param,
									'estado' : 5,
									'descripcion' : str
								};
								var callbackFuntion = function(data, status,
										headers, config) {
									if (data.status == 0) {
										alertify.success("La factura fue "
												+ "observada correctamente"
												|| strNew.length < 14);
										$scope.loadFacturas();
									} else if (data.status == 3) {
										alertify.error("La factura fue "
												+ "observada correctamente, "
												+ "pero no se pudo enviar "
												+ "el mail.");
										$scope.loadFacturas();
									} else {

									}
								};
								webServices.getWSResponsePost($http,
										'historial', 'saveHistorial', sendId,
										$cookieStore, callbackFuntion);
							}
						}, "Ingresar motivo de observación.");
	};

	$scope.goDiscard = function($param) {
		alertify
				.prompt("Esta seguro de rechazar la autorizaci\u00f3n?",
						function(e, str) {
							if (e) {
								var strNew = normalizeString
										.stringReplaceCaracter(str);
								if (str == null
										|| str == ""
										|| str == "Ingresar motivo "
												+ "de rechazo."
										|| strNew.length < 14) {
									if(strNew.length < 14){
										alertify.error("El motivo de "
												+ "rechazo de la factura debe tener al menos 14 carcteres.");
									}else{
									alertify.error("Debe indicar cual "
											+ "es el motivo de "
											+ "rechazo de la factura.");
									}
									return $scope.goDiscard($param);
								}
								var sendId = {
									'id' : $param,
									'descripcion' : str
								};
								var callbackFuntion = function(data, status,
										headers, config) {
									if (data.status == 0) {
										alertify.success("La factura fue "
												+ "rechazada "
												+ "correctamente");
										$scope.loadFacturas();
									} else if (data.status == 3) {
										alertify.error("La factura fue "
												+ "rechazada correctamente, "
												+ "pero no se pudo informar "
												+ "al usuario de "
												+ "administración. No "
												+ "se pudo enviar el mail.");
										$scope.loadFacturas();
									} else {
										alertify.error("Error al intentar "
												+ "rechazar la factura.");
									}
								};
								$scope.showTable = true;
								webServices.getWSResponsePost($http,
										'historial',
										'rechazarFacturaHistorial', sendId,
										$cookieStore, callbackFuntion);
							}
						}, "Ingresar motivo de rechazo.");
	};

	// Fin metodos para las acciones

	$scope.loadFilter = function() {
		$scope.filter.page = 1;
		$scope.filter.userId = $cookieStore.get('vatesuser').userId;
		$scope.loadFacturas();

	};

	$scope.loadFacturas = function() {
		$scope.facturaMessage = false;
		var callbackFuntion = function(data, status, headers, config) {
			$scope.facturas = data.content;
			$scope.filter.totalPages = data.totalPages;
			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			$scope.setRangePages();
			if (data.numberOfElements == 0) {
				$scope.facturaMessage = true;
				$scope.warningFacturaMessage = 'No se encontraron '
						+ 'facturas pendientes a autorizar.';
			}
			$scope.showTable = true;
		};
		webServices.getWSResponseGet($http, 'factura',
				'facturaViewPaginatedList', $scope.filter, $cookieStore,
				callbackFuntion);
	};

	$scope.prevPage = function() {
		if ($scope.filter.page > 1) {
			$scope.filter.page--;
			$scope.loadFacturas();
		}
	};

	$scope.goPage = function($number) {
		$scope.filter.page = $number;
		$scope.loadFacturas();
	};

	$scope.nextPage = function() {
		if ($scope.filter.page < $scope.filter.totalPages) {
			$scope.filter.page++;
			$scope.loadFacturas();
		}
	};

	$scope.firstPage = function() {
		$scope.filter.page = 1;
		$scope.loadFacturas();
	};

	$scope.lastPage = function() {
		$scope.filter.page = $scope.filter.totalPages;
		$scope.loadFacturas();
	};

	$scope.setRangePages = function() {
		$scope.filter.range = [];
		for (var i = 1; i <= $scope.filter.totalPages; i++) {
			$scope.filter.range.push(i);
		}
	};

	$scope.order = function($variable) {
		if ($scope.filter.variable == $variable) {
			$scope.filter.order = $scope.filter.order == false ? true : null;
		} else {
			$scope.filter.order = false;
		}
		$scope.filter.variable = $scope.filter.order == null ? "" : $variable;
		$scope.showTable = false;
		$scope.loadFacturas();
	};

	$scope.filter = getAutorizacionFileterForm();
	$scope.extFilter = getAutorizacionFilter();

	$scope.loadFilter();
});

caApp.controller('autorizacionFileController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		$routeParams, tableFactory, $filter, download) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.backFileAdm = function() {
		window.history.back();
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

});

caApp.controller('autorizacionNivelInferiorController', function($scope,
		$location, $http, ngTableParams, breadcrumbs, $cookieStore,
		webServices, normalizeString, FiltroFactura) {

	FiltroFactura.data = null;
	FiltroFactura.dataConsult = null;
	
	$scope.breadcrumbs = breadcrumbs;

	$scope.goUploadFile = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.goDetailWF = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.goURL = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.showTable = true;

	// Inicio metodos para las acciones
	// TODO

	$scope.goAuthorize = function($param) {
		alertify.confirm("Esta seguro de autorirar "
				+ "los niveles inferiores de la factura?", function(e) {
			if (e) {
				var sendId = {
					'id' : $param,
					'estado' : 1,
					'userId' : $cookieStore.get('vatesuser').userId
				};
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("La factura fue "
								+ "autorizada correctamente");
						$scope.loadFacturas();
					} else if (data.status == 3) {
						alertify.error("La factura fue "
								+ "autorizada correctamente, "
								+ "pero no se pudo enviar el mail.");
						$scope.loadFacturas();
					} else {
						alertify.error("Error al intentar "
								+ "autorizar la factura");
					}
				};
				$scope.showTable = true;
				webServices.getWSResponsePost($http, 'historial',
						'saveAnterioresHistorial', sendId, $cookieStore,
						callbackFuntion);
			}
		});
	};

	$scope.goObserve = function($param) {
		alertify.prompt("Esta seguro de observar "
				+ "los niveles inferiores de la factura?", function(e, str) {
			if (e) {
				var strNew = normalizeString.stringReplaceCaracter(str);
				if (str == null || str == ""
						|| str == "Ingresar motivo de observación."
						|| strNew.length < 14) {
					if(strNew.length < 14){
						alertify.error("El motivo de "
								+ "observación de la factura debe tener al menos 14 carcteres.");
					}else{
					alertify.error("Debe indicar cual es el motivo de "
							+ "observación de la factura.");
					}
					return $scope.goObserve($param);
				}
				var sendId = {
					'id' : $param,
					'userId' : $cookieStore.get('vatesuser').userId,
					'estado' : 5,
					'descripcion' : str
				};
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("La factura fue observada "
								+ "correctamente" || strNew.length < 14);
						$scope.loadFacturas();
					} else if (data.status == 3) {
						alertify.error("La factura fue observada "
								+ "correctamente, pero no se "
								+ "pudo enviar el mail.");
						$scope.loadFacturas();
					} else {

					}
				};
				webServices.getWSResponsePost($http, 'historial',
						'saveAnterioresHistorial', sendId, $cookieStore,
						callbackFuntion);
			}
		}, "Ingresar motivo de observación.");
	};

	$scope.goDiscard = function($param) {
		alertify.prompt("Esta seguro de rechazar los "
				+ "niveles inferiores de la factura?", function(e, str) {
			if (e) {
				var strNew = normalizeString.stringReplaceCaracter(str);
				if (str == null || str == ""
						|| str == "Ingresar motivo de rechazo."
						|| strNew.length < 14) {
					if(strNew.length < 14){
						alertify.error("El motivo de "
								+ "rechazo de la factura debe tener al menos 14 carcteres.");
					}else{
					alertify.error("Debe indicar cual es el motivo de "
							+ "rechazo de la factura.");
					}
					return $scope.goDiscard($param);
				}
				var sendId = {
					'id' : $param,
					'descripcion' : str,
					'userId' : $cookieStore.get('vatesuser').userId
				};
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("La factura fue rechazada "
								+ "correctamente");
						$scope.loadFacturas();
					} else if (data.status == 3) {
						alertify.error("La factura fue rechazada "
								+ "correctamente, pero no se "
								+ "pudo enviar el mail al usuario "
								+ "administrador.");
						$scope.loadFacturas();
					} else {
						alertify.error("Error al intentar "
								+ "rechazar la factura.");
					}
				};
				$scope.showTable = true;
				webServices.getWSResponsePost($http, 'historial',
						'rechazarAnterioresHistorial', sendId, $cookieStore,
						callbackFuntion);
			}
		}, "Ingresar motivo de rechazo.");
	};

	// Fin metodos para las acciones

	$scope.loadFilter = function() {
		$scope.filter.page = 1;
		$scope.filter.userId = $cookieStore.get('vatesuser').userId;
		$scope.loadFacturas();

	};

	$scope.loadFacturas = function() {
		$scope.facturaMessage = false;
		var callbackFuntion = function(data, status, headers, config) {
			$scope.facturas = data.content;
			$scope.filter.totalPages = data.totalPages;
			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			$scope.setRangePages();
			if (data.numberOfElements == 0) {
				$scope.facturaMessage = true;
				$scope.warningFacturaMessage = 'No se encontraron '
						+ 'facturas pendientes a autorizar.';
			}
			$scope.showTable = true;
		};

		webServices.getWSResponseGet($http, 'factura', 'nivelInferiorList',
				$scope.filter, $cookieStore, callbackFuntion);
	};

	$scope.prevPage = function() {
		if ($scope.filter.page > 1) {
			$scope.filter.page--;
			$scope.loadFacturas();
		}
	};

	$scope.goPage = function($number) {
		$scope.filter.page = $number;
		$scope.loadFacturas();
	};

	$scope.nextPage = function() {
		if ($scope.filter.page < $scope.filter.totalPages) {
			$scope.filter.page++;
			$scope.loadFacturas();
		}
	};

	$scope.firstPage = function() {
		$scope.filter.page = 1;
		$scope.loadFacturas();
	};

	$scope.lastPage = function() {
		$scope.filter.page = $scope.filter.totalPages;
		$scope.loadFacturas();
	};

	$scope.setRangePages = function() {
		$scope.filter.range = [];
		for (var i = 1; i <= $scope.filter.totalPages; i++) {
			$scope.filter.range.push(i);
		}
	};

	$scope.order = function($variable) {
		if ($scope.filter.variable == $variable) {
			$scope.filter.order = $scope.filter.order == false ? true : null;
		} else {
			$scope.filter.order = false;
		}
		$scope.filter.variable = $scope.filter.order == null ? "" : $variable;
		$scope.showTable = false;
		$scope.loadFacturas();
	};

	$scope.filter = getAutorizacionFileterForm();
	$scope.extFilter = getAutorizacionFilter();

	$scope.loadFilter();
});