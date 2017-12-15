caApp.registerFactory('FLModel', function($cookies) {
	return {
		getFileTypes : function(){
			return [ {
				name : 'Factura',
				id : 0
			}, {
				name : 'Otros',
				id : 1
			} ];
		}
	}
});
