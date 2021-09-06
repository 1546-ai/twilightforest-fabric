package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.List;

public class BrittleFlaskItem extends Item {

	private static String lastUsedPotion;
	private static int timesUsed;
	private static boolean advancementWindow;
	public static int seconds;

	public BrittleFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Uses", 0);
		stack.getOrCreateTag().putInt("Breakage", 0);
		stack.getOrCreateTag().putBoolean("Refillable", true);
		PotionUtils.setPotion(stack, Potions.EMPTY);
		return stack;
	}

	@Override
	public boolean isBarVisible(ItemStack pStack) {
		return true;
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		CompoundTag flaskTag = stack.getOrCreateTag();
		CompoundTag potionTag = other.getOrCreateTag();

		if(action == ClickAction.SECONDARY && other.getItem() instanceof PotionItem) {
			if(potionTag.contains("Potion") && canBeRefilled(stack)) {
				if(flaskTag.contains("Potion") && flaskTag.getString("Potion").equals(potionTag.getString("Potion")) && flaskTag.getInt("Uses") < 4) {
					if(!player.getAbilities().instabuild) {
						other.shrink(1);
						player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
					}
					flaskTag.putInt("Uses", flaskTag.getInt("Uses") + 1);
					player.level.playSound(null, player.blockPosition(), TFSounds.FLASK_FILL, SoundSource.PLAYERS, flaskTag.getInt("Uses") * 0.33F, player.level.random.nextFloat() * 0.1F + 0.9F);
					return true;
				} else if(!flaskTag.contains("Potion")) {
					if(!player.getAbilities().instabuild) {
						other.shrink(1);
						player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
					}
					flaskTag.putString("Potion", potionTag.getString("Potion"));
					flaskTag.putInt("Uses", flaskTag.getInt("Uses") + 1);
					player.level.playSound(null, player.blockPosition(), TFSounds.FLASK_FILL, SoundSource.PLAYERS, flaskTag.getInt("Uses") * 0.33F, player.level.random.nextFloat() * 0.1F + 0.9F);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getTag();

		if(tag != null && tag.contains("Potion") && tag.getString("Potion").equals(Potions.EMPTY.toString())) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		}

		if(tag != null && tag.contains("Uses") && tag.getInt("Uses") > 0) {
			return ItemUtils.startUsingInstantly(level, player, hand);
		}

		return InteractionResultHolder.fail(player.getItemInHand(hand));
	}

	public int getUseDuration(ItemStack pStack) {
		return 32;
	}

	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.DRINK;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		CompoundTag tag = stack.getOrCreateTag();
		if (entity instanceof Player player) {
			if (!level.isClientSide) {
				for (MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(stack)) {
					if (mobeffectinstance.getEffect().isInstantenous()) {
						mobeffectinstance.getEffect().applyInstantenousEffect(player, player, player, mobeffectinstance.getAmplifier(), 1.0D);
					} else {
						player.addEffect(new MobEffectInstance(mobeffectinstance));
					}
				}
				addTowardsAdvancement(tag.getString("Potion"), player);
			}
			level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
			player.awardStat(Stats.ITEM_USED.get(this));
			if (!player.getAbilities().instabuild) {
				tag.putInt("Uses", tag.getInt("Uses") - 1);
			}

			if(tag.getInt("Uses") <= 0) {
				tag.putString("Potion", Potions.EMPTY.getRegistryName().toString());
			}

			if (canBreak() && !player.getAbilities().instabuild) {
				if (tag.getInt("Uses") <= 0) {
					stack.shrink(1);
					level.playSound(null, entity.blockPosition(), TFSounds.BRITTLE_FLASK_BREAK, SoundSource.PLAYERS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
				} else {
					tag.putInt("Breakage", tag.getInt("Breakage") + 1);
					tag.putBoolean("Refillable", false);
					level.playSound(null, entity.blockPosition(), TFSounds.BRITTLE_FLASK_CRACK, SoundSource.PLAYERS, tag.getInt("Uses") * 0.33F, level.random.nextFloat() * 0.1F + 0.9F);
				}
			}
		}
		return super.finishUsingItem(stack, level, entity);
	}

	private void addTowardsAdvancement(String potionDrank, Player drinker) {
		if(lastUsedPotion == null) {
			lastUsedPotion = Potions.EMPTY.getRegistryName().toString();
		}

		if (!lastUsedPotion.equals(potionDrank)) {
			timesUsed = 1;
			lastUsedPotion = potionDrank;
			advancementWindow = true;
		} else {
			timesUsed++;
		}

		if(timesUsed >= 4 && drinker instanceof ServerPlayer player && drinker.isAlive() && lastUsedPotion.equals(Potions.STRONG_HARMING.getRegistryName().toString()) && advancementWindow) {
			PlayerAdvancements advancements = player.getAdvancements();
			ServerAdvancementManager manager = ((ServerLevel) player.getCommandSenderWorld()).getServer().getAdvancements();
			Advancement advancement = manager.getAdvancement(TwilightForestMod.prefix("full_mettle_alchemist"));
			if (advancement != null) {
				advancements.award(advancement, "drink_4_harming");
			}
		}
	}

	public static void ticker() {
		if(advancementWindow) seconds++;

		if(seconds == 8) {
			advancementWindow = false;
			timesUsed = 0;
			lastUsedPotion = null;
			seconds = 0;
		}
	}

	public boolean canBreak() {
		return true;
	}

	public boolean canBeRefilled(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("Refillable");
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
		tooltip.add(new TranslatableComponent("item.twilightforest.flask_capacity", stack.getOrCreateTag().getInt("Uses"), 4).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return (stack.hasTag() && stack.getTag() != null) ? stack.getTag().getInt("Breakage") : super.getDurabilityForDisplay(stack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if(allowdedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.getOrCreateTag().putInt("Uses", 0);
			stack.getOrCreateTag().putInt("Breakage", 0);
			stack.getOrCreateTag().putBoolean("Refillable", true);
			items.add(stack);
		}
	}
}
