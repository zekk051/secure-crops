package net.zekk051.securecrops.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zekk051.securecrops.SecureCrops;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandMixin extends Block {
    // Modified code from https://github.com/GitWither/feather-trampling
    public FarmlandMixin(Settings settings) { super(settings); }

    @Shadow
    private static boolean hasCrop(BlockView world, BlockPos pos) {
        return false;
    }

    @Inject(method="onLandedUpon", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    public void cancelTrample(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        boolean GAMERULE_SECURE_CROPS = world.getGameRules().getBoolean(SecureCrops.SECURE_CROPS);
        boolean GAMERULE_SECURE_FARMLAND_WITH_CROPS = world.getGameRules().getBoolean(SecureCrops.SECURE_FARMLAND_WITH_CROPS);
        boolean GAMERULE_SECURE_FARMLAND = world.getGameRules().getBoolean(SecureCrops.SECURE_FARMLAND);
        boolean hasCrop = hasCrop(world, pos);

        if(hasCrop && GAMERULE_SECURE_CROPS) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
            ci.cancel();
            printDebugMsg("[Debug] Prevented crop trampling", entity);
            return;
        }

        if(hasCrop && GAMERULE_SECURE_FARMLAND_WITH_CROPS) {
            ci.cancel();
            breakCrop(world, pos, entity);
            printDebugMsg("[Debug] Trampled crop, secure farmland", entity);
            return;
        }

        if(!hasCrop && GAMERULE_SECURE_FARMLAND) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
            ci.cancel();
            printDebugMsg("[Debug] Prevented farmland trampling", entity);
            return;
        }
    }

    public void breakCrop(World world, BlockPos pos, Entity entity) {
        world.breakBlock(pos.up(), true, entity);
    }

    public void printDebugMsg(String message, Entity entity) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment() && entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).sendMessage(Text.literal(message), false);
        }
    }
}