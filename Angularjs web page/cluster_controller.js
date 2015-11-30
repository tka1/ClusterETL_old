var app = angular.module("myApp",["ng-fusioncharts"])
app.controller('timecontrl', function ($scope, $http, $timeout, $interval) {
      $scope.attrs = {
        startingangle: "120",
        showlabels: "0",
        showlegend: "1",
        enablemultislicing: "0",
        slicingdistance: "15",
        showpercentvalues: "1",
        showpercentintooltip: "0",

        theme: "fint"
          
    };
          $scope.myDataSource = {
           data:[{
        label: "null",
        value: "0"
        }]
  };
          $scope.greeting = {
             data:[{
        label: "null",
        value: "0"
    
    }]
  };
    
     $scope.cumul = {
           data:[{
        label: "null",
        value: "0"
        }]
  };
                   
    $scope.count = 0;
    $scope.ajaxPeriodicall = function() {
        
        $http.get('http://192.168.1.33:8081/countrycount').
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.count = $scope.count + 1;
            $scope.greeting = data;
            var d = new Date();
             $scope.clock = d.toDateString() + " " + d.getHours() + ":" + d.getMinutes() ;
           // console.log($scope.greeting);
            
         }); 
        
           $http.get('http://192.168.1.33:8081/bandcount').
         success(function(data, status, headers, config) {
         $scope.bandcount = data;
         
              $scope.myDataSource = data; 
            //console.log($scope.bandcount.rows);
                $scope.attrs = {
       
    };  
                        
         });
        
              $http.get('http://192.168.1.33:8081/cumul').
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.cumul = data;
            
          
            
         }); 
                    
    };

    $scope.start = function() {
        
        
       $scope.myCall = $interval($scope.ajaxPeriodicall, 5000);        
    };

    $scope.stop = function() {
       $interval.cancel($scope.myCall);   
    };
    
    
    
})