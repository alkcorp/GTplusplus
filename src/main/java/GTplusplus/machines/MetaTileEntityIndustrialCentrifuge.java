package GTplusplus.machines;

import GTplusplus.GTppTextures;
import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.block.GTppMultiblockCasing;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.common.blocks.BlockTurbineCasing.TurbineCasingType;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;

public class MetaTileEntityIndustrialCentrifuge extends IndustrialMachine {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
        MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.INPUT_ENERGY, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS
    };

    public MetaTileEntityIndustrialCentrifuge(String metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.CENTRIFUGE_RECIPES, IndustrialType.EFFICIENCY);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityIndustrialCentrifuge(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
            .aisle("XXX", "XXX", "XXX")
            .aisle("XXX", "XPX", "XXX")
            .aisle("XXX", "XSX", "XXX")
            .setAmountAtLeast('X', 6)
            .where('S', selfPredicate())
            .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
            .where('P', statePredicate(MetaBlocks.TURBINE_CASING.getState(TurbineCasingType.STEEL_GEARBOX)))
            .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GTppTextures.CENTRIFUGE_CASING;
    }

    protected IBlockState getCasingState() {
        return GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.CENTRIFUGE_CASING);
    }
    
    
}