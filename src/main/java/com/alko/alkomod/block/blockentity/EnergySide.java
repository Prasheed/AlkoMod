package com.alko.alkomod.block.blockentity;

public enum EnergySide {
    NONE,      // Нет соединения
    INPUT,     // Вход энергии
    OUTPUT;     // Выход энергии

    public EnergySide next() {
        int nextOrdinal = (this.ordinal() + 1) % EnergySide.values().length;
        return EnergySide.values()[nextOrdinal];
    }

    public EnergySide previous() {
        int prevOrdinal = (this.ordinal() - 1 + EnergySide.values().length) % EnergySide.values().length;
        return EnergySide.values()[prevOrdinal];
    }

}
