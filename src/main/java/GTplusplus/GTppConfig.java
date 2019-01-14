package GTplusplus;

import net.minecraftforge.common.config.Config;

@Config(modid = GTplusplus.MODID)
public class GTppConfig {

    @Config.Comment("Config options for multiblock features")
    public static Multiblocks multiblocks = new Multiblocks();

    public static class Multiblocks {
        @Config.Comment("Should Electric Blast Furnace be improved(each 2 tier of coils overclock recipes) ?")
        @Config.Name("Improved EBF")
        public boolean EBF = true;
        
        @Config.Comment("Improved multiblock macerator")
        @Config.Name("Industrial Macerator")
        public boolean Macerator = true;
        
        @Config.Comment("Improved multiblock Thermal Centrifuge")
        @Config.Name("Industrial Thermal Centrifuge")
        public boolean Thermal = true;
        
        @Config.Comment("Improved multiblock Ore Washing")
        @Config.Name("Industrial Ore Washing")
        public boolean Orewash = true;
        
        @Config.Comment("Improved multiblock Centrifuge")
        @Config.Name("Industrial Centrifuge")
        public boolean Centrifuge = true;
        
        @Config.Comment("Improved multiblock Sifting")
        @Config.Name("Industrial Sifter")
        public boolean Sifter = true;
        
        @Config.Comment("Improved multiblock Electrolyzer")
        @Config.Name("Industrial Electrolyzer")
        public boolean Electrolyzer = true;
        
        @Config.Comment("Improved multiblock Wiremill")
        @Config.Name("Wire Factory")
        public boolean Wiremill = true;
        
        @Config.Comment("Improved multiblock Chemical Reactor")
        @Config.Name("Industrial Chemical Reactor")
        public boolean Reactor = true;
        
        @Config.Comment("Industrial machine that produces fish")
        @Config.Name("Industrial Fisher")
        public boolean Fisher = true;
    }

    @Config.Comment("Config options for miscellaneous stuff")
    public static Misc misc = new Misc();

    public static class Misc {
        @Config.Comment("Should small gears be harder?")
        @Config.Name("Harder Small Gears")
        public boolean hardSmallGear = true;
        
        @Config.Comment("LV-LuV Superconductors")
        @Config.Name("Superconductors")
        public boolean Superconductors = true;
    }
}

