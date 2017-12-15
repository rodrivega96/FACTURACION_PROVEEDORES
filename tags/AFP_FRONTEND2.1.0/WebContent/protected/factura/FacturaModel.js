caApp.registerFactory('FModel', function($cookies) {
	return {
		newFactura : function(){
			return {
				numero : '',
				tipoFactura : 1,
				vencimiento : '',
				id : '',
				version : '',
				estado : '',
				idFacturaAdm : '',
				cuit : '',
				razonSocial : '',
				nroFactura : '',
				descripcion : '',
				fechaVencimientoAdm : '',
				fechaContableAdm : '',
				fechaFactura : '',
				nroCuenta : '',
				descCuenta : '',
				nroControl : '',
				descControl : '',
				importeNeto : '',
				pendiente : '',
				diasVencer : '',
				autorizaciones : '',
			};
		},
		getFacturaAdmFilter : function(){
			// resto un mes para setearlo por defecto
			var auxDate = new Date().getMilliseconds();
			var onceMonth = 2592000000;
			var desdeFlfSet = new Date();
			desdeFlfSet.setMilliseconds(auxDate - onceMonth);
			extFilter = {
				cuit : '',
				razon : '',
				numero : '',
				cc : '',
				centroc : '',
				wf : '',
				fvDesde : '',
				fiDesde : '',
				flfDesde : '',
				fvHasta : '',
				fiHasta : '',
				flfHasta : '',
				userSelected : '',
				ffacDesde : getFormattedDate(desdeFlfSet),
				ffacHasta : '',
				tipo : '',
				estado : '',
			};
			return extFilter;
		},
		getFacturaAdmFilterForm : function(){
			filter = {
					page : '',
					cuit : '',
					razon : '',
					numero : '',
					cc : '',
					centroc : '',
					wf : '',
					fvDesde : '',
					fiDesde : '',
					flfDesde : '',
					fvHasta : '',
					fiHasta : '',
					flfHasta : '',
					userSelected : '',
					ffacDesde : '',
					ffacHasta : '',
					tipo : '',
					estado : '',
					limit : 10,
					range : []
				};
				return filter;
		},
		getEstadosFactura : function(){
			return [ {
				name : 'Pendiente',
				id : 0
			}, {
				name : 'Inicial',
				id : 1
			}, {
				name : 'En Autorización',
				id : 2
			}, {
				name : 'Autorizada',
				id : 3
			}, {
				name : 'Rechazada',
				id : 4
			}, {
				name : 'Autorizada con Observación',
				id : 5
			} ];
		},
		getTipos : function(){
			return [ {
				name : 'Anticipadas',
				id : 1
			}, {
				name : 'Inmediatas',
				id : 2
			}, {
				name : 'A vencer',
				id : 3
			} ];
		},
		getWorkflowOptions : function(){
			return [ {
				name : 'Si',
				id : 1
			}, {
				name : 'No',
				id : 2
			} ];
		},
		getNewNivelDTO : function(idFactura){
			return [ {
				id : '',
				facturaAdmId : idFactura,
				orden : '',
				autorizador : {
					id : ''
				},
				estado : '1',
				publicado : false
			} ];
		}
	};
});