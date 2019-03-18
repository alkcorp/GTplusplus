package gtPlusPlus.xmod.gregtech.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class StaticFields59 {

	public static final Field mGtBlockCasings5;
	public static final Field mPreventableComponents;
	public static final Field mDisabledItems;
	public static final Field mMultiblockChemicalRecipes;
	public static final Field mDescriptionArray;
	public static final Field mCasingTexturePages;
	public static final Field mAssLineVisualMapNEI;
	public static final GT_Recipe_Map sAssemblylineVisualRecipes;
	
	public static final Method mCalculatePollutionReduction;
	public static final Method mAddFurnaceRecipe;

	private static final Map<String, Materials> mMaterialCache = new LinkedHashMap<String, Materials>();


	//OrePrefixes

	static {
		Logger.INFO("[SH] Creating Static Helper for various fields which require reflective access.");
		mGtBlockCasings5 = getField(GregTech_API.class, "sBlockCasings5");
		Logger.INFO("[SH] Got Field: sBlockCasings5");
		mPreventableComponents = getField(OrePrefixes.class, "mPreventableComponents");
		Logger.INFO("[SH] Got Field: mPreventableComponents");
		mDisabledItems = getField(OrePrefixes.class, "mDisabledItems");
		Logger.INFO("[SH] Got Field: mDisabledItems");
		mDescriptionArray = getField(GT_MetaTileEntity_TieredMachineBlock.class, "mDescriptionArray");
		Logger.INFO("[SH] Got Field: mDescriptionArray");
		mCasingTexturePages = getField(BlockIcons.class, "casingTexturePages");
		Logger.INFO("[SH] Got Field: casingTexturePages");

		mAssLineVisualMapNEI = getField(GT_Recipe_Map.class, "sAssemblylineVisualRecipes");
		Logger.INFO("[SH] Got Field: mAssLineVisualMapNEI");
		GT_Recipe_Map aTemp;
		if (mAssLineVisualMapNEI != null) {
			try {
				aTemp = (GT_Recipe_Map) mAssLineVisualMapNEI.get(null);
				Logger.INFO("[SH] Got Field: sAssemblylineVisualRecipes");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				aTemp = null;
			}
		} else {
			aTemp = null;
		}

		sAssemblylineVisualRecipes = aTemp;
		mMultiblockChemicalRecipes = getField(GT_Recipe_Map.class, "sMultiblockChemicalRecipes");
		Logger.INFO("[SH] Got Field: sMultiblockChemicalRecipes");

		mCalculatePollutionReduction = getMethod(GT_MetaTileEntity_Hatch_Muffler.class, "calculatePollutionReduction",
				int.class);
		Logger.INFO("[SH] Got Method: calculatePollutionReduction");

		// Yep...
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			mAddFurnaceRecipe = getMethod(GT_ModHandler.class, "addSmeltingAndAlloySmeltingRecipe", ItemStack.class,
					ItemStack.class);
			Logger.INFO("[SH] Got Method: addSmeltingAndAlloySmeltingRecipe");
		} else {
			mAddFurnaceRecipe = getMethod(GT_ModHandler.class, "addSmeltingAndAlloySmeltingRecipe", ItemStack.class,
					ItemStack.class, boolean.class);
			Logger.INFO("[SH] Got Method: addSmeltingAndAlloySmeltingRecipe");
		}

	}

	public static synchronized final Block getBlockCasings5() {
		try {
			return (Block) mGtBlockCasings5.get(GregTech_API.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	public static int calculatePollutionReducation(GT_MetaTileEntity_Hatch_Muffler h, int i) {
		try {
			return (int) mCalculatePollutionReduction.invoke(h, i);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return 0;
		}
	}

	public static Field getField(Class a, String b) {
		return ReflectionUtils.getField(a, b);		
	}

	public static Method getMethod(Class a, String b, Class... params) {
		return ReflectionUtils.getMethod(a, b, params);
	}

	public static synchronized final Collection<Materials> getOrePrefixesBooleanDisabledItems() {
		try {
			return (Collection<Materials>) mDisabledItems.get(OrePrefixes.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new ArrayList<Materials>();
		}
	}
	

	public static synchronized final List<OrePrefixes> geOrePrefixesBooleanPreventableComponents() {
		try {
			return (List<OrePrefixes>) mPreventableComponents.get(OrePrefixes.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new ArrayList<OrePrefixes>();
		}
	}

	public static synchronized final GT_Recipe_Map getLargeChemicalReactorRecipeMap() {
		try {
			return (GT_Recipe_Map) mMultiblockChemicalRecipes.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	public static Materials getMaterial(String aMaterialName) {
		Materials m = mMaterialCache.get(aMaterialName);
		if (m != null) {
			return m;
		}
		else {			
			m = Materials.get(aMaterialName);
			if (m != null) {
				mMaterialCache.put(aMaterialName, m);
				return m;
			}
			return null;
		}
	}
	
	public static String[] getDescriptionArray(GT_MetaTileEntity_TieredMachineBlock aTile) {
		try {
			return (String[]) mDescriptionArray.get(aTile);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new String[] {aTile.mDescription};
		}
	}
	
	public static ITexture getCasingTexturePages(int a, int b) {
		try {
			ITexture[][] g = (ITexture[][]) mCasingTexturePages.get(null);
			if (g != null) {
				return g[a][b];
			}
		}
		catch (Throwable t) {
			
		}
		return null;
	}


}
