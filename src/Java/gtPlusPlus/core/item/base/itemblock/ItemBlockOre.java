package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.entity.EntityUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockOre extends ItemBlock{

	private final BlockBaseOre mThisOre;
	private final Material mThisMaterial;
	private final int mThisRadiation;
	private final int mThisColour;

	public ItemBlockOre(final Block block) {
		super(block);
		if (block instanceof BlockBaseOre){
			this.mThisOre = (BlockBaseOre) block;
			this.mThisMaterial = this.mThisOre.getMaterialEx();
			this.mThisRadiation = this.mThisMaterial.vRadiationLevel;
			this.mThisColour = this.mThisMaterial.getRgbAsHex();
		}
		else {
			this.mThisOre = null;
			this.mThisMaterial = null;
			this.mThisRadiation = 0;
			this.mThisColour = Utils.rgbtoHexValue(255, 255, 255);
		}	
	}

	public int getRenderColor(final int aMeta) {
		return this.mThisColour;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.mThisMaterial != null){
			list.add(this.mThisMaterial.vChemicalFormula);			
		}	

		//Radioactive?
		if (this.mThisRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}

		/**
		 * Tooltip Handler for Ores
		 */		
		if (this.mThisMaterial == FLUORIDES.FLUORITE){
			list.add("Mined from Sandstone with a 1/"+CORE.ConfigSwitches.chanceToDropFluoriteOre+" chance, or Limestone with a 1/"+(CORE.ConfigSwitches.chanceToDropFluoriteOre*20)+" chance.");			
		}	
		else if (this.mThisMaterial != FLUORIDES.FLUORITE){
			list.add("Mined from the Toxic Everglades.");			
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (this.mThisMaterial != null){
			if (this.mThisRadiation > 0){
				if (entityHolding instanceof EntityPlayer){
					if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode){
						EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.mThisMaterial.vRadiationLevel, world, entityHolding);	
					}
				}
			}
		}
	}

}
