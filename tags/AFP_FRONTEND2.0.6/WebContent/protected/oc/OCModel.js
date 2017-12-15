caApp.registerFactory('OCModel', function($cookies) {
		return {
			getOCFilterForm : function() {
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
			},
			getOCFilterProject : function() {
				filterProject = {
						nombre: '',
						activo: 1,
						clienteId: 0,
						propuestaId: 0,
						seleccionado:0,
						managerId : 0
					};
					return filterProject;
			},
			getOCFilterProposal : function() {
				filterProposal = {
						descripcion: '',
						comercial: '',
						fechaVigenciaDesde: '',
						fechaVigenciaHasta: '',
						clienteId: 0,
						seleccionado: 0
				};
				return filterProposal;
			},
			getOCFilterManager : function() {
				filterManager = {
						nombre : '',
						seleccionado : 0
					};
					return filterManager;
			},
			newOC : function() {
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
			},
			getActivo : function() {
				return [ {
					name : 'SI',
					id : 1
				}, {
					name : 'NO',
					id : 0
				} ];
			},
			newItem : function() {
				return {
					cantidad : '',
					unidad : '',
					descripcion : '',
					precio : '',
					total : '',
					extendido : ''
				};
			},
			newFile : function() {
				return {
					date : '',
					name : '',
					type : '',
					id : '',
					canDelete : ''
				};
			},
			newProject : function() {
				return {
					nombre : '',
					pm : '',
					id : '',
					seleccionado : '',
					extendido : ''
				};
			},
			getTypeFileOC : function() {
				return [ {
					id : '2',
					name : 'OC'
				}, {
					id : '3',
					name : 'OTRO'
				} ];
			},
			getOCFilterConsult : function() {
				filter = {
						clienteId : '',
						clienteNombre : '',
						proyectoId : 0,
						proyectoNombre : '',
						managerId : 0,
						managerNombre : '',
						numero : '',
						estadoOC : '',
						fechaDesde : '',
						fechaHasta : '',
						pageOC : '',
						limitOC : 10,
						rangeOC : [],
						totalPagesOC : 0
					};
					return filter;
			},
			getPFFilterConsult : function() {
				filter = {
						estadoPreFac : '',
						pagePF : '',
						limitPF : 4,
						rangePF : [],
						totalPagesPF : 0
					};
					return filter;
			}, newFileCont : function () {
				var fileCont = {
						file : null,
						typeFile : null
				};
				return fileCont;
			}
		};
	});