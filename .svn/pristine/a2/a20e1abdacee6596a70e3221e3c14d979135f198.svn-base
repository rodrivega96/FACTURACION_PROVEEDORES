function newFactura() {
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
		nroCuenta : '',
		descCuenta : '',
		nroControl : '',
		descControl : '',
		importeNeto : '',
		pendiente : '',
		diasVencer : '',
		autorizaciones : '',
	};
};

function newAutorizadores() {
	return [ {
		id : '',
		name : '',
	}, ];
};

function getFacturaAdmFilter() {
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
		flfDesde : getFormattedDate(desdeFlfSet),
		// flfDesde : '',
		fvHasta : '',
		fiHasta : '',
		flfHasta : '',
		tipo : '',
		estado : '',
	};
	return extFilter;
}

function getFacturaAdmFilterForm() {
	filter = {
		page : 1,
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
		tipo : '',
		estado : '',
		limit : 10,
		range : []
	};
	return filter;
}

function clearFilterFactura(pageActual, totalPageActual) {
	// resto un mes para setearlo por defecto
	var auxDate = new Date().getMilliseconds();
	var onceMonth = 2592000000;
	var desdeFlfSet = new Date();
	desdeFlfSet.setMilliseconds(auxDate - onceMonth);
	
	filter = {
		page : pageActual,
		totalPage : totalPageActual,
		cuit : '',
		razon : '',
		numero : '',
		cc : '',
		centroc : '',
		wf : '',
		fvDesde : '',
		fiDesde : '',
		flfDesde : getFormattedDate(desdeFlfSet),
		fvHasta : '',
		fiHasta : '',
		flfHasta : '',
		tipo : '',
		estado : '',
		limit : 10,
		range : []
	};
	return filter;
}

function getEstadosFactura() {
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
}

function getTipos() {
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

function getWorkflowOptions() {
	return [ {
		name : 'Si',
		id : 1
	}, {
		name : 'No',
		id : 2
	} ];
}

function getNewNivel(idFactura) {
	return [ {
		usuario : {
			id : 0,
			name : '',
		},
		factura : {
			id : idFactura,
		},
		orden : 0
	}, ];
}

function getNewNivelDTO(idFactura) {
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

function getPdfView(){
	return {
		cuit : '',
		importe : '',
		razonSocial : '',
		fecVto : '',
		nroFac : '',
		descripcion : '',
		cc : [{
			numero: '',
			cuenta: '',
			centro: '',
			importe: ''
		}],
		wf : [{
			nivel : '',
			autoriza : '',
			estado : '',
			fechaHora : '',
			descripcion : ''
			
		}]		
	};
}