package omnicore.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
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
        antimatterReactor, hydroPowerPlant, beamEnergyTransmitter;

    public static void load() {
        // ==========================================
        // ЯДРА И СПЕЦБЛОКИ
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
            env = Env.any;
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
            env = Env.any;
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

        // ==========================================
        // ЗАВОДЫ И СИНТЕЗАТОРЫ
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
            env = Env.any;

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
            env = Env.any;

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
            env = Env.any;

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

        // Синтезатор материи (кастомный Build с выбором предмета)
        matterSynthesizer = new Block("matter-synthesizer") {{
            localizedName = "Синтезатор материи";
            description = "Квантово-магнитная установка, материализующая любой изученный твёрдый ресурс из чистой энергии.";
            size = 3;
            health = 650;
            update = true;
            solid = true;
            hasPower = true;
            hasItems = true;
            itemCapacity = 20;
configurable = true;
saveConfig = true;
env = Env.any;
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
// Синтезатор жидкости (кастомный Build с выбором жидкости)
liquidSynthesizer = new Block("liquid-synthesizer") {{
localizedName = "Синтезатор жидкости";
description = "Конденсирует выбранную изученную жидкость на молекулярном уровне за счёт высокого расхода электроэнергии.";
size = 3;
health = 650;
update = true;
solid = true;
hasPower = true;
hasLiquids = true;
liquidCapacity = 100f;
configurable = true;
saveConfig = true;
env = Env.any;
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
// ЛОГИСТИКА И ДОБЫЧА
// ==========================================
electromagneticConveyor = new StackConveyor("electromagnetic-conveyor") {{
localizedName = "Электромагнитный конвейер";
description = "Высокоскоростной электромагнитный конвейер, перемещающий ресурсы плотными пакетами по 50 штук. Требует 1 единицу энергии на блок.";
size = 1;
health = 120;
speed = 0.14f;
itemCapacity = 50; // Пачки по 50 штук
recharge = 2f;
outputRouter = false; // Аналог кинетического конвейера Эрекира
hasPower = true;
consumesPower = true;
conductivePower = true;
env = Env.any;
consumePower(1f / 60f); // Ровно 1 единица энергии в секунду на блок
requirements(Category.distribution, with(
Items.copper, 2,
Items.silicon, 2,
Items.titanium, 1
));
}};
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
env = Env.any;
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
// ЭНЕРГЕТИКА
// ==========================================
antimatterReactor = new ImpactReactor("antimatter-reactor") {{
localizedName = "Реактор антиматерии";
description = "Экспериментальный реактор аннигиляции. Требует огромного пускового импульса энергии, антиматерии и непрерывной подачи сверххолодного хладагента.";
size = 3; // Размер 3x3
health = 2400;
powerProduction = 600f; // 36 000 ед./сек (чистый выход 28 800 ед./сек)
itemDuration = 180f;
warmupSpeed = 0.0015f;
explosionRadius = 35;
explosionDamage = 8000;
liquidCapacity = 100f;
itemCapacity = 15;
ambientSound = Sounds.loopPulse;
ambientSoundVolume = 0.12f;
env = Env.any;
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
size = 5; // Размер 5x5
powerProduction = 18f;
itemDuration = 60f;
hasLiquids = true;
liquidCapacity = 120f;
outputsLiquid = true;
env = Env.any;
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
env = Env.any;
requirements(Category.power, with(
Items.copper, 2
));
}};
}
// ==========================================
// ВНУТРЕННИЕ КЛАССЫ СИНТЕЗАТОРОВ
// ==========================================
public static class MatterSynthBuild extends Building {
public @Nullable Item selectedItem = null;
public float craftTimer = 0f;
public float getCraftTime(Item item) {
if (item == Items.copper || item == Items.lead || item == Items.sand || item == Items.scrap) return 15f;
if (item == Items.coal || item == Items.graphite || item == Items.metaglass || item == Items.titanium) return 30f;
if (item == Items.silicon || item == Items.plastanium || item == Items.thorium) return 45f;
return 120f;
}
@Override
public void updateTile() {
if (selectedItem != null && efficiency > 0f) {
craftTimer += delta() * efficiency;
float requiredTime = getCraftTime(selectedItem);
if (craftTimer >= requiredTime) {
if (items.get(selectedItem) < block.itemCapacity) {
items.add(selectedItem, 1);
}
craftTimer = 0f;
}
}
dump();
}
@Override
public void buildConfiguration(Table table) {
ItemSelection.buildTable(block, table,
Vars.content.items().select(UnlockableContent::unlockedNow),
() -> selectedItem,
this::configure
);
}
@Override
public Object config() {
return selectedItem;
}
@Override
public void draw() {
super.draw();
if (selectedItem != null) {
Draw.z(Layer.blockOver);
Draw.rect(selectedItem.uiIcon, x, y, 8f, 8f);
Draw.reset();
}
}
@Override
public void write(Writes write) {
super.write(write);
write.s(selectedItem == null ? -1 : selectedItem.id);
}
@Override
public void read(Reads read, byte revision) {
super.read(read, revision);
short id = read.s();
selectedItem = id == -1 ? null : Vars.content.item(id);
}
}
public static class LiquidSynthBuild extends Building {
public @Nullable Liquid selectedLiquid = null;
@Override
public void updateTile() {
if (selectedLiquid != null && efficiency > 0f) {
float rate = (selectedLiquid == Liquids.water || selectedLiquid == Liquids.oil) ? 0.5f : 0.2f;
float maxAdd = block.liquidCapacity - liquids.get(selectedLiquid);
if (maxAdd > 0f) {
liquids.add(selectedLiquid, Math.min(maxAdd, rate * delta() * efficiency));
}
}
if (selectedLiquid != null) {
dumpLiquid(selectedLiquid);
}
}
@Override
public void buildConfiguration(Table table) {
ItemSelection.buildTable(block, table,
Vars.content.liquids().select(UnlockableContent::unlockedNow),
() -> selectedLiquid,
this::configure
);
}
@Override
public Object config() {
return selectedLiquid;
}
@Override
public void draw() {
super.draw();
if (selectedLiquid != null) {
Draw.z(Layer.blockOver);
Draw.rect(selectedLiquid.uiIcon, x, y, 8f, 8f);
Draw.reset();
}
}
@Override
public void write(Writes write) {
super.write(write);
write.s(selectedLiquid == null ? -1 : selectedLiquid.id);
}
@Override
public void read(Reads read, byte revision) {
super.read(read, revision);
short id = read.s();
selectedLiquid = id == -1 ? null : Vars.content.liquid(id);
}
}
}
