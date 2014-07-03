# Cook's Assistant

A small text adventure based on a quest from RuneScape.

RuneScape® is a registered trade mark of Jagex Limited in the United Kingdom, the United States and other countries.

- `lein cljsbuild once prod` for `index.html`
- `lein cljsbuild once dev` for `index2.html`

### cook.core
- initialization, handler for input

### cook.gui[.{history,input}]
- HTML/DOM related stuff - scrolling, appending, reading values, clearing, ...

### cook.data[.{msgs,quest}]
- lots and lots of strings. synonyms for commands, items, locations etc., descriptions, dialogues, ...

### cook.state
- atom with state, new state for initialization

### cook.utils.state
- things for querying and altering the current state (current location, etc.)

### cook.describe
- describing current location, inventory, etc. to a string

### cook.commands
- the main thing: functions and multimethods for the commands (talk, use, ...)

### cook.utils.commands
- utilities for parsing input string into a command
