package net.danygames2014.nyalib.fluid;

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

    private FluidStack() {}
    
    private FluidStack(int amount) {}
    
    /**
     * Initializes a new FluidStack with the given fluid and amount
     * @param fluid The fluid to initialize the ItemStack with
     * @param amount The amount of the fluid in mB
     */
    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    /**
     * Initializes a new FluidStack with the given fluid and 1000mB
     * @param fluid The fluid to initialize the ItemStack with
     */
    public FluidStack(Fluid fluid) {
        this(fluid, 1000);
    }
}
