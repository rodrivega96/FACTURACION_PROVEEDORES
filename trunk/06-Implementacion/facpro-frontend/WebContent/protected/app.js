var caApp = angular.module('caapp', [ 'ngRoute', 'ui.bootstrap', 'ngResource',
		'ngTable', 'ng-breadcrumbs', 'ngCookies', 'angularFileUpload',
		'ngAnimate','pdf' ]);
caApp
		.config(function($routeProvider, $httpProvider, $controllerProvider, $provide, 
				                                     $compileProvider, $filterProvider) {
            var random = Math.floor(Math.random()*1000);
            caApp.registerCtrl = $controllerProvider.register;
            caApp.registerFactory =  $provide.factory;
            caApp.registerDirective = $compileProvider.directive;
            caApp.registerFilter =  $filterProvider.register;
            
            $routeProvider.getCaseRouter = function (template, controller, label){
            	return {
					templateUrl : function () {
						$('#styleDisplayId').hide(); 
		            	random = Math.floor(Math.random()*1000);
		            	return template+'?i='+random;
					},
					controller: controller,
					label : label
				};
            };
   
    		//
			$routeProvider
					.when(
							'/',
							$routeProvider.getCaseRouter('protected/main.html',
									'MainController', 'Principal'))
					.when(
							'/login-form',
							$routeProvider.getCaseRouter('protected/main.html',
									'LoginController', 'Inicio de Sesion'))
					.when(
							'/logout',
							$routeProvider.getCaseRouter('protected/main.html',
									'LogoutController', 'Principal')
					// USER
					)
					.when(
							'/user-admin',
							$routeProvider.getCaseRouter(
									'protected/user/user-admin.html',
									'userAdminController',
									'Administrar Usuarios'))
					.when(
							'/user-admin/user-form',
							$routeProvider.getCaseRouter(
									'protected/user/user-form.html',
									'userFormController', 'Crear Usuario'))
					.when(
							'/user-admin/user-form-edit/:usId',
							$routeProvider.getCaseRouter(
									'protected/user/user-form.html',
									'userFormEditController',
									'Modificar Usuario'))
					.when(
							'/user-admin/user-assign-roles/:usId',
							$routeProvider
									.getCaseRouter(
											'protected/user/user-assign-roles.html',
											'userAssignRolesController',
											'Asignar Role')
					// FACTURA
					)
					.when(
							'/factura-admin',
							$routeProvider.getCaseRouter(
									'protected/factura/factura-admin.html',
									'facturaAdminController',
									'Gestionar Facturas'))
					.when(
							'/factura-admin/factura-form-edit/:facId',
							$routeProvider.getCaseRouter(
									'protected/factura/factura-form.html',
									'facturaFormEditController',
									'Modificar Factura'))
					.when(
							'/factura-admin/factura-wf-form-edit/:facId',
							$routeProvider.getCaseRouter(
									'protected/factura/factura-wf-form.html',
									'facturaFormEditWFController',
									'Modificar Workflow')
					// FACTURA CONSULTAR
					)
					.when(
							'/factura-consult-admin',
							$routeProvider
									.getCaseRouter(
											'protected/factura/factura-consult-admin.html',
											'facturaConsultAdminController',
											'Consultar Facturas'))
					.when(
							'/factura-consult-admin/factura-consult-form/:facId',
							$routeProvider.getCaseRouter(
									'protected/factura/factura-form.html',
									'facturaFormVerController',
									'Detalle Factura'))
					.when(
							'/factura-consult-admin/factura-consult-file/:facId',
							$routeProvider
									.getCaseRouter(
											'protected/autorizacion/autorizacion-file-form.html',
											'fileConsultController',
											'Detalle Archivos'))
					.when(
							'/factura-consult-admin/factura-consult-workflow/:facId',
							$routeProvider
									.getCaseRouter(
											'protected/workflow/detalle-workflow-form.html',
											'consultWFController',
											'Detalle Workflow')
					// FACTURA FILE
					)
					.when(
							'/factura-admin/file-form/:facId',
							$routeProvider.getCaseRouter(
									'protected/file/file-form.html',
									'fileAdminController',
									'Administrar Archivos')
					// AUTORIZACION
					)
					.when(
							'/autorizacion-admin',
							$routeProvider
									.getCaseRouter(
											'protected/autorizacion/autorizacion-admin.html',
											'autorizacionAdminController',
											'Autorizaciones'))
					.when(
							'/autorizacion-admin/autorizacion-file-form/:facId',
							$routeProvider
									.getCaseRouter(
											'protected/autorizacion/autorizacion-file-form.html',
											'autorizacionFileController',
											'Adjuntos'))
					.when(
							'/autorizacion-admin/detalle-workflow/:facId',
							$routeProvider
									.getCaseRouter(
											'protected/workflow/detalle-estados-form.html',
											'detalleEstadosController',
											'Historial de Estados')
					// AUTORIZACION NIVEL INFERIOR
					)
					.when(
							'/autorizacion-nivel-inferior',
							$routeProvider
									.getCaseRouter(
											'protected/autorizacion/autorizacion-nivel-inferior.html',
											'autorizacionNivelInferiorController',
											'Autorizacion Niveles Inferiores')
					// WORKFLOW
					)
					.when(
							'/autorizacion-nivel-inferior/detalle-workflow/:facId',
							$routeProvider
									.getCaseRouter(
											'protected/workflow/detalle-estados-form.html',
											'detalleEstadosController',
											'Historial de Estados')
					//ORDENES DE COMPAR
					).when(
							'/oc-admin',
							$routeProvider.getCaseRouter(
									'protected/oc/oc-admin.html',
									'oCAdminController',
									'Administrar Ordenes de Compra')).when(
							'/oc-admin/oc-form',
							$routeProvider
									.getCaseRouter('protected/oc/oc-form.html',
											'oCFormController',
											'Crear Orden de Compra')).when(
							'/oc-admin/oc-edit/:ocId',
							$routeProvider.getCaseRouter(
									'protected/oc/oc-form.html',
									'oCEditController',
									'Editar Orden de Compra')).when(
							'/oc-admin/oc-view/:ocId',
							$routeProvider.getCaseRouter(
									'protected/oc/oc-form.html',
									'oCViewController', 'Ver Orden de Compra'))
					.when(
							'/oc-consult-admin',
							$routeProvider.getCaseRouter(
									'protected/oc/oc-consult-admin.html',
									'oCAdminConsultController',
									'Reporte de Ordenes de Compra')
					//PROVEEDOR		
					).when(
							'/proveedor-admin',
							$routeProvider.getCaseRouter(
									'protected/proveedor/proveedor-admin.html',
									'proveedorAdminController',
									'Administrar Proveedores')).when(
							'/proveedor-admin/proveedor-form',
							$routeProvider
							.getCaseRouter(
									'protected/proveedor/proveedor-form.html',
									'proveedorFormController',
									'Crear Proveedor')).when(
							'/proveedor-admin/proveedor-edit/:proveedorId',
							$routeProvider.getCaseRouter(
									'protected/proveedor/proveedor-form.html',
									'proveedorEditController',
									'Editar Proveedor')
					).otherwise({
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
			$http.pendingLocalRequests++;
			if($input == null || $input == undefined ){
				$input={};
			}
			$input.random =  Math.floor(Math.random()*1000);
			$http({
				url : wsURL,
				method : 'GET',
				params : $input,
				headers : headersSend
			}).success(function(data, status, headers, config) {
				callbackFunction(data, status, headers, config);
				$http.pendingLocalRequests--;
			}).error(
					function(data, status, headers, config) {
						if (status == 403) {
							$cookieStore.remove('vatesuser');
							window.location.href = "";
						} else {
							alertify.error("Error de Conexion, "
									+ "intente nuevamente, Status: " + status
									+ " "+wsURL);
						}
						$http.pendingLocalRequests--;

					});
		},
		getWSResponsePost : function($http, $wsClassPath, $wsMethodPath,
				$input, $cookieStore, callbackFunction) {
			var wsURL = new String(getWSURL($wsClassPath, $wsMethodPath));
			$http.pendingLocalRequests++;
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
				$http.pendingLocalRequests--;
			}).error(
					function(data, status, headers, config) {
						if (status == 403) {
							$cookieStore.remove('vatesuser');
							window.location.href = "";
						} else {
							alertify.error("Error de Conexion, "
									+ "intente nuevamente, Status: " + status
									+ " "+ wsURL);
						}
						$http.pendingLocalRequests--;
					});

		},
		getWSResponsePostFile : function($http, $wsClassPath, $wsMethodPath,
				$input, $fileName,$fileType, $id, $force, $cookieStore, callbackFunction, $idTmp) {
			var fd = new FormData();
			fd.append("fileName", $fileName);
			fd.append("file", $input);
			fd.append("type", $input.type == '' ? 'text/plain' : $input.type);
			fd.append("idFactura", $id);
			fd.append("fileType", $fileType);
			fd.append("force", $force);
			fd.append("idTmp", $idTmp);
			var wsURL = new String(getWSURL($wsClassPath, $wsMethodPath));
			$http.pendingLocalRequests++;
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
				$http.pendingLocalRequests--;
			}).error(
					function(data, status, headers, config) {
						if (status == 403) {
							$cookieStore.remove('vatesuser');
							window.location.href = "";
						} else {
							alertify.error("Error de Conexion, "
									+ "intente nuevamente, Status: " + status
									+ " "+ wsURL);
						}
						$http.pendingLocalRequests--;
					});

		}

	};
});

