caApp.controller('autorizacionAdminController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		normalizeString, FiltroFactura, tableFactory, IndexFactura,
		utilidadesConversion, tableCollapseFactory) {

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

	var facId = null;

	$scope.kanav = false;

	$scope.cleanTableRowExpand = function() {
		$scope.tableRowExpanded = true;
		$scope.tableRowNotExpanded = false;
		$scope.tableRowIndexExpandedCurr = "";
		$scope.tableRowIndexExpandedPrev = "";
		$scope.dataCollapse = [];
	};

	$scope.cleanTableRowExpand();

	$scope.dataCollapseFn = function() {
		$scope.dataCollapse = [];
	};

	$scope.loadCentros = function($facturaAdmId) {
		var tableDataAsientos = [];
		var callbackFuntion = function(data, status, headers, config) {
			if (data.status == 0) {
				$scope.facHistorial = data.dto;
				if (data.dto.kanav != null) {
					$scope.kanav = true;
				}
				tableDataAsientos = data.dto.asientos;
				$scope.tableAsientos = tableFactory.getTableData(
						tableDataAsientos, ngTableParams, $scope);
			}
		};
		facId = $facturaAdmId;
		var sendData = {
			'idFactura' : $facturaAdmId
		};
		webServices.getWSResponseGet($http, 'historial',
				'getHistorialByFinalUser', sendData, $cookieStore,
				callbackFuntion);
	};

	$scope.showPDF = false;
	$scope.pdfUrl = null;

	$scope.getNavStyle = function(scroll) {
		if (scroll > 100)
			return 'pdf-controls fixed';
		else
			return 'pdf-controls';
	};
						
	$scope.loadSelectedTableRow = function() {
		if (IndexFactura.index != null) {
			index = IndexFactura.index;
			$facturaId = IndexFactura.facId;
			$facturaAdmId = IndexFactura.facAdmId;
			$scope.tableRowExpanded = IndexFactura.tableRowExpanded;
			$scope.tableRowIndexExpandedCurr = IndexFactura.tableRowIndexExpandedCurr;
			$scope.tableRowIndexExpandedPrev = IndexFactura.tableRowIndexExpandedPrev;
			$scope.dataCollapse = IndexFactura.dataCollapse;
			$scope.pdfUrl = IndexFactura.pdfURL;
			$scope.showPDF = IndexFactura.showPDF;
			$scope.filter.page = FiltroFactura.page;
			$scope.loadCentros($facturaAdmId);
			$scope.reloadElement(index);
		};
	};

	 $scope.reloadElement = function(index) {
		setTimeout(function() {
			if (document.getElementById(index) != null) {
				document.getElementById(index).style.backgroundColor = "#FF8C00";
			} else {
				$scope.reloadElement(index);
			}
		}, 50);
	};

	$scope.loadArchivoAFP = function($facturaAdmId, $facturaId) {
		$scope.showPDF = false;
		// Validacion para que no haga el webservice cuando es la misma factura.
		if (facId == null || facId != $facturaAdmId) {
			$scope.kanav = false;
			$scope.loadCentros($facturaAdmId);
			var sendId = {
				'id' : $facturaId
			};
			webServices.getWSResponseGet($http, 'archivoFactura', 'verArchivo',
					sendId, $cookieStore, function(data, status, headers,
							config) {
						if (data.status == 0) {
							if (data.dto != null) {
								$scope.pdfName = 'Factura.pdf';
								$scope.scroll = 0;
								$scope.pdfUrl = utilidadesConversion
										.base64ToUint8Array(data.dto.content);
								$scope.showPDF = true;
							} else {
								$scope.pdfUrl = null;
								$scope.showPDF = false;
							}
							IndexFactura.showPDF = $scope.showPDF;
							IndexFactura.pdfURL = $scope.pdfUrl;
						}
					});
		} else {
			if ($scope.pdfUrl != null) {
				$scope.showPDF = true;
			}
		}
	};

	 $scope.selectTableRow = function(index, $facturaAdmId, $facturaId) {
		 if (!$scope.tableRowNotExpanded) {
			$scope.loadArchivoAFP($facturaAdmId, $facturaId);
			tableCollapseFactory.onSelect(index, $scope.dataCollapseFn(),
					$scope, 'tableRowExpanded', 'tableRowIndexExpandedCurr',
					'tableRowIndexExpandedPrev', 'dataCollapse');
			IndexFactura.index = index;
			IndexFactura.facId = $facturaId;
			IndexFactura.facAdmId = $facturaAdmId;
			IndexFactura.tableRowExpanded = $scope.tableRowExpanded;
			IndexFactura.tableRowIndexExpandedCurr = $scope.tableRowIndexExpandedCurr;
			IndexFactura.tableRowIndexExpandedPrev = $scope.tableRowIndexExpandedPrev;
			IndexFactura.dataCollapse = $scope.dataCollapse;
			FiltroFactura.page = $scope.filter.page;
		}
	};
		
	 $scope.showTable = true;
	
	 // Inicio metodos para las acciones

	$scope.goAuthorize = function($param, $str1, $index) {
		var str = "";
		if ($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== ""
				&& $index != $scope.tableRowIndexExpandedCurr) {
			str = "Está por autorizar una factura diferente a la expandida. ";
		}
		var prompt = str + "Esta seguro de autorirar la factura?";
		var success = "La factura fue autorizada correctamente";
		var fail = "La factura fue autorizada correctamente, "
				+ "pero no se pudo enviar el mail.";
		var error = "Error al intentar autorizar la factura.";
		var sendId = {
			'id' : $param,
			'estado' : 0,
			'descripcion' : ''
		};
		var placeholder = $str1 != null ? $str1 : "Observación";
		$scope.historialEstado('saveHistorial', sendId, success, fail, error,
				prompt, placeholder, false);
	};

	$scope.goDiscard = function($param, $str1, $index) {
		var str = "";
		if ($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== ""
				&& $index != $scope.tableRowIndexExpandedCurr) {
			str = "Está por rechazar una factura diferente a la expandida. ";
		}
		var prompt = str + "Esta seguro de rechazar la factura?";
		var success = "La factura fue rechazada correctamente";
		var fail = "La factura fue rechazada correctamente, "
				+ "pero no se pudo informar "
				+ "al usuario de administración. "
				+ "No se pudo enviar el mail.";
		var error = "Error al intentar autorizar la factura.";
		var sendId = {
			'id' : $param,
			'descripcion' : ''
		};
		var placeholder = $str1 != null ? $str1 : "Ingresar motivo "
				+ "de rechazo.";
		$scope.historialEstado('rechazarFacturaHistorial', sendId, success,
				fail, error, prompt, placeholder, true);
	};

	$scope.goInformation = function(id, $index) {
		var str = "";
		if ($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== ""
				&& $index != $scope.tableRowIndexExpandedCurr) {
			str = "Está por solicitar informacion "
					+ "de una factura diferente a la expandida. ";
		}
		var prompt = str + "Esta seguro de solicitar " + "mas información?";
		var success = "Se solicito la informacion correctamente";
		var error = "Hubo un error al solicitar mas informacion";
		var sendId = {
			'id' : id
		};
		$scope.historialFac('informarFacturaHistorial', sendId, success, error,
				prompt);
	};

	$scope.historialEstado = function(ws, sendId, success, fail, error, prompt,
			placeholder, discard) {
		$scope.tableRowNotExpanded = true;
		alertify.prompt(prompt, function(e, str) {
			if (e) {
				if (discard) {
					var strNew = normalizeString.stringReplaceCaracter(str);
					if (str == null || str == ""
							|| str == "Ingresar motivo de rechazo."
							|| strNew.length < 14 || str.length > 255) {
						if (strNew.length < 14) {
							alertify.error("El motivo de "
									+ "rechazo de la factura debe "
									+ "tener al menos 14 carcteres.");
						} else if (str.length > 255) {
							alertify.error("El motivo de rechazo debe "
									+ "ser menor a 255 caracteres");
						} else {
							alertify.error("Debe indicar cual "
									+ "es el motivo de "
									+ "rechazo de la factura.");
						}
						return $scope.historialEstado(ws, sendId, success,
								fail, error, prompt, placeholder, discard);
					}
					sendId.descripcion = str;
				} else {
					var tieneMotivo = false;
					if (str != null && str != "" && str != 'Observación') {
						if (str.length > 255) {
							alertify.error("El motivo de observación debe "
									+ "ser menor a 255 caracteres");
							return $scope.historialEstado(ws, sendId, success,
									fail, error, prompt, placeholder, discard);
						}
						tieneMotivo = true;
					}
					sendId.estado = tieneMotivo ? 5 : 1;
					sendId.descripcion = tieneMotivo ? str : '';
				}
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success(success);
						$scope.cleanTableRowExpand();
						$scope.loadFacturas();
					} else if (data.status == 3) {
						alertify.error(fail);
						$scope.cleanTableRowExpand();
						$scope.loadFacturas();
					} else {
						alertify.error(error);
					}
				};
				$scope.showTable = true;
				webServices.getWSResponsePost($http, 'historial', ws, sendId,
						$cookieStore, callbackFuntion);
			} else {
				$scope.tableRowNotExpanded = false;
			}
		}, placeholder);
	};

	$scope.historialFac = function(ws, sendId, success, error, prompt) {
		$scope.tableRowNotExpanded = true;
		alertify.confirm(prompt, function(e) {
			if (e) {
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success(success);
					} else if (data.status == 2) {
						alertify.error("Se produjo un "
								+ "error al enviar el mail.");
					} else {
						alertify.error(error);
					}
				};
				$scope.showTable = true;
				webServices.getWSResponseGet($http, 'historial', ws, sendId,
						$cookieStore, callbackFuntion);
				$scope.tableRowNotExpanded = false;
			}
		});
	};

	// Fin metodos para las acciones

	if (FiltroFactura.page == null) {
		FiltroFactura.page = 1;
	}

	$scope.loadFilter = function() {
		$scope.filter.page = FiltroFactura.page;
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
			facId = null;
			$scope.cleanTableRowExpand();
			$scope.loadFacturas();
		}
	};

	$scope.goPage = function($number) {
		$scope.filter.page = $number;
		facId = null;
		$scope.cleanTableRowExpand();
		$scope.loadFacturas();
	};

	$scope.nextPage = function() {
		if ($scope.filter.page < $scope.filter.totalPages) {
			$scope.filter.page++;
			facId = null;
			$scope.cleanTableRowExpand();
			$scope.loadFacturas();
		}
	};

	$scope.firstPage = function() {
		$scope.filter.page = 1;
		facId = null;
		$scope.cleanTableRowExpand();
		$scope.loadFacturas();
	};

	$scope.lastPage = function() {
		$scope.filter.page = $scope.filter.totalPages;
		facId = null;
		$scope.cleanTableRowExpand();
		$scope.loadFacturas();
	};

	$scope.setRangePages = function() {
		$scope.filter.range = [];
		for (var i = 1; i <= $scope.filter.totalPages; i++) {
			$scope.filter.range.push(i);
		}
		;
	};

	$scope.order = function($variable, $varlocal) {
		$scope.cleanTableRowExpand();
		if ($scope.filter.varlocal == $varlocal) {
			$scope.filter.order = $scope.filter.order == false ? true : null;
		} else {
			$scope.filter.order = false;
		}
		$scope.filter.varlocal = $scope.filter.order == null ? "" : $varlocal;
		$scope.filter.variable = $scope.filter.order == null ? "" : $variable;
		$scope.showTable = false;
		$scope.loadFacturas();
	};

	$scope.filter = getAutorizacionFileterForm();

	$scope.loadSelectedTableRow();
	$scope.loadFilter();	
		
});

