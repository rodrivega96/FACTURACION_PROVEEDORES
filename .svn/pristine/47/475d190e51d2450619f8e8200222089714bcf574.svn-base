caApp.registerFactory('ProveedorModel', function($cookies) {
	return {
		getProveedorFilterForm : function() {
			filter = {
				razonSocial : '',
				cuit : '',
				medioPago : '',
				limit : 10,
				range : [],
				totalPages : 0
			};
			return filter;
		},
		loadInitialDataFilter : function(param1, param2) {
			param1.tiposTelefono = param2.tiposTelefono;
			param1.mediosPago = param2.mediosPago;
		},
		newProveedor : function() {
			return {
				id : '',
				razonSocial : '',
				cuit : '',
				iibb : '',
				contacto : '',
				direccion : '',
				tipoTelefono : '',
				numeroTelefono : '',
				email : '',
				mediosPago : [],
				medioPagoDefecto : '',
				descuento : '',
				observaciones : ''
			};
		}
	};
});