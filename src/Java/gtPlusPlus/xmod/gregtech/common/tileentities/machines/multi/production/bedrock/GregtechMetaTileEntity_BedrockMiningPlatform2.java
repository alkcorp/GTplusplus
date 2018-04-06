package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.bedrock;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;

public class GregtechMetaTileEntity_BedrockMiningPlatform2 extends GregtechMetaTileEntity_BedrockMiningPlatformBase {
	public GregtechMetaTileEntity_BedrockMiningPlatform2(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_BedrockMiningPlatform2(final String aName) {
		super(aName);
	}

	public String[] getDescription() {
		return this.getDescriptionInternal("II");
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (IMetaTileEntity) new GregtechMetaTileEntity_BedrockMiningPlatform2(this.mName);
	}

	protected Material getFrameMaterial() {
		return ELEMENT.getInstance().AMERICIUM241;
	}

	protected int getCasingTextureIndex() {
		return 62;
	}

	protected int getRadiusInChunks() {
		return 9;
	}

	protected int getMinTier() {
		return 5;
	}

	protected int getBaseProgressTime() {
		return 480;
	}
}