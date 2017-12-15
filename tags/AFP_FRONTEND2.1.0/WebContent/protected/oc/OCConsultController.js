caApp.registerCtrl('oCAdminConsultController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		tableFactory, $filter, FiltroReportOC, download, utilidadesConversion,
		tableCollapseFactory, utilidades, $q, paginatedFunction, OCModel, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.facturaPorOC = {};
	
	$scope.go = function(path) {
		$location.path(utilidades.setPath($cookieStore,path,$location.path()));
	};
	
	$scope.loadDataFilter =  function(){
    	var callbackFuntion = function(data, status, headers, config) {
    		$scope.estados = data.datoInicialOrdenDTO.estados;
    		$scope.clientes = data.datoInicialOrdenDTO.clientes;	
    		$scope.estadosPreFac = data.datoInicialOrdenDTO.estadosPreFac;
    		$scope.showClientes=true;
    	};
    	webServices.getWSResponseGet($http, 'orden', 'getFiltroInicial',
			null, $cookieStore, callbackFuntion);
    };
    
    $scope.rollbackFilterFields = function() {
    	$scope.filterConsult = FiltroReportOC.dataConsult;
	};
	
    $scope.rollbackFilterFieldsPF = function() {
		$scope.filterConsultPF.estadoPreFac = FiltroReportOC.dataConsultPF.estadoPreFac;
		$scope.filterConsultPF.pagePF = FiltroReportOC.dataConsultPF.pagePF;
	};
	
	$scope.loadFilterSearch = function(){
		$scope.pagePreloadedOC = 1;
		$scope.ocId = null;
		$scope.indexOC = undefined;
		$scope.cleanTableRowOCExpand();
		$scope.loadFilter();
	};
	
	$scope.loadFilter = function() {
		$scope.filterConsult.pageOC = $scope.pagePreloadedOC;
		FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
		$scope.loadFilterFields();
		$scope.loadConsultOC();
	};
	
	$scope.loadFilterPF = function() {
		$scope.filterConsultPF.pagePF = $scope.pagePreloadedPF;
		FiltroReportOC.dataConsultPF.pagePF = $scope.filterConsultPF.pagePF;
		$scope.loadFilterFieldsPF();
	};
	
	$scope.clearFilter = function() {
		var pageOC = $scope.filterConsult.pageOC;
		var rangeOC = $scope.filterConsult.rangeOC;
		var pagePF = $scope.filterConsultPF.pagePF;
		var rangePF = $scope.filterConsultPF.rangePF;
		$scope.filterConsult = OCModel.getOCFilterConsult();
		$scope.filterConsult.pageOC = pageOC;
		$scope.filterConsult.rangeOC = rangeOC;
		$scope.filterConsultPF = OCModel.getPFFilterConsult();
		$scope.filterConsultPF.rangePF = rangePF;
		$scope.filterConsultPF.pagePF = pagePF;
	};
	
	$scope.loadFilterFields = function() {
		FiltroReportOC.dataConsult = $scope.filterConsult;
	};
	
	$scope.loadFilterFieldsPF = function() {
		FiltroReportOC.dataConsultPF.estadoPreFac = $scope.filterConsultPF.estadoPreFac;
		FiltroReportOC.dataConsultPF.pagePF = $scope.filterConsultPF.pagePF;
	};
	
	$scope.loadConsultOC = function() {
		var callbackFuntion = function(data, status, headers, config) {
			$scope.messageOC = false;
			$scope.ocs = data.ordenView.content;
			$scope.filterConsult.totalPagesOC = data.ordenView.totalPages;
			$scope.fromCountOC = (data.ordenView.number * data.ordenView.size) + 1;
			$scope.toCountOC = (data.ordenView.number * data.ordenView.size) + data.ordenView.numberOfElements;
			$scope.totalCountOC = data.ordenView.totalElements;
			$scope.countOC = (data.ordenView.numberOfElements > 0 ? $scope.fromCountOC : 0) + ' - '
					+ $scope.toCountOC + ' de ' + $scope.totalCountOC;
			if(data.ordenView.totalElements == 0){
				$scope.messageOC = true;
				$scope.warningOCMessage = 'No se encontraron Ordenes de Compra';
			}			
			$scope.setRangePagesOC();
		};
		webServices.getWSResponseGet($http, 'orden', 'ocPaginatedConsulList',
				$scope.filterConsult, $cookieStore, callbackFuntion);
	};

	$scope.loadFacturas = function(sendId) {
		var callbackFuntion = function(data, status, headers,
				config) {
			$scope.totalFactura = 0;
			if (data.status == 0) {
				$scope.facturas = data.facturaList;
				if($scope.facturas.length > 0){
					$scope.showFacturas = true;
				} else {
					$scope.showFacturas = false;
					$scope.messageFacturaWarning = "No hay facturas asociadas "
						+"en la Orden de Compra";
				}
				$scope.ocId = sendId.id;
			}
		};
		webServices.getWSResponseGet($http, 'orden',
					'getFacturaClienteByOrdenId', sendId,
					$cookieStore, callbackFuntion);
	};
	
	$scope.loadArchivos = function(sendId) {
		webServices.getWSResponseGet($http, 'orden', 'getArchivoByOrdenId',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						$scope.archivos = data.archivosList;
						if ($scope.archivos.length > 0) {
							$scope.showFiles = true;
						} else {
							$scope.messageFileWarning = "No hay archivos cargados "
								+"en la Orden de Compra";
						}
					}
		});
	};


	$scope.getNavStyle = function(scroll) {
	    if(scroll > 100) return 'pdf-controls fixed';
	    else return 'pdf-controls';
	};

	$scope.modalFindProject = false;
	$scope.modalFindManager = false;
	
	$scope.selectedConsult = function(id, idCompare){
		return id==idCompare;
	};
	
	$scope.findConsultProject = function() {
	    $scope.selectedConsultProject=$scope.filterConsult.proyectoNombre;
	    $scope.filterConsultProject.seleccionado=$scope.filterConsult.proyectoId;
	    $scope.filterConsultProject.clienteId=Number($scope.filterConsult.clienteId);
	    $scope.filterConsultProject.managerId = $scope.filterConsult.managerId;
	    $scope.modalFindProject = true;
	    if($scope.tableConsultFindProject != undefined && $scope.tableConsultFindProject.data.length > 0){
	    	$scope.showTableModalFindProject = true;
	    }
	};
	
	$scope.findConsultManager = function() {
	    $scope.selectedConsultManager=$scope.filterConsult.managerNombre;
	    $scope.filterConsultManager.seleccionado=$scope.filterConsult.managerId;
	    $scope.modalFindManager = true;
	    if($scope.tableConsultFindManager != undefined && $scope.tableConsultFindManager.data.length > 0){
	    	$scope.showTableModalFindManager = true;
	    }
	};
	
	$scope.closeFindConsultProject = function() {
		$scope.filterConsultProject.seleccionado=0;
		$scope.selectedConsultProject='';
		$scope.modalFindProject = false;
		$scope.showTableModalFindProject = false;
	};
	
	$scope.closeFindConsultManager = function() {
		$scope.filterConsultManager.seleccionado=0;
		$scope.selectedConsultManager='';
		$scope.modalFindManager = false;
		$scope.showTableModalFindManager = false;
	};
	
	$scope.acceptFindConsultProject = function() {
		$scope.filterConsult.proyectoNombre=$scope.selectedConsultProject;
		$scope.filterConsult.proyectoId=$scope.filterConsultProject.seleccionado;
		$scope.modalFindProject = false;
		$scope.showTableModalFindProject = false;	
	};
	
	$scope.acceptFindConsultManager = function() {
		if($scope.filterConsultManager.seleccionado != $scope.filterConsult.managerId){
			$scope.clearProject();
		}
		$scope.filterConsult.managerNombre = $scope.selectedConsultManager;
		$scope.filterConsult.managerId = $scope.filterConsultManager.seleccionado;		
		$scope.modalFindManager = false;
		$scope.showTableModalFindManager = false;
	};
	
	$scope.onSelectClient = function () {
		if($scope.filterConsult.clienteId != ""){
			$scope.clearProject();		
		}
	};
	
	$scope.clearProject = function () {
		$scope.tableConsultFindProject = undefined;
		$scope.clearSelectedConsultProject();
	};
	
	$scope.radioCheckConsultProject = function (project) {		
	    if ($scope.filterConsultProject.seleccionado != project.id){
	    	$scope.filterConsultProject.seleccionado=project.id;
	    	$scope.selectedConsultProject=project.nombre;
	    }     
	};
	
	$scope.radioCheckConsultManager = function (manager) {	
	    if ($scope.filterConsultManager.seleccionado != manager.id){
	    	$scope.filterConsultManager.seleccionado=manager.id;
	    	$scope.selectedConsultManager=manager.nombre;
	    }     
	};
	
	$scope.clearSelectedConsultProject = function (){
    	$scope.filterConsult.proyectoNombre='';
   	    $scope.filterConsult.proyectoId=0;
	};
	
	$scope.clearSelectedConsultManager = function (){
    	$scope.filterConsult.managerNombre='';
   	    $scope.filterConsult.managerId=0;
   	    $scope.tableConsultFindProject = undefined;
	};
	
	$scope.projectConsultFilterSearch = function(){		
		 $scope.showTableModalFindProject = false;
		 var callbackFuntion = function(data, status, headers, config) {
			 if(data.status == 0){
				 $scope.tableConsultFindProject = tableFactory.getTableDataModal(data.proyectosDTO, ngTableParams,
						 $scope,2);
				 if(data.proyectosDTO.length > 0) {
					 $scope.showTableModalFindProject = true;
					 $scope.consultProyectoMessage = false;
				 } else {
					 $scope.showTableModalFindProject = false;
					 $scope.consultProyectoMessage = true;
					 $scope.warningConsultProyectoMessage = 'No se encontraron Proyectos';
				 }
			 }
		};
		webServices.getWSResponseGet($http, 'orden', 'getFiltroProyecto',
				$scope.filterConsultProject, $cookieStore, callbackFuntion);
	 };
	 
	 $scope.managerConsultFilterSearch = function(){
		 $scope.showTableModalFindManager = false;
		 var callbackFuntion = function(data, status, headers, config) {
			 if(data.status == 0){
				 $scope.tableConsultFindManager = tableFactory.getTableDataModal(data.managersDTO, ngTableParams,
						 $scope,2);
				 if(data.managersDTO.length > 0){
					 $scope.showTableModalFindManager = true;	
					 $scope.consultManagerMessage = false;
				 } else {
					 $scope.showTableModalFindManager = false;
					 $scope.consultManagerMessage = true;
					 $scope.warningConsultManagerMessage = 'No se encontraron Managers';
				 }
			 }
		};
		webServices.getWSResponseGet($http, 'orden', 'getFiltroManager',
				$scope.filterConsultManager, $cookieStore, callbackFuntion);
	 };
	 
	// Inicio Acordeon PreFac
	$scope.cleanTableRowPreFacExpand = function() {
		$scope.tableRowPreFacExpanded = true;
		$scope.tableRowPreFacNotExpanded = false;
		$scope.tableRowPreFacIndexExpandedCurr = "";
		$scope.tableRowPreFacIndexExpandedPrev = "";
		$scope.dataCollapsePreFac = [];
		if($scope.indexPF != undefined){
			document.getElementById($scope.indexPF).style.backgroundColor = "#CACACA";
		}
	};

	$scope.cleanTableRowPreFacExpand();
	
	$scope.dataCollapsePreFacFn = function() {
		$scope.dataCollapsePreFac = [];
	};

	 $scope.selectTableRowPreFac = function(index, prefId) {
		 if (!$scope.tableRowPreFacNotExpanded) {
			 $scope.totalPeople = 0;
			    tableCollapseFactory.onSelect(index, $scope.dataCollapsePreFacFn(), $scope ,'tableRowPreFacExpanded', 'tableRowPreFacIndexExpandedCurr',
			    		'tableRowPreFacIndexExpandedPrev','dataCollapsePreFac', 'indexPF');
			   $scope.loadSelectedPrefa(prefId);
		 }
	 };
	 
	 $scope.loadSelectedPrefa = function(prefId) {
		    if($scope.prefId==null || $scope.prefId != prefId){
				var sendId = {
					'id' : prefId
				};
				webServices.getWSResponseGet($http, 'orden', 'getColaboradoresOCs',
						sendId, $cookieStore, function(data, status, headers, config) {
							if (data.status == 0) {
								$scope.totalPeople = 0;
								$scope.prefId = prefId; 
								$scope.peoples = data.peoples;
								$scope.preFacOCs = data.ocs;
							}
				});
	        }
	 };
	 
	 $scope.setTotal = function(importe, field){
		 if(importe!=undefined){
			 $scope[field] = (Number($scope[field])
				+ Number(importe)).toFixed(2);
		 }
	 };

	// Inicio Acordeon OC
	$scope.cleanTableRowOCExpand = function() {
		$scope.tableRowOCExpanded = true;
		$scope.tableRowOCNotExpanded = false;
		$scope.tableRowOCIndexExpandedCurr = "";
		$scope.tableRowOCIndexExpandedPrev = "";
		$scope.dataCollapseOC = [];
		if($scope.indexOC != undefined){
			document.getElementById($scope.indexOC).style.backgroundColor = "#CACACA";
		}	
	};

	$scope.cleanTableRowOCExpand();
	
	$scope.dataCollapseOCFn = function() {
		$scope.dataCollapseOC = [];
	};
	
	$scope.loadSelectedTableOCRow = function() {
		var sendId = {
				'id' : $scope.ocId
		};
		$scope.loadConsultOC();
		$scope.loadFacturas(sendId);
		$scope.loadArchivos(sendId);
		if($scope.tableRowOCIndexExpandedCurr != ""){
			$scope.reloadElement($scope.indexOC);						
		}
	};
	
	$scope.reloadElement = function(index) {
		setTimeout(function() {
			if ($http.pendingRequests.length == 0) {
				document.getElementById(index).style.backgroundColor = "#FF8C00";
			} else {
				$scope.reloadElement(index);
			}
		}, 50);
	};
	
	 $scope.selectTableRowOC = function(index, $idOC) {	
		 // index + 10000 Para no tener problema con el acordeon de prefac
		 index = index + 10000;
		 if (!$scope.tableRowOCNotExpanded) {
			 $scope.totalFactura = 0;
			 $scope.showPDF = false;
			 $scope.showFiles = false;
			 $scope.showFacturas = false;
				//Validacion para que no haga el webservice cuando es la misma factura.
			if($scope.ocId==null || $scope.ocId != $idOC){
				var sendId = {
						'id' : $idOC
					};
				$scope.loadFacturas(sendId);
				$scope.loadArchivos(sendId);				
			} else {
				if($scope.pdfUrl != null){
					$scope.showPDF = true;
				}
				if($scope.archivos.length > 0){
					$scope.showFiles = true;
				}
				if($scope.facturas.length > 0){
					$scope.showFacturas = true;
				}
			} 	
			tableCollapseFactory.onSelect(index, $scope.dataCollapseOCFn(), $scope ,'tableRowOCExpanded', 'tableRowOCIndexExpandedCurr',
		    		'tableRowOCIndexExpandedPrev','dataCollapseOC', 'indexOC');
			$scope.ocId = $idOC;
		 }
	 };
	 
	 $scope.download = function($file) {
		$scope.pdfUrl = null;
		$scope.showPDF = false;
		var sendId = {
			'id' : $file.id
		};
		webServices.getWSResponseGet($http, 'archivo', 'downloadArchivo',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						if(utilidades.isPDF($file.name)){
							if (data.dto != null) {
								$scope.pdfName = $file.name;
								$scope.scroll = 0;
								$scope.pdfUrl = utilidadesConversion.base64ToUint8Array(data.dto.content);
								$scope.showPDF=true;
								$scope.ocConsultShowModalPDF = true;
							}else{
								$scope.pdfUrl = null;
								$scope.showPDF=false;
							}
						} else {
							download.downloadFile(data.dto.content, $file.name,
									$file.type);						
						}
					}
		});
	};
	
	$scope.openModalPrefas = function(ocIdPrefa, index, ocNumero) {
		$scope.closeAllRowsOc(ocIdPrefa, index);
		$scope.ocIdPrefa = ocIdPrefa;
		if($scope.consultPrefaOcId!=ocIdPrefa){
			$scope.prefaNombre = ocNumero;
			$scope.filterConsultPF.estadoPreFac = '';
			$scope.loadFilterSearchPF();
		}else{
			$scope.filterConsultPF.estadoPreFac = FiltroReportOC.dataConsultPF.estadoPreFac;
			$scope.ocConsultShowModalPrefas = true;
		}
		
	};
	
	$scope.closeAllRowsOc = function(ordenId, index){
		if($scope.ocId != null && $scope.ocId != ordenId){
			$scope.cleanTableRowOCExpand();
			index = index + 10000;
		}
		$scope.tableRowOCNotExpanded = true;
	};
	
	$scope.loadFilterSearchPF = function(){
		$scope.pagePreloadedPF = 1;
		$scope.prefId = null;
		$scope.indexPF = undefined;
		$scope.cleanTableRowPreFacExpand();
		$scope.loadFilterPF();
		$scope.loadPrefas();
	};
	
	$scope.loadPrefas = function() {
		var callbackFuntion = function(data, status, headers, config) {
			$scope.messagePF = false;
			$scope.prefac = data.preFacView.content;
			$scope.filterConsultPF.totalPagesPF = data.preFacView.totalPages;
			$scope.fromCountPF = (data.preFacView.number * data.preFacView.size) + 1;
			$scope.toCountPF = (data.preFacView.number * data.preFacView.size) + data.preFacView.numberOfElements;
			$scope.totalCountPF = data.preFacView.totalElements;
			$scope.countPF = (data.preFacView.numberOfElements > 0 ? $scope.fromCountPF : 0) + ' - '
					+ $scope.toCountPF + ' de ' + $scope.totalCountPF;	
			if(data.preFacView.totalElements == 0){
				$scope.messagePF = true;
				$scope.warningPFMessage = 'No se encontraron Pre Facturaciones';
			}
			$scope.setRangePagesPF();
			$scope.ocConsultShowModalPrefas = true;
			$scope.consultPrefaOcId=$scope.ocIdPrefa;
		};
		var send = {
				ocId: $scope.ocIdPrefa,
				estadoPreFac: $scope.filterConsultPF.estadoPreFac,
				pagePF: $scope.filterConsultPF.pagePF,
				limitPF:$scope.filterConsultPF.limitPF 
		};
		
		webServices.getWSResponseGet($http, 'orden', 'ocPaginatedPrefaConsulList',
				send, $cookieStore, callbackFuntion);
	};
	
	$scope.closeFindModalPDF = function(){
		$scope.ocConsultShowModalPDF = false;
	};
	
	$scope.closeModalPrefas = function(){
		$scope.ocConsultShowModalPrefas = false;
		$scope.tableRowOCNotExpanded = false;
	};

	$scope.facturaPorOC.facturaCliente = [];
	$scope.openModalAssociateFacturas = function (ordenId, clienteId, ordenNumero, index) {		
		$scope.closeAllRowsOc(ordenId, index);
		$scope.ordenNumero = ordenNumero;
		$scope.showModalAsociar = false;
		var sendData = {
				'id' : ordenId,
				'clienteId' : clienteId
		};
		var callbackFuntion = function(data, status, headers, config) {
			$scope.facturaPorOC = data.facturaClientePorOCDTO;
			$scope.tableAssociate = tableFactory.getTableFilterData(
					$scope.facturaPorOC.facturaCliente, ngTableParams, $filter, $scope.associate,
					$scope, 4);		
			
			if($scope.facturaPorOC.facturaCliente.length > 0){
				$scope.showTableAssociate = true;
			} else {
				$scope.showTableAssociate = false;
				$scope.warningTableAssociateMessage = "No se encontraron "
					+ "facturas para asociar";
			}
			
			$scope.showModalAsociar = true;
			$scope.seleccionados();
    	};
    	webServices.getWSResponseGet($http, 'orden', 'getFacturasByOrdenId',
    			sendData, $cookieStore, callbackFuntion);
	};   

	$scope.seleccionados = function() {
		var def = $q.defer();
		def.resolve(utilidades.getFiltroSeleccionado());
		return def;
	};

	$scope.closeModalAssociateFacturas = function () {
		$scope.showModalAsociar = false;
		$scope.tableRowOCNotExpanded = false;
	};
	
	$scope.associateFacturas = function () {
		var callBackFuntion = function(data,
				status, headers, config) {
			if (data.status == 0) {
				alertify.success("Las facturas se asociarion correctamente");
				$scope.totalFactura = 0;
			} else {
				alertify.error("Error al asociar "
					+ "las facturas: " + data.message);
			}
			$scope.loadSelectedTableOCRow();
			$scope.closeModalAssociateFacturas();
		};
		webServices.getWSResponsePost($http, 'orden', 'saveAsociarFacturas',
				$scope.facturaPorOC, $cookieStore, callBackFuntion);
	};
		
	$scope.typesFile = OCModel.getTypeFileOC();
		
	$scope.getTipoArchivo = function(id) {
		return OCModel.getTypeFileOCMap()[id];
	};

	$scope.afterShow = function() {
		$('#managerNameId').focus();
		$('#projectNameId').focus();
	};
	
    $scope.showClientes = false;

	if (FiltroReportOC.dataConsult == null
			||  FiltroReportOC.dataConsult.length == 0) {
		FiltroReportOC.dataConsult = OCModel.getOCFilterConsult();		
	}
	
	if (FiltroReportOC.dataConsultPF == null
			||  FiltroReportOC.dataConsultPF.length == 0) {
		FiltroReportOC.dataConsultPF = OCModel.getPFFilterConsult();		
	}
	
	if(FiltroReportOC.dataConsult.pageOC == null){
		FiltroReportOC.dataConsult.pageOC = 1;
	}
	
	if(FiltroReportOC.dataConsultPF.pagePF == null){
		FiltroReportOC.dataConsultPF.pagePF = 1;
	}
	
	$scope.pagePreloadedOC = FiltroReportOC.dataConsult.pageOC;
	$scope.pagePreloadedPF = FiltroReportOC.dataConsultPF.pagePF;
	$scope.ocs=null;
	$scope.prefac=null;
	$scope.consultPrefaOcId = null;
	$scope.selectedConsultProject='';
	$scope.selectedConsultManager='';
	$scope.filterConsult=OCModel.getOCFilterConsult();
	$scope.filterConsultPF=OCModel.getPFFilterConsult();
	$scope.rollbackFilterFields();	
	$scope.modalFindProject = false;
	$scope.modalFindProposal = false;
	$scope.activos = OCModel.getActivo();
	$scope.filterConsultProject = OCModel.getOCFilterProject();
	$scope.filterConsultManager = OCModel.getOCFilterManager();
	$scope.loadDataFilter();
	$scope.showTableFindProject = false;
	$scope.showTableFindProposal = false;
	$scope.ocId = null;
	$scope.prefId = null;
	$scope.showPDF=false;
	$scope.pdfUrl=null;
	$scope.messageOC = false;
	$scope.messagePF = false;
	$scope.consultProyectoMessage = false;
	$scope.consultManagerMessage = false;
	$scope.ocConsultShowModalPDF = false;
	$scope.ocConsultShowModalPrefas = false;
	$scope.showMessageFacturaFile = false;
	$scope.showModalAsociar = false;
	$scope.showTableAssociate = false;

	$scope.paginatedOCConsultName='OC';
	$scope.filterNameOC='filterConsult';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedOCConsultName', $scope.rollbackFilterFields,
			'filterConsult.pageOC', $scope.loadConsultOC, 'filterConsult.totalPagesOC', 'filterConsult.rangeOC',
			function() {
				$scope.ocId = null;
				$scope.cleanTableRowOCExpand();
				FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
			});
		
	$scope.paginatedPFName='PF';
	$scope.filterNamePF='filterConsultPF';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedPFName', $scope.rollbackFilterFieldsPF,
			'filterConsultPF.pagePF', $scope.loadPrefas, 'filterConsultPF.totalPagesPF', 'filterConsultPF.rangePF',
			function() {
				FiltroReportOC.dataConsultPF.pagePF = $scope.filterConsultPF.pagePF;
			});
	
});