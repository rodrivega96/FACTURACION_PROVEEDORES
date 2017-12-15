var caApp = angular.module('caapp', [ 'ngRoute', 'ui.bootstrap', 'ngResource',
		'ngTable', 'ng-breadcrumbs', 'ngCookies', 'angularFileUpload',
		'ngAnimate','pdf' ]);
caApp
		.config(function($routeProvider, $httpProvider) {

			$routeProvider
					.when('/', {
						templateUrl : 'protected/main.html',
						controller : 'MainController',
						label : 'Principal'

					})
					.when('/about', {
						templateUrl : 'protected/help/about.html',
						label : 'Sobre'

					})
					.when('/help-contents', {
						templateUrl : 'protected/help/help-contents.html',
						label : 'Ayuda'

					})
					.when('/login-form', {
						templateUrl : 'protected/main.html',
						controller : 'LoginController',
						label : 'Inicio de Sesion'

					})
					.when('/logout', {
						templateUrl : 'protected/main.html',
						controller : 'LogoutController',
						label : 'Principal'
					})
					// USER
					.when('/user-admin', {
						templateUrl : 'protected/user/user-admin.html',
						controller : 'userAdminController',
						label : 'Administrar Usuarios'
					})
					.when('/user-admin/user-form', {
						templateUrl : 'protected/user/user-form.html',
						controller : 'userFormController',
						label : 'Crear Usuario'
					})
					.when('/user-admin/user-form-edit/:usId', {
						templateUrl : 'protected/user/user-form.html',
						controller : 'userFormEditController',
						label : 'Modificar Usuario'
					})
					.when('/user-admin/user-assign-roles/:usId', {
						templateUrl : 'protected/user/user-assign-roles.html',
						controller : 'userAssignRolesController',
						label : 'Asignar Roles'
					})
					// FACTURA
					.when('/factura-admin', {
						templateUrl : 'protected/factura/factura-admin.html',
						controller : 'facturaAdminController',
						label : 'Gestionar Facturas'
					})
					.when('/factura-admin/factura-form', {
						templateUrl : 'protected/factura/factura-form.html',
						controller : 'facturaFormController',
						label : 'Crear Facturas'
					})
					.when('/factura-admin/factura-form-edit/:facId', {
						templateUrl : 'protected/factura/factura-form.html',
						controller : 'facturaFormEditController',
						label : 'Modificar Facturas'
					})
					.when(
							'/factura-admin/factura-file-form-edit/:facId',
							{
								templateUrl : 'protected/factura/factura-file-form.html',
								controller : 'facturaFormEditFileController',
								label : 'Editar Archivos'
							})
					.when('/factura-admin/factura-wf-form-edit/:facId', {
						templateUrl : 'protected/factura/factura-wf-form.html',
						controller : 'facturaFormEditWFController',
						label : 'Editar Workflow'
					// FACTURA CONSULTAR
					})
					.when(
							'/factura-consult-admin',
							{
								templateUrl : 'protected/factura/factura-consult-admin.html',
								controller : 'facturaConsultAdminController',
								label : 'Consultar Facturas'
							})
					.when(
							'/factura-consult-admin/factura-consult-form/:facId',
							{
								templateUrl : 'protected/factura/factura-form.html',
								controller : 'facturaFormVerController',
								label : 'Detalle Factura'
							})
					.when(
							'/factura-consult-admin/factura-consult-file/:facId',
							{
								templateUrl : 'protected/autorizacion/autorizacion-file-form.html',
								controller : 'fileConsultController',
								label : 'Detalle Archivos'
							})
					.when(
							'/factura-consult-admin/factura-consult-workflow/:facId',
							{
								templateUrl : 'protected/workflow/detalle-workflow-form.html',
								controller : 'consultWFController',
								label : 'Detalle Workflow'
							})
					// FACTURA FILE
					.when('/factura-admin/file-form/:facId', {
						templateUrl : 'protected/file/file-form.html',
						controller : 'fileAdminController',
						label : 'Administrar Archivos'
					})
					// AUTORIZACION
					.when(
							'/autorizacion-admin',
							{
								templateUrl : 'protected/autorizacion/autorizacion-admin.html',
								controller : 'autorizacionAdminController',
								label : 'Autorizaciones'

							})
					.when(
							'/autorizacion-admin/autorizacion-file-form/:facId',
							{
								templateUrl : 'protected/autorizacion/autorizacion-file-form.html',
								controller : 'autorizacionFileController',
								label : 'Adjuntos'
							})
					.when(
							'/autorizacion-admin/detalle-workflow/:facId',
							{
								templateUrl : 'protected/workflow/detalle-estados-form.html',
								controller : 'detalleEstadosController',
								label : 'Historial de Estados'
							})
					// AUTORIZACION NIVEL INFERIOR
					.when(
							'/autorizacion-nivel-inferior',
							{
								templateUrl : 'protected/autorizacion/autorizacion-nivel-inferior.html',
								controller : 'autorizacionNivelInferiorController',
								label : 'Autorizacion Niveles Inferiores'
							})

					// WORKFLOW
					.when(
							'/autorizacion-nivel-inferior/detalle-workflow/:facId',
							{
								templateUrl : 'protected/workflow/detalle-estados-form.html',
								controller : 'detalleEstadosController',
								label : 'Historial de Estados'
							})
					//ORDENES DE COMPAR
							.when(
							'/oc-admin',
							{
								templateUrl : 'protected/oc/oc-admin.html',
								controller : 'oCAdminController',
								label : 'Ordenes de Compra'
							})
							.when(
							'/oc-admin/oc-form',
							{
								templateUrl : 'protected/oc/oc-form.html',
								controller : 'oCFormController',
								label : 'Crear Ordenes de Compra'
							})
							.when(
							'/oc-admin/oc-view',
							{
								templateUrl : 'protected/oc/oc-form.html',
								controller : 'oCViewController',
								label : 'Ver Ordenes de Compra'
							})
							.when(
							'/oc-consult-admin',
							{
								templateUrl : 'protected/oc/oc-consult-admin.html',
								controller : 'oCAdminConsultController',
								label : 'Gestionar Ordenes de Compra'
							})
					//		
					.otherwise({

						redirectTo : '/'

					});

			$httpProvider.defaults.useXDomain = true;
			delete $httpProvider.defaults.headers.common['X-Requested-With'];

		});

