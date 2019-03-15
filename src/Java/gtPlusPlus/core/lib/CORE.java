package gtPlusPlus.core.lib;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.authlib.GameProfile;

import gregtech.api.GregTech_API;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.sys.GeoUtils;
import gtPlusPlus.core.util.sys.NetworkUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayerFactory;

public class CORE {

	//import cpw.mods.fml.common.Optional;

	//Analytics handler
	//public static SegmentAnalytics mHelper;

	public static Map PlayerCache;	

	//Math Related
	public static final float PI = (float) Math.PI;
	public static volatile Random RANDOM = new XSTR();

	//Env. Variables
	public static Configuration Config;

	public static boolean DEVENV = false;
	public static boolean DEBUG = false;
	public static boolean NBT_PERSISTENCY_PATCH_APPLIED = false;
	//Only can be set in Dev, no config or setting elsewhere.
	public static final boolean LOAD_ALL_CONTENT = false;;

	//Mod Variables

	public static final String name = "GT++";
	public static final String MODID = "miscutils";
	public static final String VERSION = "1.7.02.99-pre";
	public static String MASTER_VERSION = NetworkUtils.getContentFromURL("https://raw.githubusercontent.com/draknyte1/GTplusplus/master/Recommended.txt").toLowerCase();
	public static String USER_COUNTRY = GeoUtils.determineUsersCountry();
	public static boolean isModUpToDate = Utils.isModUpToDate();

	//Tweakables
	public static int EVERGLADES_ID = 227;
	public static int EVERGLADESBIOME_ID = 238;
	public static int AUSTRALIA_ID = 228;	
	public static int AUSTRALIA_BIOME_DESERT_1_ID = 239;	
	public static int AUSTRALIA_BIOME_DESERT_2_ID = 240;	
	public static int AUSTRALIA_BIOME_DESERT_3_ID = 241;
	public static int AUSTRALIA_BIOME_PLAINS_ID = 242;	
	public static int AUSTRALIA_BIOME_OUTBACK_ID = 243;	
	public static int AUSTRALIA_BIOME_OCEAN_ID = 244;	
	public static int AUSTRALIA_BIOME_FOREST_ID = 245;		

	//GT Vars;
	public static final int GREG_FIRST_ID = 760;
	public static final boolean MAIN_GREGTECH_5U_EXPERIMENTAL_FORK = Meta_GT_Proxy.areWeUsingGregtech5uExperimental();
	public static final int GREGTECH_API_VERSION = GregTech_API.VERSION;
	public static int turbineCutoffBase = 75000;

