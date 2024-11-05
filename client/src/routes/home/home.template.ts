import { html } from '@genesislcap/web-core';
import { persistLayout } from '../../utils';
import type { Home } from './home';
import { HomeMessagesReceivedManager } from './messages-received-manager';
import { HomeMessagesSentManager } from './messages-sent-manager';

HomeMessagesReceivedManager;
HomeMessagesSentManager;

export const HomeTemplate = html<Home>`
  <rapid-layout auto-save-key="${() => persistLayout('HOME_1730713801905')}">
     <rapid-layout-region>
         <rapid-layout-item title="Messages Received">
             <home-messages-received-manager></home-messages-received-manager>
         </rapid-layout-item>
         <rapid-layout-item title="Messages Sent">
             <home-messages-sent-manager></home-messages-sent-manager>
         </rapid-layout-item>
     </rapid-layout-region>
  </rapid-layout>
`;
