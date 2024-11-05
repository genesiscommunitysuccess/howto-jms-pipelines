import { html, whenElse, repeat } from '@genesislcap/web-core';
import { getViewUpdateRightComponent } from '../../../utils';
import type { HomeMessagesSentManager } from './messages-sent';
import { createFormSchema } from './messages-sent.create.form.schema';
import { updateFormSchema } from './messages-sent.update.form.schema';
import { columnDefs } from './messages-sent.column.defs';


export const MessagesSentTemplate = html<HomeMessagesSentManager>`
    ${whenElse(
        (x) => getViewUpdateRightComponent(x.user, ''),
        html`
            <entity-management
                design-system-prefix="rapid"
                header-case-type="capitalCase"
                enable-row-flashing
                enable-cell-flashing
                resourceName="MESSAGE_SENT"
                createEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_MESSAGE_SENT_INSERT')}"
                :createFormUiSchema=${() => createFormSchema }
                :datasourceConfig=${() => ({pollingInterval: 5000 })}
                entityLabel="Message Sent"
                :columns=${() => columnDefs }
                modal-position="centre"
                size-columns-to-fit
            ></entity-management>
        `,
        html`
          <not-permitted-component></not-permitted-component>
        `,
    )}`;
