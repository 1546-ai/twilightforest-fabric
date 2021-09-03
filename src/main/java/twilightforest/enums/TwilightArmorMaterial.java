package twilightforest.enums;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.item.TFItems;

import java.util.function.Supplier;

public enum TwilightArmorMaterial implements ArmorMaterial {
	ARMOR_NAGA("naga_scale", 21, new int[]{3, 6, 7, 2}, 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.5F, () -> Ingredient.of(TFItems.naga_scale)),
	ARMOR_IRONWOOD("ironwood", 20, new int[]{2, 5, 7, 2}, 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0F, () -> Ingredient.of(TFItems.ironwood_ingot)),
	ARMOR_FIERY("fiery", 25, new int[]{4, 7, 9, 4}, 10, SoundEvents.ARMOR_EQUIP_GENERIC, 1.5F, () -> Ingredient.of(TFItems.fiery_ingot)),
	ARMOR_STEELEAF("steeleaf", 10, new int[]{3, 6, 8, 3}, 9, SoundEvents.ARMOR_EQUIP_GENERIC, 0F, () -> Ingredient.of(TFItems.steeleaf_ingot)),
	ARMOR_KNIGHTLY("knightly", 20, new int[]{3, 6, 8, 3}, 8, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, () -> Ingredient.of(TFItems.knightmetal_ingot)),
	ARMOR_PHANTOM("phantom", 30, new int[]{3, 6, 8, 3}, 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2.5F, () -> Ingredient.of(TFItems.knightmetal_ingot)),
	ARMOR_YETI("yetiarmor", 20, new int[]{3, 6, 7, 4}, 15, SoundEvents.ARMOR_EQUIP_GENERIC, 3F, () -> Ingredient.of(TFItems.alpha_fur)),
	ARMOR_ARCTIC("arcticarmor", 10, new int[]{2, 5, 7, 2}, 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2F, () -> Ingredient.of(TFItems.arctic_fur));

	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durability;
	private final int[] damageReduction;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final Supplier<Ingredient> repairMaterial;

	TwilightArmorMaterial(String name, int durability, int damageReduction[], int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.durability = durability;
		this.damageReduction = damageReduction;
		this.enchantability = enchantability;
		this.equipSound = sound;
		this.toughness = toughness;
		this.repairMaterial = repairMaterial;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlot slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * durability;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlot slotIn) {
		return damageReduction[slotIn.getIndex()];
	}

	@Override
	public int getEnchantmentValue() {
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}

	@Override
	public float getKnockbackResistance() {
		return 0.0F; //Discuss use in other sets
	}
}
