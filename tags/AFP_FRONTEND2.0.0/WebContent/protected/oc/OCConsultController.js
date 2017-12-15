caApp.controller('oCAdminConsultController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		$filter, FiltroReportOC, download, utilidadesConversion, tableCollapseFactory, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	
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
		$scope.filterConsult.clienteId = FiltroReportOC.dataConsult.clienteId;
		$scope.filterConsult.clienteNombre = FiltroReportOC.dataConsult.clienteNombre;
		$scope.filterConsult.fechaDesde = FiltroReportOC.dataConsult.fechaDesde;
		$scope.filterConsult.fechaHasta = FiltroReportOC.dataConsult.fechaHasta;
		$scope.filterConsult.proyectoId = FiltroReportOC.dataConsult.proyectoId;
		$scope.filterConsult.proyectoNombre = FiltroReportOC.dataConsult.proyectoNombre;
		$scope.filterConsult.managerId = FiltroReportOC.dataConsult.managerId;
		$scope.filterConsult.managerNombre = FiltroReportOC.dataConsult.managerNombre;
		$scope.filterConsult.numero = FiltroReportOC.dataConsult.numero;
		$scope.filterConsult.estadoOC = FiltroReportOC.dataConsult.estadoOC;
		$scope.filterConsult.estadoPreFac = FiltroReportOC.dataConsult.estadoPreFac;
		$scope.filterConsult.totalPagesOC = FiltroReportOC.dataConsult.totalPagesOC;
		$scope.filterConsult.totalPagesPF = FiltroReportOC.dataConsult.totalPagesPF;
		$scope.filterConsult.pageOC = FiltroReportOC.dataConsult.pageOC;
		$scope.filterConsult.pagePF = FiltroReportOC.dataConsult.pagePF;

	};
	
	$scope.loadFilterSearch = function(){
		$scope.pagePreloadedOC = 1;
		$scope.pagePreloadedPF = 1;
		$scope.loadFilter();
	};
	
	$scope.loadFilter = function() {
		$scope.filterConsult.pageOC = $scope.pagePreloadedOC;
		FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
		$scope.filterConsult.pagePF = $scope.pagePreloadedPF;
		FiltroReportOC.dataConsult.pagePF = $scope.filterConsult.pagePF;
		$scope.loadFilterFields();
		$scope.loadConsultOC();
	};
	
	$scope.clearFilter = function() {
		var pageOC = $scope.filterConsult.pageOC;
		var tpageOC = $scope.filterConsult.totalPagesOC;
		var rangeOC = $scope.filterConsult.rangeOC;
		var pagePF = $scope.filterConsult.pagePF;
		var tpagePF = $scope.filterConsult.totalPagesPF;
		var rangePF = $scope.filterConsult.rangePF;
		$scope.filterConsult = getOCFilterConsult();
		$scope.filterConsult.pageOC = pageOC;
		$scope.filterConsult.totalPagesOC = tpageOC;
		$scope.filterConsult.rangeOC = rangeOC;
		$scope.filterConsult.rangePF = rangePF;
		$scope.filterConsult.totalPagesPF = tpagePF;
		$scope.filterConsult.pagePF = pagePF;
	};
	
	$scope.loadFilterFields = function() {
		FiltroReportOC.dataConsult.clienteId = $scope.filterConsult.clienteId;
		FiltroReportOC.dataConsult.clienteNombre = $scope.filterConsult.clienteNombre;
		FiltroReportOC.dataConsult.fechaDesde = $scope.filterConsult.fechaDesde;
		FiltroReportOC.dataConsult.fechaHasta = $scope.filterConsult.fechaHasta;
		FiltroReportOC.dataConsult.proyectoId =	$scope.filterConsult.proyectoId;
		FiltroReportOC.dataConsult.proyectoNombre = $scope.filterConsult.proyectoNombre;
		FiltroReportOC.dataConsult.managerId = $scope.filterConsult.managerId;
		FiltroReportOC.dataConsult.managerNombre = $scope.filterConsult.managerNombre;
		FiltroReportOC.dataConsult.numero = $scope.filterConsult.numero;
		FiltroReportOC.dataConsult.estadoOC = $scope.filterConsult.estadoOC;
		FiltroReportOC.dataConsult.estadoPreFac = $scope.filterConsult.estadoPreFac;
		FiltroReportOC.dataConsult.totalPagesOC = $scope.filterConsult.totalPagesOC;
		FiltroReportOC.dataConsult.totalPagesPF = $scope.filterConsult.totalPagesPF;
		FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
		FiltroReportOC.dataConsult.pagePF = $scope.filterConsult.pagePF;
	};
	
	$scope.loadConsultOC = function() {
		var callbackFuntion = function(data, status, headers, config) {
			$scope.messageOC = false;
			$scope.messagePF = false;
			$scope.ocs = data.ordenView.content;
			$scope.prefac = data.preFacView.content;
			$scope.filterConsult.totalPagesOC = data.ordenView.totalPages;
			$scope.filterConsult.totalPagesPF = data.preFacView.totalPages;

			$scope.fromCountOC = (data.ordenView.number * data.ordenView.size) + 1;
			$scope.toCountOC = (data.ordenView.number * data.ordenView.size) + data.ordenView.numberOfElements;
			$scope.totalCountOC = data.ordenView.totalElements;
			$scope.countOC = (data.ordenView.numberOfElements > 0 ? $scope.fromCountOC : 0) + ' - '
					+ $scope.toCountOC + ' de ' + $scope.totalCountOC;
			$scope.fromCountPF = (data.preFacView.number * data.preFacView.size) + 1;
			$scope.toCountPF = (data.preFacView.number * data.preFacView.size) + data.preFacView.numberOfElements;
			$scope.totalCountPF = data.preFacView.totalElements;
			$scope.countPF = (data.preFacView.numberOfElements > 0 ? $scope.fromCountPF : 0) + ' - '
					+ $scope.toCountPF + ' de ' + $scope.totalCountPF;	
			if(data.ordenView.totalElements == 0){
				$scope.messageOC = true;
				$scope.warningOCMessage = 'No se encontraron Ordenes de Compra';
			}
			if(data.preFacView.totalElements == 0){
				$scope.messagePF = true;
				$scope.warningPFMessage = 'No se encontraron Pre Facturaciones';
			}
			
			$scope.setRangePagesOC();
			$scope.setRangePagesPF();
		};
		webServices.getWSResponseGet($http, 'orden', 'ocPaginatedConsulList',
				$scope.filterConsult, $cookieStore, callbackFuntion);
	};
	
	$scope.prevPageOC = function() {
		$scope.rollbackFilterFields();
		if ($scope.filterConsult.pageOC > 1) {
			$scope.ocId = null;
			$scope.cleanTableRowOCExpand();
			$scope.filterConsult.pageOC--;
			FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
			$scope.loadConsultOC();
		}
	};

	$scope.goPageOC = function($number) {
		$scope.ocId = null;
		$scope.cleanTableRowOCExpand();
		$scope.filterConsult.pageOC = $number;
		FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
		$scope.rollbackFilterFields();
		$scope.loadConsultOC();
	};

	$scope.nextPageOC = function() {
		$scope.rollbackFilterFields();
		if ($scope.filterConsult.pageOC < $scope.filterConsult.totalPagesOC) {
			$scope.ocId = null;
			$scope.cleanTableRowOCExpand();
			$scope.filterConsult.pageOC++;
			FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
			$scope.loadConsultOC();
		}
	};

	$scope.firstPageOC = function() {
		$scope.ocId = null;
		$scope.cleanTableRowOCExpand();
		$scope.rollbackFilterFields();
		$scope.filterConsult.pageOC = 1;
		FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
		$scope.loadConsultOC();
	};

	$scope.lastPageOC = function() {
		$scope.ocId = null;
		$scope.cleanTableRowOCExpand();
		$scope.rollbackFilterFields();
		$scope.filterConsult.pageOC = $scope.filterConsult.totalPagesOC;
		FiltroReportOC.dataConsult.pageOC = $scope.filterConsult.pageOC;
		$scope.loadConsultOC();
	};
    
	$scope.setRangePagesOC = function() {
		$scope.filterConsult.rangeOC = [];
		for (var i = 1; i <= $scope.filterConsult.totalPagesOC; i++) {
			$scope.filterConsult.rangeOC.push(i);
		};
	};	
	
	$scope.prevPagePF = function() {
		$scope.rollbackFilterFields();
		if ($scope.filterConsult.pagePF > 1) {
			$scope.filterConsult.pagePF--;
			FiltroReportOC.dataConsult.pagePF = $scope.filterConsult.pagePF;
			$scope.loadConsultOC();
		}
	};

	$scope.goPagePF = function($number) {
		$scope.filterConsult.pagePF = $number;
		FiltroReportOC.dataConsult.pagePF = $scope.filterConsult.pagePF;
		$scope.rollbackFilterFields();
		$scope.loadConsultOC();
	};

	$scope.nextPagePF = function() {
		$scope.rollbackFilterFields();
		if ($scope.filterConsult.pagePF < $scope.filterConsult.totalPagesPF) {
			$scope.filterConsult.pagePF++;
			FiltroReportOC.dataConsult.pagePF = $scope.filterConsult.pagePF;
			$scope.loadConsultOC();
		}
	};

	$scope.firstPagePF = function() {
		$scope.rollbackFilterFields();
		$scope.filterConsult.pagePF = 1;
		FiltroReportOC.dataConsult.pagePF = $scope.filterConsult.pagePF;
		$scope.loadConsultOC();
	};

	$scope.lastPagePF = function() {
		$scope.rollbackFilterFields();
		$scope.filterConsult.pagePF = $scope.filterConsult.totalPagesPF;
		FiltroReportOC.dataConsult.pagePF = $scope.filterConsult.pagePF;
		$scope.loadConsultOC();
	};
    
	$scope.setRangePagesPF = function() {
		$scope.filterConsult.rangePF = [];
		for (var i = 1; i <= $scope.filterConsult.totalPagesPF; i++) {
			$scope.filterConsult.rangePF.push(i);
		};
	};	

	$scope.loadFacturas = function($idOC) {
		var sendId = {
			'id' : $idOC
		};
		var callbackFuntion = function(data, status, headers,
				config) {
			if (data.status == 0) {
				$scope.facturas = data.facturaList;
				$scope.ocId = $idOC;
				$scope.totalFactura = 0;
			}
		};
		webServices.getWSResponseGet($http, 'orden',
					'getFacturaClienteByOrdenId', sendId,
					$cookieStore, callbackFuntion);
	};
	
	$scope.loadArchivos = function($idOC) {
		var sendId = {
			'id' : $idOC
		};	
		webServices.getWSResponseGet($http, 'orden', 'getArchivoByOrdenId',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						$scope.archivos = data.archivosList;
						if ($scope.archivos.length > 0) {
							$scope.showFiles = true;
						}
					}
		});
		webServices.getWSResponseGet($http, 'archivoOrden', 'verArchivo',
				sendId, $cookieStore, function(data, status, headers, config) {
			if (data.status == 0) {
				if (data.dto != null) {
					$scope.pdfName = 'Orden de Compra';
					$scope.scroll = 0;
					$scope.pdfUrl = utilidadesConversion.base64ToUint8Array(data.dto.content);
					$scope.showPDF=true;
				} else {
					$scope.pdfUrl = null;
					$scope.showPDF=false;
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
		$scope.filterConsult.managerNombre = $scope.selectedConsultManager;
		$scope.filterConsult.managerId = $scope.filterConsultManager.seleccionado;
		$scope.modalFindManager = false;
		$scope.showTableModalFindManager = false;
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
	
	$scope.findClientName = function(id) {
		return utilidades.getValueFromList($scope.clientes, id, false);
	};

	// Inicio Acordeon PreFac
	$scope.cleanTableRowPreFacExpand = function() {
		$scope.tableRowPreFacExpanded = true;
		$scope.tableRowPreFacNotExpanded = false;
		$scope.tableRowPreFacIndexExpandedCurr = "";
		$scope.tableRowPreFacIndexExpandedPrev = "";
		$scope.dataCollapsePreFac = [];
	};

	$scope.cleanTableRowPreFacExpand();
	
	$scope.dataCollapsePreFacFn = function() {
		$scope.dataCollapsePreFac = [];
	};

	 $scope.selectTableRowPreFac = function(index, prefId) {
		 if (!$scope.tableRowPreFacNotExpanded) {
			    tableCollapseFactory.onSelect(index, $scope.dataCollapsePreFacFn(), $scope ,'tableRowPreFacExpanded', 'tableRowPreFacIndexExpandedCurr',
			    		'tableRowPreFacIndexExpandedPrev','dataCollapsePreFac');
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
			 $scope[field] = $scope[field]
				+ Number(importe);
		 }
	 };

	// Inicio Acordeon OC
	$scope.cleanTableRowOCExpand = function() {
		$scope.tableRowOCExpanded = true;
		$scope.tableRowOCNotExpanded = false;
		$scope.tableRowOCIndexExpandedCurr = "";
		$scope.tableRowOCIndexExpandedPrev = "";
		$scope.dataCollapseOC = [];
	};

	$scope.cleanTableRowOCExpand();
	
	$scope.dataCollapseOCFn = function() {
		$scope.dataCollapseOC = [];
	};

	 $scope.selectTableRowOC = function(index, $idOC) {	
		 // index + 10000 Para no tener problema con el acordeon de prefac
		 index = index + 10000;
		 if (!$scope.tableRowOCNotExpanded) {
			 $scope.showPDF = false;
			 $scope.showFiles = false;
				//Validacion para que no haga el webservice cuando es la misma factura.
			if($scope.ocId==null || $scope.ocId != $idOC){
				$scope.loadFacturas($idOC);
				$scope.loadArchivos($idOC);				
			} else {
				if($scope.pdfUrl != null){
					$scope.showPDF = true;
				}
				if($scope.archivos.length > 0){
					$scope.showFiles = true;
				}
			} 	
			tableCollapseFactory.onSelect(index, $scope.dataCollapseOCFn(), $scope ,'tableRowOCExpanded', 'tableRowOCIndexExpandedCurr',
		    		'tableRowOCIndexExpandedPrev','dataCollapseOC');	
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
		
	$scope.typesFile = getTypeFileOC();
		
	$scope.getTipoArchivo = function(id) {
		return utilidades.getValueFromList($scope.typesFile, id, false);
	};

	$scope.afterShow = function() {
		$('#managerNameId').focus();
		$('#projectNameId').focus();
	};
	
    $scope.showClientes = false;

	if (FiltroReportOC.dataConsult == null
			||  FiltroReportOC.dataConsult.length == 0) {
		FiltroReportOC.dataConsult = getOCFilterConsult();		
	}
	
	if(FiltroReportOC.dataConsult.pageOC == null){
		FiltroReportOC.dataConsult.pageOC = 1;
	}
	
	if(FiltroReportOC.dataConsult.pagePF == null){
		FiltroReportOC.dataConsult.pagePF = 1;
	}
	
	$scope.pagePreloadedOC = FiltroReportOC.dataConsult.pageOC;
	$scope.pagePreloadedPF = FiltroReportOC.dataConsult.pagePF;
	$scope.ocs=null;
	$scope.prefac=null;
	$scope.selectedConsultProject='';
	$scope.selectedConsultManager='';
	$scope.filterConsult=getOCFilterConsult();
	$scope.rollbackFilterFields();	
	$scope.modalFindProject = false;
	$scope.modalFindProposal = false;
	$scope.activos = getActivo();
	$scope.filterConsultProject = getOCFilterProject();
	$scope.filterConsultManager = getOCFilterManager();
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
});