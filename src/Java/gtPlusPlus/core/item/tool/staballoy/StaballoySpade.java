package gtPlusPlus.core.item.tool.staballoy;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.MiningUtils;

public class StaballoySpade extends ItemSpade{

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(final ItemStack stack) {
		if (super.getDurabilityForDisplay(stack) > 0){
			return super.getDurabilityForDisplay(stack);}
		return 0;
	}


	/**
	 * Creates an NBT tag for this item if it doesn't have one.
	 * This also set some default values.
	 * @param rStack
	 * @return
	 */
	
	private static boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		
		tagNBT.setBoolean("FACING_HORIZONTAL", true);
		tagNBT.setString("FACING", "north");		
		tagNBT.setString("lookingDirection", "");
		
		tagMain.setTag("PickStats", tagNBT);		
		rStack.setTagCompound(tagMain);		
		return true;
	}
	
	/*
	 * Is the player facing horizontally?
	 */
	
	public static final boolean isFacingHorizontal(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("PickStats");
			if (aNBT != null) {
				return aNBT.getBoolean("FACING_HORIZONTAL");
			}
		}
		else {
		createNBT(aStack);
		}
		return true;
	}

	public static final boolean setFacingHorizontal(final ItemStack aStack, final boolean aFacingHorizontal) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("PickStats");
			if (aNBT != null) {
				aNBT.setBoolean("FACING_HORIZONTAL", aFacingHorizontal);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Handles the Direction the player is facing
	 */
	
	public static final String getFacingDirection(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("PickStats");
			if (aNBT != null) {
				return aNBT.getString("FACING");
			}
		}
		else {
		createNBT(aStack);
		}
		return "north";
	}

	public static final boolean setFacingDirection(final ItemStack aStack, final String aFacingHorizontal) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("PickStats");
			if (aNBT != null) {
				aNBT.setString("FACING", aFacingHorizontal);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * The Looking Direction handlers
	 */
	
	public static final String getLookingDirection(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("PickStats");
			if (aNBT != null) {
				return aNBT.getString("lookingDirection");
			}
		}
		else {
		createNBT(aStack);
		}
		return "";
	}

	public static final boolean setLookingDirection(final ItemStack aStack, final String aFacingHorizontal) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("PickStats");
			if (aNBT != null) {
				aNBT.setString("lookingDirection", aFacingHorizontal);
				return true;
			}
		}
		return false;
	}
	
	private int miningLevel;

	/*
	 *
	 *
	 *
	 *  Methods
	 *
	 *
	 *
	 */

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer aPlayer) {
		return super.onItemRightClick(stack, world, aPlayer);
	}



	@Override
	public boolean onBlockDestroyed(final ItemStack stack, final World world, final Block block, final int X, final int Y, final int Z, final EntityLivingBase entity) {
		if (!world.isRemote){
			if (entity instanceof EntityPlayer) {
				this.GetDestroyOrientation((EntityPlayer) entity, getLookingDirection(stack), world, X, Y, Z, stack);				
			}
		}
		return super.onBlockDestroyed(stack, world, block, X, Y, Z, entity);
	}

	public Boolean canPickaxeBlock(final Block currentBlock, final World currentWorld, final int[] xyz){
		String correctTool = "";
		if (!currentWorld.isRemote){
			try {
				correctTool = currentBlock.getHarvestTool(0);
				//Utils.LOG_WARNING(correctTool);

				Logger.WARNING("Tool for Block: "+correctTool+" | Current block: "+currentBlock.getLocalizedName());
				if (MiningUtils.getBlockType(currentBlock, currentWorld, xyz, this.miningLevel) || correctTool.equals("shovel")){
					return true;}
			} catch (final NullPointerException e){
				return false;}
		}
		return false;
	}

	private void GetDestroyOrientation(EntityPlayer player, final String FACING, final World world, final int X, final int Y, final int Z, final ItemStack heldItem){
		float DURABILITY_LOSS = 0;
		if (!world.isRemote){

			if (FACING.equals("below") || FACING.equals("above")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						DURABILITY_LOSS = (DURABILITY_LOSS + this.removeBlockAndDropAsItem(world, X + i, Y, Z + j, heldItem));
					}
				}
			}

			else if (FACING.equals("facingEast") || FACING.equals("facingWest")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						DURABILITY_LOSS = (DURABILITY_LOSS + this.removeBlockAndDropAsItem(world, X , Y + i, Z + j, heldItem));
					}
				}
			}

			else if (FACING.equals("facingNorth") || FACING.equals("facingSouth")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						DURABILITY_LOSS = (DURABILITY_LOSS + this.removeBlockAndDropAsItem(world, X + j, Y + i, Z, heldItem));
					}
				}
			}

			//int heldItemDurability = heldItem.getDamage(1);
			Logger.WARNING("Total Loss: "+(int)DURABILITY_LOSS);
			//heldItem.setDamage(heldStack, DURABILITY_LOSS);
			//Utils.LOG_WARNING("|GID|Durability: "+heldItem.getItemDamage());
			//Utils.LOG_WARNING("Durability: "+heldStack.getDamage(heldStack));
			Logger.WARNING("1x: "+(heldItem.getItemDamage()));
			final int itemdmg = heldItem.getItemDamage();
			final int maxdmg = heldItem.getMaxDamage();
			final int dodmg = (int)DURABILITY_LOSS;
			final int durNow = maxdmg-itemdmg;
			final int durLeft = (int) ((maxdmg-itemdmg)-DURABILITY_LOSS);

			Logger.WARNING(
					"Current Damage: " + itemdmg
					+ " Max Damage: " + maxdmg
					+ " Durability to be lost: " + dodmg
					+ " Current Durability: " + durNow
					+ " Remaining Durability: " + durLeft
					);


			//Break Tool
			if (((durNow-dodmg) <= (900)) && (itemdmg != 0)){
				//TODO break tool
				Logger.WARNING("Breaking Tool");
				heldItem.stackSize = 0;
			}
			//Do Damage
			else {
				//setItemDamage(heldItem, durLeft);
				Logger.WARNING(""+(durNow-durLeft));
				this.damageItem(heldItem, (durNow-durLeft)-1, player);
			}
			DURABILITY_LOSS = 0;

		}
	}

	public void damageItem(final ItemStack item, final int damage, final EntityPlayer localPlayer){
		item.damageItem(damage, localPlayer);
	}

	public void setItemDamage(final ItemStack item, final int damage){
		item.setItemDamage(damage-1);
	}

	//Should clear up blocks quicker if I chain it.
	public int removeBlockAndDropAsItem(final World world, final int X, final int Y, final int Z, final ItemStack heldItem){
		Logger.WARNING("Trying to drop/remove a block.");
		try {
			final Block block = world.getBlock(X, Y, Z);
			Logger.WARNING(block.toString());
			String removalTool = "";
			removalTool = block.getHarvestTool(0);
			if (removalTool != null){
				if (removalTool.equals("shovel")){
					if (this.canPickaxeBlock(block, world, new int[]{X,Y,Z})){
						if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){

							final int itemdmg = heldItem.getItemDamage();
							final int maxdmg = heldItem.getMaxDamage();
							final int dodmg = 100;
							final int durNow = maxdmg-itemdmg;
							final int durLeft = (maxdmg-itemdmg)-100;

							if (((durNow-dodmg) <= (900)) && (itemdmg != 0)){
								//Do Nothing, Tool is useless.
								return 0;
							}
							block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
							world.setBlockToAir(X, Y, Z);
							Logger.WARNING("Adding 100 damage to item.");
							return 100;
						}
						Logger.WARNING("Incorrect Tool for mining this block. Wrong Block Water/lava/bedrock/blacklist");
						return 0;
					}
					Logger.WARNING("Incorrect Tool for mining this block. Cannot Shovel this block type.");
					return 0;
				}
				Logger.WARNING("Incorrect Tool for mining this block. Blocks mining tool is now Shovel.");
				return 0;
			}
			Logger.WARNING("Either the block was air or it declares an invalid mining tool.");
			return 0;
		} catch (final NullPointerException e){
			Logger.WARNING("Something Broke");
			e.printStackTrace();
			return 0;
		}
	}

	public boolean checkFacing(final ItemStack aStack, final EntityPlayer aPlayer, final World world){
		if (aPlayer != null){
			final int direction = MathHelper.floor_double((aPlayer.rotationYaw * 4F) / 360F + 0.5D) & 3;
			//Utils.LOG_WARNING("Player - F: "+direction);
			//Utils.LOG_WARNING("Player - getLookVec(): "+localPlayer.getLookVec().yCoord);

			/*if (localPlayer.getLookVec().yCoord > 0){
				localPlayer.getLookVec().yCoord;
			}*/

			final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, aPlayer, false);
			if (movingobjectposition != null){
				final int sideHit = movingobjectposition.sideHit;
				String playerStandingPosition = "";
				if (movingobjectposition != null) {
					//System.out.println("Side Hit: "+movingobjectposition.sideHit);
				}

				if (sideHit == 0){
					playerStandingPosition = "above";
					setFacingHorizontal(aStack, false);
				}
				else if (sideHit == 1){
					playerStandingPosition = "below";
					setFacingHorizontal(aStack, false);
				}
				else if (sideHit == 2){
					playerStandingPosition = "facingSouth";
					setFacingHorizontal(aStack, true);
				}
				else if (sideHit == 3){
					playerStandingPosition = "facingNorth";
					setFacingHorizontal(aStack, true);
				}
				else if (sideHit == 4){
					playerStandingPosition = "facingEast";
					setFacingHorizontal(aStack, true);
				}
				else if (sideHit == 5){
					playerStandingPosition = "facingWest";
					setFacingHorizontal(aStack, true);
				}
				setLookingDirection(aStack, playerStandingPosition);

				if (direction == 0){
					setFacingDirection(aStack, "south");
				}
				else if (direction == 1){
					setFacingDirection(aStack, "west");
				}
				else if (direction == 2){
					setFacingDirection(aStack, "north");
				}
				else if (direction == 3){
					setFacingDirection(aStack, "east");
				}
			}


			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GOLD+"Spades a 3x3 area in the direction you are facing.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return EnumRarity.rare;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		return true;
	}


	@Override
	public boolean onBlockStartBreak(final ItemStack itemstack, final int X, final int Y, final int Z, final EntityPlayer aPlayer) {
		this.checkFacing(itemstack, aPlayer, aPlayer.worldObj);
		return super.onBlockStartBreak(itemstack, X, Y, Z, aPlayer);
	}
	public StaballoySpade(final String unlocalizedName, final ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setMaxDamage(3200);
	}
}
