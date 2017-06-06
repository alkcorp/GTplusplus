package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.RECIPE_CONSTANTS;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.*;
import net.minecraft.item.ItemStack;

public class GregtechAdvancedBoilers {

	public static void run() {
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Advanced Boilers.");
			run1();
		}
	}

	private static void run1(){
		//Boilers
		GregtechItemList.Boiler_Advanced_LV.set(new GT_MetaTileEntity_Boiler_LV(756, "Advanced Boiler [LV]", 1).getStackForm(1L));
		GregtechItemList.Boiler_Advanced_MV.set(new GT_MetaTileEntity_Boiler_MV(757, "Advanced Boiler [MV]", 2).getStackForm(1L));
		GregtechItemList.Boiler_Advanced_HV.set(new GT_MetaTileEntity_Boiler_HV(758, "Advanced Boiler [HV]", 3).getStackForm(1L));
		
		
		ItemStack chassisT1 = ItemUtils.getItemStack("miscutils:itemBoilerChassis", 1);
		ItemStack chassisT2 = ItemUtils.getItemStack("miscutils:itemBoilerChassis:1", 1);
		ItemStack chassisT3 = ItemUtils.getItemStack("miscutils:itemBoilerChassis:2", 1);


		//Make the Coil in each following recipe a hammer and a Screwdriver.

		GT_ModHandler.addCraftingRecipe(
		GregtechItemList.Boiler_Advanced_LV.get(1L, new Object[0]),
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"ECE", "WMW", "GPG",
			Character.valueOf('M'), ItemList.Hull_LV,
			Character.valueOf('P'), ItemList.Robot_Arm_EV, //TODO
			Character.valueOf('E'), chassisT1, //TODO
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic),
			Character.valueOf('W'), chassisT1,
			Character.valueOf('G'), OrePrefixes.gear.get(Materials.Steel)});

		GT_ModHandler.addCraftingRecipe(
		GregtechItemList.Boiler_Advanced_MV.get(1L, new Object[0]),
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"ECE", "WMW", "GPG",
			Character.valueOf('M'), ItemList.Hull_MV,
			Character.valueOf('P'), ItemList.Robot_Arm_IV, //TODO
			Character.valueOf('E'), chassisT2, //TODO
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good),
			Character.valueOf('W'), chassisT2,
			Character.valueOf('G'), ALLOY.SILICON_CARBIDE.getGear(1)});

		GT_ModHandler.addCraftingRecipe(
		GregtechItemList.Boiler_Advanced_HV.get(1L, new Object[0]),
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"ECE", "WMW", "GPG",
			Character.valueOf('M'), ItemList.Hull_HV,
			Character.valueOf('P'), RECIPE_CONSTANTS.robotArm_LuV, //TODO
			Character.valueOf('E'), chassisT3, //TODO
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced),
			Character.valueOf('W'), chassisT3,
			Character.valueOf('G'), ALLOY.SILICON_CARBIDE.getGear(1)});


		GT_ModHandler.addCraftingRecipe(
		chassisT1,
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"WCW", "GMG", "WPW",
			Character.valueOf('M'), ItemList.Hull_ULV,
			Character.valueOf('P'), OrePrefixes.pipeLarge.get(Materials.Bronze),
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Primitive),
			Character.valueOf('W'), OrePrefixes.plate.get(Materials.Lead),
			Character.valueOf('G'), OrePrefixes.pipeSmall.get(Materials.Copper)});

		GT_ModHandler.addCraftingRecipe(
		chassisT2,
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"WCW", "GMG", "WPW",
			Character.valueOf('M'), ItemList.Hull_LV,
			Character.valueOf('P'), OrePrefixes.pipeLarge.get(Materials.Steel),
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic),
			Character.valueOf('W'), OrePrefixes.plate.get(Materials.Steel),
			Character.valueOf('G'), OrePrefixes.pipeSmall.get(Materials.Bronze)});

		GT_ModHandler.addCraftingRecipe(
		chassisT3,
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"WCW", "GMG", "WPW",
			Character.valueOf('M'), ItemList.Hull_MV,
			Character.valueOf('P'), OrePrefixes.pipeLarge.get(Materials.StainlessSteel),
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good),
			Character.valueOf('W'), OrePrefixes.plate.get(Materials.Aluminium),
			Character.valueOf('G'), OrePrefixes.pipeSmall.get(Materials.Steel)});

		
		
	}

	
}