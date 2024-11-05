import com.ibm.mq.jms.MQQueueConnectionFactory
import global.genesis.jms.ProgrammaticMessageSource

// initialise your sink by providing the type of element the sink can expect to receive
val sink = jmsSink<MessageSent> {
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
    // here we provide a lambda which will be used to convert any element provided to the sink into a valid Message object using the JMS Session provided by the sink
    // element here will be of type MessageSent as defined on line 5 and for simplicity we provide a TextMessage object
    buildMessage { element, session -> session.createTextMessage(element.text) }
    // if the pipeline is unable to connect to the message queue, the process will move to WARNING state and the message displayed will contain the following monitorName
    // note: this is an optional parameter, if not provided a default monitor name will be generated based on the queue name
    monitorName = "EVENT_TO_MQ_SINK"
}

pipelines {
    pipeline("EVENT_TO_MQ_PIPELINE") {
        // here we are using the ProgrammaticMessageSource which allows the event to send data through to the source
        source(ProgrammaticMessageSource)
            // adding a map node so that the sink recognises the element type
            .map { it }
            // sourcing from the IBM MQ sink as defined above
            .sink(sink)
    }
}