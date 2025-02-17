package com.alko.alkomod.block.blockentity;

public enum EnergySide {
    NONE,      // Нет соединения
    INPUT,     // Вход энергии
    OUTPUT;     // Выход энергии

    public EnergySide next() {
        // Получаем следующий индекс по кругу
        int nextOrdinal = (this.ordinal() + 1) % EnergySide.values().length;
        return EnergySide.values()[nextOrdinal];
    }
}
