var express = require('express');
var app = express();
var fs = require("fs");
var pg = require('pg');

var conString = "postgres://postgres:powerday1!@127.0.0.1:5432/postgres";
var client = new pg.Client(conString);
client.connect(function(err) {
  if(err) {
    return console.error('could not connect to postgres', err);
  }

app.get('/users', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
       console.log( data );
       res.end( data );
   });
})


app.get('/countrycount', function (req, res, next) {
res.setHeader("Access-Control-Allow-Origin", "*");
 

  client.query('SELECT country as label, kountti as value from cluster.country_count order by 2 desc ', function(err, result) {
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
    app.get('/bandcount', function (req, res, next) {
res.setHeader("Access-Control-Allow-Origin", "*");
 

  client.query('SELECT band as label,kountti as value from cluster.band_count order by 1 desc ', function(err, result) {
    if(err) {
      return console.error('error running query', err);
    }
   // console.log(result.rows);
	
	//console.log(data3);
      
     var querydata = {};
            var data = []
            querydata.data = data;
      
      var obj = result.rows;
      for (i = 0; i < obj.length; i++) {
           querydata.data.push(obj[i]);
      }
          
      retdata = 
      res.json(querydata);
 
	
    //output: Tue Jan 15 2013 19:12:47 GMT-600 (CST)
    //client.end();
  });
});
    
})



var server = app.listen(8081, function (req, res) {
  var host = server.address().address
  var port = server.address().port
  

  console.log('Listening to ', port);
}); 

