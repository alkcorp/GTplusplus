package GTplusplus.proxy;

import GTplusplus.GTppTextures;
import GTplusplus.block.GTppMetaBlocks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;


@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public void preInit() {
        super.preInit();
        new GTppTextures();
    }

    public void postInit() {

    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    	GTppMetaBlocks.registerItemModels();
    }
}
