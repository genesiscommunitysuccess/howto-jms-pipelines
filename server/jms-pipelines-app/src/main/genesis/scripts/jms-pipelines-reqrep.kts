/**
 * This file defines request-replies of the application.
 * Request-Replies provide snapshot data from a table or view in response to a request from the front end.
 * Once the response is received, the transaction is over (unlike a Data Server, which stays connected to the client and pushes updates)

 * Full documentation on request server may be found here >> https://learn.genesis.global/docs/server/request-server/introduction/

 */

requestReplies {
  // these are the request replies the GUI will use to display the data
  requestReply(MESSAGE_SENT)
  requestReply(MESSAGE_RECEIVED)
}
