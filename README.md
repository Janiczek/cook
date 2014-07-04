# Cook's Assistant

A small text adventure based on a quest from [RuneScape](http://runescape.com), and more importantly, an experiment in functional game programming.

I plan to add more quests and some RPG elements just to see how will I have to refactor it. Maybe I will succumb to the Component-Entity-System model (very tempting given what [Brute](https://github.com/markmandel/brute) shows), but first let's try to do it on my own :)

[RuneScape®](http://runescape.com) is a registered trademark of [Jagex Limited](http://jagex.com) in the United Kingdom, the United States and other countries.

## Compile

- `lein cljsbuild once prod` for `index.html` *(should work)*
- `lein cljsbuild once dev` for `index2.html` *(if advanced compilation breaks something)*

## Run

Either **play [here](http://janiczek.github.io/cook/)** or run your compiled version: `index.html` *(prod)* or `index2.html` *(dev)*.

## How it works

### State

There is state in an `atom`, but every function that does something with it

- has its data passed explicitly as an argument
- doesn't modify the atom but instead returns the changed data

So we have referentially transparent and pure functions and not a hidden global state!

Inside it looks a little bit like this:

```clj
{:player {:location :castle
          :inventory #{}}
 :locations ...
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

- [ ] right now if we want to output something that depends on the current, modified state, we have to save it somehow (let) and then inside the body do `(output new-state (blablabla new-state))` ... some helper fn or macro to the rescue?
- [ ] refactor locations out of state?
- [ ] bag (multiset) or at least map (`{Item Count}`) for the inventory?
- [ ] minimap? squares? D3.js graph?
- [ ] inventory visible in GUI at all times, not as a command?
- [ ] Component Entity System framework (Brute)?
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
