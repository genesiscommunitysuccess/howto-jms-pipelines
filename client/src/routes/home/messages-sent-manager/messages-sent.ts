import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { MessagesSentStyles as styles } from './messages-sent.styles';
import { MessagesSentTemplate as template } from './messages-sent.template';

@customElement({
  name: 'home-messages-sent-manager',
  template,
  styles,
})
export class HomeMessagesSentManager extends GenesisElement {
  @User user: User;
}
