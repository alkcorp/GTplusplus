package GTplusplus.machines;

import com.google.common.base.Predicate;

import GTplusplus.GTppTextures;
import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.block.GTppMultiblockCasing;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.BlockWireCoil.CoilType;
import net.minecraft.block.state.IBlockState;

public class MetaTileEntityIndustrialElectrolyzer extends IndustrialMachine {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
        MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.INPUT_ENERGY, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS
    };

    public MetaTileEntityIndustrialElectrolyzer(String metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.ELECTROLYZER_RECIPES, IndustrialType.EFFICIENCY);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityIndustrialElectrolyzer(metaTileEntityId);
    }
    
    public static Predicate<BlockWorldState> heatingCoilPredicate() {
        return blockWorldState -> {
            IBlockState blockState = blockWorldState.getBlockState();
            if(!(blockState.getBlock() instanceof BlockWireCoil))
                return false;
            BlockWireCoil blockWireCoil = (BlockWireCoil) blockState.getBlock();
            CoilType coilType = blockWireCoil.getState(blockState);
            CoilType currentCoilType = blockWorldState.getMatchContext().get("CoilType", coilType);
            return currentCoilType.getName().equals(coilType.getName());
        };
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
            .aisle("XXX", "XXX", "XXX")
            .aisle("XXX", "X#X", "XXX")
            .aisle("XXX", "XSX", "XXX")
            .setAmountAtLeast('X', 6)
            .where('S', selfPredicate())
            .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
            .where('#', isAirPredicate())
            .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GTppTextures.ELECTROLYZER_CASING;
    }

    protected IBlockState getCasingState() {
    	return GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.ELECTROLYZER_CASING);
    }
}