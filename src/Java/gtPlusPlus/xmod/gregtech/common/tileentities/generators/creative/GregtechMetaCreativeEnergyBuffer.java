package gtPlusPlus.xmod.gregtech.common.tileentities.generators.creative;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.enums.Textures;
import gregtech.api.gui.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 *
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public class GregtechMetaCreativeEnergyBuffer extends GregtechMetaEnergyBuffer {


	public GregtechMetaCreativeEnergyBuffer(final String aName, final int aTier,
			final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aDescription, aTextures, aSlotCount);
		// TODO Auto-generated constructor stub
	}

	public GregtechMetaCreativeEnergyBuffer(final int aID, final String aName,
			final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, "Added by: "	+ EnumChatFormatting.DARK_GREEN+"Alkalus"};
	}

	/*
	 * MACHINE_STEEL_SIDE
	 */
	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[2][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[] { new GT_RenderedTexture(
					Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT) };
			rTextures[1][i + 1] = new ITexture[] {
					new GT_RenderedTexture(
							Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT),
					this.mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
							: Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity,
			final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive,
			final boolean aRedstone) {
		return this.mTextures[aSide == aFacing ? 1 : 0][aColorIndex + 1];
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaCreativeEnergyBuffer(this.mName, this.mTier, this.mDescription,
				this.mTextures, this.mInventory.length);
	}

	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isElectric()							{return true;}
	@Override public boolean isValidSlot(final int aIndex)				{return true;}
	@Override public boolean isFacingValid(final byte aFacing)			{return true;}
	@Override public boolean isEnetInput() 							{return true;}
	@Override public boolean isEnetOutput() 						{return true;}
	@Override public boolean isInputFacing(final byte aSide)				{return aSide!=this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isOutputFacing(final byte aSide)				{return aSide==this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isTeleporterCompatible()				{return false;}

	@Override
	public long getMinimumStoredEU() {
		return 1;
	}

	@Override
	public long maxEUStore() {
		return Long.MAX_VALUE;
	}

	@Override
	public long maxEUInput() {
		return V[this.mTier];
	}

	@Override
	public long maxEUOutput() {
		return V[this.mTier];
	}

	@Override
	public long maxAmperesIn() {
		return this.mChargeableCount * 16;
	}

	@Override
	public long maxAmperesOut() {
		return this.mChargeableCount * 16;
	}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return this.mCharge?this.mInventory.length:0;}
	@Override public int dechargerSlotCount()						{return this.mDecharge?this.mInventory.length:0;}
	@Override public int getProgresstime()							{return Integer.MAX_VALUE;}
	@Override public int maxProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyCapacity();}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		//
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		//
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		switch (this.mInventory.length) {
		case  1: return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
		case  4: return new GT_Container_2by2(aPlayerInventory, aBaseMetaTileEntity);
		case  9: return new GT_Container_3by3(aPlayerInventory, aBaseMetaTileEntity);
		case 16: return new GT_Container_4by4(aPlayerInventory, aBaseMetaTileEntity);
		}
		return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		switch (this.mInventory.length) {
		case  1: return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
		case  4: return new GT_GUIContainer_2by2(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
		case  9: return new GT_GUIContainer_3by3(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
		case 16: return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
		}
		return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		this.getBaseMetaTileEntity().increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		if (aBaseMetaTileEntity.isServerSide()) {
			this.mCharge = (aBaseMetaTileEntity.getStoredEU() / 2) > (aBaseMetaTileEntity
					.getEUCapacity() / 3);
			this.mDecharge = aBaseMetaTileEntity.getStoredEU()     < (aBaseMetaTileEntity.getEUCapacity() / 3);
			this.mBatteryCount = 1;
			this.mChargeableCount = 1;
			this.getBaseMetaTileEntity().increaseStoredEnergyUnits(this.mMax, true);
			for (final ItemStack tStack : this.mInventory) {
				if (GT_ModHandler.isElectricItem(tStack, this.mTier)) {
					if (GT_ModHandler.isChargerItem(tStack)) {
						this.mBatteryCount++;
					}
					this.mChargeableCount++;
				}
			}
		}
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		if(GT_ModHandler.isElectricItem(aStack)&&aStack.getUnlocalizedName().startsWith("gt.metaitem.01.")){
			final String name = aStack.getUnlocalizedName();
			if(name.equals("gt.metaitem.01.32510")||
					name.equals("gt.metaitem.01.32511")||
					name.equals("gt.metaitem.01.32520")||
					name.equals("gt.metaitem.01.32521")||
					name.equals("gt.metaitem.01.32530")||
					name.equals("gt.metaitem.01.32531")){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		if(!GT_Utility.isStackValid(aStack)){
			return false;
		}
		if(GT_ModHandler.isElectricItem(aStack, this.mTier)){
			return true;
		}
		return false;
	}

	@Override
	public long[] getStoredEnergy(){
		long tScale = this.getBaseMetaTileEntity().getEUCapacity();
		long tStored = this.getBaseMetaTileEntity().getStoredEU();
		this.setEUVar(Long.MAX_VALUE);
		if (this.mInventory != null) {
			for (final ItemStack aStack : this.mInventory) {
				if (GT_ModHandler.isElectricItem(aStack)) {

					if (aStack.getItem() instanceof GT_MetaBase_Item) {
						final Long[] stats = ((GT_MetaBase_Item) aStack.getItem())
								.getElectricStats(aStack);
						if (stats != null) {
							tScale = tScale + stats[0];
							tStored = tStored
									+ ((GT_MetaBase_Item) aStack.getItem())
									.getRealCharge(aStack);
						}
					} else if (aStack.getItem() instanceof IElectricItem) {
						tStored = tStored
								+ (long) ic2.api.item.ElectricItem.manager
								.getCharge(aStack);
						tScale = tScale
								+ (long) ((IElectricItem) aStack.getItem())
								.getMaxCharge(aStack);
					}
				}
			}

		}
		return new long[] { tStored, tScale };
	}

	private long count=0;
	private long mStored=0;
	private long mMax=0;

	@Override
	public String[] getInfoData() {
		this.count++;
		if((this.mMax==0)||((this.count%20)==0)){
			final long[] tmp = this.getStoredEnergy();
			this.mStored=tmp[0];
			this.mMax=tmp[1];
		}

		return new String[] {
				this.getLocalName(),
				"THIS IS A CREATIVE ITEM - FOR TESTING",
				GT_Utility.formatNumbers(this.mStored)+" EU /",
				GT_Utility.formatNumbers(this.mMax)+" EU"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}
}