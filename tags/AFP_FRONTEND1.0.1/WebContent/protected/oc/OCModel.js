function getStateOC() {
	var state = [ {
		name : 'Vigente',
		id : 1
	}, {
		name : 'Cerrada',
		id : 2
	}, {
		name : 'Extendida',
		id : 3
	} ];
	return state;
};

function getPreFacStates() {
	var state = [ {
		name : 'FACTURADA',
		id : 1
	}, {
		name : 'PENDIENTE APROBACION CLIENTE',
		id : 2
	} ];
	return state;
};

function getOcs() {
	var oc = [ {
		number : '1',
		description : 'descripcion 1',
		name : 'nombre 1',
		client : 'cliente 1',
		date : 'fecha emision 1',
		state : 'Vigente',
		facturas : 'factura Nro 123456'
	}, {
		number : '2',
		description : 'descripcion 2',
		name : 'nombre 2',
		client : 'cliente 2',
		date : 'fecha emision 2',
		state : 'Vigente',
		facturas : 'factura Nro 789123'
	} ];
	return oc;
}

function getOCDetalle() {
	var detalle = [ {
		count : '2',
		unit : 'Horas',
		description : 'descripcion detalle 1',
		price : '10.5',
		total : '21'
	}, {
		count : '4',
		unit : 'Horas',
		description : 'descripcion detalle 2',
		price : '15',
		total : '60'
	} ];
	return detalle;
}

function getOCProyect() {
	var proyetc = [ {
		id : '1',
		name : 'OC',
		pm : 'PM'
	}, {
		id : '2',
		name : 'SIGLO XXI',
		pm : 'PM'
	} ];
	return proyetc;
}

function getUnits() {
	var unit = [ {
		id : '1',
		name : 'Horas'
	}, {
		id : '2',
		name : 'Días'
	}, {
		id : '3',
		name : 'Meses'
	} ];
	return unit;
}

function getMeses() {
	var mes = [ {
		id : '1',
		name : 'Enero'
	}, {
		id : '2',
		name : 'Febrero'
	}, {
		id : '3',
		name : 'Marzo'
	}, {
		id : '4',
		name : 'Abril'
	}, {
		id : '5',
		name : 'Mayo'
	}, {
		id : '6',
		name : 'Junio'
	}, {
		id : '7',
		name : 'Julio'
	}, {
		id : '8',
		name : 'Agosto'
	}, {
		id : '9',
		name : 'Septiembre'
	}, {
		id : '10',
		name : 'Octubre'
	}, {
		id : '11',
		name : 'Noviembre'
	}, {
		id : '12',
		name : 'Diciembre'
	} ];
	return mes;
}

function getPrefas() {
	var prefa = [ {
		id : '1',
		client : 'CLARO',
		proyect : 'Proyecto CLARO',
		manager : 'atulian',
		archivo : 'ENERO_2008_PREFACT_EPEC.PDF',
		periodo : '01/2008',
		version : '1',
		state : 'FACTURADA',
		numberOC : '51244-451245'
	}, {
		id : '2',
		client : 'CLARO',
		proyect : 'Proyecto CLARO',
		manager : 'atulian',
		archivo : 'FEBRERO_2008_PREFACT_EPEC.PDF',
		periodo : '02/2008',
		version : '1',
		state : 'PENDIENTE APROBACION CLIENTE',
		numberOC : ''
	} ];
	return prefa;
};

function getConsultOCs() {
	var oc = [ {
		numberOC : '51244',
		client : 'CLARO',
		concepto : 'CONCEPTO 1',
		date : '10/2008',
		total : '150000',
		state : 'VIGENTE',
		saldo : '20000'
	}, {
		numberOC : '451245',
		client : 'CLARO',
		concepto : 'CONCEPTO 2',
		date : '10/2008',
		total : '10000',
		state : 'VIGENTE',
		saldo : '1000'
	} ];
	return oc;
};

function getSelectOCs() {
	var oc = [ {
		number : '12457'
	}, {
		number : '42124'
	}, {
		number : '42125'
	}, {
		number : '42126'
	} ];
	return oc;
};

function getSelectProyect() {
	var proy = [ {
		id : '1',
		name : 'OC'
	}, {
		id : '2',
		name : 'SIGLO XXI'
	} ];
	return proy;
}

function getFacturas() {
	var fac = [ {
		number : '1235',
		reception : '5555',
		amount : '1500'
	}, {
		number : '3232',
		reception : '234234',
		amount : '12300'
	} ];
	return fac;
}
