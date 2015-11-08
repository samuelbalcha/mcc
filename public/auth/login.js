angular.module('whatsOut')
    .controller('LoginCtrl', function($scope, $location, $auth, toastr) {
        $scope.login = function() {
            $auth.login($scope.user)
                .then(function() {
                    toastr.success('You have successfully signed in');
                    $location.path('/');
                })
                .catch(function(response) {
                    toastr.error(response.data.message, response.status);
                });
        };
        $scope.authenticate = function(provider) {
            $auth.authenticate(provider)
                .then(function() {
                    toastr.success('You have successfully signed in with ' + provider);
                    $location.path('/');
                })
                .catch(function(response) {
                    toastr.error(response.data.message);
                });
        };
    });

angular.module('whatsOut')
    .controller('SignupCtrl', function($scope, $location, $auth, toastr) {
        $scope.signup = function() {
            $auth.signup($scope.user)
                .then(function(response) {
                    $auth.setToken(response);
                    $location.path('/');
                    toastr.info('You have successfully created a new account and have been signed-in');
                })
                .catch(function(response) {
                    toastr.error(response.data.message);
                });
        };
    });

angular.module('whatsOut')
    .controller('LogoutCtrl', function($location, $auth, toastr) {
        if (!$auth.isAuthenticated()) { return; }
        $auth.logout()
            .then(function() {
                toastr.info('You have been logged out');
                $location.path('/');
            });
    });
