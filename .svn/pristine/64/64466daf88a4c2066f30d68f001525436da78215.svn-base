caApp.registerCtrl('autorizacionNivelInferiorController',
		function($scope, $location, $http, ngTableParams, breadcrumbs,
				$cookieStore, webServices, normalizeString,
				FiltroFactura, tableFactory, utilidadesConversion,
				IndexFactura, tableCollapseFactory, paginatedFunction, AModel, utilidades, FiltroFactura) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.isShowFilter = false;
	
	$scope.clearFilter = function() {
		$scope.extFilter = AModel.getFacturaAdmFilter();
		FiltroFactura.dataConsult = $scope.extFilter;
	};
	
	$scope.tipos = AModel.getTipos();
	
	$scope.showFilter = function(state) {
		$scope.isShowFilter = state;
	};

	$scope.loadFilterFields = function() {
		$scope.filter.numero = FiltroFactura.dataConsult.numero;
		$scope.filter.tipo = FiltroFactura.dataConsult.tipo;
		$scope.filter.cuit = FiltroFactura.dataConsult.cuit;
		$scope.filter.razon = FiltroFactura.dataConsult.razon;
		$scope.filter.cc = FiltroFactura.dataConsult.cc;
		$scope.filter.centroc = FiltroFactura.dataConsult.centroc;
		$scope.filter.wf = FiltroFactura.dataConsult.wf;
		$scope.filter.fvDesde = FiltroFactura.dataConsult.fvDesde;
		$scope.filter.fiDesde = FiltroFactura.dataConsult.fiDesde;
		$scope.filter.flfDesde = FiltroFactura.dataConsult.flfDesde;
		$scope.filter.fvHasta = FiltroFactura.dataConsult.fvHasta;
		$scope.filter.fiHasta = FiltroFactura.dataConsult.fiHasta;
		$scope.filter.flfHasta = FiltroFactura.dataConsult.flfHasta;
		$scope.filter.userSelected = FiltroFactura.dataConsult.userSelected;
		$scope.filter.ffacDesde = FiltroFactura.dataConsult.ffacDesde;
		$scope.filter.ffacHasta = FiltroFactura.dataConsult.ffacHasta;
		$scope.filter.estado = FiltroFactura.dataConsult.estado;
		$scope.filter.page = FiltroFactura.page;

	};

	$scope.rollbackFilterFields = function() {
		FiltroFactura.dataConsult.cuit = $scope.filter.cuit;
		FiltroFactura.dataConsult.razon = $scope.filter.razon;
		FiltroFactura.dataConsult.numero = $scope.filter.numero;
		FiltroFactura.dataConsult.cc = $scope.filter.cc;
		FiltroFactura.dataConsult.centroc = $scope.filter.centroc;
		FiltroFactura.dataConsult.wf = $scope.filter.wf;
		FiltroFactura.dataConsult.fvDesde = $scope.filter.fvDesde;
		FiltroFactura.dataConsult.fiDesde = $scope.filter.fiDesde;
		FiltroFactura.dataConsult.flfDesde = $scope.filter.flfDesde;
		FiltroFactura.dataConsult.fvHasta = $scope.filter.fvHasta;
		FiltroFactura.dataConsult.fiHasta = $scope.filter.fiHasta;
		FiltroFactura.dataConsult.flfHasta = $scope.filter.flfHasta;
		FiltroFactura.dataConsult.userSelected = $scope.filter.userSelected;
		FiltroFactura.dataConsult.ffacDesde = $scope.filter.ffacDesde;
		FiltroFactura.dataConsult.ffacHasta = $scope.filter.ffacHasta;
		FiltroFactura.dataConsult.tipo = $scope.filter.tipo;
		FiltroFactura.dataConsult.estado = $scope.filter.estado;
		FiltroFactura.page = $scope.filter.page;
	};
	$scope.goUploadFile = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
	};

	$scope.goDetailWF = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
	};

	$scope.goURL = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
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
				'idFactura' : $facturaAdmId,
				'simp' : true,
				'onlyDet' : true
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
			$scope.loadPendig(sendId);
			$scope.loadArchivoFactura(sendId);
		} else {
			if($scope.pdfUrl != null){
				$scope.showPDF=true;
			}
		}
	};
	
	$scope.loadPendig = function(sendId){
		var callBackFunction = function(data, status, headers,
				config) {
			if (data.status == 0) {
				$scope.pendientes = data.listNivelView;
			}
		};
		webServices.getWSResponseGet($http, 'factura', 'getPendingOk',
				sendId, $cookieStore, callBackFunction);
	};
	
	$scope.loadArchivoFactura = function (sendId){
		var calbackFuncion = function(data, status, headers, config) {
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
			};
		webServices.getWSResponseGet($http, 'archivoFactura', 'verArchivo',
				sendId, $cookieStore, calbackFuncion);
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
		var prompt = str + "Esta seguro de autorizar los " + "niveles inferiores de la factura?";
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
		var placeholder = "Ingrese su consulta";
		var sendId = {
			'id' : id,
			descripcion : ''
		};
		$scope.historialFac('informarFacturaHistorial', sendId, success, error,
				prompt, placeholder);
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

	$scope.historialFac = function(ws, sendId, success, error, prompt, placeholder) {
		$scope.tableRowNotExpanded = true;
		alertify.prompt(prompt, function(e, str) {
			if (e) {
				sendId.descripcion = str==placeholder?'':str;
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
			}else{
				$scope.tableRowNotExpanded = false;
			}
		}, placeholder);
	};
	
	// Fin metodos para las acciones
	
	
	

	$scope.loadFilter = function() {
		$scope.cleanTableRowExpand();
		$scope.filter.page = FiltroFactura.page;
		$scope.loadFilterFields();
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

	$scope.filter = AModel.getFacturaAdmFilterForm();
	if (FiltroFactura.dataConsult == null
			|| FiltroFactura.dataConsult.length == 0) {
		FiltroFactura.dataConsult = AModel.getFacturaAdmFilter();
	}
	if(FiltroFactura.page == null){
		FiltroFactura.page = 1;
	}
	
	$scope.userList = FiltroFactura.dataConsult.userList;
	$scope.userSelected=null;
	$scope.loadUsers = function(){
		webServices.getWSResponseGet($http, 'user', 'getAuthorizers', null,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						$scope.userList = data.userList;
						$scope.userList.sort(dynamicSort("lastName"));
						FiltroFactura.dataConsult.userList = $scope.userList;
					} else {
						alertify.error("Error al traer la lista de usuarios: "
								+ data.message);
					}
				});
	};
	 
	if($scope.userList==null){
		$scope.loadUsers();
	}
	
	$scope.extFilter = FiltroFactura.dataConsult;
	$scope.loadSelectedTableRow();	
	$scope.loadFilter();
	
	$scope.paginatedAutoInfName='';
	$scope.filterName='filter';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedAutoInfName', $scope.rollbackFilterFields, 'filter.page', $scope.loadFacturas,
			'filter.totalPages', 'filter.range', function() {
				facId = null;
				FiltroFactura.page = $scope.filter.page;
				$scope.cleanTableRowExpand();
			});
});