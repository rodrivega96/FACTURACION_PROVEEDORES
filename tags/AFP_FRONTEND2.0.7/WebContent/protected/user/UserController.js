caApp.registerCtrl('userAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, paginatedFunction, USModel) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.showMessage = false;

	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.goEdit = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.goAssignRoles = function(path, $param) {
		$location.path(path + "/" + $param);
	};

	$scope.loadFilter = function() {
		$scope.filter.page = 1;
		$scope.loadFilterFields();
		$scope.loadUsers();
	};

	$scope.loadFilterFields = function() {
		$scope.filter.name = $scope.extFilter.name;
		$scope.filter.lastName = $scope.extFilter.lastName;
		$scope.filter.active = $scope.extFilter.active;
		$scope.filter.job = $scope.extFilter.job;
	};

	$scope.rollbackFilterFields = function() {
		$scope.extFilter.name = $scope.filter.name;
		$scope.extFilter.lastName = $scope.filter.lastName;
		$scope.extFilter.job = $scope.filter.job;
	};

	$scope.loadUsers = function() {
		$scope.showMessage = false;
		var callbackFuntion = function(data, status, headers, config) {
			$scope.users = data.content;
			$scope.filter.totalPages = data.totalPages;
			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			$scope.setRangePages();
			for (var i = 0; i < $scope.users.length; i++) {
				if ($scope.users[i].active === 1) {
					$scope.users[i].active = $scope.states[2].name;
				} else {
					$scope.users[i].active = $scope.states[1].name;
				}
			}
			if (data.numberOfElements == 0) {
				$scope.showMessage = true;
				$scope.warningMessage = 'No se encontraron usuarios';
			}
		};
		webServices.getWSResponseGet($http, 'user', 'userPaginatedList',
				$scope.filter, $cookieStore, callbackFuntion);
	};

	$scope.filter = USModel.getFilter();

	$scope.extFilter = USModel.getExtFilter();

	$scope.states = USModel.getFindState();
	$scope.loadFilter();

	$scope.deleteUser = function($user) {
		alertify.confirm("Esta seguro de querer eliminar el usuario?",
				function(e) {
					if (e) {
						$user.active = USModel.stateValue($user.active);
						var callbackFuntion = function(data, status, headers,
								config) {
							if (data.status == 0) {
								alertify.success("El usuario se "
										+ "elimino correctamente");
								$scope.loadFilter();
							} else if (data.status == 1) {
								alertify.confirm("El usuario ha "
										+ "sido modificado por "
										+ "otra persona, desea "
										+ "eliminarlo de todas formas?",
										function(e) {
											if (e) {
												$scope.forceDelete($user);
											} else {
												location.reload();
											}
										});
							} else if (data.status == 4) {
								alertify.error("El usuario ya "
										+ "fue eliminado");
								$scope.loadFilter();
							} else if (data.status == 5) {
								alertify.error("El usuario no puede "
										+ "ser eliminado porque "
										+ "ha sido incluido en un WF, "
										+ "se cambio su estado a inactivo");
								$scope.loadFilter();
							}
						};
						webServices.getWSResponsePost($http, 'user',
								'deleteUser', $user, $cookieStore,
								callbackFuntion);
					}
				});
	};

	$scope.forceDelete = function($user) {
		webServices.getWSResponsePost($http, 'user', 'forceDeleteUser', $user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("El usuario se "
								+ "elimino correctamente");
						$scope.loadFilter();
					} else if (data.status == 4) {
						alertify.error("El usuario ya fue eliminado");
						$scope.loadFilter();
					} else if (data.status == 5) {
						alertify.error("El usuario no puede "
								+ "ser eliminado porque "
								+ "ha sido incluido en un WF, "
								+ "se cambio su estado a inactivo");
						$scope.loadFilter();
					}
				});
	};

	$scope.paginatedUserName = '';
	$scope.filterName='filter';
	paginatedFunction.generatePaginatedMethod($scope, 'paginatedUserName',
			$scope.rollbackFilterFields, 'filter.page', $scope.loadUsers,
			'filter.totalPages', 'filter.range', null);

});