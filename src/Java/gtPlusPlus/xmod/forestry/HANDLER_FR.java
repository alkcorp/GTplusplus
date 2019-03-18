package gtPlusPlus.xmod.forestry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cpw.mods.fml.common.Optional;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;

public class HANDLER_FR {

	public static void preInit(){
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
			//new GTPP_Bees(); TODO- Will Investigate this properly later.
		}
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
			FR_Gregtech_Recipes.registerItems();
		}
	}

	public static boolean createBlockBreakParticles(final World world, final int x, final int y, final int z, final Block block){
		if (LoadedMods.Forestry){
			createBlockBreakParticles_INTERNAL(world, x, y, z, block);
		}
		return false;
	}

	@Optional.Method(modid = "Forestry")
	private static void createBlockBreakParticles_INTERNAL(final World world, final int x, final int y, final int z, final Block block){
		if (LoadedMods.Forestry){
			Class oClass;
			try {
				oClass = ReflectionUtils.getClass("forestry.core.proxy.ProxyCommon");
				Object oProxy = ReflectionUtils.getField(oClass, "common");				
				if (oProxy != null && oClass.isInstance(oProxy)){					
					Method mParticles = ReflectionUtils.getMethod(oClass, "addBlockDestroyEffects", World.class, int.class, int.class, int.class, Block.class, int.class);					
					mParticles.invoke(oProxy, world, x, y, z, block, 0);				
				}
			}
			catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}		
	}


}
