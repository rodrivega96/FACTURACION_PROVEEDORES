caApp.registerFactory('USModel', function($cookies) {
	return {
		newUser: function(){
			var user = {
					name : '',
					lastName : '',
					mail : '',
					active : 1,
					job : '',
					password : ''
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
		stateValue: function($valueState){
			var state = 0;
			if ($valueState == "Activo") {
				state = 1;
			}
			return state;
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
		}
	};
});