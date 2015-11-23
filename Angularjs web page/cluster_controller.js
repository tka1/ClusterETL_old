function timecontrl($scope, $http, $timeout, $interval) {

    $scope.count = 0;
    $scope.ajaxPeriodicall = function() {
        
        $http.get('http://w520:8081/time').
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.count = $scope.count + 1;
            $scope.greeting = data;
             $scope.clock = new Date();
           // console.log($scope.greeting);
            
         }); 
        
           $http.get('http://w520:8081/band').
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            //$scope.count = $scope.count + 1;
            $scope.bandcount = data;
             //$scope.clock = new Date();
            console.log($scope.bandcount);
            
         }); 
       
    };

    $scope.start = function() {
        
       $scope.myCall = $interval($scope.ajaxPeriodicall, 3000);        
    };

    $scope.stop = function() {
       $interval.cancel($scope.myCall);   
    };
    
    
    
}