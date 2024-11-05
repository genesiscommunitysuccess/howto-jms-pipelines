# jms-pipelines

# Introduction

This application has been designed to demonstrate how the Genesis Integrations module `jms-genesis` can be used to send to and receive from an IBM Message Queue.
Although this project uses IBM Message Queue as the provider for JMS, any other implementation would work in the exact same way.
Two pipelines have been constructed to demonstrate these use cases. The first pipeline is sourced by an event handler and uses the data from the event to publish
to a message queue. The second pipeline then reads from this message queue and inserts it into the database. 
We have split these pipelines into 2 different scripts so that they can be run by 2 separate processes.
The example uses two simple tables called `MESSAGE_SENT` and `MESSAGE_RECEIVED` which both contain an auto generated primary key called `ID` and a `TEXT` String field
which will be used to store the message from the queue. For simplicity, we will be sending TextMessage objects to the queue.

When integrating with JMS in your Genesis application, there are a few set up steps which must be completed. 
Overviews for each step have been provided below and further detail can be found in the relevant linked files:

1. Set up docker image of IBM Message Queue: 
You have the option to host your own JMS provider and replace the respective system definitions with the ones for your instance (see step 4)
but for this example we show how to use docker to run an image of IBM Message Queue, see [docker-compose.yml](docker-compose.yml).

> ⚠️ Note: this docker instance will only work for Windows. If you're using another operating system, update the docker compose file with a container configuration compatible with your machine. Alternatively, connect to a hosted MQ - check step 4.

2. Add dependencies:
There are 3 dependencies which we need to add to the `build.gradle.kts` file of our app. See [build.gradle.kts](server/jms-pipelines-app/build.gradle.kts).

3. Set up the process: 
We will need separate processes for:
   - data supplied to the GUI
   - events + pipelines linked to event handlers
   - all other pipelines
   
In order to set up the processes you need to add certain configurations to your process definition, see [jms-pipelines-processes.xml](server/jms-pipelines-app/src/main/genesis/cfg/jms-pipelines-processes.xml).
You will also need to add the corresponding service definitions, see [jms-pipelines-service-definitions.xml](server/jms-pipelines-app/src/main/genesis/cfg/jms-pipelines-service-definitions.xml).

4. Set up system definitions:
You have the option to hard code your configurations into your pipelines script but for this example we show how to set up system definitions that will be called on inside the script,
see [jms-pipelines-system-definition.kts](server/jms-pipelines-app/src/main/genesis/cfg/jms-pipelines-system-definition.kts).
Using system definitions is more common, as it is likely the connection details for IBM Message Queue will need to be different in each environment.

5. Set up event handler: 
In order to get the event to source the data for the pipeline, we need to create an implementation of the AbstractProgrammaticSource. See [ProgrammaticMessageSource](server/jms-pipelines-app/src/main/kotlin/global/genesis/jms/ProgrammaticMessageSource.kt).
You can now write the event handler which will trigger the pipeline to send this message to the queue, see [jms-pipelines-eventhandler.kts](server/jms-pipelines-app/src/main/genesis/scripts/jms-pipelines-eventhandler.kts).

6. Write the pipelines scripts: 
You can now start writing your pipelines to send to and consume from the message queue that you've set up in the docker configuration:
   - see [jms-event-pipelines.kts](server/jms-pipelines-app/src/main/genesis/scripts/jms-event-pipelines.kts) for the JMS sink pipeline.
   - see [jms-pipelines.kts](server/jms-pipelines-app/src/main/genesis/scripts/jms-pipelines.kts) for the JMS source pipeline.

## Running the app

To get this application running, as mentioned in the docker-compose.yml, first run `docker-compose up -d` in the directory of the docker-compose.yml to start up the docker IBM Message Queue instance. 
Then check the [Quick Start](https://docs.genesis.global/docs/develop/development-environments/) guide on how to start and run a How To guide.

If you need an introduction to the Genesis platform and its modules it's worth heading [here](https://docs.genesis.global/docs/develop/platform-overview/).
# Test results
BDD test results can be found [here](https://genesiscommunitysuccess.github.io/howto-jms-pipelines/test-results)
