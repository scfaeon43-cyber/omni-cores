package omnicore.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;
import omnicore.*;
import omnicore.world.blocks.*;

import static mindustry.type.ItemStack.*;

public class ModBlocks {
    public static Block
        // Ядра и спецблоки
        coreHorizon, coreZenith, coreVoskhod,
        electroMine, laserMatrix,
        // Заводы и синтезаторы
        antimatterFactory, refrigerantCompressor, thermoniumMixer,
        matterSynthesizer, liquidSynthesizer,
        // Логистика и добыча
        electromagneticConveyor, electromagneticDrill,
        // Энергетика
        antimatterReactor, hydroPowerPlant, beamEnergyTransmitter,
        // Турели
        omegaTurret, decayTurret, rasplavTurret;

    public static void load() {
        // ==========================================
        // 1. ЯДРА И СПЕЦБЛОКИ
        // ==========================================

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
            requirements(Category.effect, Blocks.coreFoundation.requirements);
        }};

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
            requirements(Category.effect, Blocks.coreNucleus.requirements);
        }};

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
            requirements(Category.effect, with(
                Items.copper, 25,
                Items.lead, 20,
                Items.silicon, 15
            ));
        }};

        laserMatrix = new LaserMatrixBlock("laser-matrix") {{
            localizedName = "Конструктор лазерной матрицы";
            description = "Периметровый излучатель закрытого НИИ. Связывается с другим таким же узлом на расстоянии до 300 клеток, формируя сверхмощный алый лазерный барьер с электрическими разрядами.";
            size = 2;
            health = 1200;
            armor = 6f;
            consumePower(12.0f);
            requirements(Category.turret, with(
                Items.copper, 140,
                Items.lead, 120,
                Items.silicon, 100,
                Items.titanium, 85,
                Items.plastanium, 50
            ));
        }};

        // ==========================================
        // 2. ЗАВОДЫ И СИНТЕЗАТОРЫ
        // ==========================================

        antimatterFactory = new GenericCrafter("antimatter-factory") {{
            localizedName = "Синтезатор антиматерии";
            description = "Компактный ускоритель частиц. Потребляет колоссальное количество энергии для стабилизации антиматерии в магнитных капсулах.";
            size = 3;
            hasPower = true;
            hasItems = true;
            hasLiquids = true;
            itemCapacity = 10;
            liquidCapacity = 60f;
            craftTime = 240f;

            consumePower(250f);
            consumeItems(with(Items.phaseFabric, 1, Items.thorium, 2));
            consumeLiquid(ModLiquids.refrigerant, 0.2f);
            outputItem = new ItemStack(ModItems.antimatter, 1);

            drawer = new DrawMulti(
                new DrawRegion("-bottom"),
                new DrawWeave(),
                new DrawGlowRegion("-weave-glow") {{
                    color = Color.valueOf("4df0ff");
                    rotate = true;
                    rotateSpeed = 1.5f;
                }},
                new DrawDefault()
            );

            requirements(Category.crafting, with(
                Items.lead, 350,
                Items.silicon, 300,
                Items.titanium, 250,
                Items.thorium, 200,
                Items.surgeAlloy, 120,
                Items.phaseFabric, 80
            ));
        }};

        refrigerantCompressor = new GenericCrafter("refrigerant-compressor") {{
            localizedName = "Компрессор хладагента";
            description = "Сжимает криогенную жидкость с нефтепродуктами, синтезируя сверххолодный хладагент.";
            size = 3;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 60f;
            craftTime = 60f;

            consumePower(4.5f);
            consumeLiquids(LiquidStack.with(Liquids.cryofluid, 0.3f, Liquids.oil, 0.2f));
            outputLiquid = new LiquidStack(ModLiquids.refrigerant, 0.4f);

            drawer = new DrawMulti(
                new DrawRegion("-bottom"),
                new DrawLiquidTile(ModLiquids.refrigerant, 3f),
                new DrawBubbles(Color.valueOf("7b5cff")) {{
                    sides = 10;
                    recurrence = 3f;
                    spread = 6f;
                    radius = 2.5f;
                    amount = 14;
                }},
                new DrawDefault()
            );

            requirements(Category.crafting, with(
                Items.lead, 120,
                Items.titanium, 100,
                Items.silicon, 85,
                Items.metaglass, 75
            ));
        }};

        thermoniumMixer = new GenericCrafter("thermonium-mixer") {{
            localizedName = "Миксер термониума";
            description = "Переплавляет металлолом и пиротит в нефтяной основе, создавая вязкую высокотемпературную смесь.";
            size = 3;
            hasPower = true;
            hasItems = true;
            hasLiquids = true;
            itemCapacity = 20;
            liquidCapacity = 60f;
            craftTime = 90f;

            consumePower(5.0f);
            consumeItems(with(Items.pyratite, 2, Items.scrap, 3));
            consumeLiquid(Liquids.oil, 0.25f);
            outputLiquid = new LiquidStack(ModLiquids.thermonium, 0.35f);

            drawer = new DrawMulti(
                new DrawRegion("-bottom"),
                new DrawLiquidRegion(ModLiquids.thermonium) {{
                    suffix = "-liquid";
                }},
                new DrawBlurSpin("-vents", 6f) {{
                    blurThresh = 0.6f;
                }},
                new DrawDefault(),
                new DrawGlowRegion("-glow") {{
                    color = Color.valueOf("ff5500");
                }}
            );

            requirements(Category.crafting, with(
                Items.copper, 130,
                Items.graphite, 100,
                Items.silicon, 90,
                Items.plastanium, 50
            ));
        }};

        matterSynthesizer = new GenericCrafter("matter-synthesizer") {{
            localizedName = "Синтезатор материи";
            description = "Квантово-магнитная установка, материализующая любой изученный твёрдый ресурс из чистой энергии.";
            size = 3;
            health = 650;
            hasPower = true;
            hasItems = true;
            itemCapacity = 20;
            configurable = true;
            saveConfig = true;
            consumePower(45f);
drawer = new DrawMulti(
new DrawRegion("-bottom"),
new DrawCrucibleFlame() {{
flameColor = Color.valueOf("4df0ff");
midColor = Color.valueOf("0088ff");
flameRad = 3.5f;
circleSpace = 2.5f;
flameRadiusScl = 4f;
flameRadiusMag = 0.6f;
circleStroke = 1.5f;
}},
new DrawDefault(),
new DrawGlowRegion("-glow") {{
color = Color.valueOf("4df0ff");
}}
);
config(Item.class, (MatterSynthBuild build, Item item) -> build.selectedItem = item);
configClear((MatterSynthBuild build) -> build.selectedItem = null);
buildType = () -> new MatterSynthBuild();
requirements(Category.crafting, with(
Items.lead, 250,
Items.silicon, 220,
Items.titanium, 180,
Items.thorium, 140,
Items.phaseFabric, 60
));
}};
liquidSynthesizer = new GenericCrafter("liquid-synthesizer") {{
localizedName = "Синтезатор жидкости";
description = "Конденсирует выбранную изученную жидкость на молекулярном уровне за счёт высокого расхода электроэнергии.";
size = 3;
health = 650;
hasPower = true;
hasLiquids = true;
liquidCapacity = 100f;
configurable = true;
saveConfig = true;
consumePower(45f);
drawer = new DrawMulti(
new DrawRegion("-bottom"),
new DrawCrucibleFlame() {{
flameColor = Color.valueOf("52ff6e");
midColor = Color.valueOf("00aa44");
flameRad = 3.5f;
circleSpace = 2.5f;
flameRadiusScl = 4f;
flameRadiusMag = 0.6f;
circleStroke = 1.5f;
}},
new DrawDefault(),
new DrawGlowRegion("-glow") {{
color = Color.valueOf("4df0ff");
}}
);
config(Liquid.class, (LiquidSynthBuild build, Liquid liq) -> build.selectedLiquid = liq);
configClear((LiquidSynthBuild build) -> build.selectedLiquid = null);
buildType = () -> new LiquidSynthBuild();
requirements(Category.crafting, with(
Items.lead, 250,
Items.silicon, 220,
Items.metaglass, 180,
Items.thorium, 140,
Items.phaseFabric, 60
));
}};
// ==========================================
// 3. ЛОГИСТИКА И ДОБЫЧА
// ==========================================
electromagneticConveyor = new StackConveyor("electromagnetic-conveyor") {
public TextureRegion glowRegion, edgeGlowRegion;
@Override
public void load() {
super.load();
glowRegion = Core.atlas.find(name + "-glow");
edgeGlowRegion = Core.atlas.find(name + "-edge-glow");
}
{
localizedName = "Электромагнитный конвейер";
description = "Высокоскоростной электромагнитный конвейер, перемещающий ресурсы плотными пакетами по 50 штук. Требует 1 единицу энергии на блок.";
size = 1;
health = 120;
speed = 0.14f;
itemCapacity = 50;
recharge = 2f;
outputRouter = false;
hasPower = true;
consumesPower = true;
conductivePower = true;
glowColor = Color.valueOf("63b3ff");
consumePower(1f / 60f);
buildType = () -> new StackConveyorBuild() {
@Override
public void draw() {
super.draw();
if (efficiency > 0.01f && glowRegion.found()) {
Draw.z(Layer.blockOver);
Draw.blend(Blending.additive);
Draw.color(glowColor, (0.45f + Mathf.absin(Time.time, 6f, 0.35f)) * efficiency);
Draw.rect(glowRegion, x, y, rotdeg());
if (edgeGlowRegion.found()) {
for (int i = 0; i < 4; i++) {
if ((blendprox & (1 << i)) == 0) {
Draw.rect(edgeGlowRegion, x, y, i * 90f);
}
}
}
Draw.blend();
Draw.reset();
}
}
};
requirements(Category.distribution, with(
Items.copper, 2,
Items.silicon, 2,
Items.titanium, 1
));
}
};
electromagneticDrill = new Drill("electromagnetic-drill") {{
localizedName = "Электромагнитный бур";
description = "Мощная буровая установка с электромагнитным приводом. Требует нефть и большое количество энергии, но добывает ресурсы быстрее воздушного бура.";
size = 4;
tier = 5;
drillTime = 180f;
liquidBoostIntensity = 1.8f;
rotateSpeed = 6f;
drawRim = true;
heatColor = Color.valueOf("4df0ff");
consumePower(6.5f);
consumeLiquid(Liquids.oil, 0.15f);
requirements(Category.production, with(
Items.copper, 160,
Items.graphite, 120,
Items.silicon, 110,
Items.titanium, 90,
Items.thorium, 65
));
}};
// ==========================================
// 4. ЭНЕРГЕТИКА
// ==========================================
antimatterReactor = new ImpactReactor("antimatter-reactor") {{
localizedName = "Реактор антиматерии";
description = "Экспериментальный реактор аннигиляции. Требует огромного пускового импульса энергии, антиматерии и непрерывной подачи сверххолодного хладагента.";
size = 3;
health = 2400;
powerProduction = 600f;
itemDuration = 180f;
warmupSpeed = 0.0015f;
explosionRadius = 35;
explosionDamage = 8000;
liquidCapacity = 100f;
itemCapacity = 15;
ambientSound = Sounds.loopPulse;
ambientSoundVolume = 0.12f;
consumePower(120f);
consumeItem(ModItems.antimatter, 1);
consumeLiquid(ModLiquids.refrigerant, 0.4f);
drawer = new DrawMulti(
new DrawRegion("-bottom"),
new DrawDefault(),
new DrawPlasma() {{
plasmas = 4;
plasma1 = Color.valueOf("4df0ff");
plasma2 = Color.valueOf("a6f7ff");
}}
);
requirements(Category.power, with(
Items.lead, 600,
Items.silicon, 500,
Items.graphite, 400,
Items.thorium, 350,
Items.surgeAlloy, 250,
Items.metaglass, 200
));
}};
hydroPowerPlant = new ConsumeGenerator("hydro-power-plant") {{
localizedName = "Гидроэлектростанция";
description = "Массивная турбинная установка. Использует поток воды для выработки большого количества энергии и возвращает отработанную воду в систему.";
size = 5;
powerProduction = 18f;
itemDuration = 60f;
hasLiquids = true;
liquidCapacity = 120f;
outputsLiquid = true;
consumeLiquid(Liquids.water, 0.5f);
outputLiquid = new LiquidStack(Liquids.water, 0.45f);
drawer = new DrawMulti(
new DrawRegion("-bottom"),
new DrawLiquidTile(Liquids.water, 4f),
new DrawRegion("-mid"),
new DrawDefault(),
new DrawGlowRegion("-lights") {{
color = Color.valueOf("4df0ff");
glowScale = 10f;
glowIntensity = 0.5f;
}},
new DrawGlowRegion("-ventglow") {{
color = Color.valueOf("73d7ff");
glowScale = 14f;
glowIntensity = 0.7f;
}}
);
requirements(Category.power, with(
Items.copper, 140,
Items.lead, 120,
Items.metaglass, 80,
Items.silicon, 60
));
}};
beamEnergyTransmitter = new PowerNode("beam-energy-transmitter") {{
localizedName = "Лучевой энергетический канал";
description = "Дальнобойный энергетический узел передачи.";
size = 1;
health = 500;
laserRange = 40f;
maxNodes = 15;
requirements(Category.power, with(
Items.copper, 2
));
}};
    }
