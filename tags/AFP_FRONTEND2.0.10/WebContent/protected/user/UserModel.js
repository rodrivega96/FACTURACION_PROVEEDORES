caApp.registerFactory('USModel', function($cookies) {
	return {
		newUser: function(){
			var user = {
					name : '',
					lastName : '',
					mail : '',
					active : 1,
					job : ''
				};
				return user;
		},
		getFindState: function(){
			return [ {
				name : '',
				id : -1
			}, {
				name : 'Inactivo',
				id : 0
			}, {
				name : 'Activo',
				id : 1
			} ];
		},
		getState: function(){
			return [ {
				name : 'Activo',
				id : 1
			}, {
				name : 'Inactivo',
				id : 0
			} ];
		},
		getFilter: function(){
			return {
				page : 1,
				name : '',
				lastName : '',
				active : -1,
				job : '',
				limit : 10,
				range : []
			};
		},
		getExtFilter: function(){
			return {
				name : '',
				lastName : '',
				active : -1,
				job : ''
			};
		},
		getStateMap : function() {
			return {
				1 : 'Activo',
				0 : 'Inactivo'
			};
		}
	};
});