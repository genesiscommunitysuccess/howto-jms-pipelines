package scripts

import com.ibm.mq.jms.MQQueueConnectionFactory
import global.genesis.pipeline.api.db.DbOperation
import javax.jms.TextMessage

/**
 *
 *   System              : jms-pipelines
 *   Sub-System          : jms-pipelines Configuration
 *   Version             : 1.0
 *   Copyright           : (c) GENESIS
 *   Date                : 2021-09-07
 *
 *   Function : Provide Data Pipeline Configuration for jms-pipelines.
 *
 *   Modification History
 *
 */

val source = jmsSource {
    // ensure whatever queue you use has been created on the IBM MQ instance before attempting to send messages to it
    queueName = systemDefinition.getItem("QUEUE_NAME").toString()
    // configure the IBM MQ connection factory as per your requirement using pre-defined values in the system definitions
    // note: the factory can be any instance of QueueConnectionFactory - this includes IBM MQ, Active MQ etc.
    factory = MQQueueConnectionFactory().apply {
        hostName = systemDefinition.getItem("HOST_NAME").toString()
        port = systemDefinition.getItem("PORT").toString().toInt()
        queueManager = systemDefinition.getItem("QUEUE_MANAGER").toString()
        transportType = systemDefinition.getItem("TRANSPORT_TYPE").toString().toInt()
        channel = systemDefinition.getItem("CHANNEL").toString()
    }
    // the following username and password can be encrypted into the system definitions, see https://docs.genesis.global/docs/develop/server-capabilities/runtime-configuration/system-definition/#items-defined#items-defined
    // this must match the admin password set up in the docker-compose.yml
    username = "admin"
    password = "AdminPassword123"
    // if the pipeline is unable to connect to the message queue, the process will move to WARNING state and the message displayed will contain the following monitorName
    // note: this is an optional parameter, if not provided a default monitor name will be generated based on the queue name
    monitorName = "MQ_TO_DB_SOURCE"
}

pipelines {
    pipeline("MQ_TO_DB_PIPELINE") {
        // sourcing from the IBM MQ source as defined above
        source(source)
            // filter for all messages that are of type TextMessage
            .filter { it is TextMessage }
            .map {
                // construct MessageReceived DAO object using Message element knowing it is of type TextMessage
                // leaving id as null as it will be auto generated when inserting into the database
                MessageReceived(null, (it as TextMessage).text)
            }
            // convert each MessageReceived object into a DbOperation to insert into the database
            .map {
                // in order to use the database sink we must provide it a DbOperation to perform - in this case it's an insert on each MessageReceived object provided by the above operation
                DbOperation.Insert(it)
            }
            // here we are using a simple database sink to perform the above operation - txDbSink is a transactional database sink
            // if you would like to use a non-transactional database sink, simple replace txDbSink() with dbSink()
            .sink(txDbSink())
    }
}
