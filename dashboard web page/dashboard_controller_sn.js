var app = angular.module("myApp",["ng-fusioncharts"])
//var dx_call ='';
//var clusternameInput = element(by.binding('cluster.address'));

app.controller('timecontrl', function ($scope, $http, $timeout, $interval) {
    
    var resturl = "http://91.156.129.239:8081/" ;
    

      $scope.attrs = {
        startingangle: "120",
        showlabels: "1",
        showlegend: "0",
        enablemultislicing: "0",
        slicingdistance: "15",
        showpercentvalues: "1",
        showpercentintooltip: "0",
        "paletteColors": "#0075c2",

        theme: "zune"
          
    };
     $scope.avg_attrs = {
        "caption": "Average SNR",
        theme: "zune"
          
    };
          $scope.band = {
           data:[{
        label: "null",
        value: "0"
        }]
  };
          $scope.countries = {
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
    
     $scope.averagesnr = {
           data:[{
        label: "",
        value: "0"
        }]
  };
                   
                   
    $scope.count = 0;
    $scope.ajaxPeriodicall = function() {
          var cluster = document.getElementById("clusters").value;
        var de_continent = document.getElementById("de_continent").value;
        var mode = document.getElementById("mode").value;
         var dx = document.getElementById("dx").value;
        var filter= document.getElementById("filter on").checked;
              
        //var dxcall = $scope.dxcall;
          var dxselection ='';
       
        if (dx == 'DX'){dxselection=de_continent};
         if (dx == 'ALL'){dxselection=''};
        
     
      var  url = resturl + "countrycount?id='";
       


        url = url + cluster + "'";
        url = url+ "&decont='" + de_continent +"'";
        url = url + "&mode='" + mode + "'";
   
      //console.log(dx_call);
        $http.get(url).
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            //$scope.count = $scope.count + 1;
            $scope.countries = data;
            var d = new Date();
             $scope.clock = d.toDateString() + " " + d.getHours() + ":" + d.getMinutes() ;
            var time = d.toDateString() + " " + d.getHours() + ":" + d.getMinutes() ;
           
            
         }); 
        
      
     var url2 = resturl + "bandcount?id='";
        url2 = url2 + cluster + "'";
            url2 = url2+ "&decont='" + de_continent + "'";
         url2 = url2 + "&mode='" + mode + "'";
          $http.get(url2).
         success(function(data, status, headers, config) {
           
              $scope.band = data; 
              $scope.attrs = {
       
    };  
                        
         });
       
    var url3 = resturl + "cumul?id='";
      url3 = url3 + cluster + "'";
        url3 = url3+ "&decont='" + de_continent + "'";
         url3 = url3 + "&mode='" + mode + "'";
          $http.get(url3).
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.cumul = data;
                 
            
          
            
         }); 
        
         var url4 = resturl + "rows?id='";
      url4 = url4 + cluster + "'";
        url4 = url4+ "&decont='" + de_continent + "'";
        url4 = url4 + "&mode='" + mode + "'";
         url4 = url4+ "&dxfrom='" + dxselection + "'" ;
      //  console.log(url4);
       $http.get(url4).
         success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.rows = data;
                 }); 
        
        
          //console.log($scope.dx_call);
      //var dxcall = '';
       
      
      if (filter) {
       
        var dx = $scope.dx_call;
          if (dx == ''){dx = '.'};
            var dxcall = dx.toUpperCase(); 
          dxcall = dxcall + "%";
                   
        var url5 = resturl + "dxrows?id='";
        url5 = url5 + cluster + "'";
        url5 = url5+ "&dxcall='" + dxcall + "'";
          url5 = encodeURI(url5);
          url5 = url5+ "&decont='" + de_continent + "'";
          url5 = url5+ "&mode='" + mode + "'";
           // console.log(url5);
        $http.get(url5).
        success(function(data, status, headers, config) {
        // this callback will be called asynchronously
        // when the response is available
        $scope.dxrows = data;
                }); 
          
               var url6 = resturl + "averagesnr?id='";
        url6 = url6 + cluster + "'";
        url6 = url6+ "&dxcall='" + dxcall + "'";
          url6 = encodeURI(url6);
          url6 = url6+ "&decont='" + de_continent + "'";
          url6 = url6+ "&mode='" + mode + "'";
           // console.log(url5);
        $http.get(url6).
        success(function(data, status, headers, config) {
        // this callback will be called asynchronously
        // when the response is available
        $scope.averagesnr = data;
              //console.log(data);
             }); 
          
      }
         if (!filter) {
             $scope.dxrows = '';
         }
        
    
      
    };
    
      
    $scope.start = function() {
        
        
       $scope.myCall = $interval($scope.ajaxPeriodicall, 10000);    
           
        


    };

    $scope.stop = function() {
       $interval.cancel($scope.myCall);   
    };
    
    
    
})