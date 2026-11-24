package omnicore.content;

import arc.graphics.*;
import mindustry.type.*;

public class ModItems {
    public static Item antimatter;

    public static void load() {
        antimatter = new Item("antimatter", Color.valueOf("000000")) {{
            localizedName = "Антиматерия";
            description = "Дорогое производство компенсируется сверхвысокой выработкой энергии";
            explosiveness = 1000f;
            flammability = 0f;
            radioactivity = 0f;
            hardness = 1;
            alwaysUnlocked = true;
        }};
    }
}
