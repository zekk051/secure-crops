package net.zekk051.securecrops.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zekk051.securecrops.SecureCrops;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandMixin extends Block {
    // Modified code from https://github.com/GitWither/feather-trampling
    public FarmlandMixin(Settings settings) { super(settings); }

    @Inject(method="onLandedUpon", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    public void cancelTrample(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {

        if(world.getGameRules().getBoolean(SecureCrops.SECURE_CROPS) && !world.getBlockState(pos.up()).isAir()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
            ci.cancel();
            printDebugMsg("[Debug] Prevented crop trampling", entity);
            return;
        }
        if(world.getGameRules().getBoolean(SecureCrops.SECURE_FARMLAND) && world.getBlockState(pos.up()).isAir()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
            ci.cancel();
            printDebugMsg("[Debug] Prevented farmland trampling", entity);
            return;
        }
    }

    public void printDebugMsg(String message, Entity entity) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment() && entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).sendMessage(Text.literal(message), false);
        }
    }
}