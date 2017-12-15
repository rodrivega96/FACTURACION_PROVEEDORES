caApp.registerCtrl('userFormController', function($scope, $location, $http,
		breadcrumbs, $cookieStore, webServices, USModel, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.locationPath = utilidades.getPath($location.$$path);
	$scope.user = USModel.newUser();
	$scope.valueState = 1;
	$scope.states = USModel.getState();

	$scope.backUsAdm = function() {
		$location.path($scope.locationPath);
	};

	$scope.saveUs = function() {
		webServices.getWSResponsePost($http, 'user', 'saveUser', $scope.user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("El usuario se "
								+ "creo correctamente");
						$location.path($scope.locationPath);
					} else if (data.status == 6) {
						alertify.error("El usuario se creo "
								+ "correctamente, pero "
								+ "no se puedo enviar el E-mail "
								+ "a la direccion " + $scope.user.mail);
						$location.path($scope.locationPath);
					} else if (data.status == 3) {
						alertify.error("El usuario ya existe");
					} else {
						alertify.error("Error creando el usuario: "
								+ data.message);
					}
				});
	};
});

caApp.registerCtrl('userFormEditController', function($scope, $location, $http,
		$routeParams, breadcrumbs, $cookieStore, webServices, USModel, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.locationPath = utilidades.getPath($location.$$path);
	$scope.inactive = false;
	$scope.states = USModel.getState();

	$scope.backUsAdm = function() {
		$location.path($scope.locationPath);
	};

	var sendId = {
		'id' : $routeParams.usId
	};

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.user = data.userView;
			if ($scope.user.active == 0) {
				$scope.inactive = true;
			}
		} else if (data.status == 3) {
			alertify.error("El estudiante ya fue "
					+ "eliminado por otro usuario");
			$location.path($scope.locationPath);
		} else {
			alertify.error("Error cargando el estudiante: " + data.message);
		}
	};

	webServices.getWSResponseGet($http, 'user', 'getUser', sendId,
			$cookieStore, callbackFuntion);

	$scope.saveUs = function() {
		webServices.getWSResponsePost($http, 'user', 'updateUser', $scope.user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("El usuario se "
								+ "actualizo correctamente");
						$location.path($scope.locationPath);
					} else if (data.status == 1) {
						alertify.confirm("El usuario ha sido "
								+ "modificado por otra persona, desea "
								+ "guardarlo de todas formas?", function(e) {
							if (e) {
								$scope.forceUpdate($scope.user);
							} else {
								location.reload();
							}
						});
					} else if (data.status == 3) {
						alertify.error("El usuario ya existe");
					} else if (data.status == 4) {
						alertify.confirm("El usuario fue eliminado por "
								+ "otra persona, desea "
								+ "consultar el listado de usuarios?",
								function(e) {
									if (e) {
										$scope.$apply(function() {
											$location.path($scope.locationPath);
										});
									}
								});
					} else {
						alertify.error("Error guardando el usuario: "
								+ data.message);
					}
				});
	};

	$scope.forceUpdate = function($user) {
		webServices.getWSResponsePost($http, 'user', 'forceUpdateUser', $user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("El usuario se "
								+ "actualizo correctamente");
						$location.path($scope.locationPath);
					} else if (data.status == 3) {
						alertify.error("El usuario ya existe");
					} else if (data.status == 4) {
						alertify.confirm("El usuario fue eliminado por "
								+ "otra persona, desea "
								+ "consultar el listado de usuarios?",
								function(e) {
									if (e) {
										$scope.$apply(function() {
											$location.path($scope.locationPath);
										});
									}
								});
					} else {
						alertify.error("Error guardando el usuario: "
								+ data.message);
					}
				});
	};
});