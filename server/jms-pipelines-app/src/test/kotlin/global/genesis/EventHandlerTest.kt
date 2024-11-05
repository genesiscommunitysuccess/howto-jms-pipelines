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
import global.genesis.testsupport.jupiter.ScriptFile
import global.genesis.testsupport.jupiter.assertedCast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.String
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(GenesisJunit::class)
@ScriptFile("jms-pipelines-eventhandler.kts")
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
    assertTrue(messageSent.isNotEmpty())
    assertEquals(1, messages.size)
    assertEquals("1", messages[0].text)
  }
}
