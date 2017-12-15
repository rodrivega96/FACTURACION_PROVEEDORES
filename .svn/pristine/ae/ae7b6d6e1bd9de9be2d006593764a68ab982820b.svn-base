caApp.registerCtrl('facturaFormEditWFController', function($scope, $location,
		$http, $routeParams, breadcrumbs, $cookieStore, webServices, $route, FModel, utilidades) {

	$scope.init = false;

	$scope.breadcrumbs = breadcrumbs;

	$scope.backFacAdm = function() {
		$location.path(utilidades.getPath($cookieStore, true));
	};

	// WF =======================================================
	$scope.autorizadoresSeleccionados = null;
	$scope.$watch("autorizadoresList", function(value) {
		if (value) {
			$scope.autorizadoresSeleccionados = value;
		}
	}, true);
	
	$scope.actualizarFromDB = function(listaActual){
		listaActual.sort(dynamicSort("orden"));
		$scope.init = true;
		if(listaActual.length > 0){
			if (listaActual[0].estadoFactura == 3
					|| listaActual[0].estadoFactura == 5) {
				$scope.editarFactura = false;
			} else {
				$scope.editarFactura = true;
			}
			for (var int = 0; int < listaActual.length; int++) {
				$scope.userSelected = listaActual[int].autorizador;
				$scope.estadoActual = listaActual[int].estado;
				$scope.idActual = listaActual[int].id;
				$scope.publicado = listaActual[int].publicado;
				$scope.nivelDTO = listaActual[int];
				$scope.agregarAutorizadorDB();
			}	
		}
	};
	
	$scope.refreshWF = function() {
		webServices.getWSResponseGet($http, 'workFlow', 'getNivelByFactura',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						$scope.actualizarFromDB(data.nivelesDTO);
						$scope.editarFactura = $scope.editarFactura
								&& !data.publicado;
						$scope.rechazado = data.facturaEstado;
					} else {
						alertify.error("Error al traer el workflow: "
								+ data.message);
					}
				});
	};

	$scope.reiniciarWF = function() {
		alertify.confirm("Se perdera el Workflow y todos "
				+ "los niveles de autorizaciones. "
				+ "Esta seguro se reiniciar el Workflow", function(e) {
			if (e) {
				var callbackFuntion = function(data, status, headers, config) {
					if (data.status == 0) {
						alertify.success("El WorkFlow se "
								+ "reinicio con exito");
						$route.reload();
					} else {
						alertify.error("Error al reiniciar el workflow: "
								+ data.message);
					}
				};
				webServices.getWSResponsePost($http, 'workFlow', 'reiniciarWF',
						$routeParams.facId, $cookieStore, callbackFuntion);
			}
		});
	};

	$scope.autorizadoresList = null;
	$scope.userList = null;

	webServices.getWSResponseGet($http, 'user', 'getAuthorized', null,
			$cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
					$scope.userList = data.userList;
					$scope.userList.sort(dynamicSort("name"));
					$scope.refreshWF();

				} else {
					alertify.error("Error al traer la lista de usuarios: "
							+ data.message);
				}
			});

	$scope.userSelected;
	$scope.estadoActual;
	$scope.editarFactura = true;
	var sendId = {
		'id' : $routeParams.facId
	};

	$scope.mostrarPubliacado = false;
	


	$scope.nivelDTO = FModel.getNewNivelDTO(sendId.id);

	
	
	
	$scope.actualizarUserList = function() {
		// se quita del combo el usuario agregado a la lista
		for (var int = 0; int < $scope.userList.length; int++) {
			if ($scope.userList[int].id == JSON.parse($scope.userSelected).id) {
				$scope.userList.splice(int, 1);
				break;
			}
		}
		$scope.userSelected = null;
	};

	$scope.actualizarUserListDB = function() {
		// se quita del combo el usuario agregado a la lista
		for (var int = 0; int < $scope.userList.length; int++) {
			if ($scope.userList[int].id == $scope.userSelected.id) {
				$scope.userList.splice(int, 1);
				break;
			}
		}
		$scope.userSelected = null;
		$scope.estadoActual = null;
	};



	$scope.agregarAutorizadorDB = function() {
		if ($scope.userSelected == null) {
			return;
		}
		$scope.nivelAux;
		if ($scope.autorizadoresList != null) {
			$scope.nivelAux = FModel.getNewNivelDTO($routeParams.facId);
			$scope.nivelAux[0].autorizador = $scope.userSelected;
			$scope.nivelAux[0].estado = $scope.estadoActual;
			$scope.nivelAux[0].publicado = $scope.publicado;
			$scope.nivelAux[0].id = $scope.idActual;
			$scope.nivelAux[0].rechazado = $scope.rechazado;
			$scope.autorizadoresList = $scope.autorizadoresList
					.concat($scope.nivelAux);

		} else {
			$scope.autorizadoresList = FModel.getNewNivelDTO($routeParams.facId);
			$scope.autorizadoresList[0].autorizador = $scope.userSelected;
			$scope.autorizadoresList[0].estado = $scope.estadoActual;
			$scope.autorizadoresList[0].publicado = $scope.publicado;
			$scope.autorizadoresList[0].rechazado = $scope.rechazado;
			$scope.autorizadoresList[0].id = $scope.idActual;
		}
		$scope.actualizarUserListDB();
	};

	$scope.agregarAutorizador = function() {
		if ($scope.userSelected == null) {
			return;
		}
		$scope.nivelAux;
		if ($scope.autorizadoresList != null) {
			$scope.nivelAux = FModel.getNewNivelDTO($routeParams.facId);
			$scope.nivelAux[0].autorizador = JSON.parse($scope.userSelected);
			$scope.nivelAux[0].estado = 1;
			$scope.nivelAux[0].publicado = false;
			$scope.autorizadoresList = $scope.autorizadoresList
					.concat($scope.nivelAux);

		} else {
			$scope.autorizadoresList = FModel.getNewNivelDTO($routeParams.facId);
			$scope.autorizadoresList[0].autorizador = JSON
					.parse($scope.userSelected);
			$scope.autorizadoresList[0].estado = 1;
			$scope.autorizadoresList[0].publicado = false;

		}
		$scope.actualizarUserList();
	};

	
	$scope.quitarAutorizador = function(item) {
		// se quita de la lista el autorizador
		for (var int = 0; int < $scope.autorizadoresSeleccionados.length; int++) {
			if ($scope.autorizadoresSeleccionados[int].autorizador.id == item.autorizador.id) {
				$scope.autorizadoresSeleccionados.splice(int, 1);
			}
		}
		// se agrega el autorizador al combo
		$scope.userList.push(item.autorizador);
		$scope.userList.sort(dynamicSort("name"));
		$scope.userSelected = null;
	};

	$scope.saveWF = function() {
		// actualiza orden y factura acutal
		if ($scope.autorizadoresSeleccionados == null
				|| $scope.autorizadoresSeleccionados.length < 1) {
			alertify.error("Debe seleccionar al menos un autorizador.");
			return;
		}
		for (var int = 0; int < $scope.autorizadoresSeleccionados.length; int++) {
			$scope.autorizadoresSeleccionados[int].orden = int + 1;
		}

		// guarda el wf
		webServices.getWSResponsePost($http, 'workFlow', 'saveNivel',
				$scope.autorizadoresSeleccionados, $cookieStore, function(data,
						status, headers, config) {
					if (data.status == 0) {
						alertify.inform("Se guard\u00f3 el Workflow, "
								+ "pero aun no se ha enviadoa "
								+ "autorizar. Para enviar a"
								+ "autorizar click en "
								+ "'Guardar y Publicar'");
						$location.path(utilidades.getPathRedirection($cookieStore, "/factura-admin"));
					} else if (data.status == 6) {
						alertify.error("Se guard\u00f3 el Workflow, "
								+ "pero fall\u00f3 el env\u00edo "
								+ "de mail. No se pudo informar "
								+ "al autorizador. " + data.message);
						$location.path(utilidades.getPathRedirection($cookieStore, "/factura-admin"));

					} else {
						alertify.error("Error guardando Workflow: "
								+ data.message);
					}
				});
	};

	$scope.saveAndPublishWF = function() {
		if ($scope.autorizadoresSeleccionados == null
				|| $scope.autorizadoresSeleccionados.length < 1) {
			alertify.error("Debe seleccionar al menos un autorizador.");
			return;
		}

		for (var int = 0; int < $scope.autorizadoresSeleccionados.length; int++) {
			$scope.autorizadoresSeleccionados[int].orden = int + 1;
		}
		// guarda y publica el wf
		webServices.getWSResponsePost($http, 'workFlow', 'saveAndPublishNivel',
				$scope.autorizadoresSeleccionados, $cookieStore, function(data,
						status, headers, config) {
					if (data.status == 0) {
						alertify.success("Se guard\u00f3 el Workflow, y se "
								+ "ha enviado a autorizar.");
						$location.path(utilidades.setPath($cookieStore,"/factura-admin",$location.path()));
					} else if (data.status == 6) {
						alertify.error("Se guard\u00f3 el Workflow, "
								+ "pero fall\u00f3 el env\u00edo "
								+ "de mail. No se pudo informar "
								+ "al autorizador. " + data.message);
						$location.path(utilidades.getPathRedirection($cookieStore, "/factura-admin"));

					} else {
						alertify.error("Error guardando Workflow: "
								+ data.message);
					}
				});
	};

});