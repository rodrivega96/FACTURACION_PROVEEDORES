caApp.registerCtrl('proveedorAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, FiltroProveedor, paginatedFunction, ProveedorModel, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	
	
	$scope.go = function(path) {
		$location.path(utilidades.setPath($cookieStore,path,$location.path()));
	};
	
	$scope.goEdit = function(path, $param) {
		$location.path(utilidades.setPath($cookieStore,path + "/" + $param,$location.path()));
	};
	
	$scope.goDelete = function(id) {
		alertify.confirm("Esta seguro de ELIMINAR el Proveedor?", function (e) {
			if (e) {
				webServices.getWSResponsePost($http, 'proveedor', 'delete', id,
						$cookieStore, function(data, status, headers, config) {
							if (data.status == 0) {
								alertify.success("El proveedor se elimino exitosamente");
								$scope.loadProveedores();
							} else {
								alertify.error("Error al eliminar el Proveedor: "
										+ data.message);
							}
						});
			}
		});
	};

	$scope.showFilter = function(state) {
		$scope.isShowFilter = state;
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
		FiltroProveedor.page = $scope.filter.page;
		$scope.loadFilterFields();
		$scope.loadProveedores();
	};

	$scope.clearFilter = function() {
		var page = $scope.filter.page;
		var tPage = $scope.filter.totalPages;
		var range = $scope.filter.range;
		$scope.filter = ProveedorModel.getProveedorFilterForm();
		$scope.filter.page = page;
		$scope.filter.totalPages = tPage;
		$scope.filter.range = range;
	};

	$scope.rollbackFilterFields = function() {
		$scope.filter.razonSocial = FiltroProveedor.data.razonSocial;
		$scope.filter.cuit = FiltroProveedor.data.cuit;
		$scope.filter.medioPago = FiltroProveedor.data.medioPago;
		$scope.filter.page = FiltroProveedor.page;

	};

	$scope.loadFilterFields = function() {
		FiltroProveedor.data.razonSocial = $scope.filter.razonSocial;
		FiltroProveedor.data.cuit = $scope.filter.cuit;
		FiltroProveedor.data.medioPago = $scope.filter.medioPago;
		FiltroProveedor.page = $scope.filter.page;
	};

	$scope.loadProveedores = function() {
		if (FiltroProveedor.data != null) {
			$scope.loadFilterFields();
		}
		var callbackFuntion = function(data, status, headers, config) {
			$scope.proveedores = data.content;
			$scope.filter.totalPages = data.totalPages;
			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			$scope.setRangePages();
		};
		webServices.getWSResponseGet($http, 'proveedor', 'proveedorPaginatedList',
				$scope.filter, $cookieStore, callbackFuntion);
	};
	
	$scope.loadData=(FiltroProveedor.initial==null || FiltroProveedor.initial.length==0);
	if($scope.loadData){
	webServices.getWSResponseGet($http, 'proveedor', 'getDatoInicial',null,
			$cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
					ProveedorModel.loadInitialDataFilter($scope, data.datoInicialDTO);
					FiltroProveedor.initial = data.datoInicialDTO;
				} else {
					alertify.error("Error al traer los datos iniciales: "
							+ data.message);
				}
			});
	}else{
		ProveedorModel.loadInitialDataFilter($scope, FiltroProveedor.initial);
	}

	if (FiltroProveedor.data == null || FiltroProveedor.data.length == 0) {
		FiltroProveedor.data = ProveedorModel.getProveedorFilterForm();
	}

	if (FiltroProveedor.page == null) {
		FiltroProveedor.page = 1;
	}
	
	$scope.pagePreloaded = FiltroProveedor.page;
	$scope.filter=ProveedorModel.getProveedorFilterForm();
	$scope.rollbackFilterFields();
	$scope.paginatedProvName='';
	$scope.filterName='filter';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedProvName',
			$scope.rollbackFilterFields, 'filter.page', $scope.loadProveedores,
			'filter.totalPages', 'filter.range', function() {
				FiltroProveedor.page = $scope.filter.page;
			});
	$scope.loadProveedores();
});

