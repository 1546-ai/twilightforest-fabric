package twilightforest.lib.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.lib.extensions.ICachedName;
import twilightforest.lib.extensions.IItemEx;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(Item.class)
public class ItemMixin implements IItemEx, ICachedName<Item> {
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity living, int count) {

    }

    @Unique
    private Object renderProperties;

    public Object getRenderPropertiesInternal() {
        return renderProperties;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void clientInit(Item.Properties itemPro, CallbackInfo ci) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
//            initializeClient(properties -> {
//                if (properties == this)
//                    throw new IllegalStateException("Don't extend IItemRenderProperties in your item, use an anonymous class instead.");
//                this.renderProperties = properties;
//            });
        }
    }

    @Unique
    private ResourceLocation id;

    @Unique
    @Override
    public Item setRegistryName(ResourceLocation location) {
        this.id = location;
        return (Item) (Object) this;
    }

    @Unique
    @Override
    public ResourceLocation getRegistryName() {
        return this.id;
    }
}