caApp.directive('draggable', function() {
	return {
		scope : {
			enabled : '='
		},
		link : function(scope, elem, attrs) {
			var $elem = $(elem);
			$elem.draggable();

			scope.$watch('enabled', function(val) {
				$elem.draggable(val === true ? 'enable' : 'disable');
			});
		}
	};
});

caApp.directive('loading', [ '$http', function($http) {
	return {
		restrict : 'E',
		replace : true,
		link : function(scope, element, attr) {
			scope.isLoading = function() {
				return $http.pendingRequests.length > 0;
			};
			scope.$watch(scope.isLoading, function(val) {
				if (val) {
					$(element).show();
				} else {
					$(element).hide();
				}
			});
		}
	};
} ]);

caApp.factory('webServices', function($cookies) {
	return {
		getWSResponseGet : function($http, $wsClassPath, $wsMethodPath, $input,
				$cookieStore, callbackFunction) {
			var headersSend = {
				"Content-Type" : "application/json",
				"vates-session-id" : $cookieStore.get('vatesuser').token
			};
			var wsURL = new String(getWSURL($wsClassPath, $wsMethodPath));
			$http({
				url : wsURL,
				method : 'GET',
				params : $input,
				headers : headersSend
			}).success(function(data, status, headers, config) {
				callbackFunction(data, status, headers, config);
			}).error(
					function(data, status, headers, config) {
						if (status == 403) {
							$cookieStore.remove('vatesuser');
							window.location.href = "";
						} else {
							alert("Error de Conexion, "
									+ "intente nuevamente, Status: " + status
									+ wsURL);
						}

					});
		},
		getWSResponsePost : function($http, $wsClassPath, $wsMethodPath,
				$input, $cookieStore, callbackFunction) {
			var wsURL = new String(getWSURL($wsClassPath, $wsMethodPath));
			var headersSend = {
				"Content-Type" : "application/json",
				"vates-session-id" : $cookieStore.get('vatesuser').token
			};
			$http({
				url : wsURL,
				method : 'POST',
				data : $input,
				headers : headersSend
			}).success(function(data, status, headers, config) {
				callbackFunction(data, status, headers, config);
			}).error(
					function(data, status, headers, config) {
						if (status == 403) {
							$cookieStore.remove('vatesuser');
							window.location.href = "";
						} else {
							alert("Error de Conexion, "
									+ "intente nuevamente, Status: " + status
									+ wsURL);
						}
					});

		},
		getWSResponsePostFile : function($http, $wsClassPath, $wsMethodPath,
				$input, $fileName,$fileType, $idFactura, $force, $cookieStore, callbackFunction) {
			var fd = new FormData();
			fd.append("fileName", $fileName);
			fd.append("file", $input);
			fd.append("type", $input.type);
			fd.append("idFactura", $idFactura);
			fd.append("fileType", $fileType);
			fd.append("force", $force);
			var wsURL = new String(getWSURL($wsClassPath, $wsMethodPath));
			var headersSend = {
				"Content-Type" : "multipart/form-data",
				"vates-session-id" : $cookieStore.get('vatesuser').token
			};
			$http({
				url : wsURL,
				method : 'POST',
				data : fd,
				transformRequest : angular.identity,
				headers : headersSend
			}).success(function(data, status, headers, config) {
				callbackFunction(data, status, headers, config);
			}).error(
					function(data, status, headers, config) {
						if (status == 403) {
							$cookieStore.remove('vatesuser');
							window.location.href = "";
						} else {
							alert("Error de Conexion, "
									+ "intente nuevamente, Status: " + status
									+ wsURL);
						}
					});

		}

	};
});

