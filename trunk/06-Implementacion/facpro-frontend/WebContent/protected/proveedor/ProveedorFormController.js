caApp.registerCtrl('proveedorFormController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		FiltroProveedor,ProveedorModel , utilidades){
	
	$scope.breadcrumbs = breadcrumbs;

	$scope.proveedor = ProveedorModel.newProveedor();
	$scope.loadData=(FiltroProveedor.initial==null || FiltroProveedor.initial.length==0);
	if($scope.loadData){
		webServices.getWSResponseGet($http, 'proveedor', 'getDatoInicial', null,
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
	$scope.backAdm = function() {
		$location.path(utilidades.getPath($cookieStore, true));
	};
	
	$scope.saveProveedor = function() {
		var callBackFuntion = function(data, status, headers, config) {
			if (data.status == 0) {
				alertify.success("El Proveedor se genero correctamente");
				$location.path(utilidades.getPath($cookieStore, true));
			} else {
				alertify.error("Error generando el "
						+ "proveedor: " + data.message);
			}
		};
		webServices.getWSResponsePost($http, 'proveedor', 'saveProveedor',
				$scope.proveedor, $cookieStore, callBackFuntion);
	};

});

caApp.registerCtrl('proveedorEditController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		FiltroProveedor,ProveedorModel, utilidades, $routeParams) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.proveedor = ProveedorModel.newProveedor();

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



	$scope.backAdm = function() {
		$location.path(utilidades.getPath($cookieStore, true));
	};
	
	$scope.saveProveedor = function() {
		var callBackFuntion = function(data, status, headers, config) {
			if (data.status == 0) {
				alertify.success("El Proveedor se guardo correctamente");
				$location.path(utilidades.getPath($cookieStore, true));
			} else {
				alertify.error("Error generando el "
						+ "proveedor: " + data.message);
			}
		};
		webServices.getWSResponsePost($http, 'proveedor', 'saveProveedor',
				$scope.proveedor, $cookieStore, callBackFuntion);
	};
	
	var sendId = {
			'id' : $routeParams.proveedorId
	};

	
	webServices.getWSResponseGet($http, 'proveedor', 'getProveedor',sendId,
			$cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
					$scope.proveedor=data.proveedor;
				} else {
					alertify.error("Error al cargar el proveedor: "
							+ data.message);
				}
			});
	
	

});