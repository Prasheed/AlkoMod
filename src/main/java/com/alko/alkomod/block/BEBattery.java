package com.alko.alkomod.block;

import com.alko.alkomod.Items.WrenchItem;
import com.alko.alkomod.block.blockentity.BEBatteryBlockEntity;
import com.alko.alkomod.block.blockentity.EnergySide;
import com.alko.alkomod.block.blockentity.ModBlockEntity;
import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import com.alko.alkomod.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BEBattery extends Block implements EntityBlock {

    public BEBattery() {
        super(Properties.copy(Blocks.IRON_BLOCK));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntity.BE_BATTERY_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTickerHelper(pLevel);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()){
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BEBatteryBlockEntity batteryBlock){
                if(!player.isCrouching()){
                    if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof WrenchItem){
                        Direction dir = hit.getDirection();
                        EnergySide curr = batteryBlock.getSideMode(dir);
                        batteryBlock.setSideMode(dir,curr.next());
                    }else{
                        NetworkHooks.openScreen((ServerPlayer) player, batteryBlock,pos);
                    }
                }else{
                    System.out.println(batteryBlock.getStoredEnergy());
                }
            }

        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
