var app = angular.module("myApp",["ng-fusioncharts"])
//var clusternameInput = element(by.binding('cluster.address'));

app.controller('timecontrl', function ($scope, $http, $timeout, $interval) {

      $scope.attrs = {
        startingangle: "120",
        showlabels: "0",
        showlegend: "1",
        enablemultislicing: "0",
        slicingdistance: "15",
        showpercentvalues: "1",
        showpercentintooltip: "0",
        "paletteColors": "#0075c2",

        theme: "zune"
          
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
          var cluster = document.getElementById("clusters").value;
        var de_continent = document.getElementById("de_continent").value;
        var mode = document.getElementById("mode").value;
          var dx = document.getElementById("dx").value;
           //console.log(dx);
        var dxselection ='';
       
        if (dx == 'DX'){dxselection=de_continent};
         if (dx == 'ALL'){dxselection=''};
     
      var  url = "http://localhost:8081/countrycount?id='";
       


        url = url + cluster + "'";
        url = url+ "&decont='" + de_continent +"'";
        url = url + "&mode='" + mode + "'";
   
     // console.log(url);
        $http.get(url).
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.count = $scope.count + 1;
            $scope.greeting = data;
            var d = new Date();
             $scope.clock = d.toDateString() + " " + d.getHours() + ":" + d.getMinutes() ;
           // console.log($scope.greeting);
            
         }); 
        
      
     var url2 = "http://localhost:8081/bandcount?id='";
        url2 = url2 + cluster + "'";
            url2 = url2+ "&decont='" + de_continent + "'";
         url2 = url2 + "&mode='" + mode + "'";
        console.log(url2);
           $http.get(url2).
         success(function(data, status, headers, config) {
         $scope.bandcount = data;
         
              $scope.myDataSource = data; 
            //console.log($scope.bandcount.rows);
                $scope.attrs = {
       
    };  
                        
         });
       
    var url3 = "http://localhost:8081/cumul?id='";
      url3 = url3 + cluster + "'";
        url3 = url3+ "&decont='" + de_continent + "'";
         url3 = url3 + "&mode='" + mode + "'";
         console.log(url3);
        //console.log(cluster.value);
              $http.get(url3).
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.cumul = data;
                 
            
          
            
         }); 
        
         var url4 = "http://localhost:8081/rows?id='";
      url4 = url4 + cluster + "'";
        url4 = url4+ "&decont='" + de_continent + "'";
        url4 = url4 + "&mode='" + mode + "'";
         url4 = url4+ "&dxfrom='" + dxselection + "'";
        
         console.log(url4);
        //console.log(cluster.value);
              $http.get(url4).
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.rows = data;
                 
            
          
            
         }); 
                    
    };

    $scope.start = function() {
        
        
       $scope.myCall = $interval($scope.ajaxPeriodicall, 7000);    
           
        


    };

    $scope.stop = function() {
       $interval.cancel($scope.myCall);   
    };
    
    
    
})