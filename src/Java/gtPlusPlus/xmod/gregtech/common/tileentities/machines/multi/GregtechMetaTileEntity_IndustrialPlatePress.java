package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

import static gtPlusPlus.core.util.array.ArrayUtils.removeNulls;

public class GregtechMetaTileEntity_IndustrialPlatePress
extends GregtechMeta_MultiBlockBase {
	public GT_Recipe mLastRecipe;

	public GregtechMetaTileEntity_IndustrialPlatePress(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialPlatePress(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialPlatePress(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Controller Block for the Material Press",
				"Performs Bending Machine recipes",
				"50% faster than using single block machines of the same voltage",
				"Circuit for recipe goes in the Input Bus",
				"Each Input Bus can have a different Circuit",
				"Size: 3x3x3 (Hollow)",
				"Controller (front centered)",
				"1x Input Bus (anywhere)",
				"1x Output Bus (anywhere)",
				"1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)",
				"1x Muffler Hatch (anywhere)",
				"Material Press Machine Casings for the rest (16 at least!)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(4)], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(4)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MaterialPress.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sBenderRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
			ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
			tBus.mRecipeMap = getRecipeMap();
			if (isValidMetaTileEntity(tBus)) {
				for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
						tBusItems.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
				}
			}

			if (checkBusRecipe(tBusItems.toArray(new ItemStack[]{}))) return true;
		}
		return false;
	}

	private boolean checkBusRecipe(ItemStack[] aBusItems) {
		// Based on the Processing Array. A bit overkill.
		final int maxParallelRecipes = 2;
		int parallelRecipes = 0;

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBenderRecipes.findRecipe(
				getBaseMetaTileEntity(), this.mLastRecipe, false,
				gregtech.api.enums.GT_Values.V[tTier], null, aBusItems);

		if (tRecipe == null) {
			return false;
		}

		// Count recipes to do in parallel
		this.mEUt = 0;
		for (; parallelRecipes < maxParallelRecipes; parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, new FluidStack[]{}, aBusItems)) {
				break;
			}
			this.mEUt += tRecipe.mEUt;
		}

		if (parallelRecipes == 0) {
			return false;
		}

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;

		// Plate Press discounts
		this.mEUt = MathUtils.findPercentageOfInt(this.mEUt, 50);
		this.mMaxProgresstime = tRecipe.mDuration;

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
		updateSlots();
		return true;
	}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 20) {
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(Integer.valueOf(5)), 10, 1.0F, aX, aY, aZ);
		}
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 2; h++) {
					if ((h != 0) || ((((xDir + i) != 0) || ((zDir + j) != 0)) && ((i != 0) || (j != 0)))) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(4))) && (!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(4))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(4))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(4))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(4)))) {
							final Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							final byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
							if (((tBlock != ModBlocks.blockCasingsMisc) || (tMeta != 4))) {
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		return tAmount >= 16;
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
}
