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
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class GregtechMetaTileEntity_IndustrialArcFurnace
extends GregtechMeta_MultiBlockBase {

	//862
	private static final int mCasingTextureID = TAE.getIndexFromPage(3, 3);
	public static String mCasingName = "Tempered Arc Furnace Casing";
	private boolean mPlasmaMode = false;
	private int mSize = 0;

	public GregtechMetaTileEntity_IndustrialArcFurnace(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 3);
	}

	public GregtechMetaTileEntity_IndustrialArcFurnace(final String aName) {
		super(aName);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 3);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialArcFurnace(this.mName);
	}

	@Override
	public String getMachineType() {
		return "(Plasma/Electric) Arc Furnace";
	}

	@Override
	public String[] getTooltip() {

		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 3);
		}

		return new String[]{
				"Controller Block for Industrial Arc Furnace",
				"250% faster than using single block machines of the same voltage",
				"Processes 8 items per voltage tier",
				"Size: nx3xn [WxHxL] (Hollow)",
				"n can be 3, 5 or 7",
				"Max Size required to process Plasma recipes",
				mCasingName+"s (10 at least!)",
				"Controller (top centered)",
				"1x Input Bus",
				"1x Output Bus",
				"1x Input Hatch",
				"1x Output Hatch",
				"1x Energy Hatch",
				};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == 0 || aSide == 1) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[mCasingTextureID],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[mCasingTextureID]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "IndustrialExtruder";
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return mPlasmaMode ? GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes : GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return this.checkRecipeGeneric(this.mSize * 8 * GT_Utility.getTier(this.getMaxInputVoltage()), 100, 250);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return (this.mSize * 8 * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		int x = 1;
		int z = 1;
		int depth = 1;
		int mPlascreteCount = 0;
		for (int i = 1; i < 4; i++) {
			if (aBaseMetaTileEntity.getAirOffset(i, -1, 0)) {
				Logger.INFO("aitfound "+i);
				continue;				
			}
			Block tBlock = aBaseMetaTileEntity.getBlockOffset(i, -1, 0);
			int tMeta = aBaseMetaTileEntity.getMetaIDOffset(i, -1, 0);
			{
				if (isValidCasingBlock(tBlock, tMeta)) {
					x = i;
					z = i;
					Logger.INFO("Found Correct Edge Casing at offset "+i);
					break;
				} else {
					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, 0, 0);
					if (addToMachineList(tTileEntity, mCasingTextureID)) {
						Logger.INFO("Found Tile Entity on Edge Casing on top layer, it is a valid htach/bus.");
						x = i;
						z = i;
						break;
					}
					else {
						return false;
					}
				}
			}
		}
		for (int i = 0; i > -3; i--) {			
			if (!aBaseMetaTileEntity.getAirOffset(0, i, 0)) {
				Block tBlock = aBaseMetaTileEntity.getBlockOffset(0, i, 0);
				int tMeta = aBaseMetaTileEntity.getMetaIDOffset(0, i, 0);
				if (isValidCasingBlock(tBlock, tMeta)) {
					depth = i;
					Logger.INFO("Found Correct Floor Casing at offset "+depth);
					break;
				}
			}			
		}
		//!addToMachineList(tTileEntity, mCasingTextureID)
		if (depth >= -1) {
			Logger.INFO("Structure is not 5 blocks or taller. depth = "+depth);
			return false;
		}
		
		mSize = x;
		
		Logger.INFO("Using X: "+x+" | Y: "+depth+" | Z: "+z+" as offsets");
		for (int dX = -x; dX <= x; dX++) {
			for (int dZ = -z; dZ <= z; dZ++) {
				for (int dY = 0; dY >= depth; dY--) {
					if (dX == -x || dX == x || dY == -depth || dY == depth || dZ == -z || dZ == z) {
						Block tBlock = aBaseMetaTileEntity.getBlockOffset(dX, dY, dZ);
						int tMeta = aBaseMetaTileEntity.getMetaIDOffset(dX, dY, dZ);
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dX, dY, dZ);
						if (dY == 0) {
							if (dX == -x || dX == x || dZ == -z || dZ == z) {
								if (!isValidCasingBlock(tBlock, tMeta) && !addToMachineList(tTileEntity, mCasingTextureID)) {
									Logger.INFO("Found Incorrect Edge Casing at offset X:"+dX+" | Y:"+dY+" | Z:"+dZ);
									return false;
								}
							} else if (dX == 0 && dZ == 0) {

							} else {
								if (tBlock != ModBlocks.blockCasings3Misc || tMeta != 1) {
									Logger.INFO("Found Incorrect Inner Casing at offset X:"+dX+" | Y:"+dY+" | Z:"+dZ);
									return false;
								}
							}
						}
						else if (dY < 0) {							
							//Check Walls
							if (dY != depth && (dX == -x || dX == x || dZ == -z || dZ == z)) {
								if (!isValidCasingBlock(tBlock, tMeta) && !addToMachineList(tTileEntity, mCasingTextureID)) {
									Logger.INFO("Found Incorrect Wall Casing at offset X:"+dX+" | Y:"+dY+" | Z:"+dZ);
									return false;
								}
								else {
									mPlascreteCount++;
								}
							}							
							//Check Floor
							if (dY == depth) {
								if (!isValidCasingBlock(tBlock, tMeta) && !addToMachineList(tTileEntity, mCasingTextureID)) {
									Logger.INFO("Found Incorrect Floor Casing at offset X:"+dX+" | Y:"+dY+" | Z:"+dZ);
									return false;
								}
								else {
									mPlascreteCount++;
								}
							}
							//Check Spacing
							else {
								if (aBaseMetaTileEntity.getAirOffset(dX, dY, dZ)) {
									Logger.INFO("Found Incorrect Inner Spacing at offset X:"+dX+" | Y:"+dY+" | Z:"+dZ);
									return false;
								}
							}							
						}
						
						else {/*
							if (isValidCasingBlock(tBlock, tMeta)) {
							} 
							else {						
								IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dX, dY, dZ);
								if ((!addMaintenanceToMachineList(tTileEntity, mCasingTextureID))
										&& (!addEnergyInputToMachineList(tTileEntity, mCasingTextureID))) {
									if ((!addInputToMachineList(tTileEntity, mCasingTextureID))
											&& (!addOutputToMachineList(tTileEntity, mCasingTextureID)) && (!addMufflerToMachineList(tTileEntity, mCasingTextureID))) {

										if (!aBaseMetaTileEntity.getAirOffset(dX, dY, dZ)) {
											Logger.INFO("Found Invalid object @ offset  X:"+dX+" | Y:"+dY+" | Z:"+dZ +" | Block: "+(tBlock != null ? tBlock.getLocalizedName() : "Air"));
											int aX = aBaseMetaTileEntity.getXCoord()+dX;
											int aY = aBaseMetaTileEntity.getYCoord()+dY;
											int aZ = aBaseMetaTileEntity.getZCoord()+dZ;
											//aBaseMetaTileEntity.getWorld().setBlock(aX, aY, aZ, Blocks.pumpkin, 0, 3);
											return false;										
										}

									}
								}
							}
						*/}
					}
				}
			}
		}
		if (mMaintenanceHatches.size() != 1 || mEnergyHatches.size() < 1) {
			Logger.INFO("Bad Hatches");
			return false;
		}



		for (int dX = -x + 1; dX <= x - 1; dX++) {
			for (int dZ = -z + 1; dZ <= z - 1; dZ++) {
				for (int dY = -1; dY >= depth + 1; dY--) {/*

					Block aInnerBlock = aBaseMetaTileEntity.getBlockOffset(dX, dY, dZ);					
					if (aBaseMetaTileEntity.getAirOffset(dX, dY, dZ) || aInnerBlock instanceof BlockAir) {
						int aX, aY, aZ;
						aX = aBaseMetaTileEntity.getXCoord()+dX;
						aY = aBaseMetaTileEntity.getYCoord()+dY;
						aZ = aBaseMetaTileEntity.getZCoord()+dZ;
						aBaseMetaTileEntity.getWorld().setBlock(aX, aY, aZ, Blocks.lava, 0, 3);
					}

				 */}
			}
		}

		Logger.INFO("Built Structure");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 120;
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
		return ModBlocks.blockCasings4Misc;
	}


	public byte getCasingMeta() {
		return 3;
	}

	public Block getCasingBlock2() {
		return ModBlocks.blockCasings3Misc;
	}


	public byte getCasingMeta2() {
		return 15;
	}

	public boolean isValidCasingBlock(Block aBlock, int aMeta) {
		if (aBlock == getCasingBlock() && aMeta == getCasingMeta()) {
			return true;
		}
		if (aBlock == getCasingBlock2() && aMeta == getCasingMeta2()) {
			return true;
		}
		Logger.INFO("Found "+(aBlock != null ? aBlock.getLocalizedName() : "Air") + "With Meta "+aMeta);
		return false;
	}

	public byte getCasingTextureIndex() {
		return (byte) mCasingTextureID;
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (this.mSize > 3) {
			this.mPlasmaMode = Utils.invertBoolean(mPlasmaMode);
			if (mPlasmaMode) {
				PlayerUtils.messagePlayer(aPlayer, "["+EnumChatFormatting.RED+"MODE"+EnumChatFormatting.RESET+"] "+EnumChatFormatting.LIGHT_PURPLE+"Plasma"+EnumChatFormatting.RESET);
			}
			else {
				PlayerUtils.messagePlayer(aPlayer, "["+EnumChatFormatting.RED+"MODE"+EnumChatFormatting.RESET+"] "+EnumChatFormatting.YELLOW+"Electric"+EnumChatFormatting.RESET);			
			}
		}		
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mPlasmaMode", mPlasmaMode);
		aNBT.setInteger("mSize", mSize);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mPlasmaMode = aNBT.getBoolean("mPlasmaMode");
		mSize = aNBT.getInteger("mSize");
	}

	@Override
	public void onMachineBlockUpdate() {
		mUpdate = 100;
	}
}
