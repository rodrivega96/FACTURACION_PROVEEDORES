caApp.controller('oCAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		$filter) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.goView = function(path) {
		$location.path(path);
	};

	$scope.goEdit = function(path) {
		$location.path(path);
	};

	$scope.goNew = function(path) {
		$location.path(path);
	};

	$scope.modalFindProyect = false;
	$scope.modalFindProposal = false;
	$scope.clienteFilter = 'auto completar';
	$scope.siNo = getSiNoOC();
	$scope.activaFilter = '1';
	
	$scope.findProyect = function() {
		var findProyect = getFindProyect();
		$scope.activos = getSiNoOC();
		$scope.tableFindProyect = tableFactory.getTableData(findProyect, ngTableParams, $scope);
	    $scope.modalFindProyect = true;
	};
	
	$scope.closeFindProyect = function() {
		$scope.modalFindProyect = false;
	};

	$scope.findProposal = function() {
		var findProposal = getFindProposal();
		$scope.tableFindProposal = tableFactory.getTableData(findProposal, ngTableParams, $scope);
		$scope.modalFindProposal = true;
	};
	
	$scope.closeFindProposal = function() {
		$scope.modalFindProposal = false;
	};

	var ocs = getOcs();

	$scope.tableOCs = tableFactory.getTableData(ocs, ngTableParams, $scope);
	
	$('#fechaVigenciaDesdeId').datepicker({
		format : getStringFormatDate()})
			.on('changeDate', function(ev) {
				var element = angular
					.element($('#fechaVigenciaDesdeId'));
				var controller = element.controller();
				var scope = element.scope();
				scope.$apply(function() {
					scope.extFilter.fvDesde = getFormattedDate(ev.date);
				});
				$(this).datepicker('hide');
	});
	
	$('#fechaVigenciaHastaId').datepicker({
		format : getStringFormatDate()})
			.on('changeDate', function(ev) {
				var element = angular
					.element($('#fechaVigenciaHastaId'));
				var controller = element.controller();
				var scope = element.scope();
				scope.$apply(function() {
						scope.extFilter.fiDesde = getFormattedDate(ev.date);
				});
				$(this).datepicker('hide');
	});
	
});

