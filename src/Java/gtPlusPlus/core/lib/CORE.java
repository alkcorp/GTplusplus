package gtPlusPlus.core.lib;

import java.util.*;

import gregtech.api.GregTech_API;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.geo.GeoUtils;
import gtPlusPlus.core.util.gregtech.recipehandlers.GregtechRecipe;
import gtPlusPlus.core.util.networking.NetworkUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.api.objects.XSTR;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;

public class CORE {

	protected CORE(){
		//import cpw.mods.fml.common.Optional;
	}

	//Math Related
	public static final float PI = (float) Math.PI;
	public static volatile Random RANDOM = new XSTR();
	
	public static boolean DEVENV = false;

	public static final String name = "GT++";
	public static final String MODID = "miscutils";
	public static final String VERSION = "1.5.6-Pre-release";
	public static final String MASTER_VERSION = NetworkUtils.getContentFromURL("https://raw.githubusercontent.com/draknyte1/GTplusplus/master/Recommended.txt").toLowerCase();
	public static final String USER_COUNTRY = GeoUtils.determineUsersCountry();
	public static boolean isModUpToDate = Utils.isModUpToDate();
	public static boolean DEBUG = false;
	public static final boolean LOAD_ALL_CONTENT = false;
	public static final int GREG_FIRST_ID = 760;
	public static Map PlayerCache;
	public static final String[] VOLTAGES = {"ULV","LV","MV","HV","EV","IV","LuV","ZPM","UV","MAX"};
	public static List<Pair<Integer, ItemStack>> burnables = new ArrayList<Pair<Integer, ItemStack>>();
	public static final boolean MAIN_GREGTECH_5U_EXPERIMENTAL_FORK = Meta_GT_Proxy.areWeUsingGregtech5uExperimental();
	public static final int GREGTECH_API_VERSION = GregTech_API.VERSION;
	public static IGregtech_RecipeAdder RA;
	@Deprecated
	public static IGregtech_RecipeAdder sRecipeAdder;
	public static GregtechRecipe GT_Recipe = new GregtechRecipe();

	public static Configuration Config;
	public static final String GT_Tooltip = "Added by: " + EnumChatFormatting.DARK_GREEN+"Alkalus "+EnumChatFormatting.GRAY+"- "+EnumChatFormatting.RED+"[GT++]";
	public static final String GT_Tooltip_Radioactive = EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.GREEN+"Radioactive! "+EnumChatFormatting.GOLD+" Avoid direct handling without hazmat protection.";
	public static final String noItem = "";

	//Because I want to be lazy.
	public static boolean GTNH = false;
	public static int DARKBIOME_ID = 238;

	/**
	 * A List containing all the Materials, which are somehow in use by GT and therefor receive a specific Set of Items.
	 */
	public static final GT_Materials[] sMU_GeneratedMaterials = new GT_Materials[1000];

	//Tesseract map
	public static final Map<Integer, GT_MetaTileEntity_TesseractGenerator> sTesseractGenerators = new HashMap<>();

	//GUIS
	public enum GUI_ENUM
	{
		ENERGYBUFFER, TOOLBUILDER, NULL, NULL1, NULL2
	}

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



	//public static final Materials2[] MiscGeneratedMaterials = new Materials2[1000];

	public static class configSwitches {

		//Debug
		public static boolean disableEnderIOIntegration = false;
		public static boolean MACHINE_INFO = true;

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

		//Feature Related
		public static boolean enableCustomAlvearyBlocks = false;
		public static boolean enableCustomCircuits = true;

		//Single Block Machines
		public static boolean enableMachine_SolarGenerators = false;
		public static boolean enableMachine_Safes = true;
		public static boolean enableMachine_Dehydrators = true;
		public static boolean enableMachine_SteamConverter = true;
		public static boolean enableMachine_FluidTanks = true;
		public static boolean enableMachine_RocketEngines = true;
		public static boolean enableMachine_GeothermalEngines = true;
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

		//Visuals
		public static boolean enableTreeFarmerParticles = true;
		public static boolean useGregtechTextures = true;

	}

}
