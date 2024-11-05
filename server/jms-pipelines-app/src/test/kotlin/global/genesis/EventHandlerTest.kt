/**
 * This file tests the event handler APIs.

 * The event EVENT_MESSAGE_SENT_INSERT is tested
 * Full documentation on event handler tests may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/#integration-testing

 */

import global.genesis.db.rx.entity.multi.SyncEntityDb
import global.genesis.gen.config.tables.MESSAGE_SENT
import global.genesis.gen.dao.MessageSent
import global.genesis.jms.ProgrammaticMessageSource
import global.genesis.message.core.event.EventReply
import global.genesis.pipeline.api.PipelineContext
import global.genesis.testsupport.client.eventhandler.EventClientSync
import global.genesis.testsupport.jupiter.GenesisJunit
import global.genesis.testsupport.jupiter.TestScriptFile
import global.genesis.testsupport.jupiter.assertedCast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import jakarta.inject.Inject
import kotlin.String
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(GenesisJunit::class)
@TestScriptFile("jms-pipelines-eventhandler.kts")
class EventHandlerTest {
  @Inject
  lateinit var client: EventClientSync

  @Inject
  lateinit var entityDb: SyncEntityDb

  private val adminUser: String = "admin"

  @Test
  fun `test insert MESSAGE_SENT + source receives record`() = runTest(UnconfinedTestDispatcher()) {
    val messages = mutableListOf<MessageSent>()
    backgroundScope.launch {
      ProgrammaticMessageSource.read().collect {
        println("RECEIVED DATA: $it")
        messages.add(it)
        ProgrammaticMessageSource.acknowledge(PipelineContext("pipeline", it))
      }
    }

    val result = client.sendEvent(
      details = MessageSent {
        text = "1"
      },
      messageType = "EVENT_MESSAGE_SENT_INSERT",
      userName = adminUser
    )
    result.assertedCast<EventReply.EventAck>()
    val messageSent = entityDb.getBulk(MESSAGE_SENT).toList()
    assert(messageSent.isNotEmpty())
    assert(messages.size == 1)
    assert(messages[0].text == "1")
  }
}
