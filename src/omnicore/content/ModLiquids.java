package omnicore.content;

import arc.graphics.*;
import mindustry.type.*;

public class ModLiquids {
    public static Liquid thermonium, refrigerant;

    public static void load() {
        thermonium = new Liquid("thermonium", Color.valueOf("FF003F")) {{
            localizedName = "Термониум";
            description = "Сверхгорючая смесь.";
            flammability = 100f;
            temperature = 600f;
            heatCapacity = 0f;
            viscosity = 0.7f;
            explosiveness = 0f;
            alwaysUnlocked = true;
        }};

        refrigerant = new Liquid("refrigerant", Color.valueOf("9400FF")) {{
            localizedName = "Хладагент";
            description = "Просто хороший хладагент.";
            flammability = 0f;
            temperature = -200f;
            heatCapacity = 200f;
            viscosity = 0.5f;
            explosiveness = 0f;
            alwaysUnlocked = true;
        }};
    }
}
