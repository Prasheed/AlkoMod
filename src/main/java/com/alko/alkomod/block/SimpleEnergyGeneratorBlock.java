package com.alko.alkomod.block;


import com.alko.alkomod.block.blockentity.ModBlockEntities;
import com.alko.alkomod.block.blockentity.SimpleEnergyGeneratorBlockEntity;
import com.alko.alkomod.capability.PlayerAnimationCapabilityProvider;
import com.alko.alkomod.handlers.PlayerAnimationStateHandler;
import com.alko.alkomod.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SimpleEnergyGeneratorBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SimpleEnergyGeneratorBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK));
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.SIMPLE_GENERATOR_BLOCK_ENTITY.get().create(blockPos,blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (hand == InteractionHand.MAIN_HAND){
            if(!level.isClientSide()) {
                if(be instanceof SimpleEnergyGeneratorBlockEntity blockEntity) {
//                    player.getCapability(PlayerAnimationCapabilityProvider.PLAYER_ANIMATION_STATE).ifPresent(cap -> {
//                        String value = cap.getAnimationStateMap().get("angel_wings");
//                        System.out.println("Прочтено " + value);
//                    });
                    PlayerAnimationStateHandler.allPlayerAnimationStates.forEach((key, value)->{
                        player.sendSystemMessage(Component.literal(key.toString()+" "+value));
                    });
                }
            }
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTickerHelper(pLevel);
    }
}
