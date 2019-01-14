package GTplusplus.machines;

import java.util.ArrayList;
import java.util.List;

import GTplusplus.GTppTextures;
import GTplusplus.block.GTppMetaBlocks;
import GTplusplus.block.GTppMultiblockCasing;
import GTplusplus.recipes.GTppRecipeMaps;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.MultiblockRecipeMapWorkable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.builders.IntCircuitRecipeBuilder;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.util.GTUtility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.items.IItemHandlerModifiable;

public class MetaTileEntityIndustrialFisher extends RecipeMapMultiblockController{

	private int tier;
	
	private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.INPUT_ENERGY
    };
	
	public MetaTileEntityIndustrialFisher(String metaTileEntityId) {
        super(metaTileEntityId, GTppRecipeMaps.INDUSTRIALFISHER);
        this.recipeMapWorkable = new FisherWorkable(this);
        
    }
	
	@Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityIndustrialFisher(metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.tier = GTUtility.getTierByVoltage(this.energyContainer.getInputVoltage());
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.tier = 0;
    }
    
    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        if(isStructureFormed()) {
        	textList.add(new TextComponentTranslation("gregtech.multiblock.industrial_fisher.speed", tier * 4));
        }
        super.addDisplayText(textList);
    }
    
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
            .aisle("XXXXX", "XXXXX", "XXXXX")
            .aisle("XXXXX", "X###X", "X###X").setRepeatable(5)
            .aisle("XXXXX", "XXSXX", "XXXXX")
            .setAmountAtLeast('X', 10)
            .where('S', selfPredicate())
            .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
            .where('#', blockPredicate(Blocks.WATER))
            .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GTppTextures.FISHER_CASING;
    }

    protected IBlockState getCasingState() {
    	return GTppMetaBlocks.MULTIBLOCK_CASING.getState(GTppMultiblockCasing.CasingType.FISHER_CASING);
    }
    
    protected class FisherWorkable extends MultiblockRecipeMapWorkable {

        public FisherWorkable(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

		@Override
        protected Recipe findRecipe(long maxVoltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs) {
        	for(int index = 0; index < inputs.getSlots(); index++) {
                ItemStack stackInSlot = inputs.getStackInSlot(index);
                if(stackInSlot.isEmpty())
                    continue;
                int config = IntCircuitIngredient.getCircuitConfiguration(stackInSlot);
                if(config == 0)break;
                LootContext.Builder builder = new LootContext.Builder((WorldServer)this.getMetaTileEntity().getWorld());
                LootTable fishTable = this.getMetaTileEntity().getWorld().getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING);
                List<ItemStack> output = new ArrayList<ItemStack>();
                for(int i = 0; i < tier * 4; i++) {
                	List<ItemStack> fishables = fishTable.generateLootForPools(this.getMetaTileEntity().getWorld().rand, builder.build());
                	for(ItemStack fish : fishables) {
                		if(fish.isEmpty())continue;
                		output.add(fish);
                	}
                }
                @SuppressWarnings("unchecked")
				RecipeMap<IntCircuitRecipeBuilder> rmap = (RecipeMap<IntCircuitRecipeBuilder>) this.recipeMap;
                
                return rmap.recipeBuilder()
				                		.circuitMeta(1)
						                .outputs(output)
						                .EUt(10)
						                .duration(2400)
						                .cannotBeBuffered()
						                .build().getResult();
        	}
        	return null;
        }
    }
}
