import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { MessagesReceivedStyles as styles } from './messages-received.styles';
import { MessagesReceivedTemplate as template } from './messages-received.template';

@customElement({
  name: 'home-messages-received-manager',
  template,
  styles,
})
export class HomeMessagesReceivedManager extends GenesisElement {
  @User user: User;
}