caApp.factory('printFactory', function($cookies) {
	var widthPDF = 506;
	return {
		printVatesReport : function($data, $orintation, $sizePaper, $url) {
			// $orintation puede ser horizontal ("l") o vertical (p)
			// $sizePaper es el tamaÃ±o de la hoja A4, latter.
			var d = new Date().toISOString().slice(0, 10).replace(/-/g, "");
			$.get($url, function(data) {
				d, filename = 'report_' + d + '.pdf', pdf = new jsPDF(
						$orintation, 'pt', $sizePaper),
						specialElementHandlers = {
							'#editor' : function(element, renderer) {
								return true;
							}
						};
				var myImage = new Image(60, 60);
				myImage.src = 'img/logo_vates2.png';
				pdf.addImage(myImage, 'PNG', 65, 30, 60, 60);
				var str = data;
				var result = str.replace("content", $data);

				pdf.fromHTML(result,// HTML
				65, // x coord inicial
				50, // y coord inicial
				{
					'width' : widthPDF, // ancho del pdf
					elementHandlers : specialElementHandlers
				});
				pdf.save(filename);
			});
		}

	};
});

caApp.factory('getWorkflowTemplate', function($cookies) {
	return {
		get : function($facHistorial) {

			var dataHistorials = $facHistorial.historials;
			var dataAsientos =$facHistorial.asientos;
			
			var title = '<h2 style="text-align: center; padding-top: 3%;"><u>Detalle de la Factura</u></h2>';
			var tableFac = '<h3 style="text-align: left; padding-top: 3%;"><u>Datos Factura</u></h3>'
			+'<div>'
			+'<b>Nro Factura: </b>' + $facHistorial.nroFactura
			+'<HR>'
			+'<b>Cuit: </b>' + $facHistorial.cuit
			+'<HR>'
			+'<b>Razon Social: </b>' + $facHistorial.razonSocial
			+'<HR>'
			+'<b>Bruto: </b>$' + $facHistorial.importeNeto
			+'<HR>'
			+'<b>IVA: </b>$' + $facHistorial.iva
			+'<HR>'
			+'<b>Total: </b>$' + $facHistorial.total
			+'<HR>'
			+'<b>Fecha Factura: </b>' + $facHistorial.fechaFactura
			+'<HR>'
			+'<b>Vencimiento Workflow: </b>' + $facHistorial.vencimiento
			+'<HR>'
			+'<b>Descripcion: </b>' + $facHistorial.descripcion
			+'</div>';
			
			var tableCentros = '';
			if (dataAsientos.length > 0) {
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
				
				tableCentros = '<h3 style="text-align: left;"><u>Centros</u></h3>'
					+'<table class="table">'
					+'<tr>'
					+'<th>Numero</th>'
					+'<th>Cuenta</th>'
					+'<th>Centro</th>'
					+'<th>Importe</th>'
					+'</tr>'
					+ datosCentros
					+'</table>';
			}
			
			var tableHistorial = '';
			if (dataHistorials.length > 0) {
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
				tableHistorial = '<h3 style="text-align: left;"><u>Workflow</u></h3>'
					+'<table class="table">'
					+'<tr>'
					+'<th>Nivel</th>'
					+'<th>Autoriza</th>'
					+'<th>Estado</th>'
					+'<th>Fecha&nbsp;y Hora</th>'
					+'<th>Descripcion</th>'
					+'</tr>'
					+ datosHistorial
					+'</table>';
			}			
			return title + tableFac + tableCentros + tableHistorial;					
		}
	};
});


