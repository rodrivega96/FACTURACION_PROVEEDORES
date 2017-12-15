caApp.controller('facturaAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, FiltroFactura) {

	FiltroFactura.dataConsult = null;
	
	$scope.breadcrumbs = breadcrumbs;
	$scope.isShowFilter = false;
	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.showFilter = function(state) {
		$scope.isShowFilter = state;
	};

	$scope.goEdit = function(path, $param) {	
		FiltroFactura.data = $scope.filter;
		$location.path(path + "/" + $param);
	};

	$scope.goUploadFile = function(path, $param) {
		FiltroFactura.data = $scope.filter;
		$location.path(path + "/" + $param);
	};
	
	$scope.goWF = function(path, $param) {	
		FiltroFactura.data = $scope.filter;
		$location.path(path + "/" + $param);
	};

	$scope.loadFilter = function() {
		$scope.filter.page = 1;
		$scope.loadFilterFields();
		$scope.loadFacturas();
	};
	
	$scope.clearFilter = function() {
		$scope.filter = clearFilterFactura($scope.filter.page, $scope.filter.totalPage);
		$scope.extFilter = clearFilterFactura($scope.filter.page, $scope.filter.totalPage);
		FiltroFactura.data = $scope.filter;
	};

	$scope.loadFilterFields = function() {
		$scope.filter.numero = $scope.extFilter.numero;
		$scope.filter.tipo = $scope.extFilter.tipo;
		$scope.filter.cuit = $scope.extFilter.cuit;
		$scope.filter.razon = $scope.extFilter.razon;
		$scope.filter.cc = $scope.extFilter.cc;
		$scope.filter.centroc = $scope.extFilter.centroc;
		$scope.filter.wf = $scope.extFilter.wf;
		$scope.filter.fvDesde = $scope.extFilter.fvDesde;
		$scope.filter.fiDesde = $scope.extFilter.fiDesde;
		$scope.filter.flfDesde = $scope.extFilter.flfDesde;
		$scope.filter.fvHasta = $scope.extFilter.fvHasta;
		$scope.filter.fiHasta = $scope.extFilter.fiHasta;
		$scope.filter.flfHasta = $scope.extFilter.flfHasta;
		$scope.filter.estado = $scope.extFilter.estado;

	};

	
	$scope.rollbackFilterFields = function() {
		$scope.extFilter.cuit = $scope.filter.cuit;
		$scope.extFilter.razon = $scope.filter.razon;
		$scope.extFilter.numero = $scope.filter.numero;
		$scope.extFilter.cc = $scope.filter.cc;
		$scope.extFilter.centroc = $scope.filter.centroc;
		$scope.extFilter.wf = $scope.filter.wf;
		$scope.extFilter.fvDesde = $scope.filter.fvDesde;
		$scope.extFilter.fiDesde = $scope.filter.fiDesde;
		$scope.extFilter.flfDesde = $scope.filter.flfDesde;
		$scope.extFilter.fvHasta = $scope.filter.fvHasta;
		$scope.extFilter.fiHasta = $scope.filter.fiHasta;
		$scope.extFilter.flfHasta = $scope.filter.flfHasta;
		$scope.extFilter.tipo = $scope.filter.tipo;
		$scope.extFilter.estado = $scope.filter.estado;
	};

	$scope.loadFacturas = function() {
		if (FiltroFactura.data != null) {
			$scope.extFilter = FiltroFactura.data;
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

	$scope.prevPage = function() {
		$scope.rollbackFilterFields();
		if ($scope.filter.page > 1) {
			$scope.filter.page--;
			$scope.loadFacturas();
		}
	};

	$scope.goPage = function($number) {
		$scope.filter.page = $number;
		$scope.rollbackFilterFields();
		$scope.loadFacturas();
	};

	$scope.nextPage = function() {
		$scope.rollbackFilterFields();
		if ($scope.filter.page < $scope.filter.totalPages) {
			$scope.filter.page++;
			$scope.loadFacturas();
		}
	};

	$scope.firstPage = function() {
		$scope.rollbackFilterFields();
		$scope.filter.page = 1;
		$scope.loadFacturas();
	};

	$scope.lastPage = function() {
		$scope.rollbackFilterFields();
		$scope.filter.page = $scope.filter.totalPages;
		$scope.loadFacturas();
	};

	$scope.setRangePages = function() {
		$scope.filter.range = [];
		for (var i = 1; i <= $scope.filter.totalPages; i++) {
			$scope.filter.range.push(i);
		}
	};
	$scope.filter = getFacturaAdmFilterForm();
	$scope.extFilter = getFacturaAdmFilter();

	$scope.tipos = getTipos();

	$scope.wfOptions = getWorkflowOptions();

	$scope.estadosFactura = getEstadosFactura();

	$('#fvDateCalendarDesde').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fvDateCalendarDesde'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fvDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#fiDateCalendarDesde').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fiDateCalendarDesde'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fiDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#flfDateCalendarDesde').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#flfDateCalendarDesde'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.flfDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});
	$('#fvDateCalendarHasta').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fvDateCalendarHasta'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fvHasta = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#fiDateCalendarHasta').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fiDateCalendarHasta'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fiHasta = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#flfDateCalendarHasta').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#flfDateCalendarHasta'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.flfHasta = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$scope.loadFilter();
});



