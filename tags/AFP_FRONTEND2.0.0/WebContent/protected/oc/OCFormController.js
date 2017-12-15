caApp.controller('oCFormController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		$filter, normalizeString, download, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.ocShowAdd = true;

	$scope.oc = newOC();

	$scope.editNro = true;

	$scope.inactive = false;
	$scope.edit = true;
	$scope.modalDetail = false;
	$scope.modalProject = false;
	$scope.modalFile = false;
	$scope.showDetalle = false;
	$scope.showClientes = false;
	$scope.showFile = false;
	$scope.showProject = false;
	$scope.showTableSelectProject = false;
	$scope.fileOc = false;
	$scope.guardarOc = true;
	$scope.editTipo = true;

	$scope.siNo = getActivo();
	$scope.typesFile = getTypeFileOC();

	webServices.getWSResponseGet($http, 'orden', 'getDatoInicial', null,
			$cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
					$scope.states = data.datoInicialDTO.estados;
					$scope.coins = data.datoInicialDTO.monedaList;
					$scope.typesOC = data.datoInicialDTO.tipoList;
					$scope.units = data.datoInicialDTO.unidadList;
					$scope.clientes = data.datoInicialDTO.clientes;
					$scope.sizeMax = data.datoInicialDTO.sizeMax;
					$scope.showClientes = true;
				} else {
					alertify.error("Error al traer los datos iniciales: "
							+ data.message);
				}
			});

	$scope.oldType = '';
	$scope.typeChanged = function() {

		if ($scope.oc.tipo == 2) {
			$scope.oldType = $scope.oc.numero;
			$scope.editNro = false;
			$scope.oc.numero = '';
		} else {
			$scope.editNro = true;
			$scope.oc.numero = $scope.oldType != '' ? $scope.oldType
					: $scope.oc.numero;
		}
	};

	$scope.getContacto = function (){
		$scope.oc.contacto = utilidades.getValueFromList($scope.clientes, $scope.oc.clienteId, true);
	};
	
	$scope.calculateTotal = function(price, cant) {
		if (price > 0 && cant > 0 && cant % 1 == 0) {
			$scope.item.total = price * cant;
		} else {
			$scope.item.total = 0;
		}
	};

	$scope.goNewDetail = function() {
		$scope.editarDetalle = false;
		$scope.item = newItem();
		$scope.calculateTotal($scope.item.precio, $scope.item.cantidad);
		$scope.modalDetail = true;
	};

	$scope.goEditDetail = function($detalle, $index) {
		$scope.editarDetalle = true;
		$scope.oldIndexItem = $index;
		$scope.item = newItem();
		$scope.item = angular.copy($detalle);
		$scope.modalDetail = true;
	};

	$scope.closeDetail = function() {
		$scope.modalDetail = false;
	};

	$scope.goDeleteDetail = function($total, $index) {
		$scope.oc.total -= $total;
		$scope.oc.items.splice($index, 1);
		if ($scope.oc.items.length == 0) {
			$scope.showDetalle = false;
		}
	};

	$scope.addItem = function() {
		$scope.item.extendido = false;
		if ($scope.editarDetalle) {
			$scope.oc.total -= $scope.oc.items[$scope.oldIndexItem].total;
			$scope.oc.items.splice($scope.oldIndexItem, 1, $scope.item);
		} else {
			$scope.oc.items.push($scope.item);
		}
		$scope.oc.total += $scope.item.total;
		$scope.showDetalle = true;
		$scope.modalDetail = false;
	};

	$scope.getUnidadNombre = function(id) {
		return utilidades.getValueFromList($scope.units, id, false);
	};

	$scope.getExtendida = function(value) {
		return utilidades.getExpandidoFromList(value);
	};

	$scope.goNewProject= function() {
		$scope.projectFilter = getOCFilterProject();
		$scope.proyectoMessage = false;
		$scope.showTableSelectProject = false;
		$scope.modalProject = true;
	};
	
	$scope.projectFilterSearch = function() {
		$scope.proyectoMessage = false;
		$scope.showTableSelectProject = false;
		var callbackFuntion = function(data, status, headers, config) {
			$scope.projects = data.proyectosDTO;
			if($scope.oc.proyectos.length > 0){
				for (var j = 0; j < $scope.oc.proyectos.length; j++) {
					if ($scope.oc.proyectos[j].seleccionado) {
						for (var i = 0; i < $scope.projects.length; i++) {
							if($scope.oc.proyectos[j].id == $scope.projects[i].id){
								$scope.projects[i].seleccionado = true;
							}
						}
					}
				}
			}
			$scope.tableSelectProject = tableFactory.getTableDataModal(
					$scope.projects, ngTableParams, $scope, 4);	
			if ($scope.projects.length == 0) {
				$scope.showTableSelectProject = false;
				$scope.proyectoMessage = true;
				$scope.warningProyectoMessage = 'No se encontraron '
						+ 'proyectos.';
			}
			$('#nameProyId').focus();
			$scope.showTableSelectProject = true;
		};
		webServices.getWSResponseGet($http, 'orden', 'getFiltroProyecto',
				$scope.projectFilter, $cookieStore, callbackFuntion);
	};

	$scope.addProject = function() {
		if($scope.projects !== undefined){
			for (var i = 0; i < $scope.projects.length; i++) {
				if ($scope.projects[i].seleccionado) {
					if ($scope.oc.proyectos.length == 0) {
						$scope.newProject = newProject();
						$scope.newProject = $scope.projects[i];
						$scope.newProject.extendido = false;
						$scope.oc.proyectos.push($scope.newProject);
					} else {
						for (var j = 0; j < $scope.oc.proyectos.length; j++) {
							if ($scope.projects[i].id == $scope.oc.proyectos[j].id) {
								$scope.agregarProyecto = false;
								break;
							} else {
								$scope.agregarProyecto = true;
							}
						}
						if($scope.agregarProyecto){
							$scope.newProject = newProject();
							$scope.newProject = $scope.projects[i];
							$scope.newProject.extendido = false;
							$scope.oc.proyectos.push($scope.newProject);
						}
					}
				} 
			}
			for (var i = 0; i < $scope.projects.length; i++) {
				if (!$scope.projects[i].seleccionado) {
					if ($scope.oc.proyectos.length != 0) {
						for (var j = 0; j < $scope.oc.proyectos.length; j++) {
							if ($scope.projects[i].id == $scope.oc.proyectos[j].id) {
								$scope.oc.proyectos.splice(j, 1);
								break;
							}
						}
					}
				}
			}
			if ($scope.oc.proyectos.length < 1) {
				$scope.showProject = false;
			} else {
				$scope.showProject = true;
			}
		}
		$scope.modalProject = false;
	};
	
	$scope.goDeleteProject = function ($index, $projectId) {
		for (var i = 0; i < $scope.projects.length; i++) {
			if($scope.projects[i].id==$projectId){
				$scope.projects[i].seleccionado = false;
			}
		}
		$scope.oc.proyectos.splice($index, 1);
		if ($scope.oc.proyectos.length < 1) {
			$scope.showProject = false;
		}
	};

	$scope.closeProject = function() {
		$scope.modalProject = false;
	};

	$scope.goNewFile = function() {
		$scope.modalFile = true;
	};

	$scope.idTmp = Math.random().toString(36).substring(7);
	$scope.oc.idTmp = $scope.idTmp;
	

	$scope.addFile = function() {
		if ($scope.typeFile1 == 2
				&& $scope.file.type != "application/pdf") {
			alertify.error("Solo se pueden subir archivos PDF "
					+ "para el tipo OC");
		} else if ($scope.file.size > $scope.sizeMax) {
			alertify.error("El archivo seleccionado es mayor a " 
					+ $scope.sizeMax / (1024 * 1024) + " MB");
		} else if ($scope.fileOc && $scope.typeFile1 == 2) {
			alertify.inform("Se puede cargar un "
					+ "solo archivo de tipo OC");
		} else {
			$scope.archivo = newFile();
			var fileName = normalizeString
					.stringReplace($scope.file.name);
			var callBackFunction = function(data, status,
					headers, config) {
				if (data.status == 0) {
					alertify.success("El archivo se cargo con exito");
					$scope.archivo = data.archivoOrdenDTO;
					if($scope.archivo.type == 2){
						$scope.fileOc = true;
					}
					$scope.archivo.canDelete = true;
					$scope.oc.archivos.push($scope.archivo);
					$scope.typeFile1 = null;
					$scope.showFile = true;
				} else if (data.status == 3) {
					alertify.inform("El archivo ya existe");
				} else {
					alertify.error("Error cargando el archivo");
				}
				$scope.modalFile = false;
			};
			webServices.getWSResponsePostFile($http, 'archivoOrden', 'uploadFile', 
					$scope.file, fileName, $scope.typeFile1, -1, false,
					$cookieStore, callBackFunction, $scope.idTmp);
			}
		};
		
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

	$scope.goDeleteFile = function($index, $file) {
		if($file.type == 2){
			$scope.fileOc = false;
		}
		$scope.oc.archivos.splice($index, 1);
		if ($scope.oc.archivos.length == 0) {
			$scope.showFile = false;
		}
	};

	$scope.closeFile = function() {
		$scope.modalFile = false;
	};

	$scope.getTipoArchivo = function(id) {
		return utilidades.getValueFromList($scope.typesFile, id, false);
	};

	$scope.backAdm = function() {
		window.history.back();
	};

	$scope.saveOC = function() {
		if($scope.oc.clienteId == ''){
			alertify.error("Se debe seleccioar un cliente valido.");
			$('#clienteId').focus();
		} else if ($scope.oc.items.length == 0){
			alertify.error("Se debe cargar al menos un item.");
		} else if ($scope.oc.proyectos.length == 0) {
			alertify.error("Se debe cargar al menos un proyecto.");
		} else if (($scope.oc.tipo == 1 && !utilidades.validarArchivoOc($scope.oc.archivos))){
			alertify.error("Se debe cargar al menos " 
					+ "un archivo de tipo OC.");
		} else {
			var callBackFuntion = function(data,
					status, headers, config) {
				if (data.status == 0) {
					alertify.success("La Orden de Compra numero: <b>" 
							+ data.numeroInterna
						+ "</b> se genero correctamente");
					window.history.back();
				} else if (data.status == 3) {
					alertify.inform("La orden de compra <b>" 
							+ $scope.oc.numero + "</b> ya existe");
					('#nroId').focus();
				} else {
					alertify.error("Error generando la "
						+ "orden de compra: " + data.message);
				}
			};
			webServices.getWSResponsePost($http, 'orden', 'saveOrden',
					$scope.oc, $cookieStore, callBackFuntion);
		}
	};

	$scope.afterShow = function() {
		$('#idTypeFile1').focus();
		$('#nameId').focus();
		$('#cantId').focus();
	};

});

