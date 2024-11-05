package global.genesis.jms

import global.genesis.gen.dao.MessageSent
import global.genesis.pipeline.event.AbstractProgrammaticSource

// this is a realtime source which has an extra method called 'send', allowing us to send objects of the type we have initialised to the source flow
// see jms-pipelines-eventhandler.kts to see how this is used
object ProgrammaticMessageSource : AbstractProgrammaticSource<MessageSent>()