* Fix a crash in getRemainingCapacity when Fluid is null
* Nearly finish modularization
* Do not show the fluid amount in Whats This when the tank is empty
* Add opening and closing sound to the template fence gate
* Fix and L Moment regarding access wideners
* Fix FlattenedChunkMixin (AlphaMode)
* Disable simplified furnace handling by default
* Vanilla furnace now checks sides on insertion and extraction
* Add an extractItem with an Item and meta parameter
* Add FluidSlots to ScreenHandlers
* Fix NyaLib hard depending on WhatsThis
* Add automatic fluid buckets
* Add automatic fluid blocks with animated texture support
* Fix erroneously loading json overrides on server
* Added fluid screen overlay, custom swim speed and drowning in fluids
* New graphic for WhatsThis fluid tooltip integration
* Fix a crash in ItemHandler when null Item is supplied
* Fix several crashes when requesting collision/bounding box of a template block which was broken
* Add an AfterFluidRegistryEvent
* Add an ability to fetche edge and non-edge nodes in a Network
* Fix energy pathfinding thru edge nodes
* Fix energy not pathing thru initial block
* Abstract setBlockState in Structure to a overrideable method
* Fix a server crash when registering fluid colors
* Improve EnergyNetwork energy distribution and use fastutil
* Add shouldDropStack into DropInventoryOnBreak
* Respect when fluids are not placeable in world
* Rewrite the logic of putting fluids in and out of tanks in the ScreenHandler
* Milk is now a fluid
* Fluids will now try to find the closest map color before creating a new one
* Fix collion shape and bounding box of panes and fences
* Custom fluid slot rendering and size
* BlockEntityInit interface
* SimpleFluidHandler, ManagedFluidHandler and SimpleTank
* Add slot locking via LockableSlot and SlotLockingItem