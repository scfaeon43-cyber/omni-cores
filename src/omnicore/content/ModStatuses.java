package omnicore.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;

public class ModStatuses {
    public static StatusEffect piercing, incineration, irradiated;

    public static void load() {
        piercing = new StatusEffect("piercing") {{
            localizedName = "Пробит";
            description = "Пробит насквозь.";
            color = Color.valueOf("0000FF");
            speedMultiplier = 0f;
            damageMultiplier = 1f;
            reloadMultiplier = 0f;
            damage = 10f;
            healthMultiplier = 1f;
        }};

        incineration = new StatusEffect("incineration") {{
            localizedName = "Дефлаграция";
            description = "Критическое термическое разложение брони и узлов. Резко снижает скорость, скорострельность и прочность цели, нанося огромный урон.";
            color = Color.valueOf("ff5500");
            speedMultiplier = 0.3f;
            damageMultiplier = 0.1f;
            reloadMultiplier = 0.1f;
            damage = 250f / 60f; // Ровно 250 ед. урона в секунду
            healthMultiplier = 0.7f;
            effect = Fx.melting;
            effectChance = 0.4f;
        }};

        irradiated = new StatusEffect("irradiated") {{
            localizedName = "Облучение";
            description = "Ионизирующее излучение разрушает электронику и структуру цели, ослабляя её броню и замедляя системы.";
            color = Color.valueOf("52ff6e");
            speedMultiplier = 0.5f;
            damageMultiplier = 1.0f;
            reloadMultiplier = 0.5f;
            damage = 1f;
            healthMultiplier = 0.5f;
            effect = Fx.burning;
            effectChance = 0.25f;
        }};
    }
}
