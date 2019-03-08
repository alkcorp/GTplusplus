package gtPlusPlus.core.fluids;

import java.util.LinkedHashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.objects.GregtechException;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidFactory {

	public static final Map<String, Fluid> mNameToFluidMap = new LinkedHashMap<String, Fluid>();
	public static final Map<String, ItemStack> mNameToBucketMap = new LinkedHashMap<String, ItemStack>();
	public static final Map<String, Block> mNameToBlockMap = new LinkedHashMap<String, Block>();
	public static final Map<String, Integer> mNameToMetaMap = new LinkedHashMap<String, Integer>();

	public static final Map<Fluid, String> mFluidToNameMap = new LinkedHashMap<Fluid, String>();
	public static final Map<Fluid, ItemStack> mFluidToBucketMap = new LinkedHashMap<Fluid, ItemStack>();
	public static final Map<Fluid, Block> mFluidToBlockMap = new LinkedHashMap<Fluid, Block>();
	public static final Map<Fluid, Integer> mFluidToMetaMap = new LinkedHashMap<Fluid, Integer>();

	public static final Map<ItemStack, Fluid> mBucketToFluidMap = new LinkedHashMap<ItemStack, Fluid>();
	public static final Map<ItemStack, String> mBucketToNameMap = new LinkedHashMap<ItemStack, String>();
	public static final Map<ItemStack, Block> mBucketToBlockMap = new LinkedHashMap<ItemStack, Block>();
	public static final Map<ItemStack, Integer> mBucketToMetaMap = new LinkedHashMap<ItemStack, Integer>();

	public static final Map<Block, String> mBlockToNameMap = new LinkedHashMap<Block, String>();
	public static final Map<Block, Fluid> mBlockToFluidMap = new LinkedHashMap<Block, Fluid>();
	public static final Map<Block, ItemStack> mBlockToBucketMap = new LinkedHashMap<Block, ItemStack>();
	public static final Map<Block, Integer> mBlockToMetaMap = new LinkedHashMap<Block, Integer>();

	public static final Map<Integer, String> mMetaToNameMap = new LinkedHashMap<Integer, String>();
	public static final Map<Integer, Fluid> mMetaToFluidMap = new LinkedHashMap<Integer, Fluid>();
	public static final Map<Integer, ItemStack> mMetaToBucketMap = new LinkedHashMap<Integer, ItemStack>();
	public static final Map<Integer, Block> mMetaToBlockMap = new LinkedHashMap<Integer, Block>();


	private static Fluid mErrorFluid;
	private static AutoMap<FluidPackage> mGeneratedFluids = new AutoMap<FluidPackage>();

	public static void preInit() {

	}

	public static void init() {
		GameRegistry.registerItem(new ItemGenericFluidBucket(Blocks.air), "gtpp.bucket.generic");
		for (FluidPackage y : mGeneratedFluids) {
			FluidRegistry.registerFluid(y.get());
			GameRegistry.registerBlock(y.mBlock, "gtpp_" + y.mName);
			FluidContainerRegistry.registerFluidContainer(y.get(), y.mBucket, new ItemStack(Items.bucket));
		}
		Utils.registerEvent(BucketHandler.INSTANCE);
	}

	public static void postInit() {

	}

	public static FluidPackage generate(int aID, String aUnlocalName, int luminosity, int density, int temp,
			int viscosity) {

		FluidPackage aFluidToGenerate = null;

		// Check Load Phase for some Safety, only allow this to be called in Pre-Init.
		if (GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.PRE_INIT) {
			try {
				throw new GregtechException("Cannot generate Fluid Packages outside of Pre-Init!");
			} catch (GregtechException e) {
				System.exit(0);
			}
		}
		
		Fluid aGenFluid = fluid(aUnlocalName, 0, 0, 0, 0);
		Block aGenBlock = block();
		ItemStack aGenBucket = bucket();

		aFluidToGenerate = new FluidPackage(aID, aUnlocalName, aGenFluid, aGenBucket, aGenBlock);

		if (aFluidToGenerate.valid()) {
			FluidRegistry.registerFluid(aFluidToGenerate.get());			
		}
		

		// Handle Bad generation
		if (mErrorFluid == null) {
			mErrorFluid = new Fluid("baderrorfluid.gtpp").setViscosity(4000);
		}
		if (aFluidToGenerate != null) {
			mGeneratedFluids.put(aFluidToGenerate);
		}
		return aFluidToGenerate;
	}
	
	
	
	private static Fluid fluid(String aUnlocalName, int luminosity, int density, int temp,
			int viscosity) {
		return new FactoryFluid(aUnlocalName, 0, 0, 0, 0);
	}
	
	private static ItemStack bucket() {
		return null;
	}
	
	private static Block block() {
		return null;
	}

	/**
	 * Copyright © SpaceToad, 2011 http://www.mod-buildcraft.com BuildCraft is
	 * distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
	 * Please check the contents of the license located in
	 * http://www.mod-buildcraft.com/MMPL-1.0.txt
	 * 
	 * Modified version of the BC BucketHandler, except using ItemStacks > Items
	 * (Why?)
	 * 
	 * @author Alkalus
	 */

	public static class BucketHandler {

		public static BucketHandler INSTANCE = new BucketHandler();

		private BucketHandler() {

		}

		@SubscribeEvent
		public void onBucketFill(FillBucketEvent event) {
			ItemStack result = fillCustomBucket(event.world, event.target);
			if (result == null) {
				return;
			}
			event.result = result;
			event.setResult(Result.ALLOW);
		}

		private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {
			Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
			ItemStack bucket = mBlockToBucketMap.get(block);
			if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
				world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
				return ItemUtils.getSimpleStack(bucket, 1);
			} else {
				return null;
			}
		}

	}

}
