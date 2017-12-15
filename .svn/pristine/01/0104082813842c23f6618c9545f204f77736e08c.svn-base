caApp.controller('MainMenuController', function($scope, breadcrumbs, $http,
		$cookieStore, userProperties, FiltroFactura, IndexFactura, FiltroOC, FiltroReportOC, utilidades, FiltroProveedor) {
	$http.pendingLocalRequests = 0;
	utilidades.resetPath($cookieStore);
	if (typeof ($cookieStore.get('vatesuser')) == "undefined") {
		$cookieStore.put('vatesuser', userProperties.getUser());
		var cookie = $cookieStore.get('vatesuser');
		cookie.isLoged = false;
		cookie.showWait = false;
		cookie.menu = userProperties.getUser().menu;
		$cookieStore.put('vatesuser', cookie);
	}

	$scope.items = $cookieStore.get('vatesuser').menu;
	$scope.clearFilters = function() {
		utilidades.resetPath($cookieStore);
		FiltroFactura.data = [];
		FiltroFactura.dataConsult = [];
		FiltroFactura.page = null;
		IndexFactura.index = null;
		IndexFactura.facId = null;
		IndexFactura.facAdmId = null;
		IndexFactura.tableRowExpanded = '';
		IndexFactura.tableRowIndexExpandedCurr = '';
		IndexFactura.tableRowIndexExpandedPrev = '';
		IndexFactura.dataCollapse = [];
		IndexFactura.showPDF = false;
		IndexFactura.pdfURL = '';
		FiltroOC.data = [];
		FiltroOC.page = null;
		FiltroOC.initial =[];
		FiltroProveedor.data = [];
		FiltroProveedor.page = null;
		FiltroProveedor.initial =[];
		FiltroReportOC.dataConsult = [];

	};
	
//	$scope.getBack(){
//		utilidades.resetPath($cookieStore);
//	};
	
	$scope.getBack = function($index){
		utilidades.resetPathBreadCrumb($cookieStore, $index);
	};
	$scope.isLoged = $cookieStore.get('vatesuser').isLoged;
	$scope.userName = $cookieStore.get('vatesuser').userName;
	$scope.beVersion = $cookieStore.get('vatesuser').version;
	$scope.breadcrumbs = breadcrumbs;
	$scope.myAppVersion = getAppVersion();

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
		$scope.modalLoginPass = true;
	};
	
	 $scope.afterShow = function() {
		 $('#inptNameId').focus();
	 };
	
	$scope.closeLogin = function() {
		$scope.modalLoginPass = false;
		clear();
	};

	function clear() {
		var msgError = window.document.getElementById('messageId');
		msgError.style.display = "none";
		
		var form = window.document.getElementById('formLoginId');
		form.reset();
	}
	
});

caApp.controller('MainController', function($scope, $location) {

	$scope.message = 'Esta es la p�gina ' + $location.path();

});

caApp.controller('LoginController', function($scope, $http, $routeParams,
		breadcrumbs, userProperties, $window, $cookieStore, webServices) {
	$scope.breadcrumbs = breadcrumbs;

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

	$scope.loginFn = function() {
		var parameters = {
			"userName" : $scope.user.userName,
			"userPassword" : $scope.user.password,
			"userDomain" : $scope.valueDomain
		};

		webServices.getWSResponsePost($http, 'login', 'login', parameters,
				$cookieStore, function(data, status, headers, config) {
					var cookie = $cookieStore.get('vatesuser');
					if (data.status == 0) {
						cookie.isLoged = true;
						cookie.menu = data.menu;
						cookie.token = data.token;
						cookie.userId = data.userId;
						cookie.userName = data.userName;
						cookie.showWait = false;
						cookie.version = "("+data.version+")";
						$cookieStore.remove('vatesuser');
						$cookieStore.put('vatesuser', cookie);
						$window.location.href = "";
						$scope.displayLogin = 'none';
						location.reload();

					} else {
						$scope.user.password = '';
						var msgError = window.document
								.getElementById('messageId');
						msgError.style.display = "block";
					}
				});
	};

});

caApp.controller('LogoutController', function(userProperties, $window,
		$cookieStore) {
	$cookieStore.remove('vatesuser');
	userProperties.reset();
	$window.location.href = "";
});

caApp.directive('ckEditor', function() {
	return {
		require : '?ngModel',
		link : function(scope, elm, attr, ngModel) {
			var ck = CKEDITOR.replace(elm[0]);

			if (!ngModel)
				return;

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