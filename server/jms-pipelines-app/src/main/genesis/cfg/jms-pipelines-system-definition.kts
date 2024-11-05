/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide system definition config for multi-pro-code-test.
 *
 * Modification History
 */
systemDefinition {
    global {

    }

    systems {
        // the following have been set up as host specific definitions to be used as part of the pipelines scripts, each host will need to have these defined
        system(name = "DEV") {

            hosts {
                host(LOCAL_HOST)
            }
            item("HOST_NAME", "mqserver")
            item("PORT", 1414)
            // ensure the queue manager name matches with the environment variable set in the docker-compose.yml
            item("QUEUE_MANAGER", "QM1")
            item("TRANSPORT_TYPE", 1)
            item("CHANNEL", "DEV.ADMIN.SVRCONN")
            // this is the name of the queue that gets automatically generated with the docker container
            // if you want to use another queue, ensure that this has been created on the IBM MQ instance before attempting to send messages to it
            item("QUEUE_NAME", "DEV.QUEUE.1")
        }
    }

}