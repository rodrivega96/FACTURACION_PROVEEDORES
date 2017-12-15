caApp.controller('autorizacionAdminController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		normalizeString, FiltroFactura, tableFactory,utilidadesConversion, IndexFactura) {
	
	
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
				tableDataAsientos = data.dto.asientos;
				$scope.tableAsientos = tableFactory
					.getTableData(tableDataAsientos,
						ngTableParams, $scope);
			}
		};
		facId=$facturaAdmId;
		var sendData = {
				'idFactura' : $facturaAdmId,
				'idUsuario' : $cookieStore.get('vatesuser').userId
			};
		webServices.getWSResponseGet($http, 'historial',
				'getHistorialByFinalUser', sendData,
				$cookieStore, callbackFuntion);
    };
    
    
	$scope.showPDF=false;
	$scope.pdfUrl=null;

	$scope.getNavStyle = function(scroll) {
	    if(scroll > 100) return 'pdf-controls fixed';
	    else return 'pdf-controls';
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
	
    $scope.selectTableRow = function (index, $facturaAdmId, $facturaId) {
    	if(!$scope.tableRowNotExpanded){
    	$scope.showPDF=false;
		//Validacion para que no haga el webservice cuando es la misma factura.
		if(facId==null || facId != $facturaAdmId){
			$scope.loadCentros($facturaAdmId);
			var sendId = {
				'id' : $facturaId
			};
			webServices.getWSResponseGet($http, 'archivoFactura', 'verArchivo',
					sendId, $cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
						if (data.dto != null) {
								$scope.pdfName = 'Factura';
								$scope.scroll = 0;
								$scope.pdfUrl = utilidadesConversion.base64ToUint8Array(data.dto.content);
								$scope.showPDF=true;
							} else {
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
		    	
        if (typeof $scope.dataCollapse === 'undefined') {
            $scope.dataCollapseFn();
        }
        
        	if ($scope.tableRowExpanded === false && $scope.tableRowIndexExpandedCurr === "") {       	
        		$scope.tableRowIndexExpandedPrev = "";
        		$scope.tableRowExpanded = true;
        		$scope.tableRowIndexExpandedCurr = index;          
        		document.getElementById(index).style.backgroundColor = "#FF8C00";
        		$scope.dataCollapse[index] = true;
        	} else if ($scope.tableRowExpanded === true) {
        		if ($scope.tableRowIndexExpandedCurr === index) {            	
        			document.getElementById(index).style.backgroundColor = "#CACACA";
        			$scope.tableRowExpanded = false;
        			$scope.tableRowIndexExpandedCurr = "";
        			$scope.dataCollapse[index] = false;
        		} else {
        			if($scope.tableRowIndexExpandedCurr !== "" && $scope.tableRowIndexExpandedCurr != index){
        				document.getElementById($scope.tableRowIndexExpandedCurr).style.backgroundColor = "#CACACA"; 
        			}           	
        			document.getElementById(index).style.backgroundColor="#FF8C00";
        			$scope.tableRowIndexExpandedPrev = $scope.tableRowIndexExpandedCurr;
        			$scope.tableRowIndexExpandedCurr = index;
        			$scope.dataCollapse[$scope.tableRowIndexExpandedPrev] = false;
        			$scope.dataCollapse[$scope.tableRowIndexExpandedCurr] = true;
        		}
        	}
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

	
	$scope.goAuthorize = function($param,$str1, $index) {
		$scope.tableRowNotExpanded = true;
		var str = "";
		if($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== "" && $index != $scope.tableRowIndexExpandedCurr){
			str = "Está por autorizar una factura diferente a la expandida. ";
		}
		alertify
				.prompt(str + "Esta seguro de autorirar la factura?",
						function(e, str) {
							if (e) {
								var strNew = normalizeString
										.stringReplaceCaracter(str);
								var tieneMotivo = false;
								if (str != null && str != "" && str!='Observaci\u00f3n'){
									if( strNew.length < 14){
										alertify.error("El motivo de "
												+ "observaci\u00f3n de la factura debe tener al menos 14 carcteres.");
										return $scope.goAuthorize($param,str);
									} else if (str.length > 255){
										alertify.error("El motivo de observación debe "
												+ "ser menor a 255 caracteres");
										return $scope.goAuthorize($param,str);
									}
									tieneMotivo=true;
								}
								
                         var sendId = {
							'id' : $param,
							'estado' : tieneMotivo?5:1,
							'descripcion' : tieneMotivo?str:''
						};
						var callbackFuntion = function(data, status, headers,
								config) {
							if (data.status == 0) {
								alertify.success("La factura fue "
										+ "autorizada correctamente");
								$scope.cleanTableRowExpand();
								$scope.loadFacturas();
							} else if (data.status == 3) {
								alertify.error("La factura fue "
										+ "autorizada correctamente, pero "
										+ "no se pudo enviar el mail.");
								$scope.cleanTableRowExpand();
								$scope.loadFacturas();
							} else {

							}
						};
						webServices.getWSResponsePost($http, 'historial',
								'saveHistorial', sendId, $cookieStore,
								callbackFuntion);
								
							} else {
								$scope.tableRowNotExpanded = false;
							}
						}, $str1!=null?$str1:"Observaci\u00f3n");
	};

	$scope.goDiscard = function($param, $str1, $index) {
		$scope.tableRowNotExpanded = true;
		var str = "";
		if($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== "" && $index != $scope.tableRowIndexExpandedCurr){
			str = "Está por rechazar una factura diferente a la expandida. ";
		}
		alertify
				.prompt(str + "Esta seguro de rechazar la factura?",
						function(e, str) {
							if (e) {
								var strNew = normalizeString
										.stringReplaceCaracter(str);
								if (str == null
										|| str == ""
										|| str == "Ingresar motivo "
												+ "de rechazo."
										|| strNew.length < 14 || str.length > 255) {
									if(strNew.length < 14){
										alertify.error("El motivo de "
												+ "rechazo de la factura debe tener al menos 14 carcteres.");
									} else if (str.length > 255){
										alertify.error("El motivo de rechazo debe "
												+ "ser menor a 255 caracteres");
									} else{
									alertify.error("Debe indicar cual "
											+ "es el motivo de "
											+ "rechazo de la factura.");
									}
									return $scope.goDiscard($param,str);
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
										$scope.cleanTableRowExpand();
										$scope.loadFacturas();
									} else if (data.status == 3) {
										alertify.error("La factura fue "
												+ "rechazada correctamente, "
												+ "pero no se pudo informar "
												+ "al usuario de "
												+ "administración. No "
												+ "se pudo enviar el mail.");
										$scope.cleanTableRowExpand();
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
							} else {
								$scope.tableRowNotExpanded = false;
							}
						}, $str1!=null?$str1:"Ingresar motivo de rechazo.");
	};
	
	$scope.goInformation = function($facId, $index) {
		$scope.tableRowNotExpanded = true;
		var str = "";
		if($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== "" && $index != $scope.tableRowIndexExpandedCurr){
			str = "Está por solicitar informacion de una factura diferente a la expandida. ";
		}
		alertify.confirm(str + "Esta seguro de solicitar mas información" ,function(e) {
			if(e){
				var sendId = {
						'id' : $facId
					};
					
				var callbackFuntion = function(data, status,
						headers, config) {
					if (data.status == 0) {
						alertify.success("Se solicito la informacion correctamente");
					} else if (data.status == 2){
						alertify.error("Se produjo un error al enviar el mail.");
					} else {
						alertify.error("Error al solicitar la informacion");
					}
				};
				
				$scope.showTable = true;
				webServices.getWSResponseGet($http,
						'historial',
						'informarFacturaHistorial', sendId,
						$cookieStore, callbackFuntion);				
			}
			$scope.tableRowNotExpanded = false;

		});
	};
	

	// Fin metodos para las acciones

	if(FiltroFactura.page == null){
		FiltroFactura.page = 1;
	}

	$scope.loadFilter = function() {
		$scope.filter.page = FiltroFactura.page;
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
		};
	};

	$scope.order = function($variable,$varlocal) {
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
	$scope.extFilter = getAutorizacionFilter();

	$scope.loadSelectedTableRow();	
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
		webServices, normalizeString, FiltroFactura, tableFactory,utilidadesConversion, IndexFactura) {

	
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
				tableDataAsientos = data.dto.asientos;
				$scope.tableAsientos = tableFactory
					.getTableData(tableDataAsientos,
						ngTableParams, $scope);
			}
		};
		facId=$facturaAdmId;
		var sendData = {
				'idFactura' : $facturaAdmId,
				'idUsuario' : $cookieStore.get('vatesuser').userId
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
			
        
    $scope.selectTableRow = function (index, $facturaAdmId, $facturaId) {
    	if(!$scope.tableRowNotExpanded){
    	$scope.showPDF=false;
		//Validacion para que no haga el webservice cuando es la misma factura.
		if(facId==null || facId != $facturaAdmId){
			$scope.loadCentros($facturaAdmId);
			var sendId = {
					'id' : $facturaId
				};
				webServices.getWSResponseGet($http, 'archivoFactura', 'verArchivo',
						sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
							if (data.dto != null) {
									$scope.pdfName = 'Factura';
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
    	
        if (typeof $scope.dataCollapse === 'undefined') {
            $scope.dataCollapseFn();
        }

        
        	if ($scope.tableRowExpanded === false && $scope.tableRowIndexExpandedCurr === "") {       	
                $scope.tableRowIndexExpandedPrev = "";
                $scope.tableRowExpanded = true;
                $scope.tableRowIndexExpandedCurr = index;          
                document.getElementById(index).style.backgroundColor = "#FF8C00";
                $scope.dataCollapse[index] = true;
            } else if ($scope.tableRowExpanded === true) {
                if ($scope.tableRowIndexExpandedCurr === index) {            	
                	document.getElementById(index).style.backgroundColor = "#CACACA";
                    $scope.tableRowExpanded = false;
                    $scope.tableRowIndexExpandedCurr = "";
                    $scope.dataCollapse[index] = false;
                } else {
                	if($scope.tableRowIndexExpandedCurr !== "" && $scope.tableRowIndexExpandedCurr != index){
                		document.getElementById($scope.tableRowIndexExpandedCurr).style.backgroundColor = "#CACACA"; 
                	}           	
                    document.getElementById(index).style.backgroundColor = "#FF8C00";
                    $scope.tableRowIndexExpandedPrev = $scope.tableRowIndexExpandedCurr;
                    $scope.tableRowIndexExpandedCurr = index;
                    $scope.dataCollapse[$scope.tableRowIndexExpandedPrev] = false;
                    $scope.dataCollapse[$scope.tableRowIndexExpandedCurr] = true;
                }
            }
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



	$scope.goAuthorize = function($param,$str1,$index) {
		$scope.tableRowNotExpanded = true;
		var str = "";
		if($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== "" && $index != $scope.tableRowIndexExpandedCurr){
			str = "Está por autorizar una factura diferente a la expandida. ";
		}
		alertify
				.prompt(str + "Esta seguro de autorirar los " 
						+ "niveles inferiores de la factura?",
						function(e, str) {
							if (e) {
								var strNew = normalizeString
										.stringReplaceCaracter(str);
								var tieneMotivo = false;
								if (str != null && str != "" && str!='Observaci\u00f3n'){
									if( strNew.length < 14){
										alertify.error("El motivo de "
												+ "observaci\u00f3n de la factura debe tener al menos 14 carcteres.");
										return $scope.goAuthorize($param,str);
									} else if (str.length > 255){
										alertify.error("El motivo de observación debe "
												+ "ser menor a 255 caracteres");
										return $scope.goAuthorize($param,str);
									}
									tieneMotivo=true;
								}
                            var sendId = {
							'id' : $param,
							'estado' : tieneMotivo?5:1,
							'userId' : $cookieStore.get('vatesuser').userId,
							'descripcion' : tieneMotivo?str:''
						};
						var callbackFuntion = function(data, status, headers,
								config) {
							if (data.status == 0) {
								alertify.success("La factura fue "
										+ "autorizada correctamente");
								$scope.cleanTableRowExpand();
								$scope.loadFacturas();
							} else if (data.status == 3) {
								alertify.error("La factura fue "
										+ "autorizada correctamente, pero "
										+ "no se pudo enviar el mail.");
								$scope.cleanTableRowExpand();
								$scope.loadFacturas();
							} else {

							}
						};
						webServices.getWSResponsePost($http, 'historial',
								'saveAnterioresHistorial', sendId, $cookieStore,
								callbackFuntion);
								
							} else {
								$scope.tableRowNotExpanded = false;
							}
						}, $str1!=null?$str1:"Observaci\u00f3n");
	};

	$scope.goDiscard = function($param,$str1, $index) {
		$scope.tableRowNotExpanded = true;
		var str = "";
		if($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== "" && $index != $scope.tableRowIndexExpandedCurr){
			str = "Está por rechazar una factura diferente a la expandida. ";
		}
		alertify
				.prompt(str + "Esta seguro de rechazar los "
				+ "niveles inferiores de la factura?", function(e, str) {
			if (e) {
				var strNew = normalizeString.stringReplaceCaracter(str);
				if (str == null || str == ""
						|| str == "Ingresar motivo de rechazo."
						|| strNew.length < 14 || str.length > 255) {
					if(strNew.length < 14){
						alertify.error("El motivo de "
								+ "rechazo de la factura debe tener al menos 14 carcteres.");
					} else if (str.length > 255){
								alertify.error("El motivo de rechazo debe "
										+ "ser menor a 255 caracteres");
					} else{
					alertify.error("Debe indicar cual es el motivo de "
							+ "rechazo de la factura.");
					}
					return $scope.goDiscard($param,str);
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
						$scope.cleanTableRowExpand();
						$scope.loadFacturas();
					} else if (data.status == 3) {
						alertify.error("La factura fue rechazada "
								+ "correctamente, pero no se "
								+ "pudo enviar el mail al usuario "
								+ "administrador.");
						$scope.cleanTableRowExpand();
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
			} else {
				$scope.tableRowNotExpanded = false;
			}
		}, $str1!=null?$str1:"Ingresar motivo de rechazo.");
	};
	
	$scope.goInformation = function($facId, $index) {
		$scope.tableRowNotExpanded = true;
		var str = "";
		if($scope.tableRowExpanded && $scope.tableRowIndexExpandedCurr !== "" && $index != $scope.tableRowIndexExpandedCurr){
			str = "Está por solicitar informacion de una factura diferente a la expandida. ";
		}
		alertify.confirm(str + "Esta seguro de solicitar mas información" ,function(e) {
			if(e){
				var sendId = {
						'id' : $facId
					};
					
				var callbackFuntion = function(data, status,
						headers, config) {
					if (data.status == 0) {
						alertify.success("Se solicito la informacion correctamente");
					} else if (data.status == 2){
						alertify.error("Se produjo un error al enviar el mail.");
					} else {
						alertify.error("Error al solicitar la informacion");
					}
				};
				
				$scope.showTable = true;
				webServices.getWSResponseGet($http,
						'historial',
						'informarFacturaHistorial', sendId,
						$cookieStore, callbackFuntion);				
			}
			$scope.tableRowNotExpanded = false;

		});
	};

	// Fin metodos para las acciones
	
	if(FiltroFactura.page == null){
		FiltroFactura.page = 1;
	}

	$scope.loadFilter = function() {
		$scope.filter.page = FiltroFactura.page;
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
	$scope.extFilter = getAutorizacionFilter();

	$scope.loadSelectedTableRow();	
	$scope.loadFilter();
});