	//GT++ Fake Player Profile
	public static GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes("gtplusplus.core".getBytes()), "[GT++]");
	public static final WeakHashMap<World, EntityPlayerMP> fakePlayerCache = new WeakHashMap<World, EntityPlayerMP>();
	//Tooltips;
	public static final String GT_Tooltip = "Added by: " + EnumChatFormatting.DARK_GREEN+"Alkalus "+EnumChatFormatting.GRAY+"- "+EnumChatFormatting.RED+"[GT++]";
	public static final String GT_Tooltip_Radioactive = EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.GREEN+"Radioactive! "+EnumChatFormatting.GOLD+" Avoid direct handling without hazmat protection.";
	public static final String noItem = "";

	//Because I want to be lazy Gregtech New Horizons Var.
	public static boolean GTNH = false;
	//Because I want to be lazy. Beyond Reality Classic Var.
	public static boolean BRC = false;



	/**
	 * Lists/Maps
	 */

	//Burnables List
	public static List<Pair<Integer, ItemStack>> burnables = new ArrayList<Pair<Integer, ItemStack>>();


	//TesseractMapss
	public static final Map<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>> sTesseractGeneratorOwnershipMap = new HashMap<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>>();
	public static final Map<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>> sTesseractTerminalOwnershipMap = new HashMap<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>>();

	//BookMap
	public static final Map<String, ItemStack> sBookList = new ConcurrentHashMap<String, ItemStack>();




	/**
	 * Some Gregtech Material and Recipe Variables
	 */

	@Deprecated
	public static IGregtech_RecipeAdder sRecipeAdder;
	public static IGregtech_RecipeAdder RA;

	public static final GT_Materials[] sMU_GeneratedMaterials = new GT_Materials[1000];

	/**
	 * File Paths and Resource Paths
	 */

	public static final String
	TEX_DIR = "textures/",
	TEX_DIR_GUI = TEX_DIR + "gui/",
	TEX_DIR_ITEM = TEX_DIR + "items/",
	TEX_DIR_BLOCK = TEX_DIR + "blocks/",
	TEX_DIR_ENTITY = TEX_DIR + "entity/",
	TEX_DIR_ASPECTS = TEX_DIR + "aspects/",
	TEX_DIR_FLUIDS = TEX_DIR_BLOCK + "fluids/",
	RES_PATH = MODID + ":" + TEX_DIR,
	RES_PATH_GUI = MODID + ":" + TEX_DIR_GUI,
	RES_PATH_ITEM = MODID + ":" + TEX_DIR_ITEM,
	RES_PATH_BLOCK = MODID + ":" + TEX_DIR_BLOCK,
	RES_PATH_ENTITY = MODID + ":" + TEX_DIR_ENTITY,
	RES_PATH_ASPECTS = MODID + ":" + TEX_DIR_ASPECTS,
	RES_PATH_FLUIDS = MODID + ":" + TEX_DIR_FLUIDS;




	/** Used to create a {@link EntityPlayer} instance from {@link FakePlayerFactory}.
	 * 	If this instance already exists in the cache, we will return that instead.
	 *  These instances are held via weak reference, if the world object is unloaded, they too will be removed.
	 *  This is the suggested way to handle them, as suggested by Forge.
	 * 
	 * @param world - The {@link World} object for which you want to check for in the cache. 
	 * This object is used as a weak reference in a {@link WeakHashMap}.
	 * @return - An {@link EntityPlayerMP} instance, returned either from cache or created and cached prior to return.
	 */
	public static EntityPlayerMP getFakePlayer(World world) {
		if (fakePlayerCache.get(world) == null) {
			fakePlayerCache.put(world, FakePlayerFactory.get((WorldServer) world, CORE.gameProfile));
		}
		return fakePlayerCache.get(world);
	}
	
	
	
	
	
	
	
	
	
	
	






	/*
	 * Config Switch Class
	 */

	public static class ConfigSwitches {

		//Updates
		public static boolean enableUpdateChecker = true;

		//Debug
		public static boolean disableEnderIOIntegration = false;
		public static boolean disableEnderIOIngotTooltips = false;
		public static boolean MACHINE_INFO = true;
		public static boolean showHiddenNEIItems = false;
		public static boolean dumpItemAndBlockData = false;

		//Tools
		public static boolean enableSkookumChoochers = true;
		public static boolean enableMultiSizeTools = true;

		//Block Drops
		public static int chanceToDropDrainedShard = 196;
		public static int chanceToDropFluoriteOre = 32;

		//Machine Related
		public static boolean enableAlternativeBatteryAlloy = false;
		public static boolean enableThaumcraftShardUnification = false;
		public static boolean disableIC2Recipes = false;
		public static boolean enableAlternativeDivisionSigilRecipe = false;
		public static int boilerSteamPerSecond = 750;
		public static final boolean requireControlCores = false;

		//Feature Related
		public static boolean enableCustomCapes = false;
		public static boolean enableCustomCircuits = true;
		public static boolean enableOldGTcircuits = false;
		public static boolean disableZombieReinforcement = false;

		//GT Fixes
		public static boolean enableNitroFix = false;
		public static boolean enableSulfuricAcidFix = false;
		public static boolean enableAnimatedTurbines = true;

		//Single Block Machines
		public static boolean enableMachine_SolarGenerators = false;
		public static boolean enableMachine_Safes = true;
		public static boolean enableMachine_Dehydrators = true;
		public static boolean enableMachine_SteamConverter = true;
		public static boolean enableMachine_FluidTanks = true;
		public static boolean enableMachine_RocketEngines = true;
		public static boolean enableMachine_GeothermalEngines = true;
		public static boolean enableMachine_WorldAccelerators = true;
		public static boolean enableMachine_Tesseracts = true;
		public static boolean enableMachine_SimpleWasher = true;
		public static boolean enableMachine_Pollution = true;
		public static boolean enableMachine_ComponentAssemblers = false;
		public static boolean enableCustom_Pipes = true;
		public static boolean enableCustom_Cables = true;

		//Multiblocks
		public static boolean enableMultiblock_AlloyBlastSmelter = true;
		public static boolean enableMultiblock_IndustrialCentrifuge = true;
		public static boolean enableMultiblock_IndustrialCokeOven = true;
		public static boolean enableMultiblock_IndustrialElectrolyzer = true;
		public static boolean enableMultiblock_IndustrialMacerationStack = true;
		public static boolean enableMultiblock_IndustrialPlatePress = true;
		public static boolean enableMultiblock_IndustrialWireMill = true;
		public static boolean enableMultiblock_IronBlastFurnace = true;
		public static boolean enableMultiblock_MatterFabricator = true;
		public static boolean enableMultiblock_MultiTank = true;
		public static boolean enableMultiblock_PowerSubstation = true;
		public static boolean enableMultiblock_LiquidFluorideThoriumReactor = true;
		public static boolean enableMultiblock_NuclearFuelRefinery = true;
		public static boolean enableMultiblock_TreeFarmer = true;
		public static boolean enableMultiblock_IndustrialSifter = true;
		public static boolean enableMultiblock_IndustrialThermalCentrifuge = true;
		public static boolean enableMultiblock_IndustrialWashPlant = true;
		public static boolean enableMultiblock_LargeAutoCrafter = true;
		public static boolean enableMultiblock_ThermalBoiler = true;
		public static boolean enableMultiblock_IndustrialCuttingMachine = true;
		public static boolean enableMultiblock_IndustrialFishingPort = true;
		public static boolean enableMultiblock_IndustrialExtrudingMachine = true;
		public static boolean enableMultiblock_IndustrialMultiMachine = true;
		public static boolean enableMultiblock_Cyclotron = true;

		//Visuals
		public static boolean enableTreeFarmerParticles = true;
		public static boolean useGregtechTextures = true;








	}

	public static class Everglades{
		public static final String MODID = "ToxicEverglades";
		public static final String NAME = "GT++ Toxic Everglades";
		public static final String VERSION = "0.1";
	}
	
	public static class Australia{
		public static final String MODID = "Australia";
		public static final String NAME = "GT++ Australia";
		public static final String VERSION = "0.1";
	}

}