caApp.controller('userAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, FiltroFactura) {

	FiltroFactura.data = null;
	FiltroFactura.dataConsult = null;
	
	$scope.breadcrumbs = breadcrumbs;
	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.showMessage = false;

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

	$scope.prevPage = function() {
		$scope.rollbackFilterFields();
		if ($scope.filter.page > 1) {
			$scope.filter.page--;
			$scope.loadUsers();
		}
	};

	$scope.goPage = function($number) {
		$scope.filter.page = $number;
		$scope.rollbackFilterFields();
		$scope.loadUsers();
	};

	$scope.nextPage = function() {
		$scope.rollbackFilterFields();
		if ($scope.filter.page < $scope.filter.totalPages) {
			$scope.filter.page++;
			$scope.loadUsers();
		}
	};

	$scope.firstPage = function() {
		$scope.rollbackFilterFields();
		$scope.filter.page = 1;
		$scope.loadUsers();
	};

	$scope.lastPage = function() {
		$scope.rollbackFilterFields();
		$scope.filter.page = $scope.filter.totalPages;
		$scope.loadUsers();
	};

	$scope.setRangePages = function() {
		$scope.filter.range = [];
		for (var i = 1; i <= $scope.filter.totalPages; i++) {
			$scope.filter.range.push(i);
		}
	};
	$scope.filter = {
		page : 1,
		name : '',
		lastName : '',
		active : -1,
		job : '',
		limit : 10,
		range : []
	};
	$scope.extFilter = {
		name : '',
		lastName : '',
		active : -1,
		job : ''
	};

	$scope.states = getFindState();

	$scope.loadFilter();

	$scope.deleteUser = function($user) {

		alertify.confirm("Esta seguro de querer eliminar el usuario?",
				function(e) {
					if (e) {
						$user.active = stateValue($user.active);
						var callbackFuntion = function(data, status, headers,
								config) {
							if (data.status == 0) {
								alertify.success("El usuario se "
										+ "elimino correctamente");
								// location.reload();
								$scope.loadFilter();
							} else if (data.status == 1) {
								// var forceDelete =
								// window
								// .confirm("El usuario
								// ha sido modificado
								// por "
								// + "otra persona,
								// desea "
								// + "eliminarlo de
								// todas formas?");
								// if (forceDelete) {
								// $scope.forceDelete($user);
								// } else {
								// location.reload();
								// }

								alertify.confirm("El usuario ha "
										+ "sido modificado por "
										+ "otra persona, desea "
										+ "eliminarlo de todas formas?",
										function(e) {
											if (e) {
												$scope.forceDelete($user);
											} else {
												location.reload();
												// $scope.loadFilter();
											}
										});

							} else if (data.status == 4) {
								alertify.error("El usuario ya "
										+ "fue eliminado");
								// location.reload();
								$scope.loadFilter();
							} else if (data.status == 5) {
								alertify.error("El usuario no puede "
										+ "ser eliminado porque "
										+ "ha sido incluido en un WF, "
										+ "se cambio su estado a inactivo");
								// location.reload();
								$scope.loadFilter();
							}
						};
						webServices.getWSResponsePost($http, 'user',
								'deleteUser', $user, $cookieStore,
								callbackFuntion);
					}
				});

		// var confirmDelete = window.confirm('Esta seguro de
		// querer eliminar el usuario?');
		// if (confirmDelete) {
		// $user.active = stateValue($user.active);
		// var callbackFuntion = function(data, status, headers,
		// config) {
		// if (data.status == 0) {
		// alert("El usuario se elimino correctamente");
		// location.reload();
		// } else if (data.status == 1) {
		// var forceDelete = window
		// .confirm("El usuario ha sido modificado por "
		// + "otra persona, desea "
		// + "eliminarlo de todas formas?");
		// if (forceDelete) {
		// $scope.forceDelete($user);
		// } else {
		// location.reload();
		// }
		// } else if (data.status == 4) {
		// alert("El usuario ya fue eliminado");
		// location.reload();
		// } else if (data.status == 5) {
		// alert.error("El usuario no puede ser eliminado porque
		// "
		// + "ha sido incluido en un WF, "
		// + "se cambio su estado a inactivo");
		// location.reload();
		// }
		// };
		// webServices.getWSResponsePost($http, 'user',
		// 'deleteUser', $user,
		// $cookieStore, callbackFuntion);
		// }
	};

	$scope.forceDelete = function($user) {
		webServices.getWSResponsePost($http, 'user', 'forceDeleteUser', $user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						// alert("El usuario se elimino
						// correctamente");
						// location.reload();
						alertify.success("El usuario se "
								+ "elimino correctamente");
						$scope.loadFilter();

					} else if (data.status == 4) {
						// alert("El usuario ya fue
						// eliminado");
						// location.reload();
						alertify.error("El usuario ya fue eliminado");
						$scope.loadFilter();
					} else if (data.status == 5) {
						// alert("El usuario no puede
						// ser eliminado porque "
						// + "ha sido incluido en un WF,
						// "
						// + "se cambio su estado a
						// inactivo");
						// location.reload();
						alertify.error("El usuario no puede "
								+ "ser eliminado porque "
								+ "ha sido incluido en un WF, "
								+ "se cambio su estado a inactivo");
						$scope.loadFilter();
					}
				});
	};

});