caApp.controller('facturaFormEditController',
				function($scope, $location, $http, $routeParams, breadcrumbs,
						$cookieStore, webServices, ngTableParams, tableFactory) {

					$scope.breadcrumbs = breadcrumbs;
					
					$scope.factura = newFactura();

					$scope.backFacAdm = function() {
						window.history.back();
					};

					$('#dueDateCalendar').datepicker({
						format : getStringFormatDate()
					}).on('changeDate', function(ev) {
						var element = angular.element($('#dueDateCalendar'));
						var controller = element.controller();
						var scope = element.scope();
						scope.$apply(function() {
							scope.factura.vencimiento = ev.date;
						});
						$(this).datepicker('hide');
					});

					$scope.tipos = getTipos();

					$scope.wfOptions = getWorkflowOptions();

					$scope.estadosFactura = getEstadosFactura();

					var sendId = {
						'id' : $routeParams.facId
					};

					var callbackFuntion = function(data, status, headers,
							config) {
						if (data.status == 0) {
							$scope.factura = data.factura;
							if(data.factura.vencimiento!=null){
								$scope.factura.vencimiento = new Date(
										data.factura.vencimiento);
								$('#dueDateCalendar')
										.val(getFormattedDate($scope.factura.vencimiento));
							}
							var tableData = [];
							tableData = data.factura.asientos;
							$scope.tableAsientos = tableFactory.getTableData(
									tableData, ngTableParams, $scope);
						} else if (data.status == 3) {
							alertify.error("ya eliminada");
							window.history.back();
						} else {
							alertify.error("Error cargando la factura: "
									+ data.message);
						}
					};

					webServices
							.getWSResponseGet($http, 'factura', 'getFactura',
									sendId, $cookieStore, callbackFuntion);

					$scope.saveFac = function() {
						webServices
								.getWSResponsePost(
										$http,
										'factura',
										'saveFactura',
										$scope.factura,
										$cookieStore,
										function(data, status, headers, config) {
											if (data.status == 0) {
												alertify.success("La factura se actualizo correctamente");
												window.history.back();
											} else {
												alertify.error("Error guardando la factura: " + data.message);
											}
										});
					};

				});



caApp.controller('facturaFormEditFileController',
		function($scope, $location, $http, $routeParams, breadcrumbs,
						$cookieStore, webServices) {

					$scope.breadcrumbs = breadcrumbs;

					$scope.backFacAdm = function() {
						window.history.back();
					};

					var sendId = {
						'id' : $routeParams.facId
					};

					var callbackFuntion = function(data, status, headers,
							config) {
						if (data.status == 0) {
							$scope.factura = data.factura;
						} else if (data.status == 3) {
							alertify.error("ya eliminada");
							window.history.back();
						} else {
							alertify.error("Error cargando la factura: "
									+ data.message);
						}
					};

					webServices
							.getWSResponseGet($http, 'factura', 'getFactura',
									sendId, $cookieStore, callbackFuntion);

					$scope.states = getTipo();

					$scope.saveUs = function() {
						webServices
								.getWSResponsePost(
										$http,
										'factura',
										'updateFactura',
										$scope.user,
										$cookieStore,
										function(data, status, headers, config) {
											if (data.status == 0) {
												alertify.success("La factura se actualizo correctamente");
												window.history.back();
											} else {
												alertify.error("Error guardando la factura: "
														+ data.message);
											}
										});
					};

				});


