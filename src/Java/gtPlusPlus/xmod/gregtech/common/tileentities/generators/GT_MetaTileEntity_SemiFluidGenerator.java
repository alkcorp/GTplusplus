package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_SemiFluidGenerator extends GT_MetaTileEntity_BasicGenerator{

	public static final int BASE_POLLUTION = 2;
	public int mEfficiency;

	/*public GT_MetaTileEntity_SemiFluidGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
		onConfigLoad();
	}*/

	public GT_MetaTileEntity_SemiFluidGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier,
				"Requires heavy-fluid Fuel",
				new ITexture[0]);
		onConfigLoad();
	}

	public GT_MetaTileEntity_SemiFluidGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		onConfigLoad();
	}

	public int getPollution() {
		return (int) (2.0D * Math.pow(2.0D, this.mTier));
	}

	@Override
	public int getCapacity() {
		return 8000;
	}

	public void onConfigLoad() {
		this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig,
				"SemiFluidGenerator.efficiency.tier." + this.mTier, 100 - (this.mTier * 10));
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_SemiFluidGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipes() {
		return GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels;
	}
	
	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription, "Produces "+(this.getPollution()*20)+" pollution/sec", "Fuel Efficiency: "+this.getEfficiency() + "%", CORE.GT_Tooltip};
	}

	@Override
	public int getEfficiency() {
		return this.mEfficiency;
	}
	
	@Override
	public boolean isOutputFacing(byte aSide) {
		return (aSide == getBaseMetaTileEntity().getFrontFacing());
	}
	
	@Override
	public int getFuelValue(ItemStack aStack) {
		if ((GT_Utility.isStackInvalid(aStack)) || (getRecipes() == null))
			return 0;
		int rValue = Math.max(GT_ModHandler.getFuelCanValue(aStack) * 6 / 5, super.getFuelValue(aStack));
		if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
			rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3);
		}
		return rValue;
	}

	@Override
	public ITexture[] getFront(byte aColor) {
		return new ITexture[] { super.getFront(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_FRONT),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
	}

	@Override
	public ITexture[] getBack(byte aColor) {
		return new ITexture[] { super.getBack(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BACK) };
	}

	@Override
	public ITexture[] getBottom(byte aColor) {
		return new ITexture[] { super.getBottom(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM) };
	}

	@Override
	public ITexture[] getTop(byte aColor) {
		return new ITexture[] { super.getTop(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP) };
	}

	@Override
	public ITexture[] getSides(byte aColor) {
		return new ITexture[] { super.getSides(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE) };
	}

	@Override
	public ITexture[] getFrontActive(byte aColor) {
		return new ITexture[] { super.getFrontActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_FRONT_ACTIVE),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
	}

	@Override
	public ITexture[] getBackActive(byte aColor) {
		return new ITexture[] { super.getBackActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BACK_ACTIVE) };
	}

	@Override
	public ITexture[] getBottomActive(byte aColor) {
		return new ITexture[] { super.getBottomActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM_ACTIVE) };
	}

	@Override
	public ITexture[] getTopActive(byte aColor) {
		return new ITexture[] { super.getTopActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP_ACTIVE) };
	}

	@Override
	public ITexture[] getSidesActive(byte aColor) {
		return new ITexture[] { super.getSidesActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE_ACTIVE) };
	}

}
