# Templar Customizer
A wurm unlimited mod to allow players to rename their deed guards.

Right now there is little customization. Players can create a spirit contract item to allow them to rename their own spirit
templars/shadows. The requirements are descent papryusmaking skill, along with a blood from any unique creature, a shaft and 3 
pieces of paper. 

Currently GMs with a power of 2 or greater can use a black or ivory wand to force a name change with a spirit templar/shadow.

This uses a bad word filter (https://docs.google.com/spreadsheets/d/1UMi1w_T593iuSi8q7QSTdlQkNJB6bEkeGKveOadS5sU/) **Words in doc are NSFW!** and filters
names using non-alphabetical or numerical characters. This is a google doc that can be updated when needed. GMs can run the command
"/reloadbadwords" to reload the filter without requiring a server restart.

_Changelog:_

February 10th 2019:
- Added check to make sure GMs with wands can't use the nickname action on players.
- Adjusted BML on rename question
- Added a command for GMs with power >= 2 (/reloadbadwords) to run to update the bad word filter without a server restart. I would suggest using sparingly.

February 9th 2019:
- Initial push to GitHub

_Known Issues:_

- Nicknamed guards may not retain name if the server shuts down shortly after being renamed.

_Upcoming Features:_
- Configuration: GM levels for performing various functions, Templar Contract creation difficulty, set URL for bad word filter
- Forced prefix: The spirit contract will append a word or phrase before the player's custom text. For example, if a player names their templar
"Bob" the templar will be named "Guard Bob"