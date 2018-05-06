// set up ======================================================================
var express = require('express');
var app = express(); 						// create our app w/ express
var port = process.env.PORT || 8080; 				// set the port


// configuration ===============================================================

app.use(express.static('./public')); 		// set the static files location /public/img will be /img for users


// listen (start app with node server.js) ======================================
app.listen(port);
console.log("App listening on port " + port);
