# Cook's Assistant

A small text adventure based on a quest from [RuneScape](http://runescape.com).

[RuneScape®](http://runescape.com) is a registered trademark of [Jagex Limited](http://jagex.com) in the United Kingdom, the United States and other countries.

## Compile

- `lein cljsbuild once prod` for `index.html` *(should work)*
- `lein cljsbuild once dev` for `index2.html` *(if advanced compilation breaks something)*

## Run

Either **play [here](http://janiczek.github.io/cook/)** or run your compiled version: `index.html` (*prod*) or `index2.html` (*dev*).

## How it works

### State

There is global state. In Clojure version it would be a `ref`, in ClojureScript (single-threaded) it can be an `atom`. Inside it looks like this:

```clj
{:player {:location :castle
          :inventory #{}}
 :state {:hopper-has-wheat-inside false
         :flour-in-bin            false
         :cook-has-given-quest    false
         :cook-has-ingredients    false}
 :items {:farm      #{:egg}
         :cow-field #{:bucket}
         :field     #{:wheat}
         :windmill  #{:pot}}}
```

So far it's minimum structure to make the whole thing work, I feel with more quests, NPCs etc. it will have to be refactored into something more robust, I'll have to find a way to isolate each quest into its own namespace, etc.

### Commands

The commands are parsed by regexes. There, I said it. Basically it crunches the row in this order:

1. command (action)
2. object *(if the command needs it)*
3. subject *(if the command needs it)*

## TODO

- [ ] bag (multiset) or at least map (`{Item Count}`) for the inventory?
- [ ] minimap? squares? D3.js graph?
- [ ] inventory visible in GUI at all times, not as a command?

- [ ] Component Entity System framework, for handling **instances** of items and not items as themselves, etc.? As a way to get rid of global state as a map?

- [ ] another quest (let's try how code structured in this way handles complexity)
- [ ] RPG elements (XP, levels, skills, ... who knows?)

## Namespaces

- `cook.core` - init

- `cook.gui` - HTML/DOM related stuff
- `cook.gui.history` - abstraction over how does history box really look and what do we want from it (textarea, div, ...)
- `cook.gui.input` - the same, over the input

- `cook.data` - description of game world, synonyms for commands, items, ...
- `cook.data.msgs` - various strings (intro, "you cannot do that", etc.)
- `cook.data.quest` - dialogue and strings for the (only) quest

- `cook.state` - state atom, initial state map
- `cook.state.utils` - querying and altering state (current location, inventory, ...)

- `cook.describe` - fns for describing the various aspects of the world to the player

- `cook.commands` - fns and multimethods defining the commands
- `cook.commands.utils` - mainly helpers for parsing input string to a command
