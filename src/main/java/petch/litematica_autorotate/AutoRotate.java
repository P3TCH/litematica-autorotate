package petch.litematica_autorotate;

import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import petch.litematica_autorotate.mixin.BlockItemInvoker;

/**
 * Before a block is placed, simulates vanilla placement for a set of candidate look
 * rotations and, if one produces a state closer to the schematic than the player's
 * current rotation, sends that rotation to the server just ahead of the place packet.
 * Works for any block whose placement depends on look direction, without a per-block table.
 */
public final class AutoRotate {
    // Cardinals first, then diagonals, then 22.5° steps (signs, banners, skulls).
    private static final float[] YAWS = {
            0, 90, 180, 270,
            45, 135, 225, 315,
            22.5F, 67.5F, 112.5F, 157.5F, 202.5F, 247.5F, 292.5F, 337.5F
    };
    private static final float[] PITCHES = {0, 90, -90};

    private static boolean rotated;
    private static float savedYaw;
    private static float savedPitch;

    private AutoRotate() {
    }

    public static void beforePlace(LocalPlayer player, InteractionHand hand, BlockHitResult hit) {
        if (!AutoRotateConfig.AUTO_ROTATE.getBooleanValue()) {
            return;
        }

        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof BlockItem item)) {
            return;
        }

        WorldSchematic schematic = SchematicWorldHandler.getSchematicWorld();
        if (schematic == null) {
            return;
        }

        // Nested placement while already rotated: evaluate from the real rotation.
        if (rotated) {
            setLocalRotation(player, savedYaw, savedPitch);
        }

        BlockPlaceContext context = item.updatePlacementContext(new BlockPlaceContext(player, hand, stack, hit));
        if (context == null) {
            return;
        }

        BlockState target = schematic.getBlockState(context.getClickedPos());
        if (target.getBlock() != item.getBlock()) {
            return;
        }

        float yaw = player.getYRot();
        float pitch = player.getXRot();
        int maxScore = target.getProperties().size();
        int bestScore = score(player, item, hand, stack, hit, target);
        float bestYaw = yaw;
        float bestPitch = pitch;

        try {
            search:
            for (float candidatePitch : PITCHES) {
                for (float candidateYaw : YAWS) {
                    if (bestScore >= maxScore) {
                        break search;
                    }

                    setLocalRotation(player, candidateYaw, candidatePitch);
                    int candidateScore = score(player, item, hand, stack, hit, target);

                    if (candidateScore > bestScore) {
                        bestScore = candidateScore;
                        bestYaw = candidateYaw;
                        bestPitch = candidatePitch;
                    }
                }
            }
        } finally {
            setLocalRotation(player, yaw, pitch);
        }

        if (bestYaw == yaw && bestPitch == pitch) {
            restore(player);
            return;
        }

        if (!rotated) {
            savedYaw = yaw;
            savedPitch = pitch;
            rotated = true;
        }

        // The local rotation matters too: client-side prediction places the block with it.
        setLocalRotation(player, bestYaw, bestPitch);
        sendRotation(player, bestYaw, bestPitch);
    }

    public static void restore(LocalPlayer player) {
        if (!rotated) {
            return;
        }

        rotated = false;
        setLocalRotation(player, savedYaw, savedPitch);
        sendRotation(player, savedYaw, savedPitch);
    }

    public static void reset() {
        rotated = false;
    }

    private static int score(LocalPlayer player, BlockItem item, InteractionHand hand, ItemStack stack,
                             BlockHitResult hit, BlockState target) {
        try {
            BlockPlaceContext context = item.updatePlacementContext(new BlockPlaceContext(player, hand, stack, hit));
            if (context == null) {
                return -1;
            }

            BlockState state = ((BlockItemInvoker) item).autorotate$getPlacementState(context);
            if (state == null || state.getBlock() != target.getBlock()) {
                return -1;
            }

            int matching = 0;
            for (Property<?> property : target.getProperties()) {
                if (state.getValue(property).equals(target.getValue(property))) {
                    matching++;
                }
            }
            return matching;
        } catch (RuntimeException e) {
            return -1;
        }
    }

    private static void setLocalRotation(LocalPlayer player, float yaw, float pitch) {
        player.setYRot(yaw);
        player.setXRot(pitch);
    }

    private static void sendRotation(LocalPlayer player, float yaw, float pitch) {
        player.connection.send(new ServerboundMovePlayerPacket.Rot(yaw, pitch, player.onGround()));
    }
}
