# Elipse

Ein Einteilungs Interface für das PSE.

## Setup

You must have [Gurobi](http://www.gurobi.com/) installed.
Gurobi requires a few environment variables.

    GUROBI_HOME=... # wherever you unpack it
    GRB_LICENSE_FILE={$GUROBI_HOME}/gurobi.lic
    LD_LIBRARY_PATH={$GUROBI_HOME}/lib:{$LD_LIBRARY_PATH}

The actual code is in `implementierung/ellipse/`.
It uses sbt, but it can bootstrap itself.
The included jar file must be available.

    cd implementierung/ellipse/
    cp ${GUROBI_HOME}/lib/gurobi.jar lib/
    
## Running in Dev Mode

There are different ways to start the development server, the following way is the easiest:

    bin/activator run

After some time,
the application should be available at `127.0.0.1:9000`.

## Production Deployment

First follow the previous instructions for the setup,
then build a dist package.

    bin/activator dist

This creates a zip file in `target/universal/`.

To run the application, unzip the file on the target server, 
and then run the script in the bin directory. The name of the script 
is your application name, and it comes in two versions, a bash shell script, 
and a windows .bat script.

As one definitely needs further configuration of play, one needs to specify an additional configuration file:

    target/universal/stage/bin/my-first-app -Dconfig.file=/full/path/to/conf/application-prod.conf

This file is needed to specify an application secret and configuring the production setup.
The final file can look like this:

    include "application.conf" #Config file has to include the standard configuration
    play.crypto.secret="yourSecretHere" #This variable needs to be changed, otherwise an error will be thrown

It is recommended to specify a different database in your configuration file, 
e.g MySql, otherwise a SQLite database will be used:
https://www.playframework.com/documentation/2.5.x/JavaDatabase#mysql-database-engine-connection-properties

Beware: in the build file only connectors for MySQL and SQLite are added as dependencies, if you need a different connector change the build.sbt file and run 'bin/activator dist' again.
    
The activator ships with a secret-generator one can use:
https://www.playframework.com/documentation/2.5.x/ApplicationSecret#generating-an-application-secret
    
Use these instructions to run play with a different front end web server, e.g. Apache:
https://www.playframework.com/documentation/2.5.x/HTTPServer
    
For https read following instructions:
https://www.playframework.com/documentation/2.5.x/ConfiguringHttps
