caApp.registerCtrl('facturaConsultAdminController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		FiltroFactura, paginatedFunction, FModel, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.isShowFilter = false;
	$scope.go = function(path) {
		$location.path(utilidades.setPath($cookieStore,path,$location.path()));
	};

	$scope.showFilter = function(state) {
		$scope.isShowFilter = state;
	};

	$scope.goURL = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
	};

	$scope.goEdit = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
	};

	$scope.loadFilterSearch = function() {
		$scope.pagePreloaded = 1;
		$scope.loadFilter();
	};

	$scope.loadFilter = function() {
		$scope.filter.page = $scope.pagePreloaded;
		FiltroFactura.page = $scope.filter.page;
		$scope.loadFilterFields();
		$scope.loadFacturas();
	};

	$scope.clearFilter = function() {
		$scope.extFilter = FModel.getFacturaAdmFilter();
		FiltroFactura.dataConsult = $scope.extFilter;
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
		FiltroFactura.dataConsult.ffacDesde = $scope.filter.ffacDesde;
		FiltroFactura.dataConsult.ffacHasta = $scope.filter.ffacHasta;
		FiltroFactura.dataConsult.tipo = $scope.filter.tipo;
		FiltroFactura.dataConsult.estado = $scope.filter.estado;
		FiltroFactura.page = $scope.filter.page;
	};

	$scope.loadFacturas = function() {
		if (FiltroFactura.dataConsult != null) {
			$scope.loadFilterFields();
		}

		var callbackFuntion = function(data, status, headers, config) {
			$scope.facturas = data.content;
			$scope.total = data.totalAmount;
			$scope.filter.totalPages = data.totalPages;

			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			$scope.setRangePages();
		};
		webServices.getWSResponseGet($http, 'factura',
				'facturaConsultPaginatedList', $scope.filter, $cookieStore,
				callbackFuntion);
	};

	$scope.filter = FModel.getFacturaAdmFilterForm();
	if (FiltroFactura.dataConsult == null
			|| FiltroFactura.dataConsult.length == 0) {
		FiltroFactura.dataConsult = FModel.getFacturaAdmFilter();
	}
	if (FiltroFactura.page == null) {
		FiltroFactura.page = 1;
	}

	$scope.pagePreloaded = FiltroFactura.page;

	$scope.extFilter = FiltroFactura.dataConsult;
	$scope.tipos = FModel.getTipos();
	$scope.wfOptions = FModel.getWorkflowOptions();
	$scope.estadosFactura = FModel.getEstadosFactura();

	$scope.loadFilter();

	$scope.paginatedFacConsultName='';
	$scope.filterName='filter';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedFacConsultName',
			$scope.rollbackFilterFields, 'filter.page', $scope.loadFacturas,
			'filter.totalPages', 'filter.range', function() {
				FiltroFactura.page = $scope.filter.page;
			});
});