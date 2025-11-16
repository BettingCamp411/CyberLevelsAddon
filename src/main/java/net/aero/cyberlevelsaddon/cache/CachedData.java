package net.aero.cyberlevelsaddon.cache;

public class CachedData {
    private int level = 0;
    private double currentExp = 0;
    private double requiredExp = 0;

    public boolean update(int level, double currentExp, double requiredExp) {
        if (this.level != level || this.currentExp != currentExp || this.requiredExp != requiredExp) {
            this.level = level;
            this.currentExp = currentExp;
            this.requiredExp = requiredExp;
            return true;
        }
        return false;
    }
}
