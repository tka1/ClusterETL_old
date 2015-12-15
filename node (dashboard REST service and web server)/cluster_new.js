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
   /*console.log("All query strings: " + JSON.stringify(req.query));*/
    var id2 = req.query.id;
        var de_cont = req.query.decont;
        var mode = req.query.mode;
        //mode = "'%" + mode + "%'";
     // console.log(mode);
 var query = "SELECT country as label, kountti as value from cluster.country_count(";
 query = query + id2;
         query = query +",";
     query = query + de_cont;
      query = query +",";
      query = query + mode;
     query = query +") order by 2";
 //console.log(query);
  /*client.query('SELECT country as label, kountti as value from cluster.country_count order by 2 desc ',*/
    client.query(query,function(err, result) {
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
    

    
        app.get('/bandcount', function (req, res, response) {
res.setHeader("Access-Control-Allow-Origin", "*");
   /*console.log("All query strings: " + JSON.stringify(req.query));*/
    var id2 = req.query.id;
    var de_cont = req.query.decont  
    var mode = req.query.mode;
    //  console.log(id2);
 var query = 'SELECT band as label, kountti as value from cluster.band_count('
 query = query + id2;
     query = query +',';
     query = query + de_cont;
     query = query +",";
     query = query + mode;
     
     query = query + ') order by 1 desc';
 //console.log(query);
  /*client.query('SELECT country as label, kountti as value from cluster.country_count order by 2 desc ',*/
    client.query(query,function(err, result) {
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
    

    
                 app.get('/cumul', function (req, res, response) {
res.setHeader("Access-Control-Allow-Origin", "*");
   /*console.log("All query strings: " + JSON.stringify(req.query));*/
    var id2 = req.query.id;
    var de_cont = req.query.decont; 
    var mode = req.query.mode;
     // console.log(id2);
 var query = 'SELECT "day/hour" as label, spot_count as value from cluster.cumul_spot_count('
 query = query + id2;
     query = query +','
     query = query + de_cont;
     query = query +',';
     query = query + mode;
     query = query +') ';
 //console.log(query);
  /*client.query('SELECT country as label, kountti as value from cluster.country_count order by 2 desc ',*/
    client.query(query,function(err, result) {
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
       // console.log(querydata);
      retdata = 
      res.json(querydata);
  });
}); 
    
                     app.get('/rows', function (req, res, response) {
res.setHeader("Access-Control-Allow-Origin", "*");
   /*console.log("All query strings: " + JSON.stringify(req.query));*/
    var id = req.query.id;
    var de_cont = req.query.decont; 
   
      console.log(de_cont);
 var query = 'SELECT * from cluster.latestrows('
 query = query + id;
    query = query +','
     query = query + de_cont;
      query = query +') ';
// console.log(query);
  /*client.query('SELECT country as label, kountti as value from cluster.country_count order by 2 desc ',*/
    client.query(query,function(err, result) {
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
        //console.log(querydata);
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

