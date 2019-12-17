package io.github.trikzon.concepts.mixin.client;

import io.github.trikzon.concepts.block.SleepingBagBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "getSleepingDirection", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onGetSleepingDirection(CallbackInfoReturnable<Direction> cir, BlockPos pos) {
        if (cir.getReturnValue() != null && pos == null) return;

        Direction direction = SleepingBagBlock.getDirection(world, pos);
        if (direction != null)
            cir.setReturnValue(direction);
    }
}
