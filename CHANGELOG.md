General
* Finish modularizing the API

Base
* Abstract setBlockState in Structure to a overrideable method

Block
* BlockEntityInit interface
* Voxel Shapes (Thanks to Slainlight)
* Added Redstone API

Block Networks
* Fix FlattenedChunkMixin crashing in production (AlphaMode)
* Add an ability to fetche edge and non-edge nodes in a Network
* Improve Block Network loading resilience and error correction

Block Templates
* Add opening and closing sound to the template fence gate
* Fix erroneously loading json overrides on server
* Fix several crashes when requesting collision/bounding box of a template block which was broken
* Fix collision shape and bounding box of panes and fences

Capability
* Add Block Entity related methods to CapabilityHelper

Compat
* Do not show the fluid amount in Whats This when the tank is empty
* Fix NyaLib hard depending on WhatsThis
* New graphic for WhatsThis fluid tooltip integration

Energy API
* Fix energy pathfinding thru edge nodes
* Fix energy not pathing thru initial block
* Improve EnergyNetwork energy distribution and use fastutil

Fluid API
* Fix a crash in getRemainingCapacity when Fluid is null
* Add FluidSlots to ScreenHandlers
* Automatically register fluid buckets
* Add automatic fluid blocks with animated texture support
* Added fluid screen overlay, custom swim speed and drowning in fluids
* Add an AfterFluidRegistryEvent
* Fix a server crash when registering fluid colors
* Respect when fluids are not placeable in world
* Rewrite the logic of putting fluids in and out of tanks in the ScreenHandler
* Milk is now a fluid
* Fluids will now try to find the closest map color before creating a new one
* Custom fluid slot rendering and size
* Add Simple and Managed Handlers for blocks, entities and items

Item API
* Disable simplified furnace handling by default
* Add an extractItem with an Item and meta parameter
* Fix a crash in ItemHandler when null Item is supplied
* Add shouldDropStack into DropInventoryOnBreak
* Add Managed handlers for blocks, entities and items
* Add slot locking via LockableSlot and SlotLockingItem
* Added EnhancedPlacementContextItem

Multipart API
* Added Multipart API
* Multiparts allow you to create multipart components which can exist in any block space