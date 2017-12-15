function newOC() {
	return {
		nro : '',
		date : '',
		type : '',
		state : '1',
		expiration : '',
		client : 'autocompletar',
		contact : 'Sugerido desde contacto cliente',
		coin : '',
		condition : '',
		concept : '',
		activa : '1'
	};
};

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

function getTiposOC() {
	var tipo = [ {
		name : 'Externa',
		id : 1
	}, {
		name : 'Interna',
		id : 2
	} ];
	return tipo;
};

function getTypesOC() {
	var tipo = [ {
		name : 'Externa',
		id : 1
	}, {
		name : 'Interna',
		id : 2
	} ];
	return tipo;
};

function getCoinsOC() {
	var tipo = [ {
		name : 'Peso',
		id : 1
	}, {
		name : 'Dolar',
		id : 2
	}, {
		name : 'Euro',
		id : 2
	} ];
	return tipo;
};

function getSiNoOC() {
	var siNo = [ {
		name : 'SI',
		id : 1
	}, {
		name : 'NO',
		id : 2
	} ];
	return siNo;
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

function getDetalleOC() {
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

function getProyectOC() {
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

function getFileOC() {
	var file = [ {
		id : '1',
		date : '29/07/2015',
		name : 'Archivo_1.pdf',
		type : 'OC'
	}, {
		id : '2',
		date : '25/07/2015',
		name : 'Archivo_1.docx',
		type : 'OTRO'
	} ];
	return file;
}

function getTypeFileOC() {
	var type = [ {
		id : '1',
		name : 'OC'
	}, {
		id : '2',
		name : 'OTRO'
	} ];
	return type;
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
		description : 'ENERO_2008_PREFACT_EPEC.PDF',
		periodo : '01/2008',
		amount : '10000',
		state : 'FACTURADA'
	}, {
		id : '2',
		client : 'CLARO',
		proyect : 'Proyecto CLARO',
		manager : 'atulian',
		description : 'FEBRERO_2008_PREFACT_EPEC.PDF',
		periodo : '02/2008',
		amount : '12100',
		state : 'PENDIENTE APROBACION CLIENTE'
	} ];
	return prefa;
};

function getConsultOCs() {
	var oc = [ {
		numberOC : '51244',
		client : 'CLARO',
		concepto : 'CONCEPTO 1',
		date : '10/2008',
		monto : '150000',
		state : 'VIGENTE',
		total : '20000'
	}, {
		numberOC : '451245',
		client : 'CLARO',
		concepto : 'CONCEPTO 2',
		date : '10/2008',
		monto : '10000',
		state : 'VIGENTE',
		total : '1000'
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
		name : 'OC',
		pm : 'PM'
	}, {
		id : '2',
		name : 'SIGLO XXI',
		pm : 'PM'
	} ];
	return proy;
}

function getFacturasOC() {
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

function getPeoplesPreFac() {
	var people = [ {
		name : 'Manuel',
		amount : '10'
	}, {
		name : 'Lucas',
		amount : '10'
	} ];
	return people;
}

function getFindProposal() {
	var proyetc = [ {
		id : '1',
		description : 'Descripcion propuesta 1',
		dateStart : '10/10/2014',
		dateEnd : '10/10/2015',
		name : 'Propuesta 1'
	}, {
		id : '2',
		description : 'Descripcion propuesta 2',
		dateStart : '12/12/2014',
		dateEnd : '12/12/2015',
		name : 'Propuesta 2'
	}, {
		id : '3',
		description : 'Descripcion propuesta 3',
		dateStart : '05/09/2013',
		dateEnd : '26/03/2016',
		name : 'Propuesta 3'
	}, {
		id : '4',
		description : 'Descripcion propuesta 4',
		dateStart : '01/11/2010',
		dateEnd : '16/08/2019',
		name : 'Propuesta 4'
	} ];
	return proyetc;
}

function getFindProyect() {
	var proyetc = [ {
		id : '1',
		name : 'OC',
		activo : 'SI'
	}, {
		id : '2',
		name : 'SIGLO XXI',
		activo : 'NO'
	} ];
	return proyetc;
}

function getFindManager() {
	var manager = [ {
		id : '1',
		name : 'Manager 1'
	}, {
		id : '2',
		name : 'Manager 2'
	}, {
		id : '3',
		name : 'Manager 3'
	}, {
		id : '4',
		name : 'Manager 4'
	} ];
	return manager;
}

function getOCPrefac() {
	var oc = [ {
		nro : '1',
		concept : 'concepto 1'
	}, {
		nro : '2',
		concept : 'concepto 2'
	} ];
	return oc;
}

function getFilterFindProyect() {
	var proyect = {
		name : '',
		activo : 1
	};
	return proyect;
}

function getFilterFindManager() {
	var manager = {
		name : ''
	};
	return manager;
}