caApp.controller('facturaFormEditWFController',
				function($scope, $location, $http, $routeParams, breadcrumbs,
						$cookieStore, webServices) {

					$scope.breadcrumbs = breadcrumbs;

					$scope.backFacAdm = function() {
						window.history.back();
					};
										
					$scope.saveUs = function() {
						webServices.getWSResponsePost(
										$http,
										'factura',
										'updateFactura',
										$scope.user,
										$cookieStore,
										function(data, status, headers, config) {
											if (data.status == 0) {
												alertify.success("La factura se actualiz\u00f3 correctamente");
												window.history.back();
											} else {
												alertify.error("Error guardando la factura: " + data.message);
											}
										});
					};
					
					
					// WF =======================================================
					$scope.autorizadoresSeleccionados = null;
				    $scope.$watch("autorizadoresList", function(value) {
				    	if (value) {
				    		$scope.autorizadoresSeleccionados = value; 
						}				        
				    },true);
	
				    
				    $scope.autorizadoresList = null;
					$scope.userList = null;
					webServices.getWSResponseGet($http, 'user', 'getAuthorized', null, $cookieStore
							, function(data, status, headers, config) {
								if (data.status == 0) {
									$scope.userList = data.userList;
									$scope.userList.sort(dynamicSort("name"));
									$scope.refreshWF();
									
								} else {
									alertify.error("Error al traer la lista de usuarios: " + data.message);
								}
					});

					
					$scope.userSelected;
					$scope.estadoActual;
					$scope.editarFactura = true;
					var sendId = {
						'id' : $routeParams.facId						
					};
					
					

					$scope.refreshWF = function() { 
						webServices.getWSResponseGet($http, 'workFlow', 'getNivelByFactura',					
							sendId, $cookieStore, function(data, status, headers, config) {
							if (data.status == 0) {
								//$scope.autorizadoresList = data.nivelesDTO;
								actualizarFromDB(data.nivelesDTO);
								$scope.rechazado = data.facturaEstado;
							} else {
								alertify.error("Error al traer el workflow: " + data.message);
							}
						}
					);};
						
					
					$scope.nivelDTO = getNewNivelDTO(sendId.id);
					
					function actualizarFromDB(listaActual){
						listaActual.sort(dynamicSort("orden"));
						if(listaActual[0].estadoFactura == 3 || listaActual[0].estadoFactura == 5) {
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
					};
					
					
					//Ordenar array
					function dynamicSort(property) {
					    var sortOrder = 1;
					    if(property[0] === "-") {
					        sortOrder = -1;
					        property = property.substr(1);
					    }
					    return function (a,b) {
					        var result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
					        return result * sortOrder;
					    };
					}
													
					
					$scope.agregarAutorizadorDB = function() {
						if ($scope.userSelected==null) {
							return;
						}
						$scope.nivelAux;
						if ($scope.autorizadoresList != null) {
							$scope.nivelAux = getNewNivelDTO($routeParams.facId);
							$scope.nivelAux[0].autorizador = $scope.userSelected;
							$scope.nivelAux[0].estado = $scope.estadoActual;
							$scope.nivelAux[0].publicado = $scope.publicado;
							$scope.nivelAux[0].id = $scope.idActual;
							$scope.nivelAux[0].rechazado = $scope.rechazado;
							$scope.autorizadoresList = $scope.autorizadoresList.concat($scope.nivelAux);
							
						} else {
							$scope.autorizadoresList = getNewNivelDTO($routeParams.facId);
							$scope.autorizadoresList[0].autorizador = $scope.userSelected;
							$scope.autorizadoresList[0].estado = $scope.estadoActual;
							$scope.autorizadoresList[0].publicado = $scope.publicado;
							$scope.autorizadoresList[0].rechazado= $scope.rechazado;
							$scope.autorizadoresList[0].id = $scope.idActual;
						}
						actualizarUserListDB();
					};
								
					
					$scope.agregarAutorizador = function() {
						if ($scope.userSelected==null) {
							return;
						}
						$scope.nivelAux;
						if ($scope.autorizadoresList != null) {
							$scope.nivelAux = getNewNivelDTO($routeParams.facId);
							$scope.nivelAux[0].autorizador = JSON.parse($scope.userSelected);
							$scope.nivelAux[0].estado = 1;
							$scope.nivelAux[0].publicado = false;
							$scope.autorizadoresList = $scope.autorizadoresList.concat($scope.nivelAux);
							
						} else {
							$scope.autorizadoresList = getNewNivelDTO($routeParams.facId);
							$scope.autorizadoresList[0].autorizador = JSON.parse($scope.userSelected);
							$scope.autorizadoresList[0].estado = 1;
							$scope.autorizadoresList[0].publicado = false;
							
						}
						actualizarUserList();
					};
					
					
					
					function actualizarUserList() {
						//se quita del combo el usuario agregado a la lista
						for (var int = 0; int < $scope.userList.length; int++) {
							if ($scope.userList[int].id == JSON.parse($scope.userSelected).id) {
								$scope.userList.splice(int, 1);
								break;
							}
						}
						$scope.userSelected=null;						
					};
					
					
					function actualizarUserListDB() {
						//se quita del combo el usuario agregado a la lista
						for (var int = 0; int < $scope.userList.length; int++) {
							if ($scope.userList[int].id == $scope.userSelected.id) {
								$scope.userList.splice(int, 1);
								break;
							}
						}
						$scope.userSelected=null;
						$scope.estadoActual=null;						
					};
					
					$scope.quitarAutorizador = function(item) {			
						//se quita de la lista el autorizador
						for (var int = 0; int < $scope.autorizadoresSeleccionados.length; int++) {
							if ($scope.autorizadoresSeleccionados[int].autorizador.id == item.autorizador.id) {
								$scope.autorizadoresSeleccionados.splice(int, 1);
							}
						}
						//se agrega el autorizador al combo
						$scope.userList.push(item.autorizador);						
						$scope.userList.sort(dynamicSort("name"));
						$scope.userSelected=null;
					};
															
					$scope.saveWF = function() {	
						//actualiza orden y factura acutal
						if ($scope.autorizadoresSeleccionados.length < 1) {							
							alertify.error("Debe seleccionar al menos un autorizador.");
							return;
						}
						
						for (var int = 0; int < $scope.autorizadoresSeleccionados.length; int++) {							
							$scope.autorizadoresSeleccionados[int].orden = int + 1;				
						}
																		
						//guarda el wf
						webServices.getWSResponsePost($http, 'workFlow', 'saveNivel', $scope.autorizadoresSeleccionados,
							$cookieStore, function(data, status, headers, config) {							
								if (data.status == 0) {
									alertify.inform("Se guard\u00f3 el Workflow, pero aun no se ha enviado a autorizar. Para enviar a autorizar click en 'Guardar y Publicar'");
									window.history.back();
								} else if (data.status == 6){
									alertify.error("Se guard\u00f3 el Workflow, pero fall\u00f3 el env\u00edo de mail. No se pudo informar al autorizador. " + data.message);
									window.history.back();
								} else {
									alertify.error("Error guardando Workflow: " + data.message);
								}
						});
					};
					
					$scope.saveAndPublishWF = function() {
						if ($scope.autorizadoresSeleccionados.length < 1) {							
							alertify.error("Debe seleccionar al menos un autorizador.");
							return;
						}
						
						for (var int = 0; int < $scope.autorizadoresSeleccionados.length; int++) {							
							$scope.autorizadoresSeleccionados[int].orden = int + 1;
						}
						//guarda y publica el wf
						webServices.getWSResponsePost($http, 'workFlow', 'saveAndPublishNivel', $scope.autorizadoresSeleccionados,
							$cookieStore, function(data, status, headers, config) {							
								if (data.status == 0) {
									alertify.success("Se guard\u00f3 el Workflow, y se ha enviado a autorizar.");
									window.history.back();
								} else if (data.status == 6){
									alertify.error("Se guard\u00f3 el Workflow, pero fall\u00f3 el env\u00edo de mail. No se pudo informar al autorizador. " + data.message);
									window.history.back();
								} else {
									alertify.error("Error guardando Workflow: " + data.message);
								}
						});
					};

				});


caApp.controller('facturaConsultAdminController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, FiltroFactura) {

	FiltroFactura.data = null;
	
	$scope.breadcrumbs = breadcrumbs;
	$scope.isShowFilter = false;
	$scope.go = function(path) {		
		$location.path(path);
	};

	$scope.showFilter = function(state) {
		$scope.isShowFilter = state;
	};

	$scope.goURL = function(path, $param) {
		FiltroFactura.dataConsult = $scope.filter;
		$location.path(path + "/" + $param);
	};
	
	$scope.goEdit = function(path, $param) {
		FiltroFactura.dataConsult = $scope.filter;
		$location.path(path + "/" + $param);
	};
		
	$scope.loadFilter = function() {
		$scope.filter.page = 1;
		$scope.loadFilterFields();
		$scope.loadFacturas();
	};
	
	$scope.clearFilter = function() {
		$scope.filter = clearFilterFactura($scope.filter.page, $scope.filter.totalPage);
		$scope.extFilter = clearFilterFactura($scope.filter.page, $scope.filter.totalPage);
		FiltroFactura.dataConsult = $scope.filter;
	};

	$scope.loadFilterFields = function() {
		$scope.filter.numero = $scope.extFilter.numero;
		$scope.filter.tipo = $scope.extFilter.tipo;
		$scope.filter.cuit = $scope.extFilter.cuit;
		$scope.filter.razon = $scope.extFilter.razon;
		$scope.filter.cc = $scope.extFilter.cc;
		$scope.filter.centroc = $scope.extFilter.centroc;
		$scope.filter.wf = $scope.extFilter.wf;
		$scope.filter.fvDesde = $scope.extFilter.fvDesde;
		$scope.filter.fiDesde = $scope.extFilter.fiDesde;
		$scope.filter.flfDesde = $scope.extFilter.flfDesde;
		$scope.filter.fvHasta = $scope.extFilter.fvHasta;
		$scope.filter.fiHasta = $scope.extFilter.fiHasta;
		$scope.filter.flfHasta = $scope.extFilter.flfHasta;
		$scope.filter.estado = $scope.extFilter.estado;

	};

	$scope.rollbackFilterFields = function() {
		$scope.extFilter.cuit = $scope.filter.cuit;
		$scope.extFilter.razon = $scope.filter.razon;
		$scope.extFilter.numero = $scope.filter.numero;
		$scope.extFilter.cc = $scope.filter.cc;
		$scope.extFilter.centroc = $scope.filter.centroc;
		$scope.extFilter.wf = $scope.filter.wf;
		$scope.extFilter.fvDesde = $scope.filter.fvDesde;
		$scope.extFilter.fiDesde = $scope.filter.fiDesde;
		$scope.extFilter.flfDesde = $scope.filter.flfDesde;
		$scope.extFilter.fvHasta = $scope.filter.fvHasta;
		$scope.extFilter.fiHasta = $scope.filter.fiHasta;
		$scope.extFilter.flfHasta = $scope.filter.flfHasta;
		$scope.extFilter.tipo = $scope.filter.tipo;
		$scope.extFilter.estado = $scope.filter.estado;
	};
	
	
	$scope.loadFacturas = function() {
		if (FiltroFactura.dataConsult != null) {
			$scope.extFilter = FiltroFactura.dataConsult;
			$scope.loadFilterFields();
		}
		
		var callbackFuntion = function(data, status, headers, config) {		
			$scope.facturas = data.content;
			if (null != $scope.facturas && $scope.facturas.length > 0) {
				$scope.total = $scope.facturas[0].importeTotal;
			}			
			$scope.filter.totalPages = data.totalPages;
						
			var fromCount = (data.number * data.size) + 1;
			var toCount = (data.number * data.size) + data.numberOfElements;
			var totalCount = data.totalElements;
			$scope.count = (data.numberOfElements > 0 ? fromCount : 0) + ' - '
					+ toCount + ' de ' + totalCount;
			$scope.setRangePages();
		};
		webServices.getWSResponseGet($http, 'facturaConsult', 'facturaPaginatedList',
				$scope.filter, $cookieStore, callbackFuntion);
	};

	$scope.prevPage = function() {
		$scope.rollbackFilterFields();
		if ($scope.filter.page > 1) {
			$scope.filter.page--;
			$scope.loadFacturas();
		}
	};

	$scope.goPage = function($number) {
		$scope.filter.page = $number;
		$scope.rollbackFilterFields();
		$scope.loadFacturas();
	};

	$scope.nextPage = function() {
		$scope.rollbackFilterFields();
		if ($scope.filter.page < $scope.filter.totalPages) {
			$scope.filter.page++;
			$scope.loadFacturas();
		}
	};

	$scope.firstPage = function() {
		$scope.rollbackFilterFields();
		$scope.filter.page = 1;
		$scope.loadFacturas();
	};

	$scope.lastPage = function() {
		$scope.rollbackFilterFields();
		$scope.filter.page = $scope.filter.totalPages;
		$scope.loadFacturas();
	};

	$scope.setRangePages = function() {
		$scope.filter.range = [];
		for (var i = 1; i <= $scope.filter.totalPages; i++) {
			$scope.filter.range.push(i);
		}
	};
	
	$scope.filter = getFacturaAdmFilterForm();
	$scope.extFilter = getFacturaAdmFilter();

	$scope.tipos = getTipos();
	$scope.wfOptions = getWorkflowOptions();
	$scope.estadosFactura = getEstadosFactura();

	$('#fvDateCalendarDesde').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fvDateCalendarDesde'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fvDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#fiDateCalendarDesde').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fiDateCalendarDesde'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fiDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#flfDateCalendarDesde').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#flfDateCalendarDesde'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.flfDesde = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});
	$('#fvDateCalendarHasta').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fvDateCalendarHasta'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fvHasta = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#fiDateCalendarHasta').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#fiDateCalendarHasta'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.fiHasta = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$('#flfDateCalendarHasta').datepicker({
		format : getStringFormatDate()
	}).on('changeDate', function(ev) {
		var element = angular.element($('#flfDateCalendarHasta'));
		var controller = element.controller();
		var scope = element.scope();
		scope.$apply(function() {
			scope.extFilter.flfHasta = getFormattedDate(ev.date);
		});
		$(this).datepicker('hide');
	});

	$scope.loadFilter();
});


