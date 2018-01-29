package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import gtPlusPlus.api.interfaces.ITileTooltip;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBasicTile extends ItemBlock{

	private final int mID;
	
	public ItemBlockBasicTile(final Block block) {
		super(block);
		this.mID = ((ITileTooltip) block).getTooltipID();
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.mID == 0){ //Fishtrap
			list.add("This trap catches fish faster if surrounded by more water blocks.");			
		}
		else if (this.mID == 1){ //Modularity
			list.add("Used to construct modular armour & bauble upgrades..");			
		}
		else if (this.mID == 2){ //Trade
			list.add("Allows for SMP trade-o-mat type trading.");			
		}
		else if (this.mID == 3){ //Project
			list.add("Scan any crafting recipe in this to mass fabricate them in the Autocrafter..");			
		}
		//super.addInformation(stack, aPlayer, list, bool);
	}
	

}
