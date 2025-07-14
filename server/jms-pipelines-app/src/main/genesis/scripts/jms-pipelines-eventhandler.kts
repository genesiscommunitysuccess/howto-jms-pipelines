import global.genesis.jms.ProgrammaticMessageSource

/**
 * This file defines the event handler APIs. These APIs (modeled after CQRS
 * commands) allow callers to manipulate the data in the database. By default,
 * insert, update and delete event handlers (or commands) have been created.
 * These result in the data being written to the database and messages to be
 * published for the rest of the platform to by notified.
 *
 * Custom event handlers may be added to extend the functionality of the
 * application as well as custom logic added to existing event handlers.
 *
 * The following objects are visible in each eventhandler
 * `event.details` which holds the entity that this event handler is acting upon
 * `entityDb` which is database object used to perform INSERT, MODIFY and UPDATE the records
 * Full documentation on event handler may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/

 */

eventHandler {
  // this is a simple event which takes in a MessageSent DAO object
  eventHandler<MessageSent>("MESSAGE_SENT_INSERT", transactional = true) {
    onCommit { event ->
      val messageSent = event.details
      // this object then gets inserted into the database
      val insertedRow = entityDb.insert(messageSent)
      // and subsequently sent to the ProgrammaticMessageSource
      ProgrammaticMessageSource.send(insertedRow.record)
      ack()
    }
  }
}
