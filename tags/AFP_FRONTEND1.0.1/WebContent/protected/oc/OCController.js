caApp.controller('oCAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.goView = function(path) {
		$location.path(path);
	};

	$scope.goNew = function(path) {
		$location.path(path);
	};
	
	$scope.clienteFilter = 'auto completar';
	
	$scope.proyectoFilter = 'filtro';

	$scope.propuestaFilter = 'filtro';
	
	var ocs = getOcs();

	$scope.tableOCs = tableFactory.getTableData(ocs, ngTableParams, $scope);

});

caApp.controller('oCFormController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.inactive = false;
	$scope.modalDetail = false;

	$scope.states = getStateOC();

	var detalle = getOCDetalle();

	$scope.tableDetalle = tableFactory.getTableData(detalle, ngTableParams,
			$scope);

	var proyect = getOCProyect();

	$scope.tableProyect = tableFactory.getTableData(proyect, ngTableParams,
			$scope);

	$scope.goNewDetail = function() {
		$scope.units = getUnits();
		$scope.modalDetail = true;
	};

	$scope.closeDetail = function() {
		$scope.modalDetail = false;
	};
	
	$scope.goNewProyect = function() {
		var selectProyect = getSelectProyect();
		$scope.tableSelectProyect = tableFactory.getTableData(selectProyect,
				ngTableParams, $scope);
		$scope.modalProyect = true;
	};

	$scope.closeProyect = function() {
		$scope.modalProyect = false;
	};

	$scope.backAdm = function() {
		window.history.back();
	};
	
	$scope.total = 81;
	
	$('#fechaDesdeId').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fechaDesdeId'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fvDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#fechaVencimientoId').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fechaVencimientoId'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fiDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

});

caApp.controller('oCViewController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.inactive = true;

	$scope.states = getStateOC();

	var detalle = getOCDetalle();

	$scope.tableDetalle = tableFactory.getTableData(detalle, ngTableParams,
			$scope);

	var proyect = getOCProyect();

	$scope.tableProyect = tableFactory.getTableData(proyect, ngTableParams,
			$scope);

	$scope.backAdm = function() {
		window.history.back();
	};
	
	$scope.total = 81;

});

caApp.controller('oCAdminConsultController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.meses = getMeses();

	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.modalState = false;
	$scope.modalStatePreFac = false;
	$scope.modalAssign = false;
	$scope.modalPDF = false;

	var prefac = getPrefas();

	$scope.tablePrefac = tableFactory.getTableData(prefac, ngTableParams,
			$scope);

	$scope.ocs = getConsultOCs();

	$scope.preFacStates = getPreFacStates();
	$scope.states = getStateOC();
	
	$scope.goPreFacState = function() {
		$scope.modalStatePreFac = true;
	};

	$scope.closePreFac = function() {
		$scope.modalStatePreFac = false;
	};

	$scope.goModal = function() {
		$scope.modalState = true;
		$scope.tableRowNotExpanded = true;
	};

	$scope.close = function() {
		$scope.tableRowNotExpanded = false;
		$scope.modalState = false;
	};
	
	$scope.goAssign = function() {
		var select = getSelectOCs();
		$scope.tableSelectOC = tableFactory.getTableData(select, ngTableParams,
				$scope);
		$scope.modalAssign = true;
	};

	$scope.closeAssign = function() {
		$scope.modalAssign = false;
	};
	
	$scope.goAdjuntar = function() {
		$scope.modalPDF = true;
		$scope.tableRowNotExpanded = true;
	};

	$scope.closePDF = function() {
		$scope.tableRowNotExpanded = false;
		$scope.modalPDF = false;
	};
	
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
    
    $scope.loadFacturas = function () {
    	$scope.facturas = getFacturas();
    };
	
	$scope.selectTableRow = function (index) {
    	if(!$scope.tableRowNotExpanded){
		    	
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
        			if ($scope.tableRowIndexExpandedCurr !== "" && $scope.tableRowIndexExpandedCurr != index){
        				document.getElementById($scope.tableRowIndexExpandedCurr).style.backgroundColor = "#CACACA"; 
        			}           	
        			document.getElementById(index).style.backgroundColor="#FF8C00";
        			$scope.tableRowIndexExpandedPrev = $scope.tableRowIndexExpandedCurr;
        			$scope.tableRowIndexExpandedCurr = index;
        			$scope.dataCollapse[$scope.tableRowIndexExpandedPrev] = false;
        			$scope.dataCollapse[$scope.tableRowIndexExpandedCurr] = true;
        		}
        	}
        	$scope.loadFacturas();
        }
    };
    
    $('#fechaDesdeId').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fechaDesdeId'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fvDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#fechaHastaId').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fechaHastaId'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fiDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

});
