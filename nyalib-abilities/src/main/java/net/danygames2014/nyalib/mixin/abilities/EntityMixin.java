package net.danygames2014.nyalib.mixin.abilities;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.abilities.AbilityManager;
import net.danygames2014.nyalib.abilities.AbilityProvider;
import net.danygames2014.nyalib.mixininterface.NyaLibAbilitiesEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Entity.class)
public abstract class EntityMixin implements NyaLibAbilitiesEntity {
    @Unique
    public Object2ObjectOpenHashMap<Identifier, AbilityProvider> abilityProviders;
    
    @Override
    public Object2ObjectOpenHashMap<Identifier, AbilityProvider> getAbilityProviders() {
        if (abilityProviders == null) {
            abilityProviders = new Object2ObjectOpenHashMap<>();
        }
        
        return abilityProviders;
    }

    @Override
    public AbilityProvider getAbilityProvider(Identifier identifier) {
        return getAbilityProviders().computeIfAbsent(identifier, id ->
                new AbilityProvider(AbilityManager.getInstance(), (Identifier) id, (Entity) (Object) this)
        );
    }

    @Override
    public void markAbilitiesDirty() {
        AbilityManager.getInstance().markDirty((Entity) (Object) this);
    }

    @Inject(method = "markDead", at = @At(value = "TAIL"))
    public void removeEntityFromAbilityManager(CallbackInfo ci) {
        AbilityManager.getInstance().removeEntity((Entity) (Object) this);
    }
    
    // Saving and Loading
    @Inject(method = "write", at = @At(value = "TAIL"))
    public void writeAbilityNbt(NbtCompound nbt, CallbackInfo ci) {
        if (abilityProviders == null) {
            return;
        }

        NbtCompound abilitiesNbt = new NbtCompound();

        for (AbilityProvider provider : abilityProviders.values()) {
            NbtCompound providerNbt = new NbtCompound();
            provider.writeNbt(providerNbt);
            abilitiesNbt.put(provider.identifier.toString(), providerNbt);
        }

        nbt.put("nyalib:abilities", abilitiesNbt);
    }
    
    @Inject(method = "read", at = @At(value = "TAIL"))
    public void readAbilityNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!nbt.contains("nyalib:abilities")) {
            return;
        }

        NbtCompound abilitiesNbt = nbt.getCompound("nyalib:abilities");
        
        abilityProviders = new Object2ObjectOpenHashMap<>();
        for (Object providerO : abilitiesNbt.values()) {
            if (providerO instanceof NbtCompound providerNbt) {
                AbilityProvider provider = new AbilityProvider(AbilityManager.getInstance(), (Entity) (Object) this);
                provider.readNbt(providerNbt);
                abilityProviders.put(provider.identifier, provider);
            }
        }
    }
}
