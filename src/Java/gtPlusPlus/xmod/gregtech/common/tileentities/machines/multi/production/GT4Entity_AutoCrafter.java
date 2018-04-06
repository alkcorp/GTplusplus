package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.helpers.CraftingHelper;
import gtPlusPlus.xmod.gregtech.common.helpers.autocrafter.AC_Helper_Utils;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GT4Entity_AutoCrafter
extends GT_MetaTileEntity_MultiBlockBase
{

	private MODE mMachineMode = MODE.ASSEMBLY;
	private byte mTier = 1;
	private final int mHeatingCapacity = 4700;
	protected GT_Recipe mLastRecipeToBuffer;

	/** The crafting matrix inventory (3x3). */
	public CraftingHelper mInventoryCrafter;

	public static enum MODE{
		CRAFTING("DISASSEMBLY","ASSEMBLY"),
		ASSEMBLY("CRAFTING","DISASSEMBLY"),
		DISASSEMBLY("ASSEMBLY","CRAFTING");

		private final String lastMode;
		private final String nextMode;

		MODE(String previous, String next){
			this.lastMode = previous;
			this.nextMode = next;
		}

		public MODE nextMode(){
			return MODE.valueOf(this.nextMode);
		}

		public MODE lastMode(){
			return MODE.valueOf(this.lastMode);
		}

	}

	@Override
	public boolean isFacingValid(byte aFacing)
	{
		return aFacing > 1;
	}

	public void onRightclick(EntityPlayer aPlayer) {}

	public GT4Entity_AutoCrafter(int aID, String aName, String aNameRegional)
	{
		super(aID, aName, aNameRegional);
	}


	public GT4Entity_AutoCrafter(String mName) {
		super(mName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GT4Entity_AutoCrafter(this.mName);
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack)
	{
		return true;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack)
	{
		return 0;
	}

	@Override
	public boolean onRunningTick(ItemStack aStack)
	{
		return true;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack)
	{
		return false;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack)
	{
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack)
	{
		return 200;
	}

	public int getAmountOfOutputs()
	{
		return 1;
	}

	@Override
	public String[] getDescription()
	{
		return new String[]{
				"Highly Advanced Autocrafter",
				"Right Click with a Screwdriver to change mode",
				"This Machine Can Craft, Assemble or Disassemble",
				"--------------------------------------",
				"Insert a Memory stick into the GUI",
				"to automate a crafting table recipe",
				"Requires recipe to be scanned in a project table",
				"--------------------------------------",
				"Hatches & Busses can be placed anywhere",
				"1x Input Bus",
				"1x Input Hatch",
				"1x Output Bus",
				"1x Output Hatch",
				"1x Muffler Hatch",
				"1x Maintenance Hatch",
				"1x Energy Hatch",
				"--------------------------------------",
				CORE.GT_Tooltip
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(28)],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(28)]};
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack p1) {		
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; ++i) {
			for (int j = -1; j < 2; ++j) {
				for (int h = -1; h < 2; ++h) {
					if (h != 0 || ((xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0))) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(28))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,	zDir + j) != ModBlocks.blockCasings2Misc) {
								Logger.WARNING("Bad Block. Found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h,	zDir + j) .getLocalizedName());
								Logger.WARNING("Block Found at x:"+(aBaseMetaTileEntity.getXCoord()+xDir+i)+" | y:"+(aBaseMetaTileEntity.getYCoord()+h)+" | z:"+(aBaseMetaTileEntity.getZCoord()+zDir+j));
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
								Logger.WARNING("Bad Meta.");
								return false;
							}
							++tAmount;
						}
					}
				}
			}
		}

		if ((this.mInputHatches.size() == 0) || (this.mOutputHatches.size() == 0) ||
				(this.mInputBusses.size() == 0) || (this.mOutputBusses.size() == 0) || 
				(this.mMufflerHatches.size() != 1) || (this.mMaintenanceHatches.size() != 1) ||
				(this.mEnergyHatches.size() == 0)){
			Logger.WARNING("Wrong Hatch count.");
			Logger.WARNING("|"+this.mInputHatches.size()+
					"|"+this.mOutputHatches.size()+
					"|"+this.mInputBusses.size()+
					"|"+this.mOutputBusses.size()+
					"|"+this.mMufflerHatches.size()+
					"|"+this.mMaintenanceHatches.size()+
					"|"+this.mEnergyHatches.size()+"|");
			return false;
		}

		//mInventoryCrafter = new CraftingHelper(this);
		return tAmount >= 16;

	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
	}	

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mMachineMode = mMachineMode.nextMode();
		if (mMachineMode == MODE.CRAFTING){
			PlayerUtils.messagePlayer(aPlayer, "You are now running the Auto-Crafter in mode: §dAuto-Crafting");			
		}
		else if (mMachineMode == MODE.ASSEMBLY){
			PlayerUtils.messagePlayer(aPlayer, "You are now running the Auto-Crafter in mode: §aAssembly");				
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "You are now running the Auto-Crafter in mode: §cDisassembly");		
		}
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {

		final long tVoltage = this.getMaxInputVoltage();
		final byte tTier = this.mTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		if (mMachineMode == MODE.DISASSEMBLY){
			return doDisassembly();
		}
		else if (mMachineMode == MODE.CRAFTING){
			return doCrafting(aStack);
		}
		else {
			final ArrayList<ItemStack> tInputList = this.getStoredInputs();
			for (int tInputList_sS = tInputList.size(), i = 0; i < tInputList_sS - 1; ++i) {
				for (int j = i + 1; j < tInputList_sS; ++j) {
					if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
						if (tInputList.get(i).stackSize < tInputList.get(j).stackSize) {
							tInputList.remove(i--);
							tInputList_sS = tInputList.size();
							break;
						}
						tInputList.remove(j--);
						tInputList_sS = tInputList.size();
					}
				}
			}
			final ItemStack[] tInputs = tInputList.toArray(new ItemStack[tInputList.size()]);
			final ArrayList<FluidStack> tFluidList = this.getStoredFluids();
			for (int tFluidList_sS = tFluidList.size(), k = 0; k < tFluidList_sS - 1; ++k) {
				for (int l = k + 1; l < tFluidList_sS; ++l) {
					if (GT_Utility.areFluidsEqual(tFluidList.get(k), tFluidList.get(l))) {
						if (tFluidList.get(k).amount < tFluidList.get(l).amount) {
							tFluidList.remove(k--);
							tFluidList_sS = tFluidList.size();
							break;
						}
						tFluidList.remove(l--);
						tFluidList_sS = tFluidList.size();
					}
				}
			}
			final FluidStack[] tFluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);
			if (tInputList.size() > 0) {
				final GT_Recipe tRecipe = this.getRecipeMap().findRecipe(this.getBaseMetaTileEntity(), false, GT_Values.V[this.mTier], tFluids,	tInputs);
				if (tRecipe == null){
					mLastRecipeToBuffer = null;
				}
				if (tRecipe != null && tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
					mLastRecipeToBuffer = tRecipe;
					this.mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
					this.mEfficiencyIncrease = 10000;
					final int tHeatCapacityDivTiers = (this.mHeatingCapacity - tRecipe.mSpecialValue) / 900;
					if (tRecipe.mEUt <= 16) {
						this.mEUt = tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1);
						this.mMaxProgresstime = tRecipe.mDuration / (1 << tTier - 1);
					} else {
						this.mEUt = tRecipe.mEUt;
						this.mMaxProgresstime = tRecipe.mDuration;
						int m = 2;
						while (this.mEUt <= GT_Values.V[tTier - 1]) {
							this.mEUt *= 4;
							this.mMaxProgresstime /= ((tHeatCapacityDivTiers >= m) ? 4 : 2);
							m += 2;
						}
					}
					if (tHeatCapacityDivTiers > 0) {
						this.mEUt *= (int) Math.pow(0.95, tHeatCapacityDivTiers);
					}
					if (this.mEUt > 0) {
						this.mEUt = -this.mEUt;
					}
					this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
					this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0), tRecipe.getOutput(1)};
					this.updateSlots();
					return true;
				}
			}
			return false;
		}
	}

	public boolean doDisassembly(){

		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (int tInputList_sS = tInputList.size(), i = 0; i < tInputList_sS - 1; ++i) {
			for (int j = i + 1; j < tInputList_sS; ++j) {
				if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
					if (tInputList.get(i).stackSize < tInputList.get(j).stackSize) {
						tInputList.remove(i--);
						tInputList_sS = tInputList.size();
						break;
					}
					tInputList.remove(j--);
					tInputList_sS = tInputList.size();
				}
			}
		}
		final ItemStack[] tInputs = tInputList.toArray(new ItemStack[tInputList.size()]);

		ItemStack inputItem = tInputs[0];
		if (tInputs[0].stackSize <= 0){
			tInputs[0] = null;
			this.updateSlots();
		}
		int outputSlots = this.mOutputBusses.get(0).getSizeInventory();

		if (this.mOutputBusses.size() > 1){
			outputSlots=0;
			for (GT_MetaTileEntity_Hatch_OutputBus r : this.mOutputBusses){
				outputSlots+=r.getSizeInventory();
			}
		}

		this.mOutputItems = new ItemStack[outputSlots];
		if (inputItem != null && inputItem.stackSize > 0) {
			NBTTagCompound tNBT = inputItem.getTagCompound();
			if (tNBT != null) {
				tNBT = tNBT.getCompoundTag("GT.CraftingComponents");
				if (tNBT != null) {
					this.mEUt = 16 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					this.mMaxProgresstime = (100-(8*this.mTier));
					for (int i = 0; i < this.mOutputItems.length; ++i) {
						if (this.getBaseMetaTileEntity().getRandomNumber(100) < 60 + 12 * this.mTier) {
							this.mOutputItems[i] = GT_Utility.loadItem(tNBT, "Ingredient." + i);
							if (this.mOutputItems[i] != null) {
								this.mMaxProgresstime *= (int) 1.5;
							}
						}
					}
					if (this.mTier > 5) {
						this.mMaxProgresstime >>= this.mTier - 5;
					}
					if (this.mMaxProgresstime <= 20) {
						return false;
					}
					else {
						inputItem.stackSize--;
						if (inputItem.stackSize <= 0){
							tInputs[0] = null;
						}
						this.updateSlots();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean doesCrafterHave9SlotInput(){
		GT_MetaTileEntity_Hatch_InputBus craftingInput = null;
		if (!this.mInputBusses.isEmpty()){
			for (GT_MetaTileEntity_Hatch_InputBus x : this.mInputBusses){
				if (x.mInventory.length == 9){
					craftingInput = x;
				}
			}
		}
		//Return if no input hatch set.
		if (craftingInput == null){
			Logger.WARNING("Cannot do Auto-Crafting without a 9-slot Input Bus [MV].");
			return false;

		}
		else {
			return true;
		}
	}

	private boolean doCrafting(ItemStack aStack){
		try{
			//Set Crafting input hatch
			if (!doesCrafterHave9SlotInput()){
				return false;
			}

			//Read stored data from encrypted data stick.
			ItemStack storedData_Output[] = NBTUtils.readItemsFromNBT(aStack, "Output");
			ItemStack storedData_Input[] = NBTUtils.readItemsFromNBT(aStack);
			if (storedData_Output != null && storedData_Input != null){
				ItemStack loadedData[] = new ItemStack[9];
				if (storedData_Input.length >= 1){
					int number = 0;
					for (ItemStack a : storedData_Input){
						if (a.getItem() == ModItems.ZZZ_Empty){
							//Utils.LOG_WARNING("Allocating free memory into crafting manager slot "+number+".");
							loadedData[number] = null;
						}
						else {
							//Utils.LOG_WARNING("Downloading "+a.getDisplayName()+" into crafting manager slot "+number+".");
							loadedData[number] = a;
						}
						number++;
					}
				}



				//Remove inputs here
				ArrayList<ItemStack> mInputArray = new ArrayList<ItemStack>();
				ItemStack allInputs[];

				for (GT_MetaTileEntity_Hatch_InputBus x : this.mInputBusses){
					if (x.mInventory.length > 0){
						for (ItemStack r : x.mInventory){	
							if (r != null){
								mInputArray.add(r);
							}
						}
					}
				}

				if (mInputArray.isEmpty()){
					return false;
				}
				else {
					List<ItemStack> list = mInputArray;			
					allInputs = list.toArray(new ItemStack[list.size()]);

					if (allInputs != null && allInputs.length > 0){

						this.mEUt = 8 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
						this.mMaxProgresstime = MathUtils.roundToClosestInt((50-(5*MathUtils.randDouble(((this.mTier-2) <= 0 ? 1 : (this.mTier-2)), this.mTier))));

						Logger.WARNING("MPT: "+mMaxProgresstime+" | "+mEUt);
						this.getBaseMetaTileEntity().setActive(true);

						//Setup some vars
						int counter=0;

						ItemStack toUse[] = new ItemStack[9];

						outerloop:
							for (ItemStack inputItem : loadedData){
								if (inputItem == null){
									toUse[counter] = null;
									continue outerloop;
								}
								for (ItemStack r : allInputs){
									if (r != null){
										//Utils.LOG_WARNING("Input Bus Inventory Iteration - Found:" +r.getDisplayName()+" | "+allInputs.length);		
										if (GT_Utility.areStacksEqual(r, inputItem)){
											if (this.getBaseMetaTileEntity().isServerSide()){							
												toUse[counter] = inputItem;
												counter++;
												continue outerloop;
											}

										}
									}
								}
								counter++;
							}					

						int mCorrectInputs=0;
						for (ItemStack isValid : toUse){
							if (isValid == null || this.depleteInput(isValid)){
								mCorrectInputs++;
							}
							else {
								Logger.WARNING("Input in Slot "+mCorrectInputs+" was not valid.");
							}
						}


						if (this.mTier > 5) {
							this.mMaxProgresstime >>= this.mTier - 5;
						}			

						if (mCorrectInputs == 9){
							ItemStack mOutputItem = storedData_Output[0];				
							NBTUtils.writeItemsToGtCraftingComponents(mOutputItem, loadedData, true);
							this.addOutput(mOutputItem);
							this.updateSlots();
							return true;
						}
						else { 
							return false;
						}


					}
				}							
			}
		}
		//End Debug
		catch (Throwable t){
			t.printStackTrace();
			this.mMaxProgresstime = 0;
		}


		this.mMaxProgresstime = 0;
		return false;
	}


	@Override
	public String[] getInfoData() {

		final String tRunning = (this.mMaxProgresstime>0 ? "Auto-Crafter running":"Auto-Crafter stopped");
		final String tMaintainance = (this.getIdealStatus() == this.getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance");
		String tSpecialText = ""+(60 + 12 * this.mTier)+"% chance to recover disassembled parts.";
		String tMode;
		if (mMachineMode == MODE.DISASSEMBLY){
			tMode = "§cDisassembly";	
			tSpecialText = ""+(60 + 12 * this.mTier)+"% chance to recover disassembled parts.";
		}
		else if (mMachineMode == MODE.ASSEMBLY){
			tMode = "§aAssembly";
			if (mLastRecipeToBuffer != null && mLastRecipeToBuffer.mOutputs[0].getDisplayName() != null){
				tSpecialText = "Currently processing: "+mLastRecipeToBuffer.mOutputs[0].getDisplayName();				
			}
			else{
				tSpecialText = "Currently processing: Nothing";
			}
		}
		else {	
			tMode = "§dAuto-Crafting";
			tSpecialText = "Does Auto-Crafter have 9-slot input bus? "+doesCrafterHave9SlotInput();		
		}

		return new String[]{
				"Large Scale Auto-Asesembler v1.01c",
				tRunning,
				tMaintainance,
				"Mode: "+tMode,
				tSpecialText};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}


	//else if (mMachineMode == MODE.ASEEMBLY){

	private String getMode(){
		return this.mMachineMode.name();
	}


	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		String mMode = getMode();
		aNBT.setString("mMode", mMode);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		String modeString = aNBT.getString("mMode");
		MODE newMode = MODE.valueOf(modeString);
		this.mMachineMode = newMode;
		super.loadNBTData(aNBT);
	}

	@Override
	public void explodeMultiblock() {
		AC_Helper_Utils.removeCrafter(this);
		super.explodeMultiblock();
	}

	@Override
	public void onExplosion() {
		AC_Helper_Utils.removeCrafter(this);
		super.onExplosion();
	}

	@Override
	public void onRemoval() {
		AC_Helper_Utils.removeCrafter(this);
		super.onRemoval();
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		AC_Helper_Utils.removeCrafter(this);
		super.doExplosion(aExplosionPower);
	}

}