caApp.factory('tableFactory', function($cookies) {
	return {
		getTableFilterData : function($tableData, ngTableParams, $filter,
				$dataFilter, $scope) {
			return new ngTableParams({
				page : 1,
				count : 5,
				filter : {}
			}, {
				total : $tableData.length,
				counts : [],
				getData : function($defer, params) {
					var tData = params.$params;
					var totalPerPage = tData.page * tData.count;
					var fromCount = ((tData.page - 1) * tData.count) + 1;
					var totalCount = $tableData.length;
					var toCount = totalPerPage < totalCount ? totalPerPage
							: totalCount;
					$scope.count = ($tableData.length > 0 ? fromCount : 0)
							+ ' - ' + toCount + ' de ' + totalCount;
					var orderedData = params.filter() ? $filter('filter')(
							$tableData, params.filter()) : $tableData;
					$dataFilter = orderedData.slice((params.page() - 1)
							* params.count(), params.page() * params.count());
					params.total(orderedData.length);
					$defer.resolve($dataFilter);
				}
			});
		},

		getTableData : function($tableData, ngTableParams, $scope) {
			return new ngTableParams({
				page : 1,
				count : $tableData.length + 1
			}, {
				total : $tableData.length,
				counts : [],
				getData : function($defer, params) {
					var tData = params.$params;
					var totalPerPage = tData.page * tData.count;
					var fromCount = ((tData.page - 1) * tData.count) + 1;
					var totalCount = $tableData.length;
					var toCount = totalPerPage < totalCount ? totalPerPage
							: totalCount;
					$scope.count = ($tableData.length > 0 ? fromCount : 0)
							+ ' - ' + toCount + ' de ' + totalCount;
					$defer.resolve($tableData.slice((params.page() - 1)
							* params.count(), params.page() * params.count()));
				}
			});
		}
	};
});

caApp.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;
			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

caApp.factory('normalizeString',
		function($cookies) {
			return {
				stringReplace : function($string) {
					special = new Array('Ã¡', 'Ã ', 'Ã¤', 'Ã¢', 'Âª', 'Ã�',
							'Ã€', 'Ã‚', 'Ã„', 'Ã©', 'Ã¨', 'Ã«', 'Ãª', 'Ã‰',
							'Ãˆ', 'ÃŠ', 'Ã‹', 'Ã­', 'Ã¬', 'Ã¯', 'Ã®', 'Ã�',
							'ÃŒ', 'Ã�', 'ÃŽ', 'Ã³', 'Ã²', 'Ã¶', 'Ã´', 'Ã“',
							'Ã’', 'Ã–', 'Ã”', 'Ãº', 'Ã¹', 'Ã¼', 'Ã»', 'Ãš',
							'Ã™', 'Ã›', 'Ãœ', 'Ã±', 'Ã‘', ' ', 'Â´', ':', ',',
							';', '-');
					normal = new Array('a', 'a', 'a', 'a', 'a', 'A', 'A', 'A',
							'A', 'e', 'e', 'e', 'e', 'E', 'E', 'E', 'E', 'i',
							'i', 'i', 'i', 'I', 'I', 'I', 'I', 'o', 'o', 'o',
							'o', 'O', 'O', 'O', 'O', 'u', 'u', 'u', 'u', 'U',
							'U', 'U', 'U', 'n', 'N', '_', '_', '_', '_', '_',
							'_');
					deleteSpecial = new Array("\\", "Â¨", "Âº", "~", "#", "@",
							"|", "!", "\"", "Â·", "$", "%", "&", "/", "(", ")",
							"?", "'", "Â¡", "Â¿", "[", "^", "`", "]", "+", "}",
							"{", "Â¨", ">", "< ", "Ã§", "Ã‡");
					i = 0;
					while (i < special.length) {
						$string = $string.split(special[i]).join(normal[i]);
						if (i < deleteSpecial.length) {
							$string = $string.split(deleteSpecial[i]).join("");
						}
						i++;
					}
					return $string;
				},

				stringReplaceCaracter : function($string) {
					deleteSpecial = new Array("\\", "¨", "º", "-", "~", "#",
							"@", "|", "!", "\"", "•", "$", "%", "&", "/", "(",
							")", "?", "'", "¡", "¿", "[", "^", "`", "]", "+",
							"}", "{", "¨", ">", "< ", "ç", "Ç", " ", ":", ";",
							".", "*");
					i = 0;
					while (i < deleteSpecial.length) {
						$string = $string.split(deleteSpecial[i]).join("");
						i++;
					}
					return $string;
				}
			};
		});

