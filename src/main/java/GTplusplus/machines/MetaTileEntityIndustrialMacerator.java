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
import net.minecraft.block.state.IBlockState;

public class MetaTileEntityIndustrialMacerator extends IndustrialMachine {
	
	protected int parallel;

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
        MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.INPUT_ENERGY
    };

    public MetaTileEntityIndustrialMacerator(String metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.MACERATOR_RECIPES, IndustrialType.ITEM_PARALLEL);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityIndustrialMacerator(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
            .aisle("XXX", "XXX", "XXX")
            .aisle("XXX", "X#X", "XXX")
            .aisle("XXX", "XSX", "XXX")
            .setAmountAtLeast('X', 10)
            .where('S', selfPredicate())
            .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
            .where('#', isAirPredicate())
            .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GTppTextures.MACERATION_CASING;
    }

    protected IBlockState getCasingState() {
    	return GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.MACERATION_CASING);
    }
}

