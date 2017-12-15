
caApp.controller('HelpController', function($scope, $location) {

    $scope.message = 'Esta es la página ' + $location.path();

});

