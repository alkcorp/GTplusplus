package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialCuttingMachine
extends GregtechMeta_MultiBlockBase {
	public GregtechMetaTileEntity_IndustrialCuttingMachine(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialCuttingMachine(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialCuttingMachine(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Cutting Machine";
	}

	@Override
	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Industrial Cutting Factory",
				"200% faster than using single block machines of the same voltage",
				"Only uses 75% of the eu/t normally required",
				"Processes four items per voltage tier",
				"Size: 3x3x5 [WxHxL] (Hollow)", "Controller (front centered)",
				"1x Input Bus (side centered)",
				"1x Output Bus (side centered)",
				"1x Energy Hatch (top or bottom centered)",
				"1x Input Hatch (top or bottom centered)",
				"1x Muffler Hatch (top)",
				"1x Maintenance Hatch (back centered)",
				"Cutting Factory Frames for the rest (26 at least!)"
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(29)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(29)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "IndustrialCuttingMachine";
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sCutterRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((4* GT_Utility.getTier(this.getMaxInputVoltage())), 75, 200);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final byte tSide = this.getBaseMetaTileEntity().getBackFacing();
		if ((this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 1)) && (this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 2) && (this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 3)))) {
			for (byte i = 2; i < 6; i = (byte) (i + 1)) {
				IGregTechTileEntity tTileEntity;
				if ((null != (tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(i, 2))) &&
						(tTileEntity.getFrontFacing() == this.getBaseMetaTileEntity().getFrontFacing()) && (tTileEntity.getMetaTileEntity() != null) &&
						((tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntity_IndustrialCuttingMachine))) {
					//Utils.LOG_INFO("False 1");
					return false;
				}
			}
			final int tX = this.getBaseMetaTileEntity().getXCoord();
			final int tY = this.getBaseMetaTileEntity().getYCoord();
			final int tZ = this.getBaseMetaTileEntity().getZCoord();
			for (byte i = -1; i < 2; i = (byte) (i + 1)) {
				for (byte j = -1; j < 2; j = (byte) (j + 1)) {
					if ((i != 0) || (j != 0)) {
						for (byte k = 0; k < 5; k = (byte) (k + 1)) {
							if (((i == 0) || (j == 0)) && ((k == 1) || (k == 2) || (k == 3))) {
								if ((this.getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingBlock()) && (this.getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingMeta())) {
								}
								else if (!this.addToMachineList(this.getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i))) && (!this.addEnergyInputToMachineList(this.getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i))))) {
									Logger.INFO("False 2");
									return false;
								}
							}
							else if ((this.getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingBlock()) && (this.getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingMeta())) {
							}
							else {
								Logger.INFO("False 3");
								return false;
							}
						}
					}
				}
			}
			if ((this.mOutputHatches.size() != 0)) {
				Logger.INFO("Use Busses, Not Hatches for Output.");
				return false;
			}
			if ((this.mInputBusses.size() < 1) || (this.mOutputBusses.size() < 1)) {
				Logger.INFO("Incorrect amount of Input & Output busses.");
				return false;
			}
			this.mMaintenanceHatches.clear();
			final IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 4);
			if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
				if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Maintenance)) {
					this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) tTileEntity.getMetaTileEntity());
					((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).mMachineBlock = this.getCasingTextureIndex();
				} else {
					Logger.INFO("Maintenance hatch must be in the middle block on the back.");
					return false;
				}
			}
			if ((this.mMaintenanceHatches.size() != 1) || (this.mMufflerHatches.size() < 1) || (this.mEnergyHatches.size() < 1)) {
				Logger.INFO("Incorrect amount of Maintenance or Energy or Muffler hatches.");
				return false;
			}
		} else {
			Logger.INFO("False 5");
			return false;
		}
		Logger.INFO("True");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 8;
	}

	@Override
	public int getAmountOfOutputs() {
		return 2;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasings2Misc;
	}


	public byte getCasingMeta() {
		return 13;
	}


	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(29);
	}

	private boolean addToMachineList(final IGregTechTileEntity tTileEntity) {
		return ((this.addMaintenanceToMachineList(tTileEntity, this.getCasingTextureIndex())) || (this.addInputToMachineList(tTileEntity, this.getCasingTextureIndex())) || (this.addOutputToMachineList(tTileEntity, this.getCasingTextureIndex())) || (this.addMufflerToMachineList(tTileEntity, this.getCasingTextureIndex())));
	}

	private boolean addEnergyInputToMachineList(final IGregTechTileEntity tTileEntity) {
		return ((this.addEnergyInputToMachineList(tTileEntity, this.getCasingTextureIndex())));
	}
}