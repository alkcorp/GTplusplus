package GTplusplus.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class GTppJeiPlugin implements IModPlugin {

    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new GTppMultiblockInfoCategory(registry.getJeiHelpers()));
    }

    @Override
    public void register(IModRegistry registry) {
        GTppMultiblockInfoCategory.registerRecipes(registry);
    }
}
