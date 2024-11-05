/**
  * This file defines the entities (or tables) for the application.  
  * Entities aggregation a selection of the universe of fields defined in 
  * {app-name}-fields-dictionary.kts file into a business entity.  
  *
  * Note: indices defined here control the APIs available to the developer.
  * For example, if an entity requires lookup APIs by one or more of its attributes, 
  * be sure to define either a unique or non-unique index.

  * Full documentation on tables may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/data-model/

 */

tables {
  table(name = "MESSAGE_SENT", id = 11_000) {
    field("ID", STRING).sequence("MS")
    field("TEXT", STRING(100)).notNull().metadata {
      maxLength = 100
    }

    primaryKey("ID")

  }
  table(name = "MESSAGE_RECEIVED", id = 11_001) {
    field("ID", STRING).sequence("MR")
    field("TEXT", STRING(100)).notNull().metadata {
      maxLength = 100
    }

    primaryKey("ID")

  }
}
