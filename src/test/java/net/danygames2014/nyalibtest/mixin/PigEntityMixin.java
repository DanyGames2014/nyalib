package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTracker;
import net.danygames2014.nyalib.entity.datatracker.mixininterface.NyaLibExtendedDataTrackerEntity;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntity.class)
public class PigEntityMixin extends AnimalEntity implements NyaLibExtendedDataTrackerEntity {
    public PigEntityMixin(World world) {
        super(world);
    }

    @Override
    public void initExtendedDataTracker() {
        ExtendedDataTracker tracker = this.getExtendedDataTracker();
        tracker.startTrackingBoolean(NyaLibTest.NAMESPACE.id("test_entry_boolean"));
        tracker.startTrackingCharacter(NyaLibTest.NAMESPACE.id("test_entry_character"));
        tracker.startTrackingByte(NyaLibTest.NAMESPACE.id("test_entry_byte"));
        tracker.startTrackingShort(NyaLibTest.NAMESPACE.id("test_entry_short"));
        tracker.startTrackingInt(NyaLibTest.NAMESPACE.id("test_entry_int"));
        tracker.startTrackingLong(NyaLibTest.NAMESPACE.id("test_entry_long"));
        tracker.startTrackingFloat(NyaLibTest.NAMESPACE.id("test_entry_float"));
        tracker.startTrackingDouble(NyaLibTest.NAMESPACE.id("test_entry_double"));
        tracker.startTrackingVec3i(NyaLibTest.NAMESPACE.id("test_entry_vec3i"), null);
        tracker.startTrackingString(NyaLibTest.NAMESPACE.id("test_entry_string"), null);
        tracker.startTrackingItemStack(NyaLibTest.NAMESPACE.id("test_entry_stack"), null);
        tracker.startTrackingNbtCompound(NyaLibTest.NAMESPACE.id("test_entry_nbt"), null);
    }

    @Inject(method = "interact", at = @At(value = "HEAD"))
    public void testChangingTracker(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player.isSneaking()) {
            System.err.println("Entity ID: " + this.id);
            System.err.println("boolean => " + this.getExtendedDataTracker().getBoolean(NyaLibTest.NAMESPACE.id("test_entry_boolean")));
            System.err.println("char => " + this.getExtendedDataTracker().getCharacter(NyaLibTest.NAMESPACE.id("test_entry_character")));
            System.err.println("byte => " + this.getExtendedDataTracker().getByte(NyaLibTest.NAMESPACE.id("test_entry_byte")));
            System.err.println("short => " + this.getExtendedDataTracker().getShort(NyaLibTest.NAMESPACE.id("test_entry_short")));
            System.err.println("int => " + this.getExtendedDataTracker().getInt(NyaLibTest.NAMESPACE.id("test_entry_int")));
            System.err.println("long => " + this.getExtendedDataTracker().getLong(NyaLibTest.NAMESPACE.id("test_entry_long")));
            System.err.println("float => " + this.getExtendedDataTracker().getFloat(NyaLibTest.NAMESPACE.id("test_entry_float")));
            System.err.println("double => " + this.getExtendedDataTracker().getDouble(NyaLibTest.NAMESPACE.id("test_entry_double")));
            System.err.println("vec3i => " + this.getExtendedDataTracker().getVec3i(NyaLibTest.NAMESPACE.id("test_entry_vec3i")));
            System.err.println("string => " + this.getExtendedDataTracker().getString(NyaLibTest.NAMESPACE.id("test_entry_string")));
            System.err.println("stack => " + this.getExtendedDataTracker().getItemStack(NyaLibTest.NAMESPACE.id("test_entry_stack")));
            System.err.println("nbt => " + this.getExtendedDataTracker().getNbtCompound(NyaLibTest.NAMESPACE.id("test_entry_nbt")));
            return;
        }

        if (world.isRemote) {
            return;
        }

        ExtendedDataTracker tracker = this.getExtendedDataTracker();
        ItemStack hand = player.getHand();

        boolean booleanValue = random.nextBoolean();
        tracker.setBoolean(NyaLibTest.NAMESPACE.id("test_entry_boolean"), booleanValue);
        System.err.println("Set tracker boolean to " + booleanValue);

        char charValue = (char) (random.nextInt(26) + 'a');
        tracker.setCharacter(NyaLibTest.NAMESPACE.id("test_entry_character"), charValue);
        System.err.println("Set tracker char to " + charValue);

        byte byteValue = hand != null ? (byte) hand.count : 0;
        tracker.setByte(NyaLibTest.NAMESPACE.id("test_entry_byte"), byteValue);
        System.err.println("Set tracker byte to " + byteValue);

        short shortValue = (short) (random.nextInt(Short.MAX_VALUE));
        tracker.setShort(NyaLibTest.NAMESPACE.id("test_entry_short"), shortValue);
        System.err.println("Set tracker short to " + shortValue);

        int intValue = random.nextInt();
        tracker.setInt(NyaLibTest.NAMESPACE.id("test_entry_int"), intValue);
        System.err.println("Set tracker int to " + intValue);

        long longValue = random.nextLong();
        tracker.setLong(NyaLibTest.NAMESPACE.id("test_entry_long"), longValue);
        System.err.println("Set tracker long to " + longValue);

        float floatValue = random.nextFloat();
        tracker.setFloat(NyaLibTest.NAMESPACE.id("test_entry_float"), floatValue);
        System.err.println("Set tracker float to " + floatValue);

        double doubleValue = random.nextDouble();
        tracker.setDouble(NyaLibTest.NAMESPACE.id("test_entry_double"), doubleValue);
        System.err.println("Set tracker double to " + doubleValue);

        Vec3i vec3iValue = new Vec3i(random.nextInt(), random.nextInt(), random.nextInt());
        tracker.setVec3i(NyaLibTest.NAMESPACE.id("test_entry_vec3i"), vec3iValue);
        System.err.println("Set tracker vec3i to " + vec3iValue);

        String stringValue = "test";
        tracker.setString(NyaLibTest.NAMESPACE.id("test_entry_string"), stringValue);
        System.err.println("Set tracker string to " + stringValue);

        tracker.setItemStack(NyaLibTest.NAMESPACE.id("test_entry_stack"), hand);
        System.err.println("Set tracker stack to " + hand);

        NbtCompound nbtValue = new NbtCompound();
        nbtValue.putString("test_string", "test");
        nbtValue.putInt("test_int", 12345);
        tracker.setNbtCompound(NyaLibTest.NAMESPACE.id("test_entry_nbt"), nbtValue);
    }
}
