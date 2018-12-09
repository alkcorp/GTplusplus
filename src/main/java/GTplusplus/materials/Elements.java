package GTplusplus.materials;

import gregtech.api.unification.Element;
import net.minecraftforge.common.util.EnumHelper;

public class Elements {
    public static void preInit() {
        EnumHelper.addEnum(Element.class, "Ke", new Class[]
                {
                        long.class, long.class, long.class, String.class, String.class, boolean.class
                }, 181L, 133L, -1L, null, "TRINIUM", false);
    }
}
