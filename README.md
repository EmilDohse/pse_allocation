# Elipse

Ein Einteilungs Interface für das PSE.

## Running in Dev Mode

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
    bin/activator run

After some time,
the application should be available at `127.0.0.1:9000`.

## Production Deployment

For production,
build a dist package.

    bin/activator dist

This creates a zip file in `target/universal/`.

TODO What to do with the zip file?
