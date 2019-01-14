package GTplusplus.jei;

import java.util.List;

import com.google.common.collect.Lists;

import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.block.GTppMultiblockCasing;
import GTplusplus.machines.GTppTileEntities;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

public class IndustrialSifterInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GTppTileEntities.INDUSTRIAL_SIFTER;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        MultiblockShapeInfo shapeInfo = MultiblockShapeInfo.builder()
    		.aisle("BYYYY", "YYYYY", "YYYYY")
            .aisle("YYYYY", "YYYYY", "YGGGY")
            .aisle("SYYYE", "YYYYY", "YGGGY")
            .aisle("YYYYY", "YYYYY", "YGGGY")
            .aisle("IYYYY", "YYYYY", "YYYYY")
            .where('S', GTppTileEntities.INDUSTRIAL_SIFTER, EnumFacing.WEST)
            .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.HV], EnumFacing.WEST)
            .where('B', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.HV], EnumFacing.WEST)
            .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.HV], EnumFacing.EAST)
            .where('Y', GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.SIEVE_CASING))
            .where('G', GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.SIEVE_GRATE))
            .build();
        return Lists.newArrayList(shapeInfo);
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gregtech.multiblock.industrial_sifter.description")};
    }

}