caApp.controller('oCFormController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		$filter) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.oc = newOC();

	$scope.inactive = false;
	$scope.edit = true;
	$scope.modalDetail = false;
	$scope.modalProyect = false;
	$scope.modalFile = false;

	$scope.states = getStateOC();
	$scope.coins = getCoinsOC();
	$scope.typesOC = getTypesOC();
	$scope.siNo = getSiNoOC();

	var detalle = getDetalleOC();

	$scope.tableDetalle = tableFactory.getTableData(detalle, ngTableParams,
			$scope);

	var proyect = getProyectOC();

	$scope.tableProyect = tableFactory.getTableData(proyect, ngTableParams,
			$scope);

	var file = getFileOC();
	
	$scope.tableFile = tableFactory.getTableData(file, ngTableParams,
			$scope);

	$scope.goNewDetail = function() {
		$scope.units = getUnits();
		$scope.calculateTotal($scope.valuePrice, $scope.valueUnit);
		$scope.modalDetail = true;
	};

	$scope.calculateTotal = function(price, cant) {
		if (price > 0 && cant > 0 && cant % 1 == 0) {
			$scope.priceTotal = price * cant;
		} else {
			$scope.priceTotal = 0;
		}
	};

	$scope.closeDetail = function() {
		$scope.modalDetail = false;
	};

	$scope.goNewProyect = function() {
		var selectProyect = getSelectProyect();
		$scope.tableSelectProyect = tableFactory.getTableData(selectProyect, ngTableParams,
				$scope);
		$scope.modalProyect = true;
	};

	$scope.closeProyect = function() {
		$scope.modalProyect = false;
	};

	$scope.goNewFile = function() {
		$scope.typesFile = getTypeFileOC();
		$scope.modalFile = true;
	};

	$scope.closeFile = function() {
		$scope.modalFile = false;
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

caApp.controller('oCEditController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.inactive = false;
	$scope.edit = false;
	$scope.modalDetail = false;
	$scope.modalProyect = false;
	$scope.modalFile = false;

	$scope.states = getStateOC();
	$scope.coins = getCoinsOC();
	$scope.typesOC = getTypesOC();
	$scope.siNo = getSiNoOC();

	var detalle = getDetalleOC();

	$scope.tableDetalle = tableFactory.getTableData(detalle, ngTableParams,
			$scope);

	var proyect = getProyectOC();

	$scope.tableProyect = tableFactory.getTableData(proyect, ngTableParams,
			$scope);

	var file = getFileOC();

	$scope.tableFile = tableFactory.getTableData(file, ngTableParams,
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

	$scope.goNewFile = function() {
		$scope.modalFile = true;
	};

	$scope.closeFile = function() {
		$scope.modalFile = false;
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
	$scope.edit = false;

	$scope.states = getStateOC();
	$scope.coins = getCoinsOC();
	$scope.typesOC = getTypesOC();
	$scope.siNo = getSiNoOC();

	var detalle = getDetalleOC();

	$scope.tableDetalle = tableFactory.getTableData(detalle, ngTableParams,
			$scope);

	var proyect = getProyectOC();

	$scope.tableProyect = tableFactory.getTableData(proyect, ngTableParams,
			$scope);

	var file = getFileOC();

	$scope.tableFile = tableFactory.getTableData(file, ngTableParams,
			$scope);

	$scope.backAdm = function() {
		window.history.back();
	};

	$scope.total = 81;

});

caApp.controller('oCAdminConsultController', function($scope, $location, $http, 
		ngTableParams, breadcrumbs,$cookieStore, webServices, tableFactory, $filter) {

		$scope.breadcrumbs = breadcrumbs;

		$scope.meses = getMeses();
		
		$scope.loadFacturas = function() {
			$scope.facturas = getFacturasOC();
		};
		
		$scope.loadPeoples= function() {
			$scope.peoples = getPeoplesPreFac();
			$scope.preFacOCs = getOCPrefac();
		};
		
		$scope.go = function(path) {
			$location.path(path);
		};

		$scope.modalFindProyect = false;
		$scope.modalFindManager = false;
		
		$scope.prefas = getPrefas();
		
		
		$scope.findProyect = function() {
			var findProyect = getFindProyect();
			$scope.siNo = getSiNoOC();
		    $scope.tableFindProyect = tableFactory.getTableData(findProyect, ngTableParams,
					$scope);
		    $scope.modalFindProyect = true;
		};
		
		$scope.closeFindProyect = function() {
			$scope.modalFindProyect = false;
		};
		
		$scope.findManager = function() {
			var findManager = getFindManager();
			$scope.tableFindManager = tableFactory.getTableData(findManager, ngTableParams,
					$scope);
			$scope.modalFindManager = true;
		};

		$scope.closeFindManager = function() {
			$scope.modalFindManager = false;
		};

		$scope.ocs = getConsultOCs();
		$scope.preFacStates = getPreFacStates();
		$scope.states = getStateOC();
		
		//Inicio Acordeon PreFac
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

		$scope.selectTableRowPreFac = function(index) {
			if (!$scope.tableRowPreFacNotExpanded) {

				if (typeof $scope.dataCollapsePreFac === 'undefined') {
					$scope.dataCollapsePreFacFn();
				}
				if ($scope.tableRowPreFacExpanded === false
						&& $scope.tableRowPreFacIndexExpandedCurr === "") {
					$scope.tableRowPreFacIndexExpandedPrev = "";
					$scope.tableRowPreFacExpanded = true;
					$scope.tableRowPreFacIndexExpandedCurr = index;
					document.getElementById(index).style.backgroundColor = "#FF8C00";
					$scope.dataCollapsePreFac[index] = true;
				} else if ($scope.tableRowPreFacExpanded === true) {
					if ($scope.tableRowPreFacIndexExpandedCurr === index) {
						document.getElementById(index).style.backgroundColor = "#CACACA";
						$scope.tableRowPreFacExpanded = false;
						$scope.tableRowPreFacIndexExpandedCurr = "";
						$scope.dataCollapsePreFac[index] = false;
					} else {
						if ($scope.tableRowPreFacIndexExpandedCurr !== ""
								&& $scope.tableRowPreFacIndexExpandedCurr != index) {
							document
									.getElementById($scope.tableRowPreFacIndexExpandedCurr).style.backgroundColor = "#CACACA";
						}
						document.getElementById(index).style.backgroundColor = "#FF8C00";
						$scope.tableRowPreFacIndexExpandedPrev = $scope.tableRowPreFacIndexExpandedCurr;
						$scope.tableRowPreFacIndexExpandedCurr = index;
						$scope.dataCollapsePreFac[$scope.tableRowPreFacIndexExpandedPrev] = false;
						$scope.dataCollapsePreFac[$scope.tableRowPreFacIndexExpandedCurr] = true;
					}
				}
				$scope.loadPeoples();
			}
		};

		//Inicio Acordeon OC
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

		$scope.selectTableRowOC = function(index) {
			//index + 10000 Para no tener problema con el acordeon de prefac
			index = index + 10000;
			if (!$scope.tableRowOCNotExpanded) {

				if (typeof $scope.dataCollapseOC === 'undefined') {
					$scope.dataCollapseOCFn();
				}
				if ($scope.tableRowOCExpanded === false
						&& $scope.tableRowOCIndexExpandedCurr === "") {
					$scope.tableRowOCIndexExpandedPrev = "";
					$scope.tableRowOCExpanded = true;
					$scope.tableRowOCIndexExpandedCurr = index;
					document.getElementById(index).style.backgroundColor = "#FF8C00";
					$scope.dataCollapseOC[index] = true;
				} else if ($scope.tableRowOCExpanded === true) {
					if ($scope.tableRowOCIndexExpandedCurr === index) {
						document.getElementById(index).style.backgroundColor = "#CACACA";
						$scope.tableRowOCExpanded = false;
						$scope.tableRowOCIndexExpandedCurr = "";
						$scope.dataCollapseOC[index] = false;
					} else {
						if ($scope.tableRowOCIndexExpandedCurr !== ""
								&& $scope.tableRowOCIndexExpandedCurr != index) {
							document
									.getElementById($scope.tableRowOCIndexExpandedCurr).style.backgroundColor = "#CACACA";
						}
						document.getElementById(index).style.backgroundColor = "#FF8C00";
						$scope.tableRowOCIndexExpandedPrev = $scope.tableRowOCIndexExpandedCurr;
						$scope.tableRowOCIndexExpandedCurr = index;
						$scope.dataCollapseOC[$scope.tableRowOCIndexExpandedPrev] = false;
						$scope.dataCollapseOC[$scope.tableRowOCIndexExpandedCurr] = true;
					}
				}
				$scope.loadFacturas();
			}
		};
		
		var file = getFileOC();
		
		$scope.tableFile = tableFactory.getTableData(file, ngTableParams, 
				$scope);

		$('#fechaDesdeId').datepicker({
			format : getStringFormatDate()})
				.on('changeDate', function(ev) {
					var element = angular
						.element($('#fechaDesdeId'));
					var controller = element.controller();
					var scope = element.scope();
					scope.$apply(function() {
						scope.extFilter.fvDesde = getFormattedDate(ev.date);
					});
					$(this).datepicker('hide');
		});

		$('#fechaHastaId').datepicker({
			format : getStringFormatDate()})
				.on('changeDate', function(ev) {
					var element = angular
						.element($('#fechaHastaId'));
					var controller = element.controller();
					var scope = element.scope();
					scope.$apply(function() {
							scope.extFilter.fiDesde = getFormattedDate(ev.date);
					});
					$(this).datepicker('hide');
		});
});