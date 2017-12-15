function getOCFilterForm() {
	filter = {
		page : '',
		clienteId :'',
		clienteNombre : '',
		proyectoId:0,
		proyectoNombre: '',
		propuestaId: 0,
		propuestaNombre:'',
		estado:'',
		activa: '',
		limit : 10,
		range : [],
		totalPages: 0
	};
	return filter;
}

function getOCFilterProject(){
	filterProject = {
		nombre: '',
		activo: 1,
		clienteId: 0,
		propuestaId: 0,
		seleccionado:0,
		managerId : 0
	};
	return filterProject;
}

function getOCFilterProposal(){
	filterProposal = {
			descripcion: '',
			comercial: '',
			fechaVigenciaDesde: '',
			fechaVigenciaHasta: '',
			clienteId: 0,
			seleccionado: 0
	};
	return filterProposal;
}

function getOCFilterManager() {
	filterManager = {
		nombre : '',
		seleccionado : 0
	};
	return filterManager;
}

function newOC() {
	return {
		clienteId : '',
		contacto : '',
		tipo : '',
		estado : '',
		monedaId : '',
		numero : '',
		concepto : '',
		condicionPago : '',
		fechaEmision : '',
		fechaVencimiento : '',
		total : 0,
		activa : '1',
		idTmp : '',
		items : [],
		proyectos : [],
		archivos : []
	};
};

function getActivo(){
	return [ {
		name : 'SI',
		id : 1
	}, {
		name : 'NO',
		id : 0
	} ];
}

function newItem() {
	return {
		cantidad : '',
		unidad : '',
		descripcion : '',
		precio : '',
		total : '',
		extendido : ''
	};
};

function newFile(){
	return {
		date : '',
		name : '',
		type : '',
		id : '',
		canDelete : ''
	};
}

function newProject(){
	return {
		nombre : '',
		pm : '',
		id : '',
		seleccionado : '',
		extendido : ''
	};
}

function getTypeFileOC() {
	var type = [ {
		id : '2',
		name : 'OC'
	}, {
		id : '3',
		name : 'OTRO'
	} ];
	return type;
}

function getOCFilterConsult() {
	filter = {
		clienteId : '',
		clienteNombre : '',
		proyectoId : 0,
		proyectoNombre : '',
		managerId : 0,
		managerNombre : '',
		numero : '',
		estadoOC : '',
		estadoPreFac : '',
		fechaDesde : '',
		fechaHasta : '',
		pageOC : '',
		limitOC : 10,
		rangeOC : [],
		totalPagesOC : 0,
		pagePF : '',
		limitPF : 10,
		rangePF : [],
		totalPagesPF : 0
	};
	return filter;
}