import { UiSchema } from '@genesislcap/foundation-forms';

export const updateFormSchema: UiSchema = {
  "type": "VerticalLayout",
  "elements": [
    {
      "type": "Control",
      "label": "Text",
      "scope": "#/properties/TEXT",
      "options": {}
    },
    {
      "type": "Control",
      "label": "Id",
      "scope": "#/properties/ID",
      "options": {
        "readonly": true
      }
    }
  ]
}
