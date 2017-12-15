caApp.registerFactory('AModel', function($cookies) {
	return {
		getAutorizacionFileterForm : function(){
			filter = {
					page : 1,
					limit : 10,
					range : [],
					variable : '',
					order : null
				};
				return filter;
		}
	};
});