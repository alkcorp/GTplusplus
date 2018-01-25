package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

public class GregtechMetaTileEntity_IndustrialWashPlant
extends GregtechMeta_MultiBlockBase {

	public GT_Recipe mLastRecipe;

	public GregtechMetaTileEntity_IndustrialWashPlant(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialWashPlant(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialWashPlant(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Ore Washing Plant",
				"80% faster than using single block machines of the same voltage",
				"Size: 7x3x5 [WxHxL] (open)",
				"X     X",
				"X     X",
				"XXXXX",
				"Controller (front centered)",
				"1x Input Bus (Any casing)",
				"1x Output Bus (Any casing)",
				"1x Maintenance Hatch (Any casing)",
				"1x Energy Hatch (Any casing)",
				CORE.GT_Tooltip

		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "WireFactory.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sOreWasherRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		// Based on the Processing Array. A bit overkill.
		ArrayList<ItemStack> tItemInputs = getStoredInputs();
		ArrayList<FluidStack> tFluidInputs = getStoredFluids();
		final int maxParallelRecipes = 2;
		int parallelRecipes = 0;

		for (ItemStack tInput : tItemInputs) {
			long tVoltage = getMaxInputVoltage();
			byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.findRecipe(getBaseMetaTileEntity(), this.mLastRecipe, false, gregtech.api.enums.GT_Values.V[tTier], tFluidInputs.isEmpty() ? null : new FluidStack[]{tFluidInputs.get(0)}, new ItemStack[]{tInput});

			if (tRecipe == null) {
				continue;
			}

			FluidStack[] mFluidInputList = new FluidStack[tFluidInputs.size()];
			int tri = 0;
			for (FluidStack f : tFluidInputs) {
				mFluidInputList[tri] = f;
				tri++;
			}

			// Count recipes to do in parallel
			this.mEUt = 0;
			for (; parallelRecipes < maxParallelRecipes; parallelRecipes++) {
				if (!tRecipe.isRecipeInputEqual(true, mFluidInputList, tInput)) {
					break;
				}
				this.mEUt += tRecipe.mEUt;
			}

			if (parallelRecipes == 0) {
				continue;
			}

			this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
			this.mEfficiencyIncrease = 10000;

			// Washing Plant discounts
			this.mEUt = MathUtils.findPercentageOfInt(this.mEUt, 80);
			this.mMaxProgresstime = MathUtils.findPercentageOfInt(tRecipe.mDuration, 20);

			// Overclock
			if (this.mEUt <= 16) {
				this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
				this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
			} else {
				while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
					this.mEUt *= 4;
					this.mMaxProgresstime /= 2;
				}
			}

			if (this.mEUt > 0) {
				this.mEUt = (-this.mEUt);
			}

			this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

			// Collect output item types
			ItemStack[] tOutputItems = new ItemStack[tRecipe.mOutputs.length];
			for (int h = 0; h < tRecipe.mOutputs.length; h++) {
				if (tRecipe.getOutput(h) != null) {
					tOutputItems[h] = tRecipe.getOutput(h).copy();
					tOutputItems[h].stackSize = 0;
				}
			}

			// Set output item stack sizes (taking output chance into account)
			for (int f = 0; f < tOutputItems.length; f++) {
				if (tRecipe.mOutputs[f] != null && tOutputItems[f] != null) {
					for (int g = 0; g < parallelRecipes; g++) {
						if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f))
							tOutputItems[f].stackSize += tRecipe.mOutputs[f].stackSize;
					}
				}
			}


			// Collect fluid output (not applicable for Washing Plant)
			FluidStack tOutputFluid = null;
			if (tRecipe.getFluidOutput(0) != null) tOutputFluid = tRecipe.getFluidOutput(0).copy();

			if (tOutputFluid != null) {
				int tSize = tOutputFluid.amount;
				tOutputFluid.amount = tSize * parallelRecipes;
			}


			tOutputItems = removeNulls(tOutputItems);

			// Sanitize item stack size, splitting any stacks greater than max stack size
			List<ItemStack> splitStacks = new ArrayList<ItemStack>();
			for (ItemStack tItem : tOutputItems) {
				while (tItem.getMaxStackSize() < tItem.stackSize) {
					ItemStack tmp = tItem.copy();
					tmp.stackSize = tmp.getMaxStackSize();
					tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
					splitStacks.add(tmp);
				}
			}

			if (splitStacks.size() > 0) {
				ItemStack[] tmp = new ItemStack[splitStacks.size()];
				tmp = splitStacks.toArray(tmp);
				tOutputItems = ArrayUtils.addAll(tOutputItems, tmp);
			}

			// Strip empty stacks
			List<ItemStack> tSList = new ArrayList<ItemStack>();
			for (ItemStack tS : tOutputItems) {
				if (tS.stackSize > 0) tSList.add(tS);
			}
			tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

			// Commit outputs
			this.mOutputItems = tOutputItems;
			this.mOutputFluids = new FluidStack[]{tOutputFluid};
			updateSlots();
			return true;
		}
		return false;
	}

	private static ItemStack[] removeNulls(final ItemStack[] v) {
		List<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(v));
		list.removeAll(Collections.singleton((ItemStack)null));
		return list.toArray(new ItemStack[list.size()]);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {

		//Get Facing direction
		int mDirectionX  = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;

		int mCurrentDirectionX;
		int mCurrentDirectionZ;
		int mOffsetX_Lower = 0;
		int mOffsetX_Upper = 0;
		int mOffsetZ_Lower = 0;
		int mOffsetZ_Upper = 0;


		Logger.WARNING("mDirectionX "+(mDirectionX));
		if (mDirectionX == 0){
			mCurrentDirectionX = 2;
			mCurrentDirectionZ = 3;
			mOffsetX_Lower = -2;
			mOffsetX_Upper = 2;
			mOffsetZ_Lower = -3;
			mOffsetZ_Upper = 3;
			Logger.WARNING("Controler is facing Z direction.");
		}
		else {
			mCurrentDirectionX = 3;
			mCurrentDirectionZ = 2;	
			mOffsetX_Lower = -3;
			mOffsetX_Upper = 3;
			mOffsetZ_Lower = -2;
			mOffsetZ_Upper = 2;	
			Logger.WARNING("Controler is facing X direction.");	
		}

		//if (aBaseMetaTileEntity.fac)

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * mCurrentDirectionX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * mCurrentDirectionZ;

		Logger.WARNING("xDir"+(xDir));
		Logger.WARNING("zDir"+(zDir)); 
		/*if (!(aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir))) {
			return false;
		}*/
		int tAmount = 0;
		for (int i = mOffsetX_Lower; i <=mOffsetX_Upper; ++i) {
			for (int j = mOffsetZ_Lower; j <= mOffsetZ_Upper; ++j) {
				for (int h = -1; h < 2; ++h) {
					if ((h != 0) || ((((xDir + i != 0) || (zDir + j != 0))) && (((i != 0) || (j != 0))))) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h,
								zDir + j);
						if (!addToMachineList(tTileEntity)) {
							Logger.WARNING("X: "+i+" | Z: "+j);
							Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
							if ((tBlock != getCasingBlock()) && (tMeta != getCasingMeta())) {
								if ((i != mOffsetX_Lower && j !=  mOffsetZ_Lower
										&& i != mOffsetX_Upper && j != mOffsetZ_Upper) && (h == 0 || h == 1)){
									if (tBlock == Blocks.air){
										Logger.WARNING("Found Air");
									}
									else if (tBlock == Blocks.water){
										Logger.WARNING("Found Water");
									}
								}
								else {
									Logger.WARNING("[x] Did not form - Found: "+tBlock.getLocalizedName() + " | "+tBlock.getDamageValue(aBaseMetaTileEntity.getWorld(), aBaseMetaTileEntity.getXCoord()+ i, aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord() + j));
									Logger.WARNING("[x] Did not form - Found: "+(aBaseMetaTileEntity.getXCoord()+xDir + i) +" | "+ aBaseMetaTileEntity.getYCoord()+" | "+ (aBaseMetaTileEntity.getZCoord()+zDir + j));
									return false;
								}

							}
							++tAmount;
						}
					}
				}
			}
		}
		if ((tAmount >= 8)){
			Logger.WARNING("Made structure.");
		}
		else {
			Logger.WARNING("Did not make structure.");
		}
		return (tAmount >= 8);
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
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasings2Misc;
	}


	public byte getCasingMeta() {
		return 4;
	}


	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(20);
	}

	private boolean addToMachineList(final IGregTechTileEntity tTileEntity) {
		return ((this.addMaintenanceToMachineList(tTileEntity, this.getCasingTextureIndex())) 
				|| (this.addInputToMachineList(tTileEntity, this.getCasingTextureIndex())) 
				|| (this.addOutputToMachineList(tTileEntity, this.getCasingTextureIndex())) 
				|| (this.addMufflerToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addEnergyInputToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addDynamoToMachineList(tTileEntity, this.getCasingTextureIndex())));
	}

	public boolean checkForWater() {

		//Get Facing direction
		IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
		int mDirectionX  = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int mCurrentDirectionX;
		int mCurrentDirectionZ;
		int mOffsetX_Lower = 0;
		int mOffsetX_Upper = 0;
		int mOffsetZ_Lower = 0;
		int mOffsetZ_Upper = 0;

		if (mDirectionX == 0){
			mCurrentDirectionX = 2;
			mCurrentDirectionZ = 3;
			mOffsetX_Lower = -2;
			mOffsetX_Upper = 2;
			mOffsetZ_Lower = -3;
			mOffsetZ_Upper = 3;
		}
		else {
			mCurrentDirectionX = 3;
			mCurrentDirectionZ = 2;	
			mOffsetX_Lower = -3;
			mOffsetX_Upper = 3;
			mOffsetZ_Lower = -2;
			mOffsetZ_Upper = 2;	
		}

		//if (aBaseMetaTileEntity.fac)

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * mCurrentDirectionX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * mCurrentDirectionZ;

		int tAmount = 0;
		for (int i = mOffsetX_Lower; i <=mOffsetX_Upper; ++i) {
			for (int j = mOffsetZ_Lower; j <= mOffsetZ_Upper; ++j) {
				for (int h = -1; h < 2; ++h) {
					if ((h != 0) || ((((xDir + i != 0) || (zDir + j != 0))) && (((i != 0) || (j != 0))))) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h,
								zDir + j);
						if (!addToMachineList(tTileEntity)) {
							Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
							if ((tBlock != getCasingBlock()) && (tMeta != getCasingMeta())) {
								if ((i != mOffsetX_Lower && j !=  mOffsetZ_Lower
										&& i != mOffsetX_Upper && j != mOffsetZ_Upper) && (h == 0 || h == 1)){
									if (tBlock == Blocks.air || tBlock == Blocks.flowing_water || tBlock == Blocks.water){
										if (this.getStoredFluids() != null){
											for (FluidStack stored : this.getStoredFluids()){
												if (stored.isFluidEqual(FluidUtils.getFluidStack("water", 1))){
													if (stored.amount >= 1000){
														//Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
														stored.amount -= 1000;
														Block fluidUsed = null;
														if (tBlock == Blocks.air || tBlock == Blocks.flowing_water){
															fluidUsed = Blocks.water;
														}
														if (tBlock == Blocks.water){
															fluidUsed = BlocksItems.getFluidBlock(InternalName.fluidDistilledWater);
														}
														aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getXCoord()+xDir + i, aBaseMetaTileEntity.getYCoord()+h, aBaseMetaTileEntity.getZCoord()+zDir + j, fluidUsed);



													}
												}
											}
										}
									}
									if (tBlock == Blocks.water){
										++tAmount;
										//Utils.LOG_WARNING("Found Water");
									}
									else if (tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)){
										++tAmount;
										++tAmount;
										//Utils.LOG_WARNING("Found Distilled Water");										
									}
								}
								else {
									//Utils.LOG_WARNING("[x] Did not form - Found: "+tBlock.getLocalizedName() + " | "+tBlock.getDamageValue(aBaseMetaTileEntity.getWorld(), aBaseMetaTileEntity.getXCoord()+ i, aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord() + j));
									//Utils.LOG_WARNING("[x] Did not form - Found: "+(aBaseMetaTileEntity.getXCoord()+xDir + i) +" | "+ aBaseMetaTileEntity.getYCoord()+" | "+ (aBaseMetaTileEntity.getZCoord()+zDir + j));
									return false;
								}

							}
						}
					}
				}
			}
		}
		if ((tAmount == 45)){
			Logger.WARNING("Filled structure.");
		}
		else {
			Logger.WARNING("Did not fill structure.");
		}
		return (tAmount == 45);
	}

}