package GTplusplus.recipes;

import java.util.Arrays;

import GTplusplus.GTppConfig;
import GTplusplus.block.*;
import GTplusplus.machines.GTppTileEntities;
import GTplusplus.materials.GTppMaterials;
import gregtech.api.GTValues;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.blocks.BlockMetalCasing.MetalCasingType;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GTppMachineRecipes {
    public static void init() {
    	
    	//Casings
    	ModHandler.addShapedRecipe("maceration_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.MACERATION_CASING), "PhP", "PHP", "PwP", 'P', "platePalladium", 'H', "frameGtBlackSteel");
    	ModHandler.addShapedRecipe("washplant_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.WASHPLANT_CASING), "PhP", "PHP", "PwP", 'P', "plateBlueSteel", 'H', "frameGtBlackSteel");
    	ModHandler.addShapedRecipe("thermal_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.THERMAL_CASING), "PhP", "PHP", "PwP", 'P', "plateRedSteel", 'H', "frameGtBlackSteel");
    	ModHandler.addShapedRecipe("chemical_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.CHEMICAL_CASING), "PhP", "PHP", "PwP", 'P', "platePolytetrafluoroethylene", 'H', MetaBlocks.METAL_CASING.getItemVariant(MetalCasingType.STEEL_SOLID, 1));
    	ModHandler.addShapedRecipe("fisher_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.FISHER_CASING), "PhP", "PHP", "PwP", 'P', "plateInconel792", 'H', "frameGtStaballoy");
    	ModHandler.addShapedRecipe("electrolyzer_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.ELECTROLYZER_CASING), "PhP", "PHP", "PwP", 'P', "platePotin", 'H', "frameGtStaballoy");
    	ModHandler.addShapedRecipe("centrifuge_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.CENTRIFUGE_CASING), "PhP", "PHP", "PwP", 'P', "plateInconel690", 'H', "frameGtStaballoy");
    	ModHandler.addShapedRecipe("wire_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.WIRE_CASING), "PhP", "PHP", "PwP", 'P', "plateTalonite", 'H', "frameGtStaballoy");
    	ModHandler.addShapedRecipe("sieve_casing", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.SIEVE_CASING), "PhP", "PHP", "PwP", 'P', "plateEglinSteel", 'H', "frameGtBlackSteel");
    	ModHandler.addShapedRecipe("sieve_grate", GTppMetaBlocks.MULTIBLOCK_CASING.getItemVariant(GTppMultiblockCasing.CasingType.SIEVE_GRATE), "TPT", "WHW", "TWT", 'P', MetaItems.COMPONENT_FILTER.getStackForm(), 'T', "plateBlackSteel", 'W', "wireFineSteel", 'H', "frameGtSteel");
		
    	if(GTppConfig.multiblocks.EBF) {
			ModHandler.removeRecipeByName(new ResourceLocation("gregtech:electric_blast_furnace"));
			ModHandler.addShapedRecipe("improved_blast_furnace", GTppTileEntities.BLAST_FURNACE.getStackForm(), "FFF", "CHC", "WCW", 'F', new ItemStack(Blocks.FURNACE, 1), 'C', "circuitBasic", 'H', MetaBlocks.METAL_CASING.getItemVariant(MetalCasingType.INVAR_HEATPROOF, 1), 'W', "cableGtSingleTin");
		}
		
		//Industrial Machine Controllers recipes
		if(GTppConfig.multiblocks.Macerator)ModHandler.addShapedRecipe("multiblock_industrial_macerator", GTppTileEntities.INDUSTRIAL_MACERATOR.getStackForm(), "TWT", "MCM", "TIT", 'M', MetaTileEntities.MACERATOR[GTValues.HV].getStackForm(), 'C', "circuitMaster", 'W', MetaItems.COMPONENT_GRINDER_TUNGSTEN.getStackForm(), 'I', MetaTileEntities.HULL[GTValues.IV].getStackForm(), 'T', "plateTungstenCarbide");
		if(GTppConfig.multiblocks.Thermal)ModHandler.addShapedRecipe("multiblock_industrial_thermalcentrifuge", GTppTileEntities.INDUSTRIAL_THERMALCENTRIFUGE.getStackForm(), "TWT", "MCM", "TIT", 'M', "stickTalonite", 'W', "circuitMaster", 'C', MetaTileEntities.THERMAL_CENTRIFUGE[GTValues.HV].getStackForm(), 'I', "gearTalonite", 'T', "plateRedSteel");
		if(GTppConfig.multiblocks.Orewash)ModHandler.addShapedRecipe("multiblock_industrial_orewasher", GTppTileEntities.INDUSTRIAL_OREWASHER.getStackForm(), "TWT", "MCM", "TIT", 'M', "plateTalonite", 'I', "circuitMaster", 'C', MetaTileEntities.ORE_WASHER[GTValues.HV].getStackForm(), 'W', MetaItems.ELECTRIC_PUMP_EV, 'T', "plateBlueSteel");
		if(GTppConfig.multiblocks.Electrolyzer)ModHandler.addShapedRecipe("multiblock_industrial_electrolyzer", GTppTileEntities.INDUSTRIAL_ELECTROLYZER.getStackForm(), "TWT", "MCM", "TIT", 'M', MetaTileEntities.ELECTROLYZER[GTValues.HV].getStackForm(), 'W', "circuitMaster", 'C', "rotorTitanium", 'I', MetaTileEntities.HULL[GTValues.IV].getStackForm(), 'T', "platePotin");
		if(GTppConfig.multiblocks.Centrifuge)ModHandler.addShapedRecipe("multiblock_industrial_centrifuge", GTppTileEntities.INDUSTRIAL_CENTRIFUGE.getStackForm(), "TWT", "MCM", "TIT", 'M', MetaTileEntities.CENTRIFUGE[GTValues.HV].getStackForm(), 'W', "circuitMaster", 'C', "rotorTitanium", 'I', MetaTileEntities.HULL[GTValues.IV].getStackForm(), 'T', "plateInconel690");
		if(GTppConfig.multiblocks.Reactor)ModHandler.addShapedRecipe("multiblock_industrial_chemicalreactor", GTppTileEntities.INDUSTRIAL_CHEMICALREACTOR.getStackForm(), "TWT", "MCM", "TIT", 'M', MetaTileEntities.CHEMICAL_REACTOR[GTValues.HV].getStackForm(), 'W', "circuitElite", 'C', "rotorTitanium", 'I', MetaTileEntities.HULL[GTValues.EV].getStackForm(), 'T', "platePolytetrafluoroethylene");
		if(GTppConfig.multiblocks.Fisher)ModHandler.addShapedRecipe("multiblock_industrial_fisher", GTppTileEntities.INDUSTRIAL_FISHER.getStackForm(), "TWT", "MCM", "TWT", 'M', "wireFineElectrum", 'W', "circuitAdvanced", 'C', "rotorTitanium", 'T', "plateInconel792");
		if(GTppConfig.multiblocks.Wiremill)ModHandler.addShapedRecipe("multiblock_industrial_wiremill", GTppTileEntities.INDUSTRIAL_WIREMILL.getStackForm(), "TCT", "MSM", "TCT", 'S', MetaTileEntities.WIREMILL[GTValues.HV].getStackForm(), 'M', "circuitMaster", 'C', MetaTileEntities.HULL[GTValues.IV].getStackForm(), 'T', "plateTalonite");
		if(GTppConfig.multiblocks.Sifter)ModHandler.addShapedRecipe("multiblock_industrial_sifter", GTppTileEntities.INDUSTRIAL_SIFTER.getStackForm(), "TCT", "WSW", "TCT", 'W', "cableGtQuadrupleCopper", 'C', "circuitMaster", 'S', MetaTileEntities.SIFTER[GTValues.HV].getStackForm(), 'T', "plateEglinSteel");
		

		//Disassembler recipes
		ModHandler.addShapedRecipe("gtpp_disassembler.lv", GTppTileEntities.DISASSEMBLER[0].getStackForm(), "RCR", "RHR", "GCG", 'H', MetaTileEntities.HULL[GTValues.LV].getStackForm(), 'C', "circuitBasic", 'R', MetaItems.ROBOT_ARM_LV.getStackForm(), 'G', "cableGtSingleTin");
		ModHandler.addShapedRecipe("gtpp_disassembler.mv", GTppTileEntities.DISASSEMBLER[1].getStackForm(), "RCR", "RHR", "GCG", 'H', MetaTileEntities.HULL[GTValues.MV].getStackForm(), 'C', "circuitGood", 'R', MetaItems.ROBOT_ARM_MV.getStackForm(), 'G', "cableGtSingleCopper");
		ModHandler.addShapedRecipe("gtpp_disassembler.hv", GTppTileEntities.DISASSEMBLER[2].getStackForm(), "RCR", "RHR", "GCG", 'H', MetaTileEntities.HULL[GTValues.HV].getStackForm(), 'C', "circuitAdvanced", 'R', MetaItems.ROBOT_ARM_HV.getStackForm(), 'G', "cableGtSingleGold");
		ModHandler.addShapedRecipe("gtpp_disassembler.ev", GTppTileEntities.DISASSEMBLER[3].getStackForm(), "RCR", "RHR", "GCG", 'H', MetaTileEntities.HULL[GTValues.EV].getStackForm(), 'C', "circuitElite", 'R', MetaItems.ROBOT_ARM_EV.getStackForm(), 'G', "cableGtSingleAluminium");
		ModHandler.addShapedRecipe("gtpp_disassembler.iv", GTppTileEntities.DISASSEMBLER[4].getStackForm(), "RCR", "RHR", "GCG", 'H', MetaTileEntities.HULL[GTValues.IV].getStackForm(), 'C', "circuitMaster", 'R', MetaItems.ROBOT_ARM_IV.getStackForm(), 'G', "cableGtSinglePlatinum");
    	
    	
    	
    	GTppRecipeMaps.INDUSTRIALFISHER.recipeBuilder()
											        .circuitMeta(1)
											        .outputs(new ItemStack(Items.FISH, 14, 0), new ItemStack(Items.FISH, 10, 1), new ItemStack(Items.FISH, 7, 2),  new ItemStack(Items.FISH, 4, 3))
											        .EUt(10)
											        .duration(6000)
											        .cannotBeBuffered()
											        .buildAndRegister();
    	
    	
    	//Machine Recipes
    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration(628).EUt(120).input(OrePrefix.dust,GTppMaterials.Trinium).outputs(OreDictUnifier.get(OrePrefix.dust,GTppMaterials.Refined_Trinium)).buildAndRegister();
    	if(GTppConfig.misc.Superconductors) {
    		for (OrePrefix Prefix : Arrays.asList(OrePrefix.dust, OrePrefix.dustSmall, OrePrefix.dustTiny)) {
    			
    			RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (500L * Prefix.materialAmount / 3628800L)).EUt(30).input(Prefix, Materials.Lead, 2).input(Prefix, Materials.Bronze, 2).input(Prefix, Materials.Tin, 1).outputs(OreDictUnifier.getDust(GTppMaterials.Potin, 5L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (600L * Prefix.materialAmount / 3628800L)).EUt(30).input(Prefix, Materials.Nickel, 2).input(Prefix, Materials.Niobium, 1).input(Prefix, Materials.Aluminium, 2).input(Prefix, Materials.Nichrome, 1).outputs(OreDictUnifier.getDust(GTppMaterials.Inconel_792, 6L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (800L * Prefix.materialAmount / 3628800L)).EUt(30).input(Prefix, Materials.Chrome, 1).input(Prefix, Materials.Niobium, 2).input(Prefix, Materials.Molybdenum, 2).input(Prefix, Materials.Nichrome, 3).outputs(OreDictUnifier.getDust(GTppMaterials.Inconel_690, 8L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (1000L * Prefix.materialAmount / 3628800L)).EUt(120).input(Prefix, Materials.Uranium, 9).input(Prefix, Materials.Titanium, 1).outputs(OreDictUnifier.getDust(GTppMaterials.Staballoy, 10L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (1100L * Prefix.materialAmount / 3628800L)).EUt(120).input(Prefix, GTppMaterials.Tantalloy_60, 1).input(Prefix, Materials.Titanium, 6).input(Prefix, Materials.Yttrium, 4).outputs(OreDictUnifier.getDust(GTppMaterials.Tantalloy_61, 11L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (2500L * Prefix.materialAmount / 3628800L)).EUt(120).input(Prefix, Materials.Tungsten, 2).input(Prefix, Materials.Tantalum, 23).outputs(OreDictUnifier.getDust(GTppMaterials.Tantalloy_60, 25L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (1800L * Prefix.materialAmount / 3628800L)).EUt(30).input(Prefix, Materials.Cobalt, 7).input(Prefix, Materials.Chrome, 7).input(Prefix, Materials.Manganese, 2).input(Prefix, Materials.Titanium, 2).outputs(OreDictUnifier.getDust(GTppMaterials.Stellite, 18L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (1100L * Prefix.materialAmount / 3628800L)).EUt(30).input(Prefix, Materials.Cobalt, 4).input(Prefix, Materials.Chrome, 4).input(Prefix, Materials.Phosphor, 2).input(Prefix, Materials.Molybdenum, 1).outputs(OreDictUnifier.getDust(GTppMaterials.Talonite, 11L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (1000L * Prefix.materialAmount / 3628800L)).EUt(30).input(Prefix, Materials.Iron, 4).input(Prefix, Materials.Kanthal, 1).input(Prefix, Materials.Invar, 5).outputs(OreDictUnifier.getDust(GTppMaterials.Egline_Steel_Base, 10L * Prefix.materialAmount)).buildAndRegister();
    	    	RecipeMaps.MIXER_RECIPES.recipeBuilder().duration((int) (1600L * Prefix.materialAmount / 3628800L)).EUt(30).input(Prefix, GTppMaterials.Egline_Steel_Base, 10).input(Prefix, Materials.Sulfur, 1).input(Prefix, Materials.Silicon, 4).input(Prefix, Materials.Carbon, 1).outputs(OreDictUnifier.getDust(GTppMaterials.Eglin_Steel, 16L * Prefix.materialAmount)).buildAndRegister();
    	    	
    		}
    	}
    }
}
