package GTplusplus.recipes;

import javax.annotation.Nullable;

import gregtech.api.GTValues;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.blocks.BlockMachineCasing.MachineCasingType;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;


import java.util.List;

public class RecipeMapDisassembler extends RecipeMap<SimpleRecipeBuilder> {

	private int tier;
	
    public RecipeMapDisassembler(String unlocalizedName, int tier) {
        super(unlocalizedName, 1, 1, 9, 9, 0, 0, 0, 0, 1, (new SimpleRecipeBuilder()).notOptimized());
        this.tier = tier;
    }
    
    
    private ItemStack[] listAllowed = null;
    
    private ItemStack[] getAllowedUncraftIngredients()
    {
    	if(listAllowed == null) {
    		listAllowed = new ItemStack[]{MetaItems.BATTERY_RE_HV_CADMIUM.getStackForm(),
					  MetaItems.BATTERY_RE_HV_LITHIUM.getStackForm(),
					  MetaItems.BATTERY_RE_HV_SODIUM.getStackForm(),
					  MetaItems.BATTERY_RE_MV_CADMIUM.getStackForm(),
					  MetaItems.BATTERY_RE_MV_LITHIUM.getStackForm(),
					  MetaItems.BATTERY_RE_MV_SODIUM.getStackForm(),
					  MetaItems.BATTERY_RE_LV_CADMIUM.getStackForm(),
					  MetaItems.BATTERY_RE_LV_CADMIUM.getStackForm(),
					  MetaItems.BATTERY_RE_LV_SODIUM.getStackForm(),
					  MetaTileEntities.HULL[GTValues.MAX].getStackForm(),
					  MetaTileEntities.HULL[GTValues.UV].getStackForm(),
					  MetaTileEntities.HULL[GTValues.ZPM].getStackForm(),
					  MetaTileEntities.HULL[GTValues.LuV].getStackForm(),
					  MetaTileEntities.HULL[GTValues.IV].getStackForm(),
					  MetaTileEntities.HULL[GTValues.EV].getStackForm(),
					  MetaTileEntities.HULL[GTValues.HV].getStackForm(),
					  MetaTileEntities.HULL[GTValues.MV].getStackForm(),
					  MetaTileEntities.HULL[GTValues.LV].getStackForm(),
					  MetaTileEntities.HULL[GTValues.ULV].getStackForm(),
					  MetaBlocks.MACHINE_CASING.getItemVariant(MachineCasingType.BRONZE_HULL),
					  MetaBlocks.MACHINE_CASING.getItemVariant(MachineCasingType.STEEL_HULL),
					  OreDictUnifier.get(OrePrefix.stick, Materials.IronMagnetic),
					  OreDictUnifier.get(OrePrefix.stick, Materials.SteelMagnetic),
					  OreDictUnifier.get(OrePrefix.stick, Materials.NeodymiumMagnetic),
					  OreDictUnifier.get(OrePrefix.cableGtSingle, Materials.Tin),
					  OreDictUnifier.get(OrePrefix.cableGtSingle, Materials.Copper),
					  OreDictUnifier.get(OrePrefix.cableGtSingle, Materials.Gold),
					  OreDictUnifier.get(OrePrefix.cableGtSingle, Materials.Silver),
					  OreDictUnifier.get(OrePrefix.cableGtSingle, Materials.Aluminium),
					  OreDictUnifier.get(OrePrefix.cableGtSingle, Materials.Platinum),
					  OreDictUnifier.get(OrePrefix.cableGtSingle, Materials.Tungsten)
					  };
    	}
    	return listAllowed;
    }
    
    /*
     * Only allows certain items to be uncrafted.
     */
    
    private boolean getRuleUncraftable(ItemStack output)
    {
    	if(output.isEmpty())return false;
    	output = output.copy();
    	output.setCount(1);
    	for(ItemStack tested : this.getAllowedUncraftIngredients()) {
			if(ItemStack.areItemStacksEqual(output, tested))return true; //If just one is found, then the recipe is "intended" for now
		}
    	return false;
    }
    
    /*
     * Thanks Twilight Forest :D
     */
    private IRecipe getRecipeFor(ItemStack inputStack) {
		if (!inputStack.isEmpty()) {
			for (IRecipe recipe : CraftingManager.REGISTRY) {
				if (
						recipe.canFit(3, 3)
								&& !recipe.getIngredients().isEmpty()
								&& recipe.getRecipeOutput().getItem() == inputStack.getItem() && inputStack.getCount() >= recipe.getRecipeOutput().getCount()
								&& (!recipe.getRecipeOutput().getHasSubtypes() || recipe.getRecipeOutput().getItemDamage() == inputStack.getItemDamage())) {
					return recipe;
				}
			}
		}

		return null;
	}
    
    private boolean isIngredientProblematic(ItemStack ingredient) {
		return !ingredient.isEmpty() && ingredient.getItem().hasContainerItem(ingredient);
	}
    
    private ItemStack[] getIngredients(IRecipe recipe) {
		boolean isUncraftable = false;
		ItemStack[] stacks = new ItemStack[recipe.getIngredients().size()];

		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			ItemStack[] matchingStacks = recipe.getIngredients().get(i).getMatchingStacks();
			stacks[i] = matchingStacks.length > 0 ? matchingStacks[0].copy() : ItemStack.EMPTY;
			if(isIngredientProblematic(stacks[i]))stacks[i] = ItemStack.EMPTY;
			if(!isUncraftable)isUncraftable = getRuleUncraftable(stacks[i]);
		}
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			int total = 1;
			for (int j = i+1; j < recipe.getIngredients().size(); j++) {
				if(stacks[j] != ItemStack.EMPTY && ItemStack.areItemStacksEqual(stacks[j], stacks[i])) {
					total++;
					stacks[j] = ItemStack.EMPTY;
				}
			}
			stacks[i].setCount(total);
		}
		return isUncraftable ? stacks : null;
	}
    
    
    

    @Override
    @Nullable
    public Recipe findRecipe(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        Recipe normalRecipe = super.findRecipe(voltage, inputs, fluidInputs);
        if (normalRecipe != null || inputs.size() == 0 || inputs.get(0).isEmpty())
            return normalRecipe; 
        IRecipe recipeFor = this.getRecipeFor(inputs.get(0));
        if(recipeFor != null) {
        	ItemStack[] recipeOutputs = getIngredients(recipeFor);
        	if(recipeOutputs == null)return null;
        	int ticks = 80;
        	SimpleRecipeBuilder rb = this.recipeBuilder();
        	for(ItemStack output : recipeOutputs) {
        		if(output != ItemStack.EMPTY) {
        			rb = rb.chancedOutput(output, 2500 + (this.tier * 1500));
        			ticks *= 1.5;
        		}
        	}
        	return  rb
                    .inputs(recipeFor.getRecipeOutput())
                    .EUt(4 * (int)Math.pow(4, this.tier))
                    .duration(ticks)
                    .cannotBeBuffered()
                    .build().getResult();
        }
        return null;
    }
}
