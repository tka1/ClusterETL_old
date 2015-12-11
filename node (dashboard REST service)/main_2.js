var express = require('express');
var app = express();
var fs = require("fs");
var pg = require('pg');

var conString = "postgres://postgres:password@127.0.0.1:5432/postgres";
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


app.get('/time', function (req, res, next) {
res.setHeader("Access-Control-Allow-Origin", "*");
 

  client.query('SELECT * from cluster.country_count order by 1 desc ', function(err, result) {
    if(err) {
      return console.error('error running query', err);
    }
    //console.log(result.rows);
	
	//console.log(data3);
	res.json( result );
	 res.end();
	
    //output: Tue Jan 15 2013 19:12:47 GMT-600 (CST)
    //client.end();
  });
});
})



var server = app.listen(8081, function (req, res) {
  var host = server.address().address
  var port = server.address().port
  
  
  
 
  //console.log("Example app listening at http://%s:%s", host, port)
 
 //var server = require('http').createServer(function(req, res) {
 //var headerStr =JSON.stringify(req.headers);

   //set response header
  //res.setHeader("Access-Control-Allow-Origin", "*");

  //res.write(headerStr);
  //res.end();
//}); 
//var port = process.env.PORT || 8081;
//server.listen(port);
//server.on('listening', function(){
  console.log('Listening to ', port);
}); 

