package GTplusplus.proxy;

import GTplusplus.GTplusplus;
import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.recipes.GTppMachineRecipes;
import gregtech.common.blocks.VariantItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = GTplusplus.MODID)
public class CommonProxy {

    public void preInit() {

    }

    public void postInit() {

    }


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    	IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(GTppMetaBlocks.MULTIBLOCK_CASING);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(createItemBlock(GTppMetaBlocks.MULTIBLOCK_CASING, VariantItemBlock::new));
    }

    private static <T extends Block> ItemBlock createItemBlock(T block, Function<T, ItemBlock> producer) {
        ItemBlock itemBlock = producer.apply(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return itemBlock;
    }

    @SubscribeEvent()
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        GTppMachineRecipes.init();
    }
}
