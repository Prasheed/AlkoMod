package com.alko.alkomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GeneratorBlock extends HorizontalDirectionalBlock {
    //public static final IntegerProperty ROTATION = BlockStateProperties.
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GeneratorBlock() {
        super(Properties.copy(Blocks.GOLD_BLOCK).noOcclusion());
    }

    private static final VoxelShape SHAPE_A = Block.box(0, 0, 2, 16, 13, 14);
    private static final VoxelShape SHAPE_B = Block.box(2, 0, 0, 14, 13, 16);


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        switch ((Direction)pState.getValue(FACING)) {
            case NORTH:
                return SHAPE_B;
            case SOUTH:
                return SHAPE_B;
            case WEST:
                return SHAPE_A;
            case EAST:
            default:
                return SHAPE_A;
        }
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
