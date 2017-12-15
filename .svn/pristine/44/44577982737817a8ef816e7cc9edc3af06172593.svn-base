caApp.registerCtrl('facturaAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, FiltroFactura, paginatedFunction, FModel, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.isShowFilter = false;
	$scope.go = function(path) {
		$location.path(utilidades.setPath($cookieStore,path,$location.path()));
	};

	$scope.showFilter = function(state) {
		$scope.isShowFilter = state;
	};

	$scope.goEdit = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
	};

	$scope.goUploadFile = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
	};

	$scope.goWF = function(path, $param) {
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
		FiltroFactura.data = $scope.extFilter;
	};

	$scope.loadFilterFields = function() {
		$scope.filter.numero = FiltroFactura.data.numero;
		$scope.filter.tipo = FiltroFactura.data.tipo;
		$scope.filter.cuit = FiltroFactura.data.cuit;
		$scope.filter.razon = FiltroFactura.data.razon;
		$scope.filter.cc = FiltroFactura.data.cc;
		$scope.filter.centroc = FiltroFactura.data.centroc;
		$scope.filter.wf = FiltroFactura.data.wf;
		$scope.filter.fvDesde = FiltroFactura.data.fvDesde;
		$scope.filter.fiDesde = FiltroFactura.data.fiDesde;
		$scope.filter.flfDesde = FiltroFactura.data.flfDesde;
		$scope.filter.fvHasta = FiltroFactura.data.fvHasta;
		$scope.filter.fiHasta = FiltroFactura.data.fiHasta;
		$scope.filter.flfHasta = FiltroFactura.data.flfHasta;
		$scope.filter.ffacDesde = FiltroFactura.data.ffacDesde;
		$scope.filter.ffacHasta = FiltroFactura.data.ffacHasta;
		$scope.filter.estado = FiltroFactura.data.estado;
		$scope.filter.page = FiltroFactura.page;

	};

	$scope.rollbackFilterFields = function() {
		FiltroFactura.data.cuit = $scope.filter.cuit;
		FiltroFactura.data.razon = $scope.filter.razon;
		FiltroFactura.data.numero = $scope.filter.numero;
		FiltroFactura.data.cc = $scope.filter.cc;
		FiltroFactura.data.centroc = $scope.filter.centroc;
		FiltroFactura.data.wf = $scope.filter.wf;
		FiltroFactura.data.fvDesde = $scope.filter.fvDesde;
		FiltroFactura.data.fiDesde = $scope.filter.fiDesde;
		FiltroFactura.data.flfDesde = $scope.filter.flfDesde;
		FiltroFactura.data.fvHasta = $scope.filter.fvHasta;
		FiltroFactura.data.fiHasta = $scope.filter.fiHasta;
		FiltroFactura.data.flfHasta = $scope.filter.flfHasta;
		FiltroFactura.data.ffacDesde = $scope.filter.ffacDesde;
		FiltroFactura.data.ffacHasta = $scope.filter.ffacHasta;
		FiltroFactura.data.tipo = $scope.filter.tipo;
		FiltroFactura.data.estado = $scope.filter.estado;
		FiltroFactura.page = $scope.filter.page;
	};

	$scope.loadFacturas = function() {
		if (FiltroFactura.data != null) {
			$scope.loadFilterFields();
		}
		var callbackFuntion = function(data, status, headers, config) {
			$scope.facturas = data.content;
			$scope.filter.totalPages = data.totalPages;
			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			$scope.setRangePages();
		};
		webServices.getWSResponseGet($http, 'factura', 'facturaPaginatedList',
				$scope.filter, $cookieStore, callbackFuntion);
	};

	$scope.filter = FModel.getFacturaAdmFilterForm();

	if (FiltroFactura.data == null || FiltroFactura.data.length == 0) {
		FiltroFactura.data = FModel.getFacturaAdmFilter();
	}

	if (FiltroFactura.page == null) {
		FiltroFactura.page = 1;
	}

	$scope.pagePreloaded = FiltroFactura.page;

	$scope.extFilter = FiltroFactura.data;

	$scope.tipos = FModel.getTipos();

	$scope.wfOptions = FModel.getWorkflowOptions();

	$scope.estadosFactura = FModel.getEstadosFactura();

	$scope.loadFilter();
	
	$scope.paginatedFacName='';
	$scope.filterName='filter';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedFacName',
			$scope.rollbackFilterFields, 'filter.page', $scope.loadFacturas,
			'filter.totalPages', 'filter.range', function() {
				FiltroFactura.page = $scope.filter.page;
			});
});