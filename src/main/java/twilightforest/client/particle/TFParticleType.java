package twilightforest.client.particle;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.client.particle.data.PinnedFireflyData;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class TFParticleType {

	//public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TwilightForestMod.ID);

	public static final SimpleParticleType LARGE_FLAME = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":large_flame", FabricParticleTypes.simple(false));
	public static final SimpleParticleType LEAF_RUNE = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":leaf_rune", FabricParticleTypes.simple(false));
	public static final SimpleParticleType BOSS_TEAR = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":boss_tear", FabricParticleTypes.simple(false));
	public static final SimpleParticleType GHAST_TRAP = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":ghast_trap", FabricParticleTypes.simple(false));
	public static final SimpleParticleType PROTECTION = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":protection", FabricParticleTypes.simple(false));
	public static final SimpleParticleType SNOW = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":snow", FabricParticleTypes.simple(false));
	public static final SimpleParticleType SNOW_WARNING = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":snow_warning", FabricParticleTypes.simple(false));
	public static final SimpleParticleType SNOW_GUARDIAN = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":snow_guardian", FabricParticleTypes.simple(false));
	public static final SimpleParticleType ICE_BEAM = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":ice_beam", FabricParticleTypes.simple(false));
	public static final SimpleParticleType ANNIHILATE = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":annihilate", FabricParticleTypes.simple(false));
	public static final SimpleParticleType HUGE_SMOKE = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":huge_smoke", FabricParticleTypes.simple(false));
	public static final SimpleParticleType FIREFLY = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":firefly", FabricParticleTypes.simple(false));
	public static final SimpleParticleType WANDERING_FIREFLY = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":wandering_firefly", FabricParticleTypes.simple(false));
	public static final SimpleParticleType JAR_WANDERING_FIREFLY = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":jar_wandering_firefly", FabricParticleTypes.simple(false));
	public static final ParticleType<PinnedFireflyData> FIREFLY_PINNED = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":firefly_pinned", new ParticleType<PinnedFireflyData>(false, new PinnedFireflyData.Deserializer()) {
		@Override
		public Codec<PinnedFireflyData> codec() {
			return PinnedFireflyData.codecFirefly();
		}
	});
	public static final ParticleType<LeafParticleData> FALLEN_LEAF = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":fallen_leaf", new ParticleType<LeafParticleData>(false, new LeafParticleData.Deserializer()) {
		@Override
		public Codec<LeafParticleData> codec() {
			return LeafParticleData.codecLeaf();
		}
	});
	public static final SimpleParticleType OMINOUS_FLAME = Registry.register(Registry.PARTICLE_TYPE, TwilightForestMod.ID + ":ominous_flame", new SimpleParticleType(false));

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void registerFactories() {
		ParticleFactoryRegistry.getInstance().register(TFParticleType.LARGE_FLAME, LargeFlameParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.LEAF_RUNE, LeafRuneParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.BOSS_TEAR, new GhastTearParticle.Factory());
		ParticleFactoryRegistry.getInstance().register(TFParticleType.GHAST_TRAP, GhastTrapParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.PROTECTION, ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW, SnowParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_GUARDIAN, SnowGuardianParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_WARNING, SnowWarningParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ICE_BEAM, IceBeamParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ANNIHILATE, AnnihilateParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.HUGE_SMOKE, SmokeScaleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FIREFLY, FireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.WANDERING_FIREFLY, WanderingFireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.JAR_WANDERING_FIREFLY, WanderingFireflyParticle.FromJarFactory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FIREFLY_PINNED, PinnedFireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FALLEN_LEAF, LeafParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.OMINOUS_FLAME, FlameParticle.SmallFlameProvider::new);
	}
}
