package GTplusplus.machines;

import GTplusplus.GTppConfig;
import GTplusplus.recipes.GTppRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.render.Textures;

public class GTppTileEntities {
    public static SimpleMachineMetaTileEntity[] DISASSEMBLER = new SimpleMachineMetaTileEntity[8];

    public static MetaTileEntityIndustrialMacerator INDUSTRIAL_MACERATOR;
    public static MetaTileEntityIndustrialThermalCentrifuge INDUSTRIAL_THERMALCENTRIFUGE;
    public static MetaTileEntityIndustrialOreWasher INDUSTRIAL_OREWASHER;
    public static MetaTileEntityIndustrialCentrifuge INDUSTRIAL_CENTRIFUGE;
    public static MetaTileEntityIndustrialElectrolyzer INDUSTRIAL_ELECTROLYZER;
    public static MetaTileEntityIndustrialChemicalReactor INDUSTRIAL_CHEMICALREACTOR;
    public static MetaTileEntityIndustrialFisher INDUSTRIAL_FISHER;
    public static MetaTileEntityIndustrialWiremill INDUSTRIAL_WIREMILL;
    public static MetaTileEntityIndustrialSifter INDUSTRIAL_SIFTER;
    public static MetaTileEntityImprovedBlastFurnace BLAST_FURNACE;

    public static void init() {
    	DISASSEMBLER[0] = GregTechAPI.registerMetaTileEntity(3000, new SimpleMachineMetaTileEntity("disassembler.lv", GTppRecipeMaps.DISASSEMBLER_LV, Textures.ASSEMBLER_OVERLAY, 1));
    	DISASSEMBLER[1] = GregTechAPI.registerMetaTileEntity(3001, new SimpleMachineMetaTileEntity("disassembler.mv", GTppRecipeMaps.DISASSEMBLER_MV, Textures.ASSEMBLER_OVERLAY, 2));
    	DISASSEMBLER[2] = GregTechAPI.registerMetaTileEntity(3002, new SimpleMachineMetaTileEntity("disassembler.hv", GTppRecipeMaps.DISASSEMBLER_HV, Textures.ASSEMBLER_OVERLAY, 3));
    	DISASSEMBLER[3] = GregTechAPI.registerMetaTileEntity(3003, new SimpleMachineMetaTileEntity("disassembler.ev", GTppRecipeMaps.DISASSEMBLER_EV, Textures.ASSEMBLER_OVERLAY, 4));
    	DISASSEMBLER[4] = GregTechAPI.registerMetaTileEntity(3004, new SimpleMachineMetaTileEntity("disassembler.iv", GTppRecipeMaps.DISASSEMBLER_IV, Textures.ASSEMBLER_OVERLAY, 5));
    	
    	DISASSEMBLER[5] = GregTechAPI.registerMetaTileEntity(3005, new SimpleMachineMetaTileEntity("disassembler.luv", GTppRecipeMaps.DISASSEMBLER_IV, Textures.ASSEMBLER_OVERLAY, 6));
    	DISASSEMBLER[6] = GregTechAPI.registerMetaTileEntity(3006, new SimpleMachineMetaTileEntity("disassembler.zpm", GTppRecipeMaps.DISASSEMBLER_IV, Textures.ASSEMBLER_OVERLAY, 7));
    	DISASSEMBLER[7] = GregTechAPI.registerMetaTileEntity(3007, new SimpleMachineMetaTileEntity("disassembler.uv", GTppRecipeMaps.DISASSEMBLER_IV, Textures.ASSEMBLER_OVERLAY, 8));
    	
        if (GTppConfig.multiblocks.Macerator) {
        	INDUSTRIAL_MACERATOR = GregTechAPI.registerMetaTileEntity(3008, new MetaTileEntityIndustrialMacerator("industrial_macerator"));
        }
        
        if (GTppConfig.multiblocks.Thermal) {
        	INDUSTRIAL_THERMALCENTRIFUGE = GregTechAPI.registerMetaTileEntity(3009, new MetaTileEntityIndustrialThermalCentrifuge("industrial_thermalcentrifuge"));
        }
        
        if (GTppConfig.multiblocks.Orewash) {
        	INDUSTRIAL_OREWASHER = GregTechAPI.registerMetaTileEntity(3010, new MetaTileEntityIndustrialOreWasher("industrial_orewasher"));
        }
        
        if (GTppConfig.multiblocks.Electrolyzer) {
        	INDUSTRIAL_ELECTROLYZER = GregTechAPI.registerMetaTileEntity(3011, new MetaTileEntityIndustrialElectrolyzer("industrial_electrolyzer"));
        }
        
        if (GTppConfig.multiblocks.Centrifuge) {
        	INDUSTRIAL_CENTRIFUGE = GregTechAPI.registerMetaTileEntity(3012, new MetaTileEntityIndustrialCentrifuge("industrial_centrifuge"));
        }
        
        if (GTppConfig.multiblocks.Reactor) {
        	INDUSTRIAL_CHEMICALREACTOR = GregTechAPI.registerMetaTileEntity(3013, new MetaTileEntityIndustrialChemicalReactor("industrial_chemicalreactor"));
        }
        
        if (GTppConfig.multiblocks.Fisher) {
        	INDUSTRIAL_FISHER = GregTechAPI.registerMetaTileEntity(3014, new MetaTileEntityIndustrialFisher("industrial_fisher"));
        }
        
        if(GTppConfig.multiblocks.EBF) {
        	BLAST_FURNACE = GregTechAPI.registerMetaTileEntity(3015, new MetaTileEntityImprovedBlastFurnace("improved_blast_furnace"));
        }
        
        if(GTppConfig.multiblocks.Wiremill) {
        	INDUSTRIAL_WIREMILL = GregTechAPI.registerMetaTileEntity(3016, new MetaTileEntityIndustrialWiremill("industrial_wiremill"));
        }
        
        if(GTppConfig.multiblocks.Wiremill) {
        	INDUSTRIAL_SIFTER = GregTechAPI.registerMetaTileEntity(3017, new MetaTileEntityIndustrialSifter("industrial_sifter"));
        }
    }
}
