package net.zekk051.mixin;

import net.zekk051.SecureCrops;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
//? if >=1.19 {
import net.minecraft.text.Text;
 //?} else
/*import net.minecraft.text.LiteralText;*/
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandMixin extends Block {
    // Modified code from https://github.com/GitWither/feather-trampling
    public FarmlandMixin(Settings settings) {
        super(settings);
    }

    @Shadow
    private static boolean hasCrop(BlockView world, BlockPos pos) {
        return false;
    }

    @Inject(method="onLandedUpon", cancellable = true, at = @At(value = "INVOKE", target =
            /*? if >=1.19.4 {*/ "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V" /*?}*/
            /*? if <1.19.4 {*/ /*"Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V" *//*?}*/
    ))
    public void cancelTrample(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        boolean GAMERULE_SECURE_FARMLAND_AND_CROPS = world.getGameRules().getBoolean(SecureCrops.SECURE_FARMLAND_AND_CROPS);
        boolean GAMERULE_SECURE_FARMLAND_BREAK_CROPS = world.getGameRules().getBoolean(SecureCrops.SECURE_FARMLAND_BREAK_CROPS);
        boolean GAMERULE_SECURE_FARMLAND_IF_EMPTY = world.getGameRules().getBoolean(SecureCrops.SECURE_FARMLAND_IF_EMPTY);
        boolean hasCrop = hasCrop(world, pos);

        if(hasCrop && GAMERULE_SECURE_FARMLAND_AND_CROPS) {
            ci.cancel();
            printDebugMsg("Secure farmland and crop", entity);
            return;
        }

        if(hasCrop && GAMERULE_SECURE_FARMLAND_BREAK_CROPS) {
            ci.cancel();
            breakCrop(world, pos, entity);
            printDebugMsg("Secure farmland, trample crop", entity);
            return;
        }

        if(!hasCrop && GAMERULE_SECURE_FARMLAND_IF_EMPTY) {
            ci.cancel();
            printDebugMsg("Secure empty farmland", entity);
            return;
        }

        printDebugMsg("Vanilla trample", entity);
    }

    @Unique
    public void breakCrop(World world, BlockPos pos, Entity entity) {
        world.breakBlock(pos.up(), true, entity);
    }

    @Unique
    public void printDebugMsg(String debugMessage, Entity entity) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment() && entity instanceof PlayerEntity) {
            debugMessage = "[Debug] ".concat(debugMessage);
            //? if >=1.19 {
            ((PlayerEntity) entity).sendMessage(Text.literal(debugMessage), false);
             //?} else
            /*((PlayerEntity) entity).sendMessage(new LiteralText(debugMessage), false);*/
        }
    }
}
