caApp.registerCtrl('oCAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		FiltroOC, paginatedFunction, OCModel) {

    $scope.loadDataFilter =  function(){
    	var callbackFuntion = function(data, status, headers, config) {
    		$scope.estados = data.datoInicialOrdenDTO.estados;
    		$scope.clientes=data.datoInicialOrdenDTO.clientes;	
    		FiltroOC.initial=data.datoInicialOrdenDTO;
    		$scope.showClientes=true;
    	};
    	webServices.getWSResponseGet($http, 'orden', 'getFiltroInicial',
			null, $cookieStore, callbackFuntion);
    };
	
	$scope.rollbackFilterFields = function() {
		$scope.filter.clienteId = FiltroOC.data.clienteId;
		$scope.filter.clienteNombre = FiltroOC.data.clienteNombre;
		$scope.filter.page = FiltroOC.page;
		$scope.filter.proyectoId = FiltroOC.data.proyectoId;
		$scope.filter.proyectoNombre = FiltroOC.data.proyectoNombre;
		$scope.filter.propuestaId = FiltroOC.data.propuestaId;
		$scope.filter.propuestaNombre = FiltroOC.data.propuestaNombre;
		$scope.filter.estado = FiltroOC.data.estado;
		$scope.filter.activa = FiltroOC.data.activa;
	};
	
	$scope.loadFilterSearch = function(){
		$scope.pagePreloaded = 1;
		$scope.loadFilter();
	};
    
	$scope.loadFilter = function() {
		$scope.filter.page = $scope.pagePreloaded;
		FiltroOC.page = $scope.filter.page;
		$scope.loadFilterFields();
		$scope.loadOC();
	};
	
	$scope.clearFilter = function() {
		var page = $scope.filter.page;
		var tPage = $scope.filter.totalPages;
		var range = $scope.filter.range;
		$scope.filter = OCModel.getOCFilterForm();
		$scope.filter.page = page;
		$scope.filter.totalPages = tPage;
		$scope.filter.range = range;
	};
	
	$scope.loadFilterFields = function() {
		FiltroOC.data.clienteId = $scope.filter.clienteId;
		FiltroOC.data.clienteNombre = $scope.filter.clienteNombre;
		FiltroOC.page = $scope.filter.page;
		FiltroOC.data.proyectoId = $scope.filter.proyectoId ;
		FiltroOC.data.proyectoNombre = $scope.filter.proyectoNombre ;
		FiltroOC.data.propuestaId = $scope.filter.propuestaId ;
		FiltroOC.data.propuestaNombre = $scope.filter.propuestaNombre ;
		FiltroOC.data.estado = $scope.filter.estado ;
		FiltroOC.data.activa  = $scope.filter.activa;
	};
	
	$scope.loadOC = function() {
		var callbackFuntion = function(data, status, headers, config) {
			$scope.messageFilterOC = false;
			$scope.ocs = data.content;
			$scope.filter.totalPages = data.totalPages;			
			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			if(data.totalElements == 0){
				$scope.messageFilterOC = true;
				$scope.warningFilterOCMessage = 'No se encontraron Ordenes de Compra';
			} 
			$scope.setRangePages();	
		};
		webServices.getWSResponseGet($http, 'orden', 'ocPaginatedList',
				$scope.filter, $cookieStore, callbackFuntion);
	};	
    
    $scope.go = function(path) {
		$location.path(path);
	};
	
	$scope.goVigente = function(id, numero) {
		var str = "Esta seguro de entrar en vigencia la Orden de Compra <b>"+numero+"</b>?";
		var success ="La Orden de Compra <b>"+numero+"</b> entro en Vigencia con exito.";
		var fail ="Hubo un error al intentar entrar en vigencia la Orden de Compra <b>"+numero+"</b>.";
		var errorStatus = "La Orden de compra externa <b>" + numero + "</b> no puede entrar en vigencia porque no posee archivo de tipo OC.";
		$scope.updateOc('changeStateOrden', id, str, success, fail, errorStatus);
	};
	
	
	$scope.goCerrar = function(id, numero) {
		var str = "Esta seguro de cerrar la Orden de Compra <b>"+numero+"</b>?";
		var success ="La Orden de Compra <b>"+numero+"</b> se cerro con exito.";
		var fail ="Hubo un error al intentar cerrar la Orden de Compra <b>"+numero+"</b>.";
		$scope.updateOc('closeOrden', id, str, success, fail );
	};
	
	$scope.goDelete = function(id, numero) {
		var str = "Esta seguro de desactivar la Orden de Compra <b>"+numero+"</b>?";
		var success ="La Orden de Compra <b>"+numero+"</b> se desactivo con exito";
		var fail ="Hubo un error al intentar desactivar la Orden de Compra <b>"+numero+"</b>.";
		$scope.updateOc('deleteOrden', id, str, success, fail );
	};
	
	$scope.updateOc = function (ws, id, question, success, fail, errorStatus){
		alertify.confirm(question ,function (e) {
		    if (e) {
		    	var callbackFuntion = function(data, status, headers, config) {
		    		if(data.status==0){
		    			alertify.success(success);
		    			$scope.loadFilter();
		    		} else if (data.status==4){
		    			alertify.error(errorStatus != null ? errorStatus : fail);
		    		} else {
		    			alertify.error(fail+": "+data.message);
		    		}
				};
				webServices.getWSResponsePost($http, 'orden', ws,
						id, $cookieStore, callbackFuntion);
		    }
		});
	};

	$scope.goView = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.goNew = function(path) {
		$location.path(path);
	};
	
	$scope.selected = function(id, idCompare){
		return id==idCompare;
	};

	$scope.goFindProject = function() {
	    $scope.selectedProject=$scope.filter.proyectoNombre;
	    $scope.filterProject.seleccionado=$scope.filter.proyectoId;
	    $scope.filterProject.clienteId=Number($scope.filter.clienteId);
	    $scope.filterProject.propuestaId=$scope.filter.propuestaId;
	    $scope.modalFindProject = true;
	    if($scope.tableFindProject != undefined && $scope.tableFindProject.data.length > 0){
	    	$scope.showTableFindProject = true;
	    }
	};
	
	$scope.closeFindProject = function() {
		$scope.filterProject.seleccionado=0;
		$scope.selectedProject='';
		$scope.modalFindProject = false;
		$scope.showTableFindProject = false;
	};
	
	$scope.acceptFindProject = function() {
    	$scope.filter.proyectoNombre=$scope.selectedProject;
   	    $scope.filter.proyectoId=$scope.filterProject.seleccionado;
		$scope.modalFindProject = false;
		$scope.showTableFindProject = false;
	};

	$scope.goFindProposal = function() {
		$scope.modalFindProposal = true;
		$scope.selectedProposal=$scope.filter.propuestaNombre;
		$scope.filterProposal.seleccionado=$scope.filter.propuestaId;
		$scope.filterProposal.clienteId=Number($scope.filter.clienteId);		
	    if($scope.tableFindProposal != undefined && $scope.tableFindProposal.data.length > 0){
	    	$scope.showTableFindProposal = true;
	    }
	    	
	};
		
	$scope.closeFindProposal = function() {
		$scope.filterProposal.seleccionado=0;
		$scope.selectedProposal='';
		$scope.modalFindProposal = false;
		$scope.showTableFindProposal  = false;
	};
	
	$scope.acceptFindProposal = function() {
    	$scope.filter.propuestaNombre=$scope.selectedProposal;
    	$scope.filter.propuestaId=$scope.filterProposal.seleccionado;
		$scope.modalFindProposal = false;
		$scope.showTableFindProposal  = false;
	};
	
	$scope.radioCheckProject = function (project) {		
	    if ($scope.filterProject.seleccionado != project.id){
	    	$scope.filterProject.seleccionado=project.id;
	    	$scope.selectedProject=project.nombre;
	    }
	       
	};
	
	$scope.radioCheckProposal = function (proposal) {
	    if ($scope.filterProposal.seleccionado != proposal.id){
	    	$scope.filterProposal.seleccionado=proposal.id;
	    	$scope.selectedProposal=proposal.descripcion;
	    }
	};
	
	$scope.clearSelectedProject = function (){
    	$scope.filter.proyectoNombre='';
   	    $scope.filter.proyectoId=0;
	};
	
	$scope.clearSelectedProposal = function (){
    	$scope.filter.propuestaNombre='';
    	$scope.filter.propuestaId=0;
	};
	
	$scope.afterShow = function() {
		 $('#descriptionId').focus();
		 $('#nameId').focus();
	 };
	 
	$scope.projectFilterSearch = function(){
		 $scope.showTableFindProject = false;
		 var callbackFuntion = function(data, status, headers, config) {
			 $scope.tableFindProject = tableFactory.getTableDataModal(data.proyectosDTO, ngTableParams,
					 $scope,2);
			 if(data.status == 0){
				 if(data.proyectosDTO.length > 0){
					 $scope.showTableFindProject = true;
					 $scope.findProjectMessage = false;					 
				 } else {
					 $scope.findProjectMessage = true;
					 $scope.warningFindProjectMessage = 'No se encontraron Proyectos';
				 }
			 }
		};
		webServices.getWSResponseGet($http, 'orden', 'getFiltroProyecto',
				$scope.filterProject, $cookieStore, callbackFuntion);
	 };
	 
	$scope.proposalFilterSearch = function(){
		 $scope.showTableFindProposal = false;
		 var callbackFuntion = function(data, status, headers, config) {
			 $scope.tableFindProposal = tableFactory.getTableDataModal(data.propuestasDTO, ngTableParams,
					 $scope,2);
			 if(data.status == 0){
				 if(data.propuestasDTO.length > 0){
					 $scope.showTableFindProposal = true;
					 $scope.findProposalMessage = false;					 
				 } else {
					 $scope.findProposalMessage = true;
					 $scope.warningFindProposalMessage = 'No se encontraron Propuestas';
				 }
				 
			 }
		};
		webServices.getWSResponseGet($http, 'orden', 'getFiltroPropuesta',
				$scope.filterProposal, $cookieStore, callbackFuntion);
	 };
	 
	$scope.isEnPreparacion = function (estado){
		return estado == 1;
	};
	
	$scope.isVigenteExtendida = function (estado){
			return estado == 2 || estado == 3;
	};
	
	$scope.breadcrumbs = breadcrumbs;
    $scope.showClientes = false;
    $scope.loadTable = false;
	if (FiltroOC.data == null
			||  FiltroOC.data.length == 0) {
		FiltroOC.data = OCModel.getOCFilterForm();		
	}else{
		$scope.loadTable = true;
	}
	
	if(FiltroOC.page == null){
		FiltroOC.page = 1;
	}
	
	$scope.pagePreloaded = FiltroOC.page;
	$scope.ocs=null;
	$scope.selectedProject='';
	$scope.selectedProposal='';
	$scope.filter=OCModel.getOCFilterForm();
	$scope.rollbackFilterFields();
	$scope.modalFindProject = false;
	$scope.modalFindProposal = false;
	$scope.activos = OCModel.getActivo();
	$scope.filterProject = OCModel.getOCFilterProject();
	$scope.filterProposal = OCModel.getOCFilterProposal();
	if(FiltroOC.initial == null
			||  FiltroOC.initial.length == 0){
		$scope.loadDataFilter();
	}else{
		$scope.estados = FiltroOC.initial.estados;
		$scope.clientes = FiltroOC.initial.clientes;
		$scope.showClientes = true;
	}
	$scope.showTableFindProject = false;
	$scope.showTableFindProposal = false;
	if ($scope.loadTable) {
		$scope.loadFilter();		
	}
	$scope.findProjectMessage = false;	
	$scope.findProposalMessage = false;
	$scope.messageFilterOC = false;	
	$scope.paginatedOCName='';
	$scope.filterName='filter';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedOCName', $scope.rollbackFilterFields,
			'filter.page', $scope.loadOC, 'filter.totalPages', 'filter.range',
			function() {
				FiltroOC.page = $scope.filter.page;
			});
});