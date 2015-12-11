
var express = require('express');
var app = express();

app.use(express.static(__dirname + '/dashboard'));

app.listen(process.env.PORT || 3000);

  console.log('Listening to ', 3000);