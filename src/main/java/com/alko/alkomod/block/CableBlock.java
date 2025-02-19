package com.alko.alkomod.block;

import com.alko.alkomod.Items.WrenchItem;
import com.alko.alkomod.block.blockentity.BEBlockEntity;
import com.alko.alkomod.block.blockentity.CableBlockBlockEntity;
import com.alko.alkomod.block.blockentity.EnergySide;
import com.alko.alkomod.block.blockentity.ModBlockEntity;
import com.alko.alkomod.energy.EnergyNetwork;
import com.alko.alkomod.energy.EnergyNetworkList;
import com.alko.alkomod.energy.EnergySystemUtils;
import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import com.alko.alkomod.util.EnergyUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CableBlock extends Block implements EntityBlock {
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
        if (!level.isClientSide()) {
            BlockEntity thisEntity = level.getBlockEntity(pos);
            if(thisEntity instanceof CableBlockBlockEntity cableEntity){
                if(cableEntity.getNetworkId() != -1){
                    BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                    if (neighborEntity instanceof BEBlockEntity beBlockEntity) {
                        EnergySystemUtils.rebuildNetworkById(cableEntity);
                    }
                }else{
                    Map<String, HashSet<BEBlockEntity>> newNetwork = EnergySystemUtils.buildNetwork(pos, level, false);
                    HashSet<BEBlockEntity> input = newNetwork.get("INPUT");
                    HashSet<BEBlockEntity> output = newNetwork.get("OUTPUT");
                    EnergyNetworkList.addNetwork(new EnergyNetwork(input,output,EnergyNetworkList.getFreeId()));
                }

            }
        }
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
        return level.getBlockState(pos).getBlock() instanceof CableBlock || level.getBlockEntity(pos) instanceof IBeerEnergyStorageBlock    ;
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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide()){
            if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof WrenchItem && hand == InteractionHand.MAIN_HAND){

            }
        }


        return super.use(state,level,pos,player,hand,hit);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntity.CABLE_BLOCK_BLOCK_ENTITY.get().create(blockPos,blockState);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean pIsMoving) {
        if(!level.isClientSide() && !oldState.is(state.getBlock())){
            Set<EnergyNetwork> potentialNetworks = new HashSet<>();
            for (Direction side : Direction.values()){ // Проверка на соседние кабели в составе существующих сетей (для потенциального объединения)
                BlockPos neighborPos = pos.relative(side);
                if(level.getBlockState(neighborPos).hasBlockEntity() && level.getBlockEntity(neighborPos) instanceof CableBlockBlockEntity cableEntity){
                    if(cableEntity.getNetworkId() != -1){
                        potentialNetworks.add(EnergyNetworkList.getNetworkById(cableEntity.getNetworkId()));
                    }
                }
            }
            if (potentialNetworks.size() > 1){
                System.out.println("Ситуация для объединения сетей");
                //mergeNetworks
                //Соединяем несколько найденных сетей в одну и всё
                return;
            } else if (potentialNetworks.isEmpty()) {
                //Рядом нет проводов (по крайней мере в составе существующих сетей) и мы на данном этапе ничего не делаем... или нет?
                //Проверяем на наличие энергоблока и создаём новую (маленькую) сеть
                Map<String, HashSet<BEBlockEntity>> newNetwork = EnergySystemUtils.buildNetwork(pos, level,false);
                HashSet<BEBlockEntity> input = newNetwork.get("INPUT");
                HashSet<BEBlockEntity> output = newNetwork.get("OUTPUT");
                EnergyNetworkList.addNetwork(new EnergyNetwork(input,output,EnergyNetworkList.getFreeId()));
            }else {
                //Найдены провод(а) в составе всего одной сети, значит присоединяемся к ней
                EnergyNetwork network = potentialNetworks.iterator().next();
                EnergySystemUtils.addCablesToNetwork(pos,level, network.getNetworkId());
                //Потом проверяем на наличие вокруг блоков которые могут быть присоединены и если есть подходящие, добавляем их в сеть в которую только что вступили
                for(Direction side : Direction.values()){
                    BlockPos neighborPos = pos.relative(side);
                    if(level.getBlockState(neighborPos).hasBlockEntity() && level.getBlockEntity(neighborPos) instanceof BEBlockEntity beEntity){
                        EnergySide mode = beEntity.getSideMode(side.getOpposite());
                        switch (mode){
                            case INPUT -> network.addInputBlock(beEntity);
                            case OUTPUT -> network.addOutputBlock(beEntity);
                        }
                    }
                }
                //Потом проверяем на наличие кабелей
            }


        }
    }
}