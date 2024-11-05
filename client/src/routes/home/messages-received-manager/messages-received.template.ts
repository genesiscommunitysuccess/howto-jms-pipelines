import { html, whenElse, repeat } from '@genesislcap/web-core';
import { getViewUpdateRightComponent } from '../../../utils';
import type { HomeMessagesReceivedManager } from './messages-received';
import { createFormSchema } from './messages-received.create.form.schema';
import { updateFormSchema } from './messages-received.update.form.schema';
import { columnDefs } from './messages-received.column.defs';


export const MessagesReceivedTemplate = html<HomeMessagesReceivedManager>`
    ${whenElse(
        (x) => getViewUpdateRightComponent(x.user, ''),
        html`
            <entity-management
                design-system-prefix="rapid"
                header-case-type="capitalCase"
                enable-row-flashing
                enable-cell-flashing
                resourceName="MESSAGE_RECEIVED"
                :datasourceConfig=${() => ({pollingInterval: 5000 })}
                entityLabel="Message Received"
                :columns=${() => columnDefs }
                modal-position="centre"
                size-columns-to-fit
            ></entity-management>
        `,
        html`
          <not-permitted-component></not-permitted-component>
        `,
    )}`;
