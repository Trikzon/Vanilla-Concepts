package io.github.trikzon.concepts.mixin;

import io.github.trikzon.concepts.block.SleepingBagBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract Optional<BlockPos> getSleepingPosition();

    @Inject(method = "isSleepingInBed", at = @At("RETURN"), cancellable = true)
    private void trikzon_onIsSleepingInBed(CallbackInfoReturnable<Boolean> cir) {
        boolean result = this.getSleepingPosition().map((blockPos) ->
                this.world.getBlockState(blockPos).getBlock() instanceof SleepingBagBlock).orElse(false);

        if (result) cir.setReturnValue(true);
    }
}
