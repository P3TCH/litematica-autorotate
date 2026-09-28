package petch.litematica_autorotate.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import petch.litematica_autorotate.AutoRotate;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    // performUseItemOn runs inside useItemOn's prediction lambda, after Litematica's
    // easy place HEAD hook and right before ServerboundUseItemOnPacket is built,
    // so a rotation packet sent here reaches the server ahead of the placement.
    @Inject(method = "performUseItemOn", at = @At("HEAD"))
    private void autorotate$beforePlace(LocalPlayer player, InteractionHand hand, BlockHitResult hit,
                                        CallbackInfoReturnable<InteractionResult> cir) {
        AutoRotate.beforePlace(player, hand, hit);
    }

    @Inject(method = "useItemOn", at = @At("RETURN"))
    private void autorotate$afterPlace(LocalPlayer player, InteractionHand hand, BlockHitResult hit,
                                       CallbackInfoReturnable<InteractionResult> cir) {
        AutoRotate.restore(player);
    }
}
