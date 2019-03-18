package gtPlusPlus.core.item.init;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.foods.BaseItemFood;
import gtPlusPlus.core.item.base.foods.BaseItemHotFood;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class ItemsFoods {

	public static void load(){
		run();
	}

	private static void run(){

		//Raisin Bread
		ModItems.itemIngotRaisinBread = new BaseItemFood("itemIngotRaisinBread", "Raisin Bread", 3, 1.5f, false, new PotionEffect(Potion.weakness.id, 40, 1)).setAlwaysEdible();
		GT_OreDictUnificator.registerOre("foodRaisinBread", ItemUtils.getItemStackFromFQRN(CORE.MODID+":itemIngotRaisinBread", 1));
		//Hot Raisin Bread
		ModItems.itemHotIngotRaisinBread = new BaseItemHotFood("itemHotIngotRaisinBread", 1, 0.5f, "Raisin Bread", 120, ModItems.itemIngotRaisinBread);
		GT_OreDictUnificator.registerOre("foodHotRaisinBread", ItemUtils.getItemStackFromFQRN(CORE.MODID+":itemHotIngotRaisinBread", 1));

		//Raisin Bread
		ModItems.itemFoodRaisinToast = new BaseItemFood("itemFoodRaisinToast", "Raisin Toast", 1, 0.5f, false).setAlwaysEdible();
		GT_OreDictUnificator.registerOre("foodRaisinToast", ItemUtils.getItemStackFromFQRN(CORE.MODID+":itemFoodRaisinToast", 1));
		//Hot Raisin Bread
		ModItems.itemHotFoodRaisinToast = new BaseItemHotFood("itemHotFoodRaisinToast", 1, 0.5f, "Raisin Toast", 20, ModItems.itemFoodRaisinToast);
		GT_OreDictUnificator.registerOre("foodHotRaisinToast", ItemUtils.getItemStackFromFQRN(CORE.MODID+":itemHotFoodRaisinToast", 1));

		//Raisin Bread
		ModItems.itemFoodCurriedSausages = new BaseItemFood("itemFoodCurriedSausages", "Curried Sausages", 5, 2f, false);
		GT_OreDictUnificator.registerOre("foodCurriedSausages", ItemUtils.getItemStackFromFQRN(CORE.MODID+":itemFoodCurriedSausages", 1));
		//Hot Raisin Bread
		ModItems.itemHotFoodCurriedSausages = new BaseItemHotFood("itemHotFoodCurriedSausages", 1, 0.5f, "Curried Sausages", 240, ModItems.itemFoodCurriedSausages);
		GT_OreDictUnificator.registerOre("foodHotCurriedSausages", ItemUtils.getItemStackFromFQRN(CORE.MODID+":itemHotFoodCurriedSausages", 1));

	}

}
