package gtPlusPlus.xmod.gregtech.registration.gregtech;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import net.minecraft.item.ItemStack;

import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraftforge.fluids.FluidStack;

public class GregtechNitroDieselFix {

	@SuppressWarnings("unchecked")
	public static void run(){
		if (CORE.ConfigSwitches.enableNitroFix){
			Logger.INFO("Gregtech5u Content | Attempting to Fix Nitro-Diesel production.");
			try {
				int mSub = Utils.getGregtechSubVersion();
				if (mSub != 0){
					if (mSub >= 30){							
						Class mb = ReflectionUtils.getClass("gregtech.api.enums.MaterialBuilder");
						Object df = mb.getConstructor(int.class, TextureSet.class, String.class).newInstance(975, TextureSet.SET_FLUID, "Nitro-Diesel [Old]");
						if (mb.isInstance(df)){

							Logger.INFO("[Nitro] Created new instance of Material builder, for Nitro fix.");

							//Get Methods
							Method addFluid = mb.getDeclaredMethod("addFluid");
							Method addCell = mb.getDeclaredMethod("addCell");									
							Method setColour = mb.getDeclaredMethod("setColor", Dyes.class);
							Method setFuelPower = mb.getDeclaredMethod("setFuelPower", int.class);
							Method setMaterials = mb.getDeclaredMethod("setMaterialList", List.class);
							Method setTemp = mb.getDeclaredMethod("setLiquidTemperature", int.class);
							Method setRGB = mb.getDeclaredMethod("setRGB", int.class, int.class, int.class);							
							Method construct = mb.getDeclaredMethod("constructMaterial");
							Logger.INFO("[Nitro] Got internal methods for setting fields.");

							//Invoke the methods
							addFluid.invoke(df);
							addCell.invoke(df);									
							setColour.invoke(df, Dyes.dyeLime);
							setFuelPower.invoke(df, 512000);
							setMaterials.invoke(df, Arrays.asList(new MaterialStack(Materials.Glyceryl, 1), new MaterialStack(Materials.Fuel, 4)));
							setTemp.invoke(df, 295);
							setRGB.invoke(df, 200, 255, 0);									
							Materials mNitroFix = (Materials) construct.invoke(df);
							Logger.INFO("[Nitro] Invoked 8 method calls successfully.");	

							GT_Mod.gregtechproxy.addFluid("NitroFuel_Old", "Nitro Diesel [Old]", mNitroFix, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, mNitroFix, 1L), ItemUtils.getEmptyCell(), 1000);
							Logger.INFO("[Nitro] Added a fluid.");


							/**
							 * Reflectively get all the values to maintain 5.08.xx compat
							 */
							

							GT_Recipe_Map recipeMapMultiblockChemReactor;
							recipeMapMultiblockChemReactor = (GT_Recipe_Map) FieldUtils.getDeclaredField(GT_Recipe_Map.class, "sMultiblockChemicalRecipes", true).get(null);
							
							
							
							/**
							 * Do Recipe Stuff
							 */

							//Set up some Variables
							final Collection<GT_Recipe> xSingle = GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList;
							final Collection<GT_Recipe> xMulti = recipeMapMultiblockChemReactor.mRecipeList;
							final FluidStack mNitroStack = Materials.NitroFuel.getFluid(1000);
							GT_Recipe toRemoveSingle[] = new GT_Recipe[99];
							GT_Recipe toRemoveMulti[] = new GT_Recipe[99];									
							int mIndexSingle = 0;
							int mIndexMulti = 0;

							//Iterate Single Block recipes for Nitro production.
							for (GT_Recipe rnd : xSingle){
								for (FluidStack mOutputFluid : rnd.mFluidOutputs){
									if (mOutputFluid.isFluidEqual(mNitroStack)){
										toRemoveSingle[mIndexSingle++] = rnd;
									}
								}										
							}									
							Logger.INFO("[Nitro] Found "+mIndexSingle+" single block Chemical Reactor recipes to remove.");

							//Iterate Multi Block recipes for Nitro production.
							for (GT_Recipe rnd : xMulti){
								for (FluidStack mOutputFluid : rnd.mFluidOutputs){
									if (mOutputFluid.isFluidEqual(mNitroStack)){
										toRemoveMulti[mIndexMulti++] = rnd;
									}
								}										
							}
							Logger.INFO("[Nitro] Found "+mIndexMulti+" multi block Chemical Reactor recipes to remove.");

							//Remove Single Block recipes found.
							int mRemovedSingle = 0;
							int mRemovedMulti = 0;
							for (GT_Recipe single : toRemoveSingle){
								if (GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList.remove(single)){
									mRemovedSingle++;
								}
							}									
							Logger.INFO("[Nitro] Removed "+mRemovedSingle+" single block Chemical Reactor recipes.");
						
							//Remove Multi Block recipes found.
							for (GT_Recipe multi : toRemoveMulti){
								if (recipeMapMultiblockChemReactor.mRecipeList.remove(multi)){
									mRemovedMulti++;
								}
							}									
							Logger.INFO("[Nitro] Removed "+mRemovedMulti+" multi block Chemical Reactor recipes.");
							
							Materials mGlycerol = Materials.valueOf("Glycerol");
							Materials mLightFuel = Materials.valueOf("LightFuel");

							//Build a New Recipe set
							Materials mFuels[] = {mLightFuel, Materials.Fuel};									
							for (Materials fuel : mFuels){
								boolean didAdd[] = new boolean[3];
								Logger.INFO("[Nitro] Getting ready to add back in the old nitro-diesel recipe to the mixer, using "+fuel.mDefaultLocalName+" as the fuel input.");
								didAdd[0] = GT_Values.RA.addMixerRecipe(getCells(fuel, 4), getCells(mGlycerol, 1), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, getCells(mNitroFix, 5), 20, 30);
								didAdd[1] = GT_Values.RA.addMixerRecipe(getCells(fuel, 4), GT_Values.NI, GT_Values.NI, GT_Values.NI, mGlycerol.getFluid(1000L),mNitroFix.getFluid(5000L), ItemList.Cell_Empty.get(4L), 20, 30);
								didAdd[2] = GT_Values.RA.addMixerRecipe(getCells(mGlycerol, 1), GT_Values.NI,GT_Values.NI,GT_Values.NI, fuel.getFluid(4000L),mNitroFix.getFluid(5000L), ItemList.Cell_Empty.get(1L), 20, 30);
								Logger.INFO("[Nitro] Did the recipes add? 1: "+didAdd[0]+" |  2: "+didAdd[1]+" |  3: "+didAdd[2]);
							}		

							for (Materials fuel : mFuels){
								boolean didAdd[] = new boolean[3];
								Logger.INFO("[Nitro] Getting ready to add back in the old nitro-diesel recipe to the chemical reactors, using "+fuel.mDefaultLocalName+" as the fuel input.");										
								didAdd[0] = GT_Values.RA.addChemicalRecipe(getCells(fuel, 4), getCells(mGlycerol, 1), GT_Values.NF, GT_Values.NF, getCells(mNitroFix, 5), 20);
								didAdd[1] = GT_Values.RA.addChemicalRecipe(getCells(fuel, 4), GT_Values.NI, mGlycerol.getFluid(1000L),mNitroFix.getFluid(5000L), ItemList.Cell_Empty.get(4L), 20);
								didAdd[2] = GT_Values.RA.addChemicalRecipe(getCells(mGlycerol, 1), GT_Values.NI, fuel.getFluid(4000L),mNitroFix.getFluid(5000L), ItemList.Cell_Empty.get(1L), 20);
								Logger.INFO("[Nitro] Did the recipes add? 1: "+didAdd[0]+" |  2: "+didAdd[1]+" |  3: "+didAdd[2]);
							}	

							Logger.INFO("[Nitro] Getting ready to add back in the old glycerol recipe!");
							GT_Values.RA.addChemicalRecipe(getCells(Materials.Nitrogen, 1), getDust(Materials.Carbon, 1), Materials.Water.getFluid(2000L), mGlycerol.getFluid(3000L), ItemList.Cell_Empty.get(1), 3000);
							Logger.INFO("[Nitro] Added recipes.");

						}
					}
				}
			}
			catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				Logger.INFO("[Nitro] ================ Error ================");
				e.printStackTrace();
				Logger.INFO("[Nitro] ================ Error ================");
			}			
		}
	}


	public static ItemStack getCells(Materials mat, int amount){
		String mName = MaterialUtils.getMaterialName(mat);		
		return ItemUtils.getItemStackOfAmountFromOreDict("cell"+mName, amount);
	}

	public static ItemStack getDust(Materials mat, int amount){
		String mName = MaterialUtils.getMaterialName(mat);
		return ItemUtils.getItemStackOfAmountFromOreDict("dust"+mName, amount);
	}


}
