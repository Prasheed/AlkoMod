package com.alko.alkomod.Items;

import com.alko.alkomod.block.blockentity.BEBlockEntity;
import com.alko.alkomod.block.blockentity.CableBlockBlockEntity;
import com.alko.alkomod.block.blockentity.EnergySide;
import com.alko.alkomod.energy.EnergyNetworkList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WrenchItem extends Item {
    public WrenchItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        if(!level.isClientSide()){
            if(level.getBlockEntity(pos) instanceof CableBlockBlockEntity cableEntity){
                if(ctx.getPlayer().isCrouching()){
                    if(cableEntity.getNetworkId() != -1){
                        EnergyNetworkList.getNetworkById(cableEntity.getNetworkId()).highlightMembers(level);
                    }else{
                        System.out.println("У кабеля нет своей сети");
                    }
                }else{
                    ctx.getPlayer().sendSystemMessage(Component.literal(String.valueOf(cableEntity.getNetworkId())));
                }
            }
        }
        return super.useOn(ctx);
    }
}