caApp.controller('facturaFormVerController',
	function($scope, $location, $http, $routeParams, breadcrumbs,
				$cookieStore, webServices, ngTableParams, tableFactory) {

			$scope.breadcrumbs = breadcrumbs;
			
			$scope.factura = newFactura();

			$scope.backFacAdm = function() {
				window.history.back();
			};

			$('#dueDateCalendar').datepicker({
					format : getStringFormatDate()
				}).on('changeDate',
							function(ev) {
								var element = angular.element($('#dueDateCalendar'));
								var controller = element.controller();
								var scope = element.scope();
								scope.$apply(function() {
										scope.factura.vencimiento = ev.date;
								});
								$(this).datepicker('hide');
				});

			$scope.tipos = getTipos();
			$scope.wfOptions = getWorkflowOptions();
			$scope.estadosFactura = getEstadosFactura();

			var sendId = {
				'id' : $routeParams.facId
			};

			var callbackFuntion = function(data, status, headers, config) {
				if (data.status == 0) {
					$scope.factura = data.factura;
					$scope.factura.canSave = false;
					if(data.factura.vencimiento!=null){
						$scope.factura.vencimiento = new Date(data.factura.vencimiento);
						$('#dueDateCalendar').val(getFormattedDate($scope.factura.vencimiento));						
					}
					var tableData = [];
					tableData = data.factura.asientos;
					$scope.tableAsientos = tableFactory.getTableData(
							tableData, ngTableParams, $scope);
				} else if (data.status == 3) {
					alertify.error("ya eliminada");
					window.history.back();
				} else {
					alertify.error("Error cargando la factura: "
							+ data.message);
				}
			};
						
			webServices.getWSResponseGet($http, 'factura', 'getFactura',
					sendId, $cookieStore, callbackFuntion);

});



