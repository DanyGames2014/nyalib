* Add automatic item models
* Block Templates have been moved to the NyaLib Templates modules
* Removed the ability for a bucket to specify its size
* Fixed NyaLib Item API implementation for chests not working on blocks extending it
* Fixed a NullPointerException in ItemHandler
* Fix a wrong log message when registering capabilities
* Fix block network discovery finding duplicate entries, improving performance
* Fix Block Networks not loading properly on server
* Fix Block Networks not taking into account whether a chunk is loaded
* Refactor Block Networks to use fastutil
* Added HasBlockEntity interface that can be implemented on Block
* Rewrite Block Networks loading and saving
  * The saved data will no longer be corrupted when incorrectly quitting the game
  * Corrupted save will no longer cause a forever read-only state
  * Moved part of saving to be off-thread
* Moved BlockEntityInit into the NyaLib Block module
* Add ItemStack context to FluidBucket methods
* Update to StationAPI 2.0.0-alpha.6.4
* Block Templates now use StationAPI Runtime Resources Event
* Fix custom bucket items extending FluidBucket not being handled properly
* Extended Data Tracker
  * Provides Identifier based data tracker for entities
  * Supports custom data types
  * By default provides: boolean, byte, char, double, float, int, long, short, ItemStack, String, Vec3i, NbtCompound
* Removed an instance of evil division
* Abilities API
  * Allows entities to have abilities provided either by custom providers or by items in their inventory
  * Items can implemented the InventoryAbilityItem
  * Mods can register custom abilities
  * By default provides: Fire Immunity, Flight, Flight Speed, Invincibility
  * If multiple mods provide the same ability, the result ability value depends on the rule set by the ability