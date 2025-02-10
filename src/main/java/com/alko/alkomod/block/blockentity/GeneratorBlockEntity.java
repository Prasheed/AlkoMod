package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GeneratorBlockEntity extends BlockEntity implements TickableBlockEntity {

    public boolean workState = false;
    public int ticks;
    public GeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag AlkoModData = tag.getCompound(Alkomod.MOD_ID);
        this.workState = AlkoModData.getBoolean("work_state");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        var AlkoModData = new CompoundTag();
        AlkoModData.putBoolean("work_state", this.workState);
        tag.put(Alkomod.MOD_ID,AlkoModData);

    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    public boolean getWorkState() {
        return workState;
    }

    public boolean changeWorkState(){
        workState = !workState;
        return workState;
    }


    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide())
            return;

        ticks++;

        // Выполняем действие каждые 10 тиков (0.5 секунды)
        if (ticks % 10 == 0) {
            // Получаем позицию нашего блока
            BlockPos pos = this.getBlockPos();

            // Создаем зону поиска (радиус 5 блоков)
            AABB area = new AABB(pos).inflate(5);

            // Получаем все Entity в радиусе, исключая игроков
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, area,
                    entity -> !(entity instanceof Player));

            // Наносим урон всем подходящим Entity
            for (Entity entity : entities) {
                entity.hurt(level.damageSources().generic(), 1.0F); // 1 сердце урона
            }
        }

        // Сбрасываем счетчик, чтобы избежать переполнения
        if (ticks >= 100) ticks = 0;
    }
}
