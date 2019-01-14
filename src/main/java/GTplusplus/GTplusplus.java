package GTplusplus;

import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.machines.GTppTileEntities;
import GTplusplus.materials.Elements;
import GTplusplus.materials.GTppMaterials;
import GTplusplus.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = GTplusplus.MODID,
        name = GTplusplus.NAME,
        version = GTplusplus.VERSION,
        dependencies = "required-after:gregtech"
)
public class GTplusplus {
    public static final String MODID = "gtplusplus";
    public static final String NAME = "GT++";
    public static final String VERSION = "@VERSION@";

    @SidedProxy(
            modId = MODID,
            clientSide = "GTplusplus.proxy.ClientProxy",
            serverSide = "GTplusplus.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    public static Logger logger;

    public GTplusplus() {
        Elements.preInit();
        new GTppMaterials();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        GTppMetaBlocks.init();
        GTppTileEntities.init();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
