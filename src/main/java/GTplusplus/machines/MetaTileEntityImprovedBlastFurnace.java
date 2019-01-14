package GTplusplus.machines;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.MultiblockRecipeMapWorkable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockMetalCasing.MetalCasingType;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.BlockWireCoil.CoilType;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.function.Predicate;



public class MetaTileEntityImprovedBlastFurnace extends RecipeMapMultiblockController {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
        MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS,
        MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.EXPORT_FLUIDS,
        MultiblockAbility.INPUT_ENERGY
    };

    private int blastFurnaceTemperature;

    public MetaTileEntityImprovedBlastFurnace(String metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.BLAST_RECIPES);
        this.recipeMapWorkable = new ImprovedWorkable(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityImprovedBlastFurnace(metaTileEntityId);
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        if(isStructureFormed()) {
            textList.add(new TextComponentTranslation("gregtech.multiblock.blast_furnace.max_temperature", blastFurnaceTemperature));
            
        }
        super.addDisplayText(textList);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.blastFurnaceTemperature = context.get("CoilType", CoilType.CUPRONICKEL).getCoilTemperature();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.blastFurnaceTemperature = 0;
    }

    @Override
    public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
        int recipeRequiredTemp = recipe.getIntegerProperty("blast_furnace_temperature");
        return this.blastFurnaceTemperature >= recipeRequiredTemp;
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
            .aisle("XXX", "CCC", "CCC", "XXX")
            .aisle("XXX", "C#C", "C#C", "XXX")
            .aisle("XSX", "CCC", "CCC", "XXX")
            .setAmountAtLeast('X', 10)
            .where('S', selfPredicate())
            .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
            .where('C', heatingCoilPredicate())
            .where('#', isAirPredicate())
            .build();
    }

    protected IBlockState getCasingState() {
        return MetaBlocks.METAL_CASING.getState(MetalCasingType.INVAR_HEATPROOF);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.HEAT_PROOF_CASING;
    }
    
    protected class ImprovedWorkable extends MultiblockRecipeMapWorkable {

        public ImprovedWorkable(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        @Override
        protected Recipe findRecipe(long maxVoltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs) {
        	Recipe recipe = super.findRecipe(maxVoltage, inputs, fluidInputs);
        	if(recipe == null)return null;
        	int recipeRequiredTemp = recipe.getIntegerProperty("blast_furnace_temperature");
        	int eff = (blastFurnaceTemperature - recipeRequiredTemp) / 900;
        	if(eff < 1)return recipe;
        	int overclocks = eff / 2;
        	
        	@SuppressWarnings("unchecked")
			RecipeMap<BlastRecipeBuilder> rmap = (RecipeMap<BlastRecipeBuilder>) this.recipeMap;
        	
        	return rmap.recipeBuilder()
	                    .inputsIngredients(recipe.getInputs())
	                    .fluidInputs(recipe.getFluidInputs())
	                    .outputs(recipe.getOutputs())
	                    .fluidOutputs(recipe.getFluidOutputs())
	                    .EUt((int)(recipe.getEUt() * (1.0f - (eff * 0.05f))))
	                    .duration((int)(recipe.getDuration() / Math.pow(2.0d, overclocks)))
	                    .cannotBeBuffered()
	                    .build().getResult();
        }
    }

}