caApp.controller('oCEditController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		$routeParams, normalizeString, download, utilidades) {

	$scope.breadcrumbs = breadcrumbs;
	$scope.oc = newOC();

	$scope.inactive = false;
	$scope.ocShowAdd = true;
	$scope.showClientes = false;
	$scope.edit = true;
	$scope.modalDetail = false;
	$scope.modalProject = false;
	$scope.modalFile = false;
	$scope.estadoOc;
	$scope.fileOc = false;
	$scope.guardarOc = true;
	$scope.editTipo = false;

	var sendId = {
		id : $routeParams.ocId
	};

	$scope.typesFile = getTypeFileOC();

	webServices.getWSResponseGet($http, 'orden', 'getOrdenById', sendId,
			$cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
					$scope.oc = data.dto;
					$scope.states = data.datoInicialDTO.estados;
					$scope.typesOC = data.datoInicialDTO.tipoList;
					$scope.coins = data.datoInicialDTO.monedaList;
					$scope.units = data.datoInicialDTO.unidadList;
					$scope.clientes = data.datoInicialDTO.clientes;
					$scope.oc.archivos = data.archivosList;
					$scope.sizeMax = data.datoInicialDTO.sizeMax;
					$scope.showClientes = true;
					$scope.findClientName($scope.oc.clienteId);					
					if ($scope.oc.archivos.length > 0) {
						$scope.showFile = true;
					}
					$scope.showDetalle = true;
					$scope.showProject = true;
					if($scope.oc.activa == 1){
						if ($scope.oc.estado == 1) {
							$scope.inactive = false;
							$scope.ocShowAdd = false;
						} else {
							$scope.inactive = true;
							$scope.ocShowAdd = true;
						}
					} else {
						$scope.inactive = true;
						$scope.ocShowAdd = false;
					}
					$scope.estadoOc = $scope.oc.estado;								
				} else {
					alertify.error("Error al traer los datos iniciales: "
							+ data.message);
				}
			});

	$scope.findClientName = function(id) {
		$scope.clienteNombre = utilidades.getValueFromList($scope.clientes, id, false);
	};

	$scope.getExtendida = function(value) {
		return utilidades.getExpandidoFromList(value);
	};

	$scope.getTipoArchivo = function(id) {
		return utilidades.getValueFromList($scope.typesFile, id, false);
	};
	
	$scope.getContacto = function() {
		$scope.oc.contacto = utilidades.getValueFromList(
				$scope.clientes, $scope.oc.clienteId, true);
	};

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

	$scope.siNo = getActivo();

	$scope.goNewDetail = function() {
		$scope.editarDetalle = false;
		$scope.item = newItem();
		$scope.calculateTotal($scope.item.precio, $scope.item.cantidad);
		$scope.modalDetail = true;
	};

	$scope.goEditDetail = function($detalle, $index) {
		$scope.editarDetalle = true;
		$scope.oldIndexItem = $index;
		$scope.item = newItem();
		$scope.item = angular.copy($detalle);
		$scope.modalDetail = true;
	};

	$scope.closeDetail = function() {
		$scope.modalDetail = false;
	};

	$scope.goDeleteDetail = function($total, $index) {
		$scope.oc.total -= $total;
		$scope.oc.items.splice($index, 1);
		if ($scope.oc.items.length == 0) {
			$scope.showDetalle = false;
		}
	};

	$scope.addItem = function() {
		$scope.item.extendido = $scope.estadoOc == 1 ? false : true;
		if ($scope.editarDetalle) {
			$scope.oc.total -= $scope.oc.items[$scope.oldIndexItem].total;
			$scope.oc.items.splice($scope.oldIndexItem, 1, $scope.item);
		} else {
			$scope.oc.items.push($scope.item);
		}
		$scope.oc.total += $scope.item.total;
		$scope.showDetalle = true;
		$scope.modalDetail = false;
	};

	$scope.calculateTotal = function(price, cant) {
		if (price > 0 && cant > 0 && cant % 1 == 0) {
			$scope.item.total = price * cant;
		} else {
			$scope.item.total = 0;
		}
	};

	$scope.getUnidadNombre = function(id) {
		return utilidades.getValueFromList($scope.units, id, false);
	};
	
	$scope.goNewProject= function() {
		$scope.modalProject = true;
	};
	
	$scope.projectFilterSearch = function() {
		$scope.showTableSelectProject = false;
		var callbackFuntion = function(data, status, headers, config) {
			$scope.projects = data.proyectosDTO;
			if($scope.oc.proyectos.length > 0){
				for (var j = 0; j < $scope.oc.proyectos.length; j++) {
					if ($scope.oc.proyectos[j].seleccionado) {
						for (var i = 0; i < $scope.projects.length; i++) {
							if($scope.oc.proyectos[j].id == $scope.projects[i].id){
								$scope.projects[i].seleccionado = true;
								$scope.projects[i].canDelete = $scope.oc.proyectos[j].canDelete;
							}
						}
					}
				}
			}
			$scope.tableSelectProject = tableFactory.getTableDataModal(
					$scope.projects, ngTableParams, $scope, 4);
			$scope.showTableSelectProject = true;
		};
		webServices.getWSResponseGet($http, 'orden', 'getFiltroProyecto',
				$scope.projectFilter, $cookieStore, callbackFuntion);
	};

	$scope.addProject = function() {
		$scope.agregarProyecto;
		for (var i = 0; i < $scope.projects.length; i++) {
			if ($scope.projects[i].seleccionado) {
				if ($scope.oc.proyectos.length == 0) {
					$scope.newProject = newProject();
					$scope.newProject = $scope.projects[i];
					$scope.newProject.extendido = $scope.estadoOc == 1 ? false 
							: true;
					$scope.oc.proyectos.push($scope.newProject);
				} else {
					for (var j = 0; j < $scope.oc.proyectos.length; j++) {
						if ($scope.projects[i].id == $scope.oc.proyectos[j].id) {
							$scope.agregarProyecto = false;
							break;
						} else {
							$scope.agregarProyecto = true;
						}
					}
					if($scope.agregarProyecto){				
						$scope.newProject = newProject();
						$scope.newProject= $scope.projects[i];
						$scope.newProject.extendido = $scope.estadoOc == 1 ? false
								: true;
						$scope.oc.proyectos.push($scope.newProject);
					}
				}
			} 
		}
		for (var i = 0; i < $scope.projects.length; i++) {
			if (!$scope.projects[i].seleccionado) {
				if ($scope.oc.proyectos.length != 0) {
					for (var j = 0; j < $scope.oc.proyectos.length; j++) {
						if ($scope.projects[i].id == $scope.oc.proyectos[j].id) {
							$scope.oc.proyectos.splice(j, 1);
							break;
						}
					}
				}
			}
		}
		if ($scope.oc.proyectos.length < 1) {
			$scope.showProject = false;
		} else {
			$scope.showProject = true;
		}
		$scope.modalProject = false;
	};
	
	$scope.goDeleteProject = function ($index, $projectId) {
		if($scope.projects != undefined){
			//Ya se cargaron los proyectos
			for (var i = 0; i < $scope.projects.length; i++) {
				if($scope.projects[i].id==$projectId){
					$scope.projects[i].seleccionado = false;
				}
			}
		}
		$scope.oc.proyectos.splice($index, 1);
		if ($scope.oc.proyectos.length < 1) {
			$scope.showProject = false;
		}
	};

	$scope.closeProject = function() {
		$scope.modalProject = false;
	};

	$scope.goNewFile = function() {
		$scope.modalFile = true;
	};

	$scope.closeFile = function() {
		$scope.modalFile = false;
	};
	
	$scope.addFile = function() {
		if ($scope.typeFile1 == 2
				&& $scope.file.type != "application/pdf") {
			alertify.error("Solo se pueden subir archivos PDF "
					+ "para el tipo OC");
		} else if ($scope.file.size > $scope.sizeMax) {
			alertify.error("El archivo seleccionado es mayor a "
					+ $scope.sizeMax / (1024 * 1024) + " MB");
		} else if ($scope.fileOc && $scope.typeFile1 == 2) {
			alertify.inform("Se puede cargar un "
					+ "solo archivo de tipo OC");
		} else {
			$scope.archivo = newFile();
			var fileName = normalizeString
					.stringReplace($scope.file.name);
			var callBackFunction = function(data, status,
					headers, config) {
				if (data.status == 0) {
					$scope.archivo = data.archivoOrdenDTO;
					if($scope.archivo.type == 2){
						$scope.fileOc = true;
					}
					$scope.archivo.canDelete = true;
					$scope.oc.archivos.push($scope.archivo);
					$scope.typeFile1 = null;
					$scope.showFile = true;
					alertify.success("El archivo se cargo con exito");
				} else if (data.status == 3) {
					alertify.inform("El archivo ya existe");
				} else {
					alertify.error("Error cargando el archivo");
				}
				$scope.modalFile = false;
			};
			webServices.getWSResponsePostFile($http, 'archivoOrden', 
					'uploadFile', $scope.file, fileName, $scope.typeFile1,
					$scope.oc.id, true, $cookieStore, callBackFunction, null);
		}
	};
	
	$scope.goDeleteFile = function($index, $file) {
		alertify.confirm("El archivo se eliminara en "
			+ "forma permanete. Esta seguro "
			+ "de elimiar el archivo?", function(e) {
			if (e) {
				var callBackFuntion = function(data, status, headers,	config) {
					if (data.status == 0) {
						alertify.success("El archivo se elimino con exito");
						if($file.type == 2){
							$scope.fileOc = false;
						}
						$scope.oc.archivos.splice($index,1);
						if ($scope.oc.archivos.length == 0) {
							$scope.showFile = false;
						}
					} else {
						alertify.error("Error al eliminar el archivo: "
								+ data.message);
					}
				};
				webServices.getWSResponsePost($http, 'archivoOrden',
						'deleteFile', $file,$cookieStore, callBackFuntion);
			}
		});
	};
	

	$scope.saveOC = function() {
		if ($scope.oc.clienteId == '') {
			alertify.error("Se debe seleccioar "
					+ "un cliente valido.");
			$('#clienteId').focus();
		} else if ($scope.oc.items.length == 0) {
			alertify.error("Se debe cargar al menos un item.");
		} else if ($scope.oc.proyectos.length == 0) {
			alertify.error("Se debe cargar "
					+ "al menos un proyecto.");
		} else if (($scope.oc.tipo == 1 && !utilidades.validarArchivoOc($scope.oc.archivos))) {
			alertify.error("Se debe cargar al menos "
					+ "un archivo de tipo OC.");
		} else {
			var callBackFuntion = function(data, status,
					headers, config) {
				if (data.status == 0) {
					alertify.success("La orden de compra "
							+ "actualizo correctamente");
					window.history.back();
				} else if (data.status == 1) {
					alertify.confirm("La orden ha sido "
							+ "modificado por otra persona, desea "
							+ "guardarlo de todas formas?", function(e) {
						if (e) {
							$scope.forceUpdate();
						}
					});
				} else if (data.status == 3) {
					alertify.error("La orden de "
							+ "compra ya existe");
				} else {
					alertify.error("Error generando la "
							+ "orden de compra: " + data.message);
				}
			};
			webServices.getWSResponsePost($http, 'orden', 'updateOrden',
					$scope.oc, $cookieStore, callBackFuntion);
		}
	};
	
	$scope.forceUpdate = function () {
		var callBackFuntion = function(data, status,
				headers, config) {
			if (data.status == 0) {
				alertify.success("La orden de compra "
						+ "actualizo correctamente");
				window.history.back();
			} else if (data.status == 3) {
				alertify.error("La orden de "
						+ "compra ya existe");
			} else {
				alertify.error("Error generando la "
						+ "orden de compra: " + data.message);
			}
		};
		webServices.getWSResponsePost($http, 'orden', 'forceUpdateOrden',
				$scope.oc, $cookieStore, callBackFuntion);
	};

	$scope.backAdm = function() {
		window.history.back();
	};

	$scope.afterShow = function() {
		$('#idTypeFile1').focus();
		$('#nameId').focus();
		$('#cantId').focus();
	};

});

