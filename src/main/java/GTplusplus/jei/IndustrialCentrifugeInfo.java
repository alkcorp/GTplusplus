package GTplusplus.jei;

import java.util.List;

import com.google.common.collect.Lists;

import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.block.GTppMultiblockCasing;
import GTplusplus.machines.GTppTileEntities;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.blocks.BlockTurbineCasing.TurbineCasingType;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

public class IndustrialCentrifugeInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GTppTileEntities.INDUSTRIAL_CENTRIFUGE;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        MultiblockShapeInfo shapeInfo = MultiblockShapeInfo.builder()
            .aisle("XXX", "XBX", "XXX")
            .aisle("XXX", "CPE", "XXX")
            .aisle("XXX", "XIX", "XXX")
            .where('C', GTppTileEntities.INDUSTRIAL_CENTRIFUGE, EnumFacing.WEST)
            .where('X', GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.CENTRIFUGE_CASING))
            .where('P', MetaBlocks.TURBINE_CASING.getState(TurbineCasingType.STEEL_GEARBOX))
            .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.HV], EnumFacing.SOUTH)
            .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.HV], EnumFacing.EAST)
            .where('B', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.HV], EnumFacing.NORTH)
            .build();
        return Lists.newArrayList(shapeInfo);
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gregtech.multiblock.industrial_centrifuge.description")};
    }

}
