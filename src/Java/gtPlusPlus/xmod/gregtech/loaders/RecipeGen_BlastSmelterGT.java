package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.*;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class RecipeGen_BlastSmelterGT  implements Runnable{

	public RecipeGen_BlastSmelterGT(){

	}

	@Override
	public void run() {
		generateRecipes();
	}

	public static void generateRecipes(){

		final Materials[] GregMaterials = Materials.values();

		GT:	for (final Materials M : GregMaterials){
			if (!M.equals(Materials._NULL)){

				//Add a Blast Smelting Recipe, Let's go!
				ItemStack tStack;
				if ((null != (tStack = GT_OreDictUnificator.get(OrePrefixes.ingot, M.mSmeltInto, 1L))) && (!M.contains(SubTag.NO_SMELTING) && (M.contains(SubTag.METAL)))) {

					//Prepare some Variables
					ItemStack[] components;
					MaterialStack[] tMaterial;
					short counter=0;
					int inputStackCount=0;
					int fluidAmount=0;
					boolean doTest = true;

					//This Bad boy here is what dictates unique recipes. Fuck life, right?
					ItemStack circuitGT = ItemUtils.getGregtechCircuit(0);


					//Set a duration
					int duration = 0;
					if (M.mBlastFurnaceTemp > 150){
						duration = (int) ((Math.max(M.getMass() / 50L, 1L) * M.mBlastFurnaceTemp)*0.8);
					}
					else {
						duration = (int) ((Math.max(M.getMass() / 50L, 1L) * 150)*0.8);
					}

					//Sets the materials 'tier'. Will probably replace this logic.
					final int x = M.mMeltingPoint;
					final int vVoltageMultiplier = (x <= 800 ? 8 : (x <= 1600 ? 15 : (x <= 2800 ? 30 : (x <= 3600 ? 60 : (x <= 4200 ? 120 : (x <= 5400 ? 240 : (x <= 7200 ? 480 : 1000)))))));


					//Make a simple one Material Materialstack[] and log it for validity.
					tMaterial = new MaterialStack[]{new MaterialStack(M, 1)};
					circuitGT = ItemUtils.getGregtechCircuit(1);
					final ItemStack[] tItemStackTest = new ItemStack[]{circuitGT, ItemUtils.getGregtechDust(M, 1)};
					inputStackCount = 1;
					fluidAmount = 144*inputStackCount;
					Utils.LOG_WARNING("Adding an Alloy Blast Smelter Recipe for "+M+". Gives "+fluidAmount+"L of molten metal.");
					Utils.LOG_WARNING("tMaterial.length: "+tMaterial.length+".");
					for (int das=0;das<tItemStackTest.length;das++){
						if (tItemStackTest[das] != null) {
							Utils.LOG_WARNING("tMaterial["+das+"]: "+tItemStackTest[das].getDisplayName()+" Meta: "+tItemStackTest[das].getItemDamage()+", Amount: "+tItemStackTest[das].stackSize);
						}
					}

					//Generate Recipes for all singular materials that can be made molten.
					if (M.mBlastFurnaceRequired) {
						doTest = CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration, 8*vVoltageMultiplier);
					}
					else {
						doTest = CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration/2, 4*vVoltageMultiplier);
					}

					for (final MaterialStack xMaterial : M.mMaterialList){
						if ((xMaterial.mMaterial == Materials.Magic) || (xMaterial.mMaterial == Materials.Metal)){
							continue GT;
						}
						if (null == GT_OreDictUnificator.get(OrePrefixes.dust, xMaterial, 1L)){
							continue GT;
						}
					}

					if (doTest){
						//Reset the Variables for compounds if last recipe was a success.
						inputStackCount=0;
						counter=0;


						int mMaterialListSize=0;
						for (final MaterialStack ternkfsdf:M.mMaterialList){
							mMaterialListSize++;
						}
						Utils.LOG_WARNING("Size: "+mMaterialListSize);
						//If this Material has some kind of compound list, proceed
						if (mMaterialListSize > 1){
							final MaterialStack[] tempStack = new MaterialStack[mMaterialListSize];
							circuitGT = ItemUtils.getGregtechCircuit(mMaterialListSize);
							//Just double checking
							if (tempStack.length > 1){

								//Builds me a MaterialStack[] from the MaterialList of M.
								int ooo=0;
								for (final MaterialStack xMaterial : M.mMaterialList){
									Utils.LOG_WARNING("FOUND: "+xMaterial.mMaterial);
									Utils.LOG_WARNING("ADDING: "+xMaterial.mMaterial);
									tempStack[ooo] = M.mMaterialList.get(ooo);
									ooo++;
								}

								//Builds me an ItemStack[] of the materials. - Without a circuit - this gets a good count for the 144L fluid multiplier
								components = new ItemStack[tempStack.length];
								for (final MaterialStack aOutputPart : tempStack){
									if (aOutputPart != null){
										Utils.LOG_WARNING("Finding dust: "+aOutputPart.mMaterial);
										final ItemStack rStack = ItemUtils.getGregtechDust(aOutputPart.mMaterial, (int) aOutputPart.mAmount);
										if (rStack != null){
											Utils.LOG_WARNING("Found dust: "+aOutputPart.mMaterial);
											components[counter] = rStack;
											inputStackCount = inputStackCount+rStack.stackSize;
										}
									}
									counter++;
								}


								if ((mMaterialListSize > 0) && (mMaterialListSize < 9)){
									final ItemStack[] components_NoCircuit = components;
									//Builds me an ItemStack[] of the materials. - With a circuit
									components = new ItemStack[components_NoCircuit.length+1];
									for (int fr=0;fr<components.length;fr++){
										if (fr==0){
											components[0] = circuitGT;
										}
										else {
											components[fr] = components_NoCircuit[fr-1];
										}
									}
								}

								/*//Add a shapeless recipe for each dust this way - Compat mode.
								ItemStack outputStack = tStack;
								outputStack.stackSize = mMaterialListSize;
								RecipeUtils.buildShapelessRecipe(outputStack, components);*/



								//Set Fluid output
								fluidAmount = 144*inputStackCount;


								Utils.LOG_WARNING("Adding an Alloy Blast Smelter Recipe for "+M+" using it's compound dusts. This material has "+ inputStackCount+" parts. Gives "+fluidAmount+"L of molten metal.");
								Utils.LOG_WARNING("tMaterial.length: "+components.length+".");
								for (int das=0;das<components.length;das++){
									if (components[das] != null) {
										Utils.LOG_WARNING("tMaterial["+das+"]: "+components[das].getDisplayName()+" Meta: "+components[das].getItemDamage()+", Amount: "+components[das].stackSize);
									}
								}

								CORE.RA.addBlastSmelterRecipe(
										components,
										M.getMolten(fluidAmount),
										100,
										(int) Math.max(M.getMass() * 2L * 1, 1),
										8 * vVoltageMultiplier); // EU Cost

								/*if (M.mBlastFurnaceRequired) {
									CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 500);
								}
								else {
									CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 240);
								}*/
							}
						}
					}
				}
			}
		}
	}
}
