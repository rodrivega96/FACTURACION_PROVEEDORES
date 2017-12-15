caApp.registerCtrl('userAssignRolesController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		$routeParams, $filter, tableFactory, utilidades, $q) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.userInactive = false;

	$scope.backArAdm = function() {
		window.history.back();
	};

	var sendId = {
		'id' : $routeParams.usId
	};

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.user = data.user;
			$scope.apeNom = $scope.user.lastName + ' ,' + $scope.user.name;
		} else if (data.status == 3) {
			alertify.error("El usuario ya fue eliminado por otro usuario");
			window.history.back();
		} else {
			alertify.error("Error cargando el usuario: " + data.message);
		}
	};

	webServices.getWSResponseGet($http, 'user', 'getUser', sendId,
			$cookieStore, callbackFuntion);

	$scope.showTRoles = false;
	var showRoles = 'Seleccionar Roles';
	var hideRoles = 'Ocultar Roles';
	$scope.showTable = showRoles;
	$scope.buttonTable = function() {
		$scope.showTRoles = $scope.showTRoles ? false : true;
		$scope.showTable = !$scope.showTRoles ? $scope.showTable = showRoles
				: $scope.showTable = hideRoles;
	};

	var tableData = [];
	webServices.getWSResponseGet($http, 'role', 'getRolesByUserId', sendId,
			$cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
					tableData = data.roles;
					$scope.tableSelectedRoles = tableFactory
							.getTableFilterData(tableData, ngTableParams,
									$filter, $scope.role, $scope);
					$scope.tableRoles = tableFactory.getTableData(tableData,
							ngTableParams, $scope);
					$scope.seleccionados();
				} else if (data.status == 4) {
					// alert("El usuario fue eliminado por " + "otra persona.");
					alertify.error("El usuario fue eliminado por "
							+ "otra persona.");
					window.history.back();
				}
			});

	$scope.seleccionados = function() {
		var def = $q.defer();
		def.resolve(utilidades.getFiltroSeleccionado());
		return def;
	};
	
	$scope.saveAr = function() {
		var roleDTO = [];
		var exitOne = false;
		for (var i = 0; i < tableData.length; i++) {
			if (tableData[i].selected == true) {
				exitOne = true;
			}
			roleDTO.push({
				roleId : tableData[i].roleId,
				selected : tableData[i].selected
			});
		}

		var dto = {
			userId : $scope.user.id,
			roles : roleDTO
		};

		var callbackFunction = function(data, status, headers, config) {
			if (data.status == 0) {
				alertify.success("Los roles se guardaron correctamente");
				window.history.back();
			} else if (data.status == 4) {
				alertify.confirm("El usuario fue eliminado por "
						+ "otra persona, desea "
						+ "consultar el listado de usuarios?", function(e) {
					if (e) {
						window.history.back();
					}
				});

			} else {
				alertify.error("Error guardando los roles: " + data.message);
			}
		};
		if (exitOne) {
			webServices.getWSResponsePost($http, 'role', 'saveRoles', dto,
					$cookieStore, callbackFunction);
		} else {
			$scope.userError = true;
			$scope.showMessage = 'Se debe seleccionar al menos un rol';
		}
	};
});
