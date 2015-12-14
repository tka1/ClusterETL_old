# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

With DX cluster dashboard you can see on line statistics from CW/RTTY skimmer or from Reverse Beacon network.

There is 4 different application:
ETl routine for saving data to database, Java application
REST service, Node.js
Web application (SPA page, Angularjs / Fusion chart) 
WEB server, node.js





### How do I get set up? ###

* install Java
* install Postgresql
	connect to db using PGADMIN
	run schema_install script.
* install Node.js
	make new directory, example clusteretl
	go to to new directory
	install express
	install fs
	install pg
	copy cluster.js to directory
	run service in command prompt: node cluster.js
	
	
*  
*

### How do run the application ###

* The user interface will be visible in the localhost:3000


### Mongodb - related configurations ###
* Mongo db is running on port 3001. You may access it by this command "mongo localhost:3001"
* Inside mongo a "meteor" database is created
* the application initialy creates the "users" collection. The other collections will be created during the usage of the program.


### Mongodb - some examples/useful mongo commands ###
* shell: "meteor mongo" (opens command line connection to mongodb - this command must be done in meteor-application directory)
* mongo: "use meteor" (switches to "meteor" database inside the mongodb)
* mongo: "show collections" (shows db collections)
* mongo: "db.users.update({_id:"u2jhg7NXqkuJkET8p" },{$set: { roles : "admin", }})"
* mongo: "db.waters.find({},{geo_place_name:1})"
* mongo: "db.catches.find().pretty()"
* mongo: "load("../files/init_mongo.js")" <-- this can be used to load initial data to mongodb
### Configurations and dependencies ###
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact