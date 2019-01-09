package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import gregtech.api.GregTech_API;
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
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialThermalCentrifuge
extends GregtechMeta_MultiBlockBase {
	public GregtechMetaTileEntity_IndustrialThermalCentrifuge(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialThermalCentrifuge(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialThermalCentrifuge(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Thermal Centrifuge";
	}

	@Override
	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Industrial Thermal Centrifuge",
				"150% faster than using single block machines of the same voltage",
				"Only uses 80% of the eu/t normally required",
				"Processes eight items per voltage tier",
				"Size: 3x2x3 [WxHxL]", "Controller (front centered, top layer)",
				"1x Input Bus (Any bottom layer casing)",
				"1x Output Bus (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)",
				"1x Muffler Hatch (Casing under controller)",
				"1x Energy Hatch (Any bottom layer casing)",
				"Thermal processing Casings for the rest (8 at least!)",
				"Noise Hazard Sign Blocks also count as valid casings",
				
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "IndustrialThermalCentrifuge";
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((8* GT_Utility.getTier(this.getMaxInputVoltage())), 80, 150);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		/*if (!(aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir))) {
			return false;
		}*/
		int tAmount = 0;
		for (int i = -1; i < 2; ++i) {
			for (int j = -1; j < 2; ++j) {
				for (int h = -1; h < 1; ++h) {
					if ((xDir + i == 0) && (zDir + j == 0) && (h == 0)) continue; // controller block

					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h,
							zDir + j);

					Logger.INFO("------------------");
					Logger.INFO("xDir: " + xDir + " | zDir: " + zDir);
					Logger.INFO("i: " + i + " | j: " + j + " | h: " + h);
					if ((h == 0) || !addToMachineList(tTileEntity)) { // only bottom layer allows machine parts
						// top layer, or not machine part, must be casing
						Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
						byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
						if ((((tBlock != ModBlocks.blockCasings2Misc) || (tMeta != 0)))
								&& (((tBlock != GregTech_API.sBlockCasings3) || (tMeta != 9)))) {
							Logger.INFO("Wrong Block?");
							return false;
						}
						tAmount++;
					}
				}

			}
		}
		Logger.INFO("------------------");
		Logger.WARNING("Trying to assemble structure. Completed? "+(tAmount >= 8));
		return (tAmount >= 8);
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 50;
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
		return 0;
	}


	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(16);
	}

	private boolean addToMachineList(final IGregTechTileEntity tTileEntity) {
		return ((this.addMaintenanceToMachineList(tTileEntity, this.getCasingTextureIndex())) 
				|| (this.addInputToMachineList(tTileEntity, this.getCasingTextureIndex())) 
				|| (this.addOutputToMachineList(tTileEntity, this.getCasingTextureIndex())) 
				|| (this.addMufflerToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addEnergyInputToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addDynamoToMachineList(tTileEntity, this.getCasingTextureIndex())));
	}
}