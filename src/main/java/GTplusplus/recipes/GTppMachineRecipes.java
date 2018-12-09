package GTplusplus.recipes;

import GTplusplus.materials.GTppMaterials;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.ore.OrePrefix;

public class GTppMachineRecipes {
    public static void init() {
        RecipeMaps.MIXER_RECIPES.recipeBuilder().duration(628).EUt(120).input(OrePrefix.dust,GTppMaterials.Trinium).outputs(OreDictUnifier.get(OrePrefix.dust,GTppMaterials.Refined_Trinium)).buildAndRegister();
    }
}