caApp.controller('userFormController', function($scope, $location, $http,
		breadcrumbs, $cookieStore, webServices) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.user = new newUser();
	$scope.valueState = 1;

	$scope.backUsAdm = function() {
		window.history.back();
	};

	$scope.states = getState();

	$scope.saveUs = function() {
		webServices.getWSResponsePost($http, 'user', 'saveUser', $scope.user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						// alert("El usuario se creo
						// correctamente");
						alertify.success("El usuario se "
								+ "creo correctamente");
						window.history.back();
					} else if (data.status == 6) {
						// alert("El usuario se creo
						// correctamente, pero "
						// + "no se puedo enviar el
						// E-mail "
						// + "a la direccion " +
						// $scope.user.mail);
						alertify.error("El usuario se creo "
								+ "correctamente, pero "
								+ "no se puedo enviar el E-mail "
								+ "a la direccion " + $scope.user.mail);
						window.history.back();
					} else if (data.status == 3) {
						// alert("El usuario ya
						// existe");
						alertify.error("El usuario ya existe");
					} else {
						// alert("Error creando el
						// usuario: " + data.message);
						alertify.error("Error creando el usuario: "
								+ data.message);
					}
				});
	};

});

caApp.controller('userFormEditController', function($scope, $location, $http,
		$routeParams, breadcrumbs, $cookieStore, webServices) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.inactive = false;

	$scope.backUsAdm = function() {
		window.history.back();
	};

	var sendId = {
		'id' : $routeParams.usId
	};

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.user = data.user;
			if ($scope.user.active == 0) {
				$scope.inactive = true;
			}
		} else if (data.status == 3) {
			// alert("El estudiante ya fue eliminado por otro
			// usuario");
			alertify.error("El estudiante ya fue "
					+ "eliminado por otro usuario");
			window.history.back();
		} else {
			// alert("Error cargando el estudiante: " +
			// data.message);
			alertify.error("Error cargando el estudiante: " + data.message);
		}
	};

	webServices.getWSResponseGet($http, 'user', 'getUser', sendId,
			$cookieStore, callbackFuntion);

	$scope.states = getState();

	$scope.saveUs = function() {
		webServices.getWSResponsePost($http, 'user', 'updateUser', $scope.user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						// alert("El usuario se
						// actualizo correctamente");
						alertify.success("El usuario se "
								+ "actualizo correctamente");
						window.history.back();
					} else if (data.status == 1) {
						// var forceUpdate = window
						// .confirm("El usuario ha sido
						// modificado por "
						// + "otra persona, desea "
						// + "guardarlo de todas
						// formas?");
						// if (forceUpdate) {
						// $scope.forceUpdate($scope.user);
						// } else {
						// location.reload();
						// }

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
						// alert("El usuario ya
						// existe");
						alertify.error("El usuario ya existe");
					} else if (data.status == 4) {
						// var deleteUser = window
						// .confirm("El usuario fue
						// eliminado por "
						// + "otra persona, desea "
						// + "consultar el listado de
						// usuarios?");
						// if (deleteUser) {
						// window.history.back();
						// }

						alertify.confirm("El usuario fue eliminado por "
								+ "otra persona, desea "
								+ "consultar el listado de usuarios?",
								function(e) {
									if (e) {
										window.history.back();
									}
								});
					} else {
						// alert("Error guardando el
						// usuario: " + data.message);
						alertify.error("Error guardando el usuario: "
								+ data.message);
					}
				});
	};

	$scope.forceUpdate = function($user) {
		webServices.getWSResponsePost($http, 'user', 'forceUpdateUser', $user,
				$cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						// alert("El usuario se
						// actualizo correctamente");
						alertify.success("El usuario se "
								+ "actualizo correctamente");
						window.history.back();
					} else if (data.status == 3) {
						// alert("El usuario ya
						// existe");
						alertify.error("El usuario ya existe");
					} else if (data.status == 4) {
						// var deleteUser = window
						// .confirm("El usuario fue
						// eliminado por "
						// + "otra persona, desea "
						// + "consultar el listado de
						// usuarios?");
						// if (deleteUser) {
						// window.history.back();
						// }

						alertify.confirm("El usuario fue eliminado por "
								+ "otra persona, desea "
								+ "consultar el listado de usuarios?",
								function(e) {
									if (e) {
										window.history.back();
									}
								});

					} else {
						// alert("Error guardando el
						// usuario: " + data.message);
						alertify.error("Error guardando el usuario: "
								+ data.message);
					}
				});
	};

});

caApp.controller('userAssignRolesController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		$routeParams, $filter, tableFactory) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.backArAdm = function() {
		window.history.back();
	};

	$scope.userInactive = false;

	var sendId = {
		'id' : $routeParams.usId
	};

	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.user = data.user;
			$scope.apeNom = $scope.user.lastName + ' ,' + $scope.user.name;
		} else if (data.status == 3) {
			// alert("El usuario ya fue eliminado por otro usuario");
			alertify.error("El usuario ya fue eliminado por otro usuario");
			window.history.back();
		} else {
			// alert("Error cargando el usuario: " + data.message);
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
				} else if (data.status == 4) {
					// alert("El usuario fue eliminado por " + "otra persona.");
					alertify.error("El usuario fue eliminado por "
							+ "otra persona.");
					window.history.back();
				}
			});

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
				// alert("Los roles se guardaron correctamente");
				alertify.success("Los roles se guardaron correctamente");
				window.history.back();
			} else if (data.status == 4) {
				// var deleteUser = window.confirm("El usuario fue eliminado por
				// "
				// + "otra persona, desea "
				// + "consultar el listado de usuarios?");
				// if (deleteUser) {
				// window.history.back();
				// }

				alertify.confirm("El usuario fue eliminado por "
						+ "otra persona, desea "
						+ "consultar el listado de usuarios?", function(e) {
					if (e) {
						window.history.back();
					}
				});

			} else {
				// alert("Error guardando los roles: " + data.message);
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
