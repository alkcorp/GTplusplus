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

public class IndustrialThermalCentrifugeInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GTppTileEntities.INDUSTRIAL_THERMALCENTRIFUGE;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        MultiblockShapeInfo shapeInfo = MultiblockShapeInfo.builder()
        	.aisle("XEX", "XXX")
            .aisle("XXX", "XXX")
            .aisle("XCX", "IXB")
            .where('C', GTppTileEntities.INDUSTRIAL_THERMALCENTRIFUGE, EnumFacing.SOUTH)
            .where('X', GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.THERMAL_CASING))
            .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.HV], EnumFacing.SOUTH)
            .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.HV], EnumFacing.NORTH)
            .where('B', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.HV], EnumFacing.SOUTH)
            .build();
        return Lists.newArrayList(shapeInfo);
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gregtech.multiblock.industrial_thermalcentrifuge.description")};
    }

}