caApp.controller('autorizacionFileController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		$routeParams, tableFactory, $filter, download, utilidadesConversion,
		utilidades) {

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

caApp.controller('autorizacionNivelInferiorController',
		function($scope, $location, $http, ngTableParams, breadcrumbs,
				$cookieStore, webServices, normalizeString,
				FiltroFactura, tableFactory, utilidadesConversion,
				IndexFactura, tableCollapseFactory) {

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
	
	$scope.showPDF=false;
	$scope.pdfUrl = null;
	$scope.kanav = false;

	$scope.getNavStyle = function(scroll) {
	    if(scroll > 100) return 'pdf-controls fixed';
	    else return 'pdf-controls';
	};
	
    var facId = null;
	
    $scope.cleanTableRowExpand = function (){
		$scope.tableRowExpanded = true;
		$scope.tableRowNotExpanded = false;
	    $scope.tableRowIndexExpandedCurr = "";
	    $scope.tableRowIndexExpandedPrev = "";
	    $scope.dataCollapse = [];
	};
	
	$scope.cleanTableRowExpand();
    
    $scope.dataCollapseFn = function () {
    	$scope.dataCollapse = [];
    };
    
    $scope.loadCentros = function($facturaAdmId){
    	var tableDataAsientos = [];
    	var callbackFuntion = function(data, status, headers,
				config) {
			if (data.status == 0) {
				$scope.facHistorial = data.dto;
				if(data.dto.kanav != null){
					$scope.kanav = true;
				}
				tableDataAsientos = data.dto.asientos;
				$scope.tableAsientos = tableFactory
					.getTableData(tableDataAsientos,
						ngTableParams, $scope);
			}
		};
		facId=$facturaAdmId;
		var sendData = {
				'idFactura' : $facturaAdmId
			};
		webServices.getWSResponseGet($http, 'historial',
				'getHistorialByFinalUser', sendData,
				$cookieStore, callbackFuntion);
    };
	    
    $scope.loadSelectedTableRow = function(){
		if(IndexFactura.index != null){
			index = IndexFactura.index;
			$facturaId = IndexFactura.facId;
			$facturaAdmId = IndexFactura.facAdmId;
			$scope.tableRowExpanded = IndexFactura.tableRowExpanded;
			$scope.tableRowIndexExpandedCurr = IndexFactura.tableRowIndexExpandedCurr;
			$scope.tableRowIndexExpandedPrev = IndexFactura.tableRowIndexExpandedPrev;
			$scope.dataCollapse = IndexFactura.dataCollapse;
			$scope.pdfUrl = IndexFactura.pdfURL;
			$scope.showPDF = IndexFactura.showPDF;
			$scope.filter.page = FiltroFactura.page;
			$scope.loadCentros($facturaAdmId);
			$scope.reloadElement(index);
		};
	};
	
	$scope.reloadElement = function(index){
		setTimeout(function() {
			if(document.getElementById(index) != null){
				document.getElementById(index).style.backgroundColor = "#FF8C00";						
			} else {
				$scope.reloadElement(index);
			}
		}, 50);
	};
		
	$scope.loadArchivoAFP = function($facturaAdmId, $facturaId){
		$scope.showPDF=false;
		//Validacion para que no haga el webservice cuando es la misma factura.
		if(facId==null || facId != $facturaAdmId){
			$scope.kanav = false;
			$scope.loadCentros($facturaAdmId);
			var sendId = {
					'id' : $facturaId
				};
				webServices.getWSResponseGet($http, 'archivoFactura', 'verArchivo',
						sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
							if (data.dto != null) {
									$scope.pdfName = 'Factura.pdf';
									$scope.scroll = 0;
									$scope.pdfUrl = utilidadesConversion.base64ToUint8Array(data.dto.content);
									$scope.showPDF=true;
								}else{
									$scope.pdfUrl = null;
									$scope.showPDF=false;
								}
							IndexFactura.showPDF = $scope.showPDF;
							IndexFactura.pdfURL = $scope.pdfUrl;
							}
						});
		} else {
			if($scope.pdfUrl != null){
				$scope.showPDF=true;
			}
		}
	};
        
    $scope.selectTableRow = function (index, $facturaAdmId, $facturaId) {
    	if(!$scope.tableRowNotExpanded){
    		$scope.loadArchivoAFP($facturaAdmId, $facturaId);
			tableCollapseFactory.onSelect(index, $scope.dataCollapseFn(), $scope ,'tableRowExpanded', 'tableRowIndexExpandedCurr',
					'tableRowIndexExpandedPrev','dataCollapse');
        	IndexFactura.index = index;
        	IndexFactura.facId = $facturaId;
        	IndexFactura.facAdmId = $facturaAdmId;
        	IndexFactura.tableRowExpanded = $scope.tableRowExpanded;
        	IndexFactura.tableRowIndexExpandedCurr = $scope.tableRowIndexExpandedCurr;
        	IndexFactura.tableRowIndexExpandedPrev = $scope.tableRowIndexExpandedPrev;
        	IndexFactura.dataCollapse = $scope.dataCollapse;	
        	FiltroFactura.page = $scope.filter.page;
        }
    };

	$scope.showTable = true;

	// Inicio metodos para las acciones
	
	$scope.goAuthorize = function($param, $str1, $index) {
		var str = "";
		if ($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== ""
				&& $index != $scope.tableRowIndexExpandedCurr) {
			str = "Está por autorizar una factura diferente a la expandida. ";
		}
		var prompt = str + "Esta seguro de autorirar los " + "niveles inferiores de la factura?";
		var success = "La factura fue autorizada correctamente";
		var fail = "La factura fue autorizada correctamente, "
				+ "pero no se pudo enviar el mail.";
		var error = "Error al intentar autorizar la factura.";
		var sendId = {
			'id' : $param,
			'estado' : 0,
			'descripcion' : ''
		};
		var placeholder = $str1 != null ? $str1 : "Observación";
		$scope.historialEstado('saveAnterioresHistorial', sendId, success, fail, error,
				prompt, placeholder, false);
	};

	$scope.goDiscard = function($param, $str1, $index) {
		var str = "";
		if ($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== ""
				&& $index != $scope.tableRowIndexExpandedCurr) {
			str = "Está por rechazar una factura diferente a la expandida. ";
		}
		var prompt = str + "Esta seguro de rechazar los " + "niveles inferiores de la factura?";
		var success = "La factura fue rechazada correctamente";
		var fail = "La factura fue rechazada correctamente, "
				+ "pero no se pudo informar "
				+ "al usuario de administración. "
				+ "No se pudo enviar el mail.";
		var error = "Error al intentar autorizar la factura.";
		var sendId = {
			'id' : $param,
			'descripcion' : ''
		};
		var placeholder = $str1 != null ? $str1 : "Ingresar motivo "
				+ "de rechazo.";
		$scope.historialEstado('rechazarAnterioresHistorial', sendId, success,
				fail, error, prompt, placeholder, true);
	};

	$scope.goInformation = function(id, $index) {
		var str = "";
		if ($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== ""
				&& $index != $scope.tableRowIndexExpandedCurr) {
			str = "Está por solicitar informacion "
					+ "de una factura diferente a la expandida. ";
		}
		var prompt = str + "Esta seguro de solicitar " + "mas información?";
		var success = "Se solicito la informacion correctamente";
		var error = "Hubo un error al solicitar mas informacion";
		var sendId = {
			'id' : id
		};
		$scope.historialFac('informarFacturaHistorial', sendId, success, error,
				prompt);
	};

	$scope.historialEstado = function(ws, sendId, success, fail, error, prompt,
			placeholder, discard) {
		$scope.tableRowNotExpanded = true;
		alertify.prompt(prompt, function(e, str) {
			if (e) {
				if (discard) {
					var strNew = normalizeString.stringReplaceCaracter(str);
					if (str == null || str == ""
							|| str == "Ingresar motivo de rechazo."
							|| strNew.length < 14 || str.length > 255) {
						if (strNew.length < 14) {
							alertify.error("El motivo de "
									+ "rechazo de la factura debe "
									+ "tener al menos 14 carcteres.");
						} else if (str.length > 255) {
							alertify.error("El motivo de rechazo debe "
									+ "ser menor a 255 caracteres");
						} else {
							alertify.error("Debe indicar cual "
									+ "es el motivo de "
									+ "rechazo de la factura.");
						}
						return $scope.historialEstado(ws, sendId, success,
								fail, error, prompt, placeholder, discard);
					}
					sendId.descripcion = str;
				} else {
					var tieneMotivo = false;
					if (str != null && str != "" && str != 'Observación') {
						if (str.length > 255) {
							alertify.error("El motivo de observación debe "
									+ "ser menor a 255 caracteres");
							return $scope.historialEstado(ws, sendId, success,
									fail, error, prompt, placeholder, discard);
						}
						tieneMotivo = true;
					}
					sendId.estado = tieneMotivo ? 5 : 1;
					sendId.descripcion = tieneMotivo ? str : '';
				}
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success(success);
						$scope.cleanTableRowExpand();
						$scope.loadFacturas();
					} else if (data.status == 3) {
						alertify.error(fail);
						$scope.cleanTableRowExpand();
						$scope.loadFacturas();
					} else {
						alertify.error(error);
					}
				};
				$scope.showTable = true;
				webServices.getWSResponsePost($http, 'historial', ws, sendId,
						$cookieStore, callbackFuntion);
			} else {
				$scope.tableRowNotExpanded = false;
			}
		}, placeholder);
	};

	$scope.historialFac = function(ws, sendId, success, error, prompt) {
		$scope.tableRowNotExpanded = true;
		alertify.confirm(prompt, function(e) {
			if (e) {
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success(success);
					} else if (data.status == 2) {
						alertify.error("Se produjo un "
								+ "error al enviar el mail.");
					} else {
						alertify.error(error);
					}
				};
				$scope.showTable = true;
				webServices.getWSResponseGet($http, 'historial', ws, sendId,
						$cookieStore, callbackFuntion);
				$scope.tableRowNotExpanded = false;
			}
		});
	};
	
	// Fin metodos para las acciones
	
	if(FiltroFactura.page == null){
		FiltroFactura.page = 1;
	}

	$scope.loadFilter = function() {
		$scope.filter.page = FiltroFactura.page;
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
			facId = null;
			$scope.cleanTableRowExpand();
			$scope.loadFacturas();
		}
	};

	$scope.goPage = function($number) {
		$scope.filter.page = $number;
		facId = null;
		$scope.cleanTableRowExpand();
		$scope.loadFacturas();
	};

	$scope.nextPage = function() {
		if ($scope.filter.page < $scope.filter.totalPages) {
			$scope.filter.page++;
			facId = null;
			$scope.cleanTableRowExpand();
			$scope.loadFacturas();
		}
	};

	$scope.firstPage = function() {
		$scope.filter.page = 1;
		facId = null;
		$scope.cleanTableRowExpand();
		$scope.loadFacturas();
	};

	$scope.lastPage = function() {
		$scope.filter.page = $scope.filter.totalPages;
		facId = null;
		$scope.cleanTableRowExpand();
		$scope.loadFacturas();
	};

	$scope.setRangePages = function() {
		$scope.filter.range = [];
		for (var i = 1; i <= $scope.filter.totalPages; i++) {
			$scope.filter.range.push(i);
		}
	};

	$scope.order = function($variable, $varlocal) {
		$scope.cleanTableRowExpand();
		if ($scope.filter.variable == $variable) {
			$scope.filter.order = $scope.filter.order == false ? true : null;
		} else {
			$scope.filter.order = false;
		}
		$scope.filter.varlocal=$scope.filter.order == null ? "" : $varlocal;
		$scope.filter.variable = $scope.filter.order == null ? "" : $variable;
		$scope.showTable = false;
		$scope.loadFacturas();
	};

	$scope.filter = getAutorizacionFileterForm();

	$scope.loadSelectedTableRow();	
	$scope.loadFilter();
});