caApp.controller('fileConsultController', function($scope, $location,
		$http, ngTableParams, breadcrumbs, $cookieStore, webServices,
		$routeParams, tableFactory, $filter, download) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.backFileAdm = function() {
		window.history.back();
	};

	var tableData = [];
	var sendId = {
		'id' : $routeParams.facId
	};
	webServices.getWSResponseGet($http, 'archivoFactura',
			'getAllArchivoFactura', sendId, $cookieStore, function(data,
					status, headers, config) {
				if (data.status == 0) {
					tableData = data.headerDTO.listArchivoHeader;
					$scope.tableFile = tableFactory.getTableFilterData(
							tableData, ngTableParams, $filter, $scope.file,
							$scope);
				}
			});

	$scope.download = function($file) {
		var sendId = {
			'id' : $file.id
		};
		webServices.getWSResponseGet($http, 'archivo', 'downloadArchivo',
				sendId, $cookieStore, function(data, status, headers, config) {
					if (data.status == 0) {
						download.downloadFile(data.dto.content, $file.name,
								$file.type);
					}
				});
	};

});


caApp.controller('consultWFController', function($scope, $location, $http,
		breadcrumbs, $cookieStore, $routeParams, $filter, webServices,
		tableFactory, ngTableParams, printFactory) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.backAutAdm = function() {
		window.history.back();
	};

	var sendData = {
		'idFactura' : $routeParams.facId,
		'idUsuario' : $cookieStore.get('vatesuser').userId
	};
	

	var tableData = [];
	$scope.states = getHistoryState();
	var callbackFuntion = function(data, status, headers, config) {
		if (data.status == 0) {
			$scope.facHistorial = data.dto;
			for (var i = 0; i < data.dto.historials.length; i++) {
				for (var j = 0; j < $scope.states.length; j++) {
					if (data.dto.historials[i].estado == $scope.states[j].id) {
						data.dto.historials[i].estado = $scope.states[j].name;
					}
				}
			}
			tableData = data.dto.historials;
			tableDataAsientos = data.dto.asientos;
			$scope.tableHistorial = tableFactory.getTableData(tableData,
					ngTableParams, $scope);
			$scope.tableAsientos = tableFactory.getTableData(tableDataAsientos,
					ngTableParams, $scope);
		} else {
			alertify.error("No se pudo cargar el detalle de Workflow");
		}
	};

	webServices.getWSResponseGet($http, 'historial', 'getHistorialByFinalUser',
			sendData, $cookieStore, callbackFuntion);
	
	
	$scope.imprimir = function() {
		$scope.dataView = '';
//		$scope.pdfView = getPdfView();

		var dataHistorials = $scope.facHistorial.historials;
		var dataAsientos = $scope.facHistorial.asientos;
		
		var title = '<h2 style="text-align: center; padding-top: 3%;"><u>Detalle de la Factura</u></h2>';
		var tableFac = '<h3 style="text-align: left; padding-top: 3%;"><u>Datos Factura</u></h3>'
		+'<div>'
		+'<b>Cuit: </b>' + $scope.facHistorial.cuit
		+'<HR>'
		+'<b>Importe: </b>$' + $scope.facHistorial.importeNeto
		+'<HR>'
		+'<b>Razon Social: </b>' + $scope.facHistorial.razonSocial
		+'<HR>'
		+'<b>Fecha Vto: </b>' + $scope.facHistorial.vencimiento
		+'<HR>'
		+'<b>Nro Factura: </b>' + $scope.facHistorial.nroFactura
		+'<HR>'
		+'<b>Descripcion: </b>' + $scope.facHistorial.descripcion;
		+'</div>';
		
		var datosCentros = '';
		for(var i=0; i < dataAsientos.length; i++) {
			datosCentros = datosCentros 
			+'<tr>'
			+'<td>' + dataAsientos[i].nroCentroCostos + '</td>'
			+'<td>' + dataAsientos[i].descCuenta + '</td>'
			+'<td>' + dataAsientos[i].descControl + '</td>'
			+'<td>$' + dataAsientos[i].importeNeto + '</td>'
			+'</tr>';
		}
		
		var tableCentros = '<h3 style="text-align: left; padding-top: 3%;"><u>Centros</u></h3>'
		+'<table class="table">'
		+'<tr>'
		+'<th>Numero</th>'
		+'<th>Cuenta</th>'
		+'<th>Centro</th>'
		+'<th>Importe</th>'
		+'</tr>'
		+ datosCentros
		+'</table>';
		
		var datosHistorial = '';
		for(var i=0; i < dataHistorials.length; i++) {
			datosHistorial = datosHistorial +'<tr>'
			+'<td>' + dataHistorials[i].nivel + '</td>'
			+'<td>' + dataHistorials[i].autoriza + '</td>'
			+'<td>' + dataHistorials[i].estado + '</td>'
			+'<td>' + dataHistorials[i].fecha + '</td>'	
			+'<td>' + (dataHistorials[i].descripcion == null ? "" : dataHistorials[i].descripcion) + '</td>'
			+'</tr>';
		}		
		
		var tableHistorial = '<h3 style="text-align: left; padding-top: 3%;"><u>Workflow</u></h3>'
		+'<table class="table">'
		+'<tr>'
		+'<th>Nivel</th>'
		+'<th>Autoriza</th>'
		+'<th>Estado</th>'
		+'<th>Fecha y Hora</th>'
		+'<th>Descripcion</th>'
		+'</tr>'
		+ datosHistorial
		+'</table>';
			
		$scope.dataView = title + tableFac + tableCentros + tableHistorial;
		
		printFactory.printVatesReport($scope.dataView, 'p', 'letter', 'protected/template/templatePDF.html');
		
};
	
});
