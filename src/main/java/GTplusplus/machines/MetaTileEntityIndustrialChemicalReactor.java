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
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.blocks.BlockBoilerCasing.BoilerCasingType;
import gregtech.common.blocks.BlockWireCoil.CoilType;
import net.minecraft.block.state.IBlockState;

public class MetaTileEntityIndustrialChemicalReactor extends IndustrialMachine {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
        MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.INPUT_ENERGY, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS
    };

    public MetaTileEntityIndustrialChemicalReactor(String metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.CHEMICAL_RECIPES, IndustrialType.EFFICIENCY);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityIndustrialChemicalReactor(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
        		.aisle("XXX", "XXX", "XXX")
                .aisle("XCX", "XPX", "XXX")
                .aisle("XXX", "XSX", "XXX")
                .setAmountAtLeast('X', 6)
                .where('S', selfPredicate())
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('P', statePredicate(MetaBlocks.BOILER_CASING.getState(BoilerCasingType.STEEL_PIPE)))
                .where('C', statePredicate(MetaBlocks.WIRE_COIL.getState(CoilType.CUPRONICKEL)))
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GTppTextures.CHEMICAL_CASING;
    }

    protected IBlockState getCasingState() {
    	return GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.CHEMICAL_CASING);
    }
}

