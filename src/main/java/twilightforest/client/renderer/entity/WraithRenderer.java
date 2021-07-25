package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.WraithModel;
import twilightforest.entity.WraithEntity;

import javax.annotation.Nullable;

public class WraithRenderer extends HumanoidMobRenderer<WraithEntity, WraithModel> {

	private static final ResourceLocation textureWraith = TwilightForestMod.getModelTexture("ghost.png");

	public WraithRenderer(EntityRenderDispatcher manager, WraithModel modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
	}

	@Nullable
	@Override
	protected RenderType getRenderType(WraithEntity entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return RenderType.entityTranslucent(getTextureLocation(entity));
	}

	@Override
	public ResourceLocation getTextureLocation(WraithEntity wraith) {
		return textureWraith;
	}
}
