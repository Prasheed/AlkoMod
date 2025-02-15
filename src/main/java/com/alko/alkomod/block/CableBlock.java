package com.alko.alkomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class CableBlock extends Block {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    
    private static final Map<Direction, BooleanProperty> PROPERTY_MAP = new EnumMap<>(Map.of(
            Direction.NORTH, NORTH,
            Direction.SOUTH, SOUTH,
            Direction.EAST, EAST,
            Direction.WEST, WEST,
            Direction.UP, UP,
            Direction.DOWN, DOWN
    ));

    public CableBlock() {
        super(Properties.copy(Blocks.STONE).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(EAST, false).setValue(WEST, false)
                .setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(NORTH, canConnect(level, pos.north()))
                .setValue(SOUTH, canConnect(level, pos.south()))
                .setValue(EAST, canConnect(level, pos.east()))
                .setValue(WEST, canConnect(level, pos.west()))
                .setValue(UP, canConnect(level, pos.above()))
                .setValue(DOWN, canConnect(level, pos.below()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, moved);
        level.setBlock(pos, getUpdatedState(state, level, pos), 2);
    }

    private BlockState getUpdatedState(BlockState state, Level level, BlockPos pos) {
        return state.setValue(NORTH, canConnect(level, pos.north()))
                .setValue(SOUTH, canConnect(level, pos.south()))
                .setValue(EAST, canConnect(level, pos.east()))
                .setValue(WEST, canConnect(level, pos.west()))
                .setValue(UP, canConnect(level, pos.above()))
                .setValue(DOWN, canConnect(level, pos.below()));
    }

    private boolean canConnect(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof CableBlock;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.box(0.3, 0.3, 0.3, 0.7, 0.7, 0.7);
        if (state.getValue(NORTH)) shape = Shapes.or(shape, Shapes.box(0.3, 0.3, 0, 0.7, 0.7, 0.3));
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, Shapes.box(0.3, 0.3, 0.7, 0.7, 0.7, 1));
        if (state.getValue(EAST)) shape = Shapes.or(shape, Shapes.box(0.7, 0.3, 0.3, 1, 0.7, 0.7));
        if (state.getValue(WEST)) shape = Shapes.or(shape, Shapes.box(0, 0.3, 0.3, 0.3, 0.7, 0.7));
        if (state.getValue(UP)) shape = Shapes.or(shape, Shapes.box(0.3, 0.7, 0.3, 0.7, 1, 0.7));
        if (state.getValue(DOWN)) shape = Shapes.or(shape, Shapes.box(0.3, 0, 0.3, 0.7, 0.3, 0.7));
        return shape;
    }
}