caApp.factory('printFactory', function($cookies) {
	var widthPDF = 506;
	return {
		printVatesReport : function($data, $orintation, $sizePaper, $url, type) {
			// $orintation puede ser horizontal ("l") o vertical (p)
			// $sizePaper es el tamaÃ±o de la hoja A4, latter.
			var d = new Date().toISOString().slice(0, 10).replace(/-/g, "");
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
			var str = $.ajax(
					  {
						    type: 'GET',
						    async: false,
						    url:  $url
						  }).responseText;
			var result = str.replace("content", $data);

			pdf.fromHTML(result,// HTML
			65, // x coord inicial
			50, // y coord inicial
			{
				'width' : widthPDF, // ancho del pdf
				elementHandlers : specialElementHandlers
			});
			if (type == null || type == undefined){
				return 	pdf.save(filename);
			}else{
				return pdf.byType(type, filename);
			}			
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
				$dataFilter, $scope, $count) {
			return new ngTableParams({
				page : 1,
				count : ($count != null && $count != undefined) ? $count : 5,
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
		},
		
		getTableDataModal : function($tableData, ngTableParams, $scope, $count) {
			return new ngTableParams({
				page : 1,
				count : $count
			}, {
				total : $tableData.length,
				counts : [],
				getData : function($defer, params) {
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
		version:'',
		menu : [],
		backUrl:"/"
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
				version:'',
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

caApp.factory('FiltroOC', function() {
	return {
		data : [],
		page : null,
	    initial:[] 
	};
});

caApp.factory('FiltroProveedor', function() {
	return {
		data : [],
		page : null,
	    initial:[] 
	};
});

caApp.factory('FiltroReportOC', function() {
	return {
		dataConsult : []
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
		}
	};
});

caApp.directive('myShow', function($animate) {
	return {
		scope : {
			'myShow' : '=',
			'afterShow' : '&',
			'afterHide' : '&'
		},
		link : function(scope, element) {
			scope.$watch('myShow', function(show) {
				if (show) {
					$animate.removeClass(element, 'ng-hide', scope.afterShow);
				}
				if (!show) {
					$animate.addClass(element, 'ng-hide', scope.afterHide);
				}
			});
		}
	};
});

caApp.directive('autoComplete', function($timeout) {
	return function(scope, iElement, iAttrs) {
		iElement.autocomplete({
			source : function(request, response) { 
	        	if(request.term.trim() == ''){
	        		response(scope[iAttrs.acoptions]);
	        	}else if(request.term.length>2){
			        var data = $.grep(scope[iAttrs.acoptions], function(value) {
			            return   value.value.toLowerCase().indexOf(request.term.toLowerCase()) > -1;
			        });            
			        response(data);
				}
		    },        
			select : function(event, ui) {
				var functionDo = function(element, 
						elementPrevName,value) {
					element[elementPrevName]=value;
					return element[elementPrevName];
				};
				getStringElement(
						iAttrs.acvalue, scope, functionDo, 
						ui.item.id);
				$timeout(function() {
						iElement.trigger('input');
				}, 0);
			}
			}).keydown(function(event) {
				var functionDo = function(element, elementPrevName, value) {
					if (element[elementPrevName] != '') {//Si presiona borrar o delete y hay algo seleccionado lo deselecciona
						if (event.keyCode == 8 || event.keyCode == 46) {
							element[elementPrevName] = '';
							iElement.val("");
							var functionDoBlank = function(element1, elementPrevName1,
									value) {
								element1[elementPrevName1] = value;
								return null;
							};
							getStringElement(iAttrs.ngModel, scope, functionDoBlank,
									null);
						} else {
							return false;
						}
					}else{
						if (event.keyCode == 32){ //si presiona un espacio y no hay ninngun elemento seleccionado busca todo
							iElement.autocomplete('search', '');
						}
					}
				};
				return getStringElement(iAttrs.acvalue,
						scope, functionDo, null);

			});
	};
});

caApp.directive('datePickerNg', function() {
	return function(scope, iElement, iAttrs) {
		var options = {
			format : getStringFormatDate(),
			onRender : function(date) {
				var functionDo = function(element, elementPrevName) {
					return element[elementPrevName];
				};
				if (iAttrs.minDatePicker !== undefined){
					var elem = getStringElement(iAttrs.minDatePicker, 
							scope, functionDo, null);
					if(elem != '' && elem != undefined && date < getDate(elem)){
						return getFormattedDate(date) != getFormattedDate(new Date()) ? 'disabled' 
								: 'disabled highlight-today' ;							
					}
				} else if (iAttrs.maxDatePicker !== undefined) {
					var elem = getStringElement(iAttrs.maxDatePicker, scope,
							functionDo, null);
					if(elem != '' && elem != undefined && date >  getDate(elem)){
						return getFormattedDate(date) != getFormattedDate(new Date()) ? 'disabled' 
								: 'disabled highlight-today';
					}
				}
				return getFormattedDate(date) == getFormattedDate(new Date()) ? 'highlight-today' : true;
			}
		};
		var changeFn = function(ev) {
			var element = angular.element(iElement);
			var controller = element.controller();
			var scope = element.scope();
			scope.$apply(function() {
				var functionDo = function(element,
						elementPrevName, value) {
					element[elementPrevName] = value;
					return null;
				};
				getStringElement(iAttrs.ngModel, scope,
						functionDo, getFormattedDate(ev.date));
				});
			$(this).datepicker('hide');
			};
		var clickFn = function(ev){
			var elem = '';
			if(iElement.val() == ''){
				var functionDo = function(element,
						elementPrevName, value) {
					return element[elementPrevName];
				};
				if (iAttrs.minDatePicker !== undefined) {
					var newElem = getStringElement(iAttrs.minDatePicker, 
							scope, functionDo, null);
					elem = newElem > getFormattedDate(new Date()) ? newElem : '';
				} else if (iAttrs.maxDatePicker !== undefined) {
					var newElem = getStringElement(iAttrs.maxDatePicker, 
							scope, functionDo, null);
					elem = newElem < getFormattedDate(new Date()) ? newElem : '';
				}			
			} else {
				elem = iElement.val();
			}
			iElement.datepicker('update', elem);
		};
			iElement.datepicker(options)
					.on('changeDate', changeFn)
					.on('click', clickFn);
	};
});

caApp.factory('tableCollapseFactory', function($cookies) {
	return {
		onSelect : function(index, dataCollapseFn, scope ,tableRowExpanded, tableRowIndexExpandedCurr,
				tableRowIndexExpandedPrev,dataCollapse, indexName) {
			if(indexName!= null && indexName != undefined){
				scope[indexName] = index;
			}
			if (typeof scope[dataCollapse] === 'undefined') {
		 		dataCollapseFn();
		 	}
		 	if (scope[tableRowExpanded] === false
		 			&& scope[tableRowIndexExpandedCurr] === "") {
		 		scope[tableRowIndexExpandedPrev] = "";
		 		scope[tableRowExpanded] = true;
		 		scope[tableRowIndexExpandedCurr] = index;
		 		document.getElementById(index).style.backgroundColor = "#FF8C00";
		 		scope[dataCollapse][index] = true;
		 	} else if (scope[tableRowExpanded] === true) {
		 		if (scope[tableRowIndexExpandedCurr] === index) {
		 			document.getElementById(index).style.backgroundColor = "#CACACA";
		 			scope[tableRowExpanded] = false;
		 			scope[tableRowIndexExpandedCurr] = "";
		 			scope[dataCollapse][index] = false;
		 		} else {
		 			if (scope[tableRowIndexExpandedCurr] !== ""
		 				&& scope[tableRowIndexExpandedCurr] != index) {
		 				document.getElementById(scope[tableRowIndexExpandedCurr])
		 				.style.backgroundColor = "#CACACA";
		 			}
		 			document.getElementById(index).style.backgroundColor = "#FF8C00";
		 			scope[tableRowIndexExpandedPrev] =
		 				scope[tableRowIndexExpandedCurr];
		 			scope[tableRowIndexExpandedCurr] = index;
		 			scope[dataCollapse][scope[tableRowIndexExpandedPrev]] =
		 				false;
		 			scope[dataCollapse][scope[tableRowIndexExpandedCurr]] = true;
		 		}
		 	}
		}

	};
});

caApp.factory('utilidades', function($cookies) {
	var urls = {backUrl : '/'};
	return {
		getValueFromList : function(list, id, contacto) {
			for (var i = 0; i < list.length; i++) {
				if (list[i].id == id) {
					if (list[i].contacto !== undefined && contacto) {
						return list[i].contacto;
					} else if (list[i].value !== undefined) {
						return list[i].value;
					} else if (list[i].nombre !== undefined) {
						return list[i].nombre;
					} else {
						return '';
					}
				}
			}
		},
		getExpandidoFromList : function(value) {
			if (value) {
				return "Extendido";
			} else {
				return "Original";
			}
		}, validarArchivoOc : function (listArchOc){
			if(listArchOc.length > 0){
				for(var i = 0; i < listArchOc.length; i++){
					if(listArchOc[i].type == 2){
						return true;
					}
				}
			}
			return false;
		}, isPDF : function (name) {
			return name.substr(-4).toUpperCase() === '.PDF';
		}, getFiltroSeleccionado : function() {
			return [ {
				title : 'SI',
				id : true
			}, {
				title : 'NO',
				id : false
			} ];
		},
		getEstadosOC : function() {
			return {
				EXTENDIDA : 3,
				CERRADA : 4
			};
		}, twoDecimals : function (number){
			return Number(Number(parseInt(number * 100)) / 100);
		}, getPath : function ($coockieStore, remove){
			var coockieUrl = $coockieStore.get('urls');
			var pos = coockieUrl.backUrl.length-1;
			var url = coockieUrl.backUrl[pos];
			if(pos>0){
				if(remove){
					coockieUrl.backUrl.splice(pos,1);
					$coockieStore.remove('urls');
					$coockieStore.put('urls', coockieUrl);
				}
			}
			return  url;
		}, setPath : function ($coockieStore, $path, $pathOrigen){
			var coockieUrl = $coockieStore.get('urls');
			coockieUrl.backUrl[coockieUrl.backUrl.length] = $pathOrigen;
			$coockieStore.remove('urls');
			$coockieStore.put('urls', coockieUrl);
			return $path;
		},getPathRedirection : function ($coockieStore, goUrl){
			var coockieUrl = $coockieStore.get('urls');
			var pos = coockieUrl.backUrl.length;
			var index = coockieUrl.backUrl.indexOf(goUrl);
			if(pos>1 && index>0){
				coockieUrl.backUrl.splice(index, (pos-index));
				$coockieStore.remove('urls');
				$coockieStore.put('urls', coockieUrl);
			}
			return  goUrl;
		},resetPath : function ($coockieStore){
			var basePath = '/';
			var coockieUrl = {backUrl:[]};
			coockieUrl.backUrl[0] = basePath;
			$coockieStore.remove('urls');
			$coockieStore.put('urls', coockieUrl);
			return basePath;
		},resetPathBreadCrumb : function ($coockieStore, $index){
			var coockieUrl = $coockieStore.get('urls');
			var pos = coockieUrl.backUrl.length;
			if(pos>1 && $index>0){
				coockieUrl.backUrl.splice($index,(pos-$index));
				$coockieStore.remove('urls');
				$coockieStore.put('urls', coockieUrl);
			}
		}, getCurrency: function ($value){
			$value = Number($value);
		    return $value.toFixed(2).replace(/\./g, ',')
		    .replace(/[0-9](?=(?:[0-9]{3})+(?![0-9]))/g, '$&.');
		}, getCurrencyNumber: function ($value){
		    return $value.replace(/\./g, '').replace(/\,/g, '.');
		}
	};
});

caApp.directive('toggle', function() {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			if (attrs.toggle == "tooltip") { 
				$(element).tooltip();
			}
			if (attrs.toggle == "popover") {
				$(element).popover();
			}
		}
	};
});

caApp.directive('styleDisplay', [ '$http', function($http)  {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var functionDo = function(data) {
				return 'display : ' + data;
			};
			scope.isDisplayValue = function() {
				return $http.pendingRequests.length > 0 && $http.pendingLocalRequests == 0;
			};
			scope.$watch(scope.isDisplayValue, function(val) {
				if (val) {
					attrs.$set('style', functionDo('none !important'));
				} else {
					attrs.$set('style', functionDo(null));
				}
			});		
		}
	};
}]);

caApp.factory('paginatedFunction', function($cookies) {
	return {
		generatePaginatedMethod : function(scope, paginatedNameVar, rollbackFn,
				filterPageName, loadFn, filterTotalPageName, filterRangeName,
				otherFn) {
			var paginatedName = scope[paginatedNameVar];
			var setValue = function(element, elementPrevName, value) {
				element[elementPrevName] = value;
				return null;
			};
			var getValue = function(element, elementPrevName, value) {
				return element[elementPrevName];
			};
			var pushValue = function(element, elementPrevName, value) {
				element[elementPrevName].push(value);
				return null;
			};

			scope['prevPage' + paginatedName] = function() {
				if (rollbackFn != null) {
					rollbackFn();
				}
				var pageValue = getStringElement(filterPageName, scope,
						getValue, null);
				if (pageValue > 1) {
					getStringElement(filterPageName, scope, setValue,
							--pageValue);
					if (otherFn != null) {
						otherFn();
					}
					loadFn();
				}
			};

			scope['goPage' + paginatedName] = function($number) {
				getStringElement(filterPageName, scope, setValue, $number);
				if (otherFn != null) {
					otherFn();
				}
				if (rollbackFn != null) {
					rollbackFn();
				}
				loadFn();
			};

			scope['nextPage' + paginatedName] = function() {
				if (rollbackFn != null) {
					rollbackFn();
				}
				var pageValue = getStringElement(filterPageName, scope,
						getValue, null);
				if (pageValue < getStringElement(filterTotalPageName, scope,
						getValue, null)) {
					getStringElement(filterPageName, scope, setValue,
							++pageValue);
					if (otherFn != null) {
						otherFn();
					}
					loadFn();
				}
			};

			scope['firstPage' + paginatedName] = function() {
				if (rollbackFn != null) {
					rollbackFn();
				}
				getStringElement(filterPageName, scope, setValue, 1);
				if (otherFn != null) {
					otherFn();
				}
				loadFn();
			};

			scope['lastPage' + paginatedName] = function() {
				if (rollbackFn != null) {
					rollbackFn();
				}
				getStringElement(filterPageName, scope, setValue,
						getStringElement(filterTotalPageName, scope, getValue,
								null));
				if (otherFn != null) {
					otherFn();
				}
				loadFn();
			};

			scope['setRangePages' + paginatedName] = function() {
				getStringElement(filterRangeName, scope, setValue, []);
				var totalPages = getStringElement(filterTotalPageName, scope,
						getValue, null);
				for (var i = 1; i <= totalPages; i++) {
					getStringElement(filterRangeName, scope, pushValue, i);
				}
			};
		}
	};
});


caApp.directive('paginatedButton', function() {
	return {
		restrict : 'A',
		link : function(scope, iElement, iAttrs) {
			var name = scope[iAttrs.paginatedname];
			var div = document.createElement("div");
			iElement.append(div);
			var el = angular.element(div);
			var filter = scope[iAttrs.paginatedfilter];
			var filterPage = filter + ".page" + name;
			var filterTotalPage = filter + ".totalPages" + name;
			var filterRange = filter + ".range" + name;

			// first
			var button = document.createElement("a");
			var span = document.createElement("span");
			var img = document.createElement("img");
			var methodName = "firstPage" + name + "()";
			img.setAttribute("class", "img-responsive");
			img.setAttribute("src", "img/first.png");
			img.setAttribute("width", "60%");
			img.setAttribute("height", "60%");
			span.appendChild(img);
			button.setAttribute("role", "button");
			button.setAttribute("type", "button");
			button.setAttribute("ng-click", methodName);
			button.setAttribute("class", "btn btn-default btn-sm");
			button.setAttribute("data-toggle", "tooltip");
			button.setAttribute("data-placement", "top");
			button.setAttribute("title", "Primera");
			button.setAttribute("ng-disabled", filterPage + " == 1");
			button.appendChild(span);
			el.append(button);

			// prev
			button = document.createElement("a");
			span = document.createElement("span");
			img = document.createElement("img");
			var methodName = "prevPage" + name + "()";
			img.setAttribute("class", "img-responsive");
			img.setAttribute("src", "img/prev.png");
			img.setAttribute("width", "60%");
			img.setAttribute("height", "60%");
			span.appendChild(img);
			button.setAttribute("role", "button");
			button.setAttribute("type", "button");
			button.setAttribute("ng-click", methodName);
			button.setAttribute("class", "btn btn-default btn-sm");
			button.setAttribute("data-toggle", "tooltip");
			button.setAttribute("data-placement", "top");
			button.setAttribute("title", "Previa");
			button.setAttribute("ng-disabled", filterPage + " == 1");
			button.appendChild(span);
			el.append(button);

			// go
			button = document.createElement("a");
			var methodName = "goPage" + name + "(number)";
			button.setAttribute("role", "button");
			button.setAttribute("type", "button");
			button.setAttribute("ng-repeat", "number in " + filterRange);
			button.setAttribute("ng-bind", "number");
			button.setAttribute("ng-click", methodName);
			button.setAttribute("ng-class", "{active: number == "
							+ filterPage + "}");
			button.setAttribute("href", "");
			button.setAttribute("class", "btn");
			var i = document.createElement("i");
			i.setAttribute("class", "icon-fast-forward");
			button.appendChild(i);
			el.append(button);

			// next
			button = document.createElement("a");
			span = document.createElement("span");
			img = document.createElement("img");
			var methodName = "nextPage" + name + "()";
			img.setAttribute("class", "img-responsive");
			img.setAttribute("src", "img/next.png");
			img.setAttribute("width", "60%");
			img.setAttribute("height", "60%");
			span.appendChild(img);
			button.setAttribute("role", "button");
			button.setAttribute("type", "button");
			button.setAttribute("ng-click", methodName);
			button.setAttribute("class", "btn btn-default btn-sm");
			button.setAttribute("data-toggle", "tooltip");
			button.setAttribute("data-placement", "top");
			button.setAttribute("title", "Siguiente");
			button.setAttribute("ng-disabled", filterPage + " == "
					+ filterTotalPage);
			button.appendChild(span);
			el.append(button);

			// last
			button = document.createElement("a");
			span = document.createElement("span");
			img = document.createElement("img");
			var methodName = "lastPage" + name + "()";
			img.setAttribute("class", "img-responsive");
			img.setAttribute("src", "img/last.png");
			img.setAttribute("width", "60%");
			img.setAttribute("height", "60%");
			span.appendChild(img);
			button.setAttribute("role", "button");
			button.setAttribute("type", "button");
			button.setAttribute("ng-click", methodName);
			button.setAttribute("class", "btn btn-default btn-sm");
			button.setAttribute("data-toggle", "tooltip");
			button.setAttribute("data-placement", "top");
			button.setAttribute("title", "Ultima");
			button.setAttribute("ng-disabled", filterPage + " == "
					+ filterTotalPage);
			button.appendChild(span);
			el.append(button);

			$scope = el.scope();
			$injector = el.injector();
			$injector.invoke(function($compile) {
				$compile(el)($scope);
			});
		}
	};
});

caApp.directive('onlyNumber', function() {
	return {
		restrict : 'A',
		link : function(scope, iElement, iAttrs) {
			iElement.bind('keypress', function(e) {
				var expresion = /^\d+$/;
				return expresion.test(String.fromCharCode(e.charCode))|| e.keyCode == 8
						|| e.keyCode == 46
						|| (e.keyCode >= 37 && e.keyCode <= 40);
			});
		}
	};
});