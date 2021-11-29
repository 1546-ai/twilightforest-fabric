package twilightforest.enums;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.item.TFItems;

import java.util.function.Supplier;

public enum TwilightItemTier implements Tier {
    // harvestLevel, maxUses, efficiency, damage, enchantability
    IRONWOOD(2, 512, 6.5F, 2, 25, () -> Ingredient.of(TFItems.IRONWOOD_INGOT.get())),
    FIERY(4, 1024, 9F, 4, 10, () -> Ingredient.of(TFItems.FIERY_INGOT.get())),
    STEELEAF(3, 131, 8.0F, 3, 9, () -> Ingredient.of(TFItems.STEELEAF_INGOT.get())),
    KNIGHTMETAL(3, 512, 8.0F, 3, 8, () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get())),
    GIANT(1, 1024, 4.0F, 1.0F, 5, () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get())),
    ICE(0, 32, 1.0F, 3.5F, 5, () -> Ingredient.of(Blocks.PACKED_ICE)),
    GLASS(0, 1, 1.0F, 36.0F, 30, () -> Ingredient.EMPTY);

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    TwilightItemTier(int harvestLevel, int maxUses, float efficiency, float damage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = damage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}