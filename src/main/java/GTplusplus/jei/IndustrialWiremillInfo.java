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
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class IndustrialWiremillInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GTppTileEntities.INDUSTRIAL_WIREMILL;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        MultiblockShapeInfo shapeInfo = MultiblockShapeInfo.builder()
            .aisle("XXX", "XBX", "XXX")
            .aisle("XXX", "C#E", "XXX")
            .aisle("XXX", "XIX", "XXX")
            .where('C', GTppTileEntities.INDUSTRIAL_WIREMILL, EnumFacing.WEST)
            .where('X', GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.WIRE_CASING))
            .where('#', Blocks.AIR.getDefaultState())
            .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.HV], EnumFacing.SOUTH)
            .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.HV], EnumFacing.EAST)
            .where('B', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.HV], EnumFacing.NORTH)
            .build();
        return Lists.newArrayList(shapeInfo);
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gregtech.multiblock.industrial_wiremill.description")};
    }

}
