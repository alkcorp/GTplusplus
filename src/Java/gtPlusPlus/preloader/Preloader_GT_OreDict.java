package gtPlusPlus.preloader;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

import net.minecraft.item.ItemStack;

import gregtech.common.items.GT_MetaGenerated_Item_01;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class Preloader_GT_OreDict {

	public static boolean shouldPreventRegistration(final String string, final ItemStack bannedItem) {

		if (bannedItem == null) {
			return false;
		}
		else if (!CORE_Preloader.enableOldGTcircuits && !LoadedMods.Mekanism){
			return false;
		}

		try {
			if (CORE_Preloader.enableOldGTcircuits){
				if ((bannedItem != null) && ItemUtils.getModId(bannedItem).toLowerCase().equals("gregtech")){
					final int damageValue = bannedItem.getItemDamage() - 32000;
					if (bannedItem.getItem() instanceof GT_MetaGenerated_Item_01) { // 700-720
						if ((damageValue >= 700) && (damageValue <= 720)) {
							return true;
						}
					}
					else {
							if (ReflectionUtils.doesClassExist("gregtech.common.items.GT_MetaGenerated_Item_03")) { // 6/11/12/14/16/20/30-57/69-73/79-96
								final Class<?> MetaItem03 = ReflectionUtils.getClass("gregtech.common.items.GT_MetaGenerated_Item_03");
								if (isInstanceOf(MetaItem03, bannedItem.getItem())) {
									if ((damageValue == 6) || (damageValue == 7) || (damageValue == 11) || (damageValue == 12) || (damageValue == 14)
											|| (damageValue == 16) || (damageValue == 20) || (damageValue == 21) || (damageValue == 22)) {
										return true;
									}
									else if ((damageValue >= 30) && (damageValue <= 57)) {
										return true;
									}
									else if ((damageValue >= 69) && (damageValue <= 73)) {
										return true;
									}
									else if ((damageValue >= 78) && (damageValue <= 96)) {
										return true;
									}
								}
							}
					}
				}
			}

			//Mekanism Support - Let's not make Mek Osmium useful in GT anymore.
			if ((((bannedItem != null) && !LoadedMods.RedTech && (ItemUtils.getModId(bannedItem).toLowerCase().equals("mekanism"))) || (LoadedMods.Mekanism)) && !LoadedMods.RedTech){
				//Circuits
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemControlCircuit")) {
					final Class<?> MekCircuit = ReflectionUtils.getClass("mekanism.common.item.ItemControlCircuit");
					if (isInstanceOf(MekCircuit, bannedItem.getItem())) {
						for (int r=0;r<4;r++){
							if (bannedItem.getItemDamage() == r){
								FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
								return true;
							}
						}
					}
				}
				//Ingots
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemIngot")) {
					final Class<?> MekIngot = ReflectionUtils.getClass("mekanism.common.item.ItemIngot");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 1){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
							return true;
						}
					}
				}
				//Dirty Dust
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemDirtyDust")) {
					final Class<?> MekIngot = ReflectionUtils.getClass("mekanism.common.item.ItemDirtyDust");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
							return true;
						}
					}
				}
				//Dust
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemDust")) {
					final Class<?> MekIngot = ReflectionUtils.getClass("mekanism.common.item.ItemDust");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
							return true;
						}
					}
				}
				//Crystal
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemCrystal")) {
					final Class<?> MekIngot = ReflectionUtils.getClass("mekanism.common.item.ItemCrystal");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
							return true;
						}
					}
				}
				//Shard
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemShard")) {
					final Class<?> MekIngot = ReflectionUtils.getClass("mekanism.common.item.ItemShard");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
							return true;
						}
					}
				}
				//Clump
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemClump")) {
					final Class<?> MekIngot = ReflectionUtils.getClass("mekanism.common.item.ItemClump");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
							return true;
						}
					}
				}
				//Ores
				if (ReflectionUtils.doesClassExist("mekanism.common.item.ItemBlockOre")) {
					final Class<?> MekOre = ReflectionUtils.getClass("mekanism.common.item.ItemBlockOre");
					if (isInstanceOf(MekOre, bannedItem.getItem()) || (bannedItem == ItemUtils.simpleMetaStack("Mekanism:OreBlock", 0, 1))) {
						if (bannedItem.getItemDamage() == 0){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing %s from the OreDictionary to balance Mekanism.", bannedItem.getDisplayName());
							return true;
						}
					}
				}
			}

		} catch (final Throwable e) {
			if (CORE.ConfigSwitches.showHiddenNEIItems) {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "A mod tried to register an invalid item with the OreDictionary.");
			if (bannedItem != null){
				FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Please report this issue to the authors of %s", ItemUtils.getModId(bannedItem));
				try {
					if (bannedItem.getItemDamage() <= Short.MAX_VALUE-1) {
						FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Item was not null, but still invalidly registering: %s", bannedItem.getDisplayName() != null ? bannedItem.getDisplayName() : "INVALID ITEM FOUND");
					}
					else {
						FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Item was not null, but still invalidly registering: %s", "Found Wildcard item that is being registered too early.");
					}
				}
				catch (Exception h) {
					h.printStackTrace();
				}
			}
			}
			//FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "%s", e.getMessage());
		}
		return false;
	}

	// Simplification of Life.
	private static boolean isInstanceOf(final Class<?> clazz, final Object obj) {
		return clazz.isInstance(obj);
	}

}