caApp.factory('download', function($cookies) {
	return {
		// strData byte array
		downloadFile : function(strData, strFileName, strMimeType) {
			var D = document, A = arguments;
			var a = D.createElement("a"), n = A[1];
			var newdata = "data:" + strMimeType + ";base64," + escape(strData);
			a.href = newdata;
			if ('download' in a) {
				a.setAttribute("download", n);
				a.innerHTML = "downloading...";
				D.body.appendChild(a);
				setTimeout(function() {
					var e = D.createEvent("MouseEvents");
					e.initMouseEvent("click", true, false, window, 0, 0, 0, 0,
							0, false, false, false, false, 0, null);
					a.dispatchEvent(e);
					D.body.removeChild(a);
				}, 66);
				return true;
			}
		}
	};
});

caApp.factory('userProperties', function($cookies) {
	var user = {
		userName : '',
		token : '',
		showWait : false,
		isLoged : false,
		userId : 0,
		userName : '',
		menu : []
	};
	return {
		getUser : function() {
			return user;
		},
		reset : function(nUser) {
			user = {
				userName : '',
				token : '',
				showWait : false,
				isLoged : false,
				userId : 0,
				userName : '',
				menu : []
			};
		}
	};
});

caApp.directive('dndList', function() {
	return function(scope, element, attrs) {
		// variables used for dnd
		var toUpdate;
		var startIndex = -1;

		// watch the model, so we always know what element
		// is at a specific position
		scope.$watch(attrs.dndList, function(value) {
			toUpdate = value;
		}, true);

		// use jquery to make the element sortable (dnd). This is called
		// when the element is rendered
		$(element[0]).sortable({
			items : 'li',
			start : function(event, ui) {
				// on start we define where the item is dragged from
				startIndex = ($(ui.item).index());
			},
			stop : function(event, ui) {
				// on stop we determine the new index of the
				// item and store it there
				var newIndex = ($(ui.item).index());
				var toMove = toUpdate[startIndex];
				toUpdate.splice(startIndex, 1);
				toUpdate.splice(newIndex, 0, toMove);

				// we move items in the array, if we want
				// to trigger an update in angular use $apply()
				// since we're outside angulars lifecycle
				scope.$apply(scope.model);
			},
			axis : 'y',
			revert : 'invalid', // makes the item to return if it isn't placed
								// into droppable
			revertDuration : 1000, // duration while the item returns to its
									// place
			opacity : 0.75
		});
	};
});

caApp.factory('FiltroFactura', function() {
	return {
		data : [],
		dataConsult : [], 
		page : null
	};
});

caApp.factory('IndexFactura', function() {
	return {
		index : null,
		facId : null,
		facAdmId : null,
		tableRowExpanded : '',
		tableRowIndexExpandedCurr : '',
		tableRowIndexExpandedPrev : '',
		dataCollapse : [],
		showPDF : false,
		pdfURL : ''
	};
});

caApp.factory('utilidadesConversion', function($cookies) {

	return {
		base64ToUint8Array : function(base64) {
			var raw = atob(base64); 
			var uint8Array = new Uint8Array(new ArrayBuffer(raw.length));
			for (var i = 0; i < raw.length; i++) {
				uint8Array[i] = raw.charCodeAt(i);
			}
			return uint8Array;
		},
		prueba : function() {
			return 'ok';
		}
	};
});