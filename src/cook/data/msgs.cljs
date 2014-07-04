(ns cook.data.msgs)

(def intro "Welcome to Cook's Assistant!
Created by Martin Janiczek (<a href=\"http://twitter.com/janiczek\">@janiczek</a>).
Inspired by <a href=\"http://runescape.com\">RuneScape</a>. RuneScape is a registered trademark of <a href=\"http://jagex.com\">Jagex Limited</a>.
The source code is on <a href=\"https://github.com/janiczek/cook\">GitHub</a>.

The game now begins... Go help the Cook!")

(def help "The commands are:
  talk to ...
  go ...
  pick ...
  use ...
  use ... on ...
  look around
  inventory
  help")

(def unknown         "Unknown command!")
(def empty-inventory "Your inventory is empty!")
(def cant-go         "You cannot go there.")
(def cant-pick       "You can't pick that here!")
(def picked-up       "You picked it up.")
(def talk-unknown    "Talk to whom?")
(def cant-use        "You cannot use that!")
(def wont-work       "That ... won't work.")
