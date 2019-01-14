package GTplusplus.jei;

import java.util.List;

import com.google.common.collect.Lists;

import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.block.GTppMultiblockCasing;
import GTplusplus.machines.GTppTileEntities;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.blocks.BlockBoilerCasing.BoilerCasingType;
import gregtech.common.blocks.BlockWireCoil.CoilType;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

public class IndustrialChemicalReactorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GTppTileEntities.INDUSTRIAL_CHEMICALREACTOR;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        MultiblockShapeInfo shapeInfo = MultiblockShapeInfo.builder()
            .aisle("XXX", "XBX", "XXX")
            .aisle("XHX", "CPE", "XXX")
            .aisle("XXX", "XIX", "XXX")
            .where('C', GTppTileEntities.INDUSTRIAL_CHEMICALREACTOR, EnumFacing.WEST)
            .where('X', GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.CHEMICAL_CASING))
            .where('I', MetaTileEntities.ITEM_IMPORT_BUS[GTValues.HV], EnumFacing.SOUTH)
            .where('E', MetaTileEntities.ENERGY_INPUT_HATCH[GTValues.HV], EnumFacing.EAST)
            .where('B', MetaTileEntities.ITEM_EXPORT_BUS[GTValues.HV], EnumFacing.NORTH)
            .where('P', MetaBlocks.BOILER_CASING.getState(BoilerCasingType.STEEL_PIPE))
            .where('H', MetaBlocks.WIRE_COIL.getState(CoilType.CUPRONICKEL))
            .build();
        return Lists.newArrayList(shapeInfo);
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gregtech.multiblock.industrial_chemicalreactor.description")};
    }

}
