package twilightforest.client;

import com.google.common.base.Charsets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import twilightforest.TwilightForestMod;
import twilightforest.lib.mixin.client.BlockModelAccessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class PatchModelLoader implements ModelResourceProvider {
    public static final PatchModelLoader INSTANCE = new PatchModelLoader();

    private PatchModelLoader() {
    }

    @Override
    public UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) {
        if(!resourceId.getNamespace().equals(TwilightForestMod.ID))
            return null;
        JsonObject modelContents = BlockModelAccessor.getGSON().fromJson(getModelJson(resourceId), JsonObject.class);
        if(modelContents.has("loader")) {
            if(!modelContents.get("loader").getAsString().equals("twilightforest:patch"))
                return null;
            if (!modelContents.has("texture"))
                throw new RuntimeException("Patch model missing value for 'texture'.");

            return new UnbakedPatchModel(new ResourceLocation(modelContents.get("texture").getAsString()), JsonUtils.getBooleanOr("shaggify", modelContents, false));
        }

        return null;
    }

    static BufferedReader getModelJson(ResourceLocation location) {
        ResourceLocation file = new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
        Resource resource = null;
        try {
            resource = Minecraft.getInstance().getResourceManager().getResource(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BufferedReader(new InputStreamReader(resource.getInputStream(), Charsets.UTF_8));
    }
}
