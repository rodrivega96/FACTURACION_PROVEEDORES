caApp.registerFactory('AModel', function($cookies) {
	return {
		getAutorizacionFileterForm : function(){
			filter = {
					cuit : '',
					razon : '',
					numero : '',
					cc : '',
					centroc : '',
					fvDesde : '',
					fiDesde : '',
					flfDesde : '',
					fvHasta : '',
					fiHasta : '',
					flfHasta : '',
					userSelected : '',
					userList : null,
					ffacDesde : '',
					ffacHasta : '',
					tipo : '',
					page : 1,
					limit : 10,
					range : [],
					variable : '',
					order : null
				};
				return filter;
		},
		getFacturaAdmFilter : function(){
			extFilter = {
				cuit : '',
				razon : '',
				numero : '',
				cc : '',
				centroc : '',
				fvDesde : '',
				fiDesde : '',
				flfDesde : '',
				fvHasta : '',
				fiHasta : '',
				flfHasta : '',
				userSelected : '',
				userList : null,
				ffacDesde : '',
				ffacHasta : '',
				tipo : '',
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
					userList : null,
					ffacDesde : '',
					ffacHasta : '',
					tipo : '',
					estado : '',
					limit : 10,
					range : []
				};
				return filter;
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
		}
	};
});