caApp.controller('oCViewController', function($scope, $location, $http,
		ngTableParams, breadcrumbs, $cookieStore, webServices, tableFactory,
		$routeParams, download, utilidades) {

	$scope.breadcrumbs = breadcrumbs;

	$scope.inactive = true;
	$scope.edit = false;
	$scope.ocShowAdd = false;
	$scope.showClientes = false;
	$scope.showFile = false;
	$scope.guardarOc = false;
	$scope.editTipo = false;

	$scope.typesFile = getTypeFileOC();

	var sendId = {
		id : $routeParams.ocId
	};

	webServices.getWSResponseGet($http, 'orden', 'getOrdenById', sendId,
			$cookieStore, function(data, status, headers, config) {
				if (data.status == 0) {
					$scope.oc = data.dto;
					$scope.states = data.datoInicialDTO.estados;
					$scope.typesOC = data.datoInicialDTO.tipoList;
					$scope.coins = data.datoInicialDTO.monedaList;
					$scope.units = data.datoInicialDTO.unidadList;
					$scope.clientes = data.datoInicialDTO.clientes;
					$scope.oc.archivos = data.archivosList;
					$scope.showClientes = true;
					$scope.showDetalle = true;
					$scope.showProject = true;
					$scope.findClientName($scope.oc.clienteId);
					if ($scope.oc.archivos.length > 0) {
						$scope.showFile = true;
					}
				} else {
					alertify.error("Error al traer los datos iniciales: "
							+ data.message);
				}
			});

	$scope.findClientName = function(id) {
		$scope.clienteNombre = utilidades.getValueFromList($scope.clientes, id,
				false);
	};

	$scope.getExtendida = function(value) {
		return utilidades.getExpandidoFromList(value);
	};

	$scope.getUnidadNombre = function(id) {
		return utilidades.getValueFromList($scope.units, id, false);
	};

	$scope.getTipoArchivo = function(id) {
		return utilidades.getValueFromList($scope.typesFile, id);
	};

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

	$scope.siNo = getActivo();

	$scope.backAdm = function() {
		window.history.back();
	};

});