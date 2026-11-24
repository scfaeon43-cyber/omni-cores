package omnicore.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;
import omnicore.world.blocks.*;

import static mindustry.type.ItemStack.*;

public class ModBlocks {
    public static Block
        coreHorizon, coreZenith, coreVoskhod,
        electroMine, laserMatrix;

    public static void load() {
        // 1. Ядро: Горизонт (= Штаб, но вместимость * 1.5, юнит Протон)
        coreHorizon = new CoreBlock("core-horizon") {{
            localizedName = "Ядро: Горизонт";
            description = "Командный комплекс закрытого НИИ. Управляет штурмовым стражем «Протон».";
            size = Blocks.coreFoundation.size;
            health = Blocks.coreFoundation.health;
            armor = Blocks.coreFoundation.armor;
            itemCapacity = (int) (Blocks.coreFoundation.itemCapacity * 1.5f);
            unitType = ModUnits.proton;
            unitCapModifier = ((CoreBlock) Blocks.coreFoundation).unitCapModifier;
            incinerateNonBuildable = false;
            isFirstTier = false;
            requiresCoreZone = true;
            env = Env.any;
            requirements(Category.effect, Blocks.coreFoundation.requirements);
        }};

        // 2. Ядро: Зенит (= Атом, но вместимость * 1.5, юнит Электрон)
        coreZenith = new CoreBlock("core-zenith") {{
            localizedName = "Ядро: Зенит";
            description = "Командный комплекс закрытого НИИ. Управляет штурмовым стражем «Электрон».";
            size = Blocks.coreNucleus.size;
            health = Blocks.coreNucleus.health;
            armor = Blocks.coreNucleus.armor;
            itemCapacity = (int) (Blocks.coreNucleus.itemCapacity * 1.5f);
            unitType = ModUnits.electron;
            unitCapModifier = ((CoreBlock) Blocks.coreNucleus).unitCapModifier;
            incinerateNonBuildable = false;
            isFirstTier = false;
            requiresCoreZone = true;
            env = Env.any;
            requirements(Category.effect, Blocks.coreNucleus.requirements);
        }};

        // 3. Ядро: Восход (Флагманское ядро, юнит Нейтрон)
        coreVoskhod = new CoreBlock("core-voskhod") {{
            localizedName = "Ядро: Восход";
            description = "Высший командный комплекс закрытого НИИ. Управляет тяжёлым осадным стражем «Нейтрон».";
            size = 5;
            health = 12000;
            armor = 15f;
            itemCapacity = 20000;
            unitType = ModUnits.neutron;
            unitCapModifier = 32;
            incinerateNonBuildable = false;
            isFirstTier = false;
            requiresCoreZone = true;
            env = Env.any;
            requirements(Category.effect, with(
                Items.copper, 3500,
                Items.lead, 3500,
                Items.graphite, 2500,
                Items.silicon, 2500,
                Items.titanium, 2000,
                Items.thorium, 1500,
                Items.surgeAlloy, 800
            ));
        }};

        // 4. Электромина
        electroMine = new ShockMine("electro-mine") {{
            localizedName = "Электромина";
            description = "Контактная мина с конденсатором высокого напряжения. При срабатывании выпускает каскад шаровых молний, мгновенно уничтожая лёгкую и среднюю технику.";
            size = 1;
            health = 60;
            hasShadow = false;
            cooldown = 180f;
            tileDamage = 15f;
            damage = 45f;
            length = 14;
            tendrils = 14;
            lightningColor = Color.valueOf("4df0ff");
            teamAlpha = 0.4f;
            env = Env.any;
            requirements(Category.effect, with(
                Items.copper, 25,
                Items.lead, 20,
                Items.silicon, 15
            ));
        }};

        // 5. Конструктор лазерной матрицы
        laserMatrix = new LaserMatrixBlock("laser-matrix") {{
            localizedName = "Конструктор лазерной матрицы";
            description = "Периметровый излучатель закрытого НИИ. Связывается с другим таким же узлом на расстоянии до 300 клеток, формируя сверхмощный алый лазерный барьер с электрическими разрядами.";
            size = 2;
            health = 1200;
            armor = 6f;
            env = Env.any;
            consumePower(12.0f);
            requirements(Category.turret, with(
                Items.copper, 140,
                Items.lead, 120,
                Items.silicon, 100,
                Items.titanium, 85,
                Items.plastanium, 50
            ));
        }};
    }
}
