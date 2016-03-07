var express = require('express');
var app = express();
var fs = require("fs");
var pg = require('pg');

var conString = "postgres://cluster:Saturnus1!@localhost:5432/postgres";
var client = new pg.Client(conString);
client.connect(function(err) {
  if(err) {
    return console.error('could not connect to postgres', err);
  }





    
    app.get('/countrycount', function (req, res, response) {
res.setHeader("Access-Control-Allow-Origin", "*");
     res.setHeader('Cache-Control', 'public, max-age=30');
       var id2 = req.query.id;
        var de_cont = req.query.decont;
        var mode = req.query.mode;
     
    client.query("SELECT country as label, kountti as value from cluster.country_count where title =$1 and de_continent = $2 and mode =$3 order by 2 desc",[id2,de_cont,mode],function(err, result) {
    if(err) {
      return console.error('error running query', err);
    }
        var querydata = {};
         var chart = {"caption": "COUNTRIES FROM LAST HOUR","showValues": "1","theme" : "fint"};
       
          querydata.chart = chart;
            var data = []
            querydata.data = data;
            var obj = result.rows;
      for (i = 0; i < obj.length; i++) {
           querydata.data.push(obj[i]);
      }
      retdata = 
      res.json(querydata);
  });
});
    

    
        app.get('/bandcount', function (req, res, response) {
res.setHeader("Access-Control-Allow-Origin", "*");
           res.setHeader('Cache-Control', 'public, max-age=30');
    var id2 = req.query.id;
    var de_cont = req.query.decont  
    var mode = req.query.mode;

    client.query("SELECT band as label, kountti as value from cluster.band_count where title = $1 and de_continent = $2 and mode =$3 order by 1 desc",[id2,de_cont,mode],function(err, result) {
    if(err) {
      return console.error('error running query', err);
    }
        var querydata = {};
        var chart = {"caption": "LAST HOUR DISTINCT CALLSIGNS / BAND","xAxisname": "BAND","showValues": "1","theme" : "zune"};
       
          querydata.chart = chart;
            var data = []
            querydata.data = data;
            var obj = result.rows;
      for (i = 0; i < obj.length; i++) {
           querydata.data.push(obj[i]);
      }
      retdata = 
      res.json(querydata);
  });
});
    

    
                 app.get('/cumul', function (req, res, response) {
res.setHeader("Access-Control-Allow-Origin", "*");
                    res.setHeader('Cache-Control', 'public, max-age=30');

    var id2 = req.query.id;
    var de_cont = req.query.decont; 
    var mode = req.query.mode;

    client.query("SELECT day_hour as label, spot_count as value from cluster.cumul_spot_count where title = $1 and de_continent = $2 and mode =$3",[id2,de_cont,mode],function(err, result) {
    if(err) {
      return console.error('error running query', err);
    }
        var querydata = {};
         var chart = {"caption": "DISTINCT CALLSIGNS FROM LAST 7 HOURS","xAxisname": "DAY/HOUR","showValues": "1","theme" : "zune"};
       
          querydata.chart = chart;
            var data = []
            querydata.data = data;
            var obj = result.rows;
      for (i = 0; i < obj.length; i++) {
           querydata.data.push(obj[i]);
      }
          retdata = 
      res.json(querydata);
  });
}); 
    
                     app.get('/rows', function (req, res, response) {
                         res.setHeader("Access-Control-Allow-Origin", "*");
res.setHeader('Cache-Control', 'public, max-age=30');
 
    var id = req.query.id;
    var de_cont = req.query.decont; 
    var mode = req.query.mode; 
   var dx_from = req.query.dxfrom; 
 
    client.query("SELECT * from cluster.latestrows where title =$1  and de_continent =$2 and mode = $3 and dx_continent != $4 order by insert_time desc limit 30 ",[id,de_cont,mode,dx_from],function(err, result) {
    if(err) {
      return console.error('error running query', err);
    }
        var querydata = {};
            var data = []
            querydata.data = data;
            var obj = result.rows;
      for (i = 0; i < obj.length; i++) {
           querydata.data.push(obj[i]);
      }
             retdata = 
      res.json(querydata);
//console.log(querydata);
  });
}); 
    
                 app.get('/dxrows', function (req, res, response) {
                         res.setHeader("Access-Control-Allow-Origin", "*");
res.setHeader('Cache-Control', 'public, max-age=30');
  
    var id = req.query.id;
    var dxcall = req.query.dxcall; 
    var de_cont = req.query.decont; 
    var mode = req.query.mode; 
 
    client.query("SELECT * from cluster.dxrows where title =$1 and dxcall like $2 and de_continent =$3 and mode = $4 order by insert_time desc limit 30",[id,dxcall.substring(0,10),de_cont,mode],function(err, result) {
    if(err) {
      return console.error('error running query', err);
    }
        var querydata = {};
            var data = []
            querydata.data = data;
            var obj = result.rows;
      for (i = 0; i < obj.length; i++) {
           querydata.data.push(obj[i]);
      }
             retdata = 
      res.json(querydata);
  });
});
    
   
    
})



var server = app.listen(8081, function (req, res) {
  var host = server.address().address
  var port = server.address().port
  

  console.log('Listening to ', port);
}); 

