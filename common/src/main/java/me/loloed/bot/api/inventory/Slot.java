package me.loloed.bot.api.inventory;

import java.util.Objects;

public class Slot {
    public static Slot fromMain(int index) {
        return new Slot(index + 9);
    }

    public static Slot fromOffHand() {
        return new Slot(45);
    }

    public static Slot fromArmor(int index) {
        return new Slot(index + 5);
    }

    public static Slot fromHotBar(int index) {
        return new Slot(index + 36);
    }
    public static final int MAX_INDEX = 45;

    private final int index;

    public Slot(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getArmorIndex() {
        return index - 5;
    }

    public int getHotBarIndex() {
        return index - 36;
    }

    public int getMainIndex() {
        return index - 9;
    }

    public int getVanillaIndex() {
        if (isOffHand()) return 40;
        if (isArmor()) return getArmorIndex() + 36;
        if (isHotBar()) return getHotBarIndex();
        return getMainIndex() + 9;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return index == slot.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public boolean isArmor() {
        return index > 4 && index < 9;
    }

    public boolean isOffHand() {
        return index == 45;
    }

    public boolean isMain() {
        return index > 8 && index < 36;
    }

    public boolean isHotBar() {
        return index > 35 && index < 45;
    }
}
