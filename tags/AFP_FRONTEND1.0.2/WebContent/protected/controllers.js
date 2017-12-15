caApp.controller('MainMenuController', function($scope,
		breadcrumbs, $http, $cookieStore, userProperties, FiltroFactura, IndexFactura) {
	if(typeof($cookieStore.get('vatesuser')) == "undefined"){
		$cookieStore.put('vatesuser', userProperties.getUser());
		var cookie = $cookieStore.get('vatesuser');
		cookie.isLoged=false;
		cookie.showWait=false;
		cookie.menu = userProperties.getUser().menu;
		$cookieStore.put('vatesuser',cookie);
		
	}
	
	$scope.items=$cookieStore.get('vatesuser').menu;
	$scope.clearFiltersFactura = function(){
		FiltroFactura.data=[];
		FiltroFactura.dataConsult=[];
		FiltroFactura.page=null;
		IndexFactura.index = null;
		IndexFactura.facId = null;
		IndexFactura.facAdmId = null;
		IndexFactura.tableRowExpanded = '';
		IndexFactura.tableRowIndexExpandedCurr = '';
		IndexFactura.tableRowIndexExpandedPrev = '';
		IndexFactura.dataCollapse = [];
		IndexFactura.showPDF = false;
		IndexFactura.pdfURL = '';
		
	};
	$scope.isLoged = $cookieStore.get('vatesuser').isLoged;
	$scope.userName = $cookieStore.get('vatesuser').userName;
	$scope.breadcrumbs = breadcrumbs;
	$scope.myAppVersion=getAppVersion();

	//$scope.displayLogin = 'none';	
	clear();
	
	
	$scope.status = {
		isopen : false
	};

	
	$scope.toggled = function(open) {
		console.log('Dropdown is now: ', open);
	};

	
	$scope.toggleDropdown = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.status.isopen = !$scope.status.isopen;
	};
	
	
	$scope.openLogin = function() {
		$scope.modalPass = false;
		$scope.modalLogin = true;		
		$scope.modalLoginPass = true;
		
		
		//TODO ver por que no funciona pointer-events en chrome
		var body = window.document.getElementById('main-menu');		
		body.style = 'pointer-events: none;';
		
		var modal = window.document.getElementById('modalLoginId');		
		modal.style = 'pointer-events: auto;';
		
		//$scope.pointerEventsIndex = 'none';
		//$scope.pointerEventsLogin = 'auto';		

		var name = window.document.getElementById('inptNameId');		
		name.focus();		
	};
	
	$scope.closeLogin = function() { 
		//$scope.displayLogin = 'none';
		$scope.modalLoginPass = false;
		$scope.modalLogin = true;
		$scope.modalPass = false;
		
		var body = window.document.getElementById('main-menu');		
		body.style = 'pointer-events: auto;';
		
		var body = window.document.getElementById('modalLoginId');		
		body.style = 'pointer-events: auto;';
		location.reload();
		//$scope.pointerEventsIndex = 'auto';
		//$scope.pointerEventsLogin = 'auto';	

		clear();
	};
	
	function clear(){
		var msgError = window.document.getElementById('messageId');
		msgError.style.display = "none";
		
		var form = window.document.getElementById('formLoginId');
		form.reset();
	};

});


caApp.controller('MainController', function($scope, $location) {

	$scope.message = 'Esta es la p�gina ' + $location.path();

});


caApp.controller('LoginController', function($scope, $http, $routeParams,
		breadcrumbs, userProperties, $window, $cookieStore, webServices) {
	$scope.breadcrumbs = breadcrumbs;
	//$scope.messageErrReset = false;
	
	$scope.user = {
		userName : '',
		password : '',
		userMail : ''
	};
	
	$scope.valueDomain = 1;
	
	$scope.domains = [ {
		name : 'VATESSA',
		id : 1
	}, {
		name : 'Local',
		id : 0
	} ];
	
	$scope.resetPass = function () {
		$scope.modalPass = true;
		$scope.modalLogin = false;
	};
	
	$scope.closeResetPass = function () { 
		//$scope.modalLoginPass = false;
		$scope.modalPass = false;
		$scope.modalLogin = true;
		//$scope.messageErrReset = false;
	};
	
	$scope.resetFn = function() {
		var parameters = {
			"userName" : $scope.user.userName,
			"userMail" : $scope.user.userMail
		};
				
		webServices.getWSResponsePost($http, 'login', 'reset', parameters, $cookieStore,
			function(data, status, headers, config) {
				if (data.status == 0) {
					//$scope.messageErrReset = false;
					//alert("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
					alertify.success("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
				} else if (data.status == 4) {
					//$scope.messageErrReset = true;
					//alert("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
					alertify.success("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
				} else if (data.status == 3) {
					//alert("Error al intentar enviar e-mail con los nuevos datos");
					//alert("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
					alertify.success("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
				} else {
					//$scope.messageErrReset = true;
					//alert("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
					alertify.success("Si los datos ingresados son correctos, se ha gestionado el nuevo password. Verifique su correo electronico");
				}
			});
	};
	
	$scope.loginFn = function() {
		var parameters = {
			"userName" : $scope.user.userName,
			"userPassword" : $scope.user.password,
			"userDomain" : $scope.valueDomain
	};
		

		webServices.getWSResponsePost($http, 'login', 'login', parameters, $cookieStore,
				function(data, status, headers, config) {
				var cookie = $cookieStore.get('vatesuser');
					if (data.status == 0) {
						cookie.isLoged=true;
//						cookie.userName=$scope.user.userName;
						cookie.menu = data.menu;
						cookie.token = data.token;
						cookie.userId = data.userId;
						cookie.userName = data.userName;
						cookie.showWait = false;
						$cookieStore.remove('vatesuser');
						$cookieStore.put('vatesuser',cookie);
						$window.location.href= "";
						$scope.displayLogin = 'none';

					} else {
						var msgError = window.document.getElementById('messageId');						
						msgError.style.display = "block";						
					}
				});
	};

});

caApp.controller('LogoutController', function(userProperties, $window,
		$cookieStore) {
	$cookieStore.remove('vatesuser');
	userProperties.reset();
	$window.location.href= "";
});

caApp.directive('ckEditor', function() {
	  return {
	    require: '?ngModel',
	    link: function(scope, elm, attr, ngModel) {
	      var ck = CKEDITOR.replace(elm[0]);

	      if (!ngModel) return;

	      ck.on('pasteState', function() {
	        scope.$apply(function() {
	          ngModel.$setViewValue(ck.getData());
	        });
	      });

	      ngModel.$render = function(value) {
	        ck.setData(ngModel.$viewValue);
	      };
	    }
	  };
	});