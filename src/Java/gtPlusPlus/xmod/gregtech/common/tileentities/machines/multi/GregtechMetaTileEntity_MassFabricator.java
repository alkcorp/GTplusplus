package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MatterFab;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_MassFabricator extends GregtechMeta_MultiBlockBase {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3215;
	public static boolean sRequiresUUA = false;
	private int mAmplifierUsed = 0;
	private int mMatterProduced = 0;
	ItemStack scrapPile = ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrap"));
	ItemStack scrapBox = ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrapbox"));

	private static Block IC2Glass = Block.getBlockFromItem(ItemUtils.getItem("IC2:blockAlloyGlass"));
	FluidStack tempFake = FluidUtils.getFluidStack("uuamplifier", 1);
	GT_Recipe fakeRecipe;

	public int getAmplifierUsed(){
		return this.mAmplifierUsed;
	}

	public int getMatterProduced(){
		return this.mMatterProduced;
	}

	public GregtechMetaTileEntity_MassFabricator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_MassFabricator(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Matter Fabricator",
				"Produces UU-Matter from UU-Amplifier",
				"Size(WxHxD): 5x4x5, Controller (Bottom center)",
				"3x1x3 Matter Generation Coils (Inside bottom 5x1x5 layer)",
				"9x Matter Generation Coils (Centered 3x1x3 area in Bottom layer)",
				"1x Input Hatch (Any bottom layer casing)",
				"1x Output Hatch (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)",
				"1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
				"1x Energy Hatch (Any bottom layer casing)",
				"24x IC2 Reinforced Glass for the walls",
				"Matter Fabricator Casings for the edges & top (40 at least!)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MatterFab(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {
		super.onConfigLoad(aConfig);
		sDurationMultiplier = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUM_Duration_Multiplier", sDurationMultiplier);
		sUUAperUUM = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_per_UUM", sUUAperUUM);
		sUUASpeedBonus = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Speed_Bonus", sUUASpeedBonus);
		sRequiresUUA = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Requirement", sRequiresUUA);
		Materials.UUAmplifier.mChemicalFormula = ("Mass Fabricator Eff/Speed Bonus: x" + sUUASpeedBonus);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		Logger.WARNING("Fabricating Matter.");
		if (this.mInputHatches.size() != 1){
			Logger.INFO("Too many input hatches. Found: "+this.mInputHatches.size()+" | Expected: 1");
			return false;
		}
		Logger.WARNING("Step 1");

		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (int i = 0; i < (tInputList.size() - 1); i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
					if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
						tInputList.remove(j--);
					} else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}
		Logger.WARNING("Step 2");

		final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);



		final ArrayList<FluidStack> tFluidList = this.getStoredFluids();
		for (int i = 0; i < (tFluidList.size() - 1); i++) {
			for (int j = i + 1; j < tFluidList.size(); j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
					} else {
						tFluidList.remove(i--);
						break;
					}
				}
			}
		}
		Logger.WARNING("Step 3");

		final long tVoltage = this.getMaxInputVoltage();
		final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		FluidStack[] tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tFluidList.size()]), 0, tFluidList.size());


		//Find Scrap and make UU-A
		/*if (tInputs.length >= 1){
			Utils.LOG_INFO("Found Items in input bus.");
			if (getStoredInputs().contains(this.scrapPile.getItem())){
				Utils.LOG_INFO("Step 3.5");
				for (int scrapSlots=0;scrapSlots<tInputs.length;scrapSlots++){
					if (tInputs[scrapSlots].getItem() == this.scrapPile.getItem()){
						if (tInputs[scrapSlots].stackSize >= 9 ){
							Utils.LOG_INFO("Found enough scrap for a special recipe. x"+tInputs[scrapSlots].stackSize+", recipe requires 9.");
							for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
								if (isValidMetaTileEntity(tHatch)) {
									Utils.LOG_INFO("Input fluid empty - Time to generate 2L of UU-A.");
									if (tHatch.mFluid == null){
										tHatch.mFluid = FluidUtils.getFluidStack("uuamplifier", 2);
									}
									else{
										tHatch.mFluid.amount++;
										tHatch.mFluid.amount++;
									}
									tInputs[scrapSlots].stackSize = tInputs[scrapSlots].stackSize - 9;
									Utils.LOG_INFO("Remaining after recipe. x"+tInputs[scrapSlots].stackSize);
								}
							}
						}
					}
				}
			}
			else {
				Utils.LOG_INFO("Did not find an itemstack containing 9 IC2 Scrap or more.");
			}
		}*/
		Logger.WARNING("Step 4");

		tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tFluidList.size()]), 0, tFluidList.size());

		if (tFluids.length > 0) {
			Logger.WARNING("Input fluid found");
			for(int i = 0;i<tFluids.length;i++){
				final GT_Recipe tRecipe = Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.findRecipe(this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{tFluids[i]}, new ItemStack[]{});
				if (tRecipe != null) {
					if (tRecipe.isRecipeInputEqual(true, tFluids, new ItemStack[]{})) {
						this.mEfficiency = (10000 - ((this.getIdealStatus() - this.getRepairStatus()) * 1000));
						this.mEfficiencyIncrease = 10000;
						if (tRecipe.mEUt <= 16) {
							this.mEUt = (tRecipe.mEUt * (1 << (tTier - 1)) * (1 << (tTier - 1)));
							this.mMaxProgresstime = ((tRecipe.mDuration/**sDurationMultiplier*/) / (1 << (tTier - 1)));
						} else {
							this.mEUt = tRecipe.mEUt;
							this.mMaxProgresstime = (tRecipe.mDuration/**sDurationMultiplier*/);
							while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
								this.mEUt *= 2;
								this.mMaxProgresstime /= 4;
							}
						}
						if (this.mEUt > 0) {
							this.mEUt = (-this.mEUt);
						}
						this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
						this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
						this.mOutputFluids = tRecipe.mFluidOutputs.clone();
						ArrayUtils.reverse(this.mOutputFluids);
						this.mMatterProduced++;
						this.mAmplifierUsed++;
						this.updateSlots();
						//Utils.LOG_INFO("Recipes Finished: "+mMatterProduced);
						return true;
					}
				}
				else {
					Logger.INFO("Invalid Recipe");
					return false;
				}
			}
		}
		else if (tFluids.length == 0) {
			Logger.WARNING("Input fluid not found");
			this.fakeRecipe = Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.findRecipe(this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], new FluidStack[]{this.tempFake}, new ItemStack[]{});

			this.mEfficiency = (10000 - ((this.getIdealStatus() - this.getRepairStatus()) * 1000));
			this.mEfficiencyIncrease = 10000;

			this.mEUt = 32;
			this.mMaxProgresstime = (160*20);
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= 2;
			}

			if (this.mEUt > 0) {
				this.mEUt = (-this.mEUt);
			}

			if (this.fakeRecipe != null) {
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				this.mOutputItems = new ItemStack[]{this.fakeRecipe.getOutput(0)};
				this.mOutputFluids = this.fakeRecipe.mFluidOutputs.clone();
				ArrayUtils.reverse(this.mOutputFluids);
				this.mMatterProduced++;
				this.updateSlots();
				//Utils.LOG_INFO("Recipes Finished: "+mMatterProduced);
				return true;
			}
		}
		else {
			Logger.INFO("Invalid no input Recipe");
		}
		Logger.INFO("Fabricating Matter.bad");
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 4; h++) {

					//Utils.LOG_INFO("Logging Variables - xDir:"+xDir+" zDir:"+zDir+" h:"+h+" i:"+i+" j:"+j);

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
					/*if (tTileEntity != Block.getBlockFromItem(UtilsItems.getItem("IC2:blockAlloyGlass"))) {
						Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
						double tX = tTileEntity.getXCoord();
						double tY = tTileEntity.getYCoord();
						double tZ = tTileEntity.getZCoord();
						Utils.LOG_INFO("Found Glass at X:"+tX+" Y:"+tY+" Z:"+tZ);
						//return false;
					}*/
					if (((i != -2) && (i != 2)) && ((j != -2) && (j != 2))) {// innerer 3x3 ohne h�he
						if (h == 0) {// innen boden (kantal coils)
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Logger.INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
								Logger.INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
						} else if (h == 3) {// innen decke (ulv casings + input + muffler)
							if ((!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(9)))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
						} else {// innen air
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Logger.INFO("Make sure the inner 3x3 of the Multiblock is Air.");
								return false;
							}
						}
					} else {// Outer 5x5
						if (h == 0) {// au�en boden (controller, output, energy, maintainance, rest ulv casings)
							if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(9)))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
								}
							}
						} else {// au�en �ber boden (ulv casings)
							if (h == 1) {

								if (((i == -2) || (i == 2)) && ((j == -2) || (j == 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
								}

								else if (((i != -2) || (i != 2)) && ((j != -2) || (j != 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Logger.INFO("Glass Casings Missing from somewhere in the second layer.");
										return false;
									}
								}
							}
							if (h == 2) {
								if (((i == -2) || (i == 2)) && ((j == -2) || (j == 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
								}

								else if (((i != -2) || (i != 2)) && ((j != -2) || (j != 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Logger.INFO("Glass Casings Missing from somewhere in the third layer.");
										return false;
									}
								}
							}
							if (h == 3) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
							}
						}
					}
				}
			}
		}
		Logger.INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 10;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_MassFabricator(this.mName);
	}

}