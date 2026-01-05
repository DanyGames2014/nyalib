package net.danygames2014.nyalib.fluid;

import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

@SuppressWarnings("unused")
public class FluidStack {
    /**
     * The fluid this stack contains
     */
    public Fluid fluid;
    /**
     * Amount in mB
     * 1000mB = 1B
     */
    public int amount;

    private FluidStack() {
    }

    private FluidStack(int amount) {
    }

    /**
     * Initializes a new FluidStack with the given fluid and amount
     *
     * @param fluid  The fluid to initialize the ItemStack with
     * @param amount The amount of the fluid in mB
     */
    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public FluidStack(Identifier id) {
        this(id, 1000);
    }

    public FluidStack(Identifier id, int amount) {
        this.fluid = FluidRegistry.get(id);
        this.amount = amount;
    }

    public FluidStack(NbtCompound nbt) {
        this.readNbt(nbt);
    }

    public FluidStack copy() {
        return new FluidStack(this.fluid, this.amount);
    }

    /**
     * Initializes a new FluidStack with the given fluid and 1000mB
     *
     * @param fluid The fluid to initialize the ItemStack with
     */
    public FluidStack(Fluid fluid) {
        this(fluid, 1000);
    }

    // Localization
    public String getTranslationKey() {
        return fluid.getTranslationKey(this);
    }

    public String getTranslatedName() {
        return fluid.getTranslatedName(this);
    }
    
    // Comparison
    public boolean isFluidEqual(FluidStack other) {
        if (other == null) {
            return false;
        }

        return other.fluid == this.fluid;
    }

    public static boolean areEqual(FluidStack left, FluidStack right) {
        if (left == null && right == null) {
            return true;
        } else {
            return left != null && left.equals(right);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public final boolean equals(Object other) {
        if (other instanceof FluidStack otherStack) {
            if (!isFluidEqual(otherStack)) {
                return false;
            }
            
            if (this.amount != otherStack.amount) {
                return false;
            }
            
            return true;
        }

        return false;
    }

    // NBT
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("fluid", this.fluid.getIdentifier().toString());
        nbt.putInt("amount", this.amount);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        this.fluid = FluidRegistry.get(Identifier.of(nbt.getString("fluid")));
        this.amount = nbt.getInt("amount");
    }

    public static FluidStack fromNbt(NbtCompound nbt) {
        FluidStack stack = new FluidStack();

        stack.readNbt(nbt);

        if (stack.fluid != null) {
            return stack;
        } else {
            return null;
        }
    }

    // To String
    @Override
    public String toString() {
        return "FluidStack { Fluid = " + getTranslatedName() + " | Amount = " + amount + "mB }";
    }
}
