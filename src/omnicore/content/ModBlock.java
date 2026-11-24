package omnicore.content;

import arc.*
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
import mindustry.entities.effect.*;
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
import mindustry.world.consumers.*;
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
                glowColor = Color.valueOf("4df0ff");
                env = Env.any;

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
size = 5;
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
// ==========================================
// 5. ТУРЕЛИ (ОМЕГА, РАСПАД, РАСПЛАВ)
// ==========================================
// 5.1 ОМЕГА (16x16, 10 мин постройки, 1 мин откат, 30 000 энергии + 5 000 хладагента за выстрел, импульс Паруса в бирюзе)
import omnicore.*
Effect omegaCharge = new Effect(40f, e -> {
            Draw.color(Color.valueOf("4df0ff"), Color.white, e.fin());
            Lines.stroke(1.2f + e.fin() * 3.5f);
            // 8 небольших лучей: 4 справа (скошены под 45° вверх-влево), 4 слева (скошены под 45° вверх-вправо)
            for (int i = 0; i < 4; i++) {
                float forwardOffset = -16f + i * 18f;
                float sideOffset = 46f - i * 4f;
                float lineLen = e.finpow() * 28f;
                for (int side : new int[]{-1, 1}) {
                    float startX = e.x + Angles.trnsx(e.rotation, forwardOffset) + Angles.trnsx(e.rotation + 90f * side, sideOffset);
                    float startY = e.y + Angles.trnsy(e.rotation, forwardOffset) + Angles.trnsy(e.rotation + 90f * side, sideOffset);
                    // side == -1 (справа от оси) -> наклон влево-вверх (+45° к оси)
                    // side == +1 (слева от оси) -> наклон вправо-вверх (-45° к оси)
                    float slashAngle = e.rotation - 45f * side;
                    float endX = startX + Angles.trnsx(slashAngle, lineLen);
                    float endY = startY + Angles.trnsy(slashAngle, lineLen);
                    Lines.line(startX, startY, endX, endY);
                }
            }
        });

        Effect omegaSteam = new Effect(110f, e -> {
            Draw.color(Color.valueOf("d9f7ff"), Color.valueOf("7a8c99"), e.fin());
            Draw.alpha(0.65f * e.fout());
            for (int i = 0; i < 22; i++) {
                int seed = e.id + i * 13;
                int side = (i % 2 == 0) ? 1 : -1;
                float alongBarrel = Mathf.randomSeed(seed, 10f, 64f);
                float spreadSide = (18f + e.finpow() * Mathf.randomSeed(seed + 1, 20f, 40f)) * side;
                float driftForward = alongBarrel + e.fin() * Mathf.randomSeed(seed + 2, -8f, 12f);
                float sx = e.x + Angles.trnsx(e.rotation, driftForward) + Angles.trnsx(e.rotation + 90f, spreadSide);
                float sy = e.y + Angles.trnsy(e.rotation, driftForward) + Angles.trnsy(e.rotation + 90f, spreadSide);
                float radius = (3f + e.finpow() * 7.5f) * Mathf.randomSeed(seed + 3, 0.7f, 1.3f);
                Fill.circle(sx, sy, radius);
            }
            Draw.reset();
        });

        Effect omegaHitFx = new Effect(24f, e -> {
            Draw.color(Color.valueOf("4df0ff"), Color.white, e.fin());
            Lines.stroke(2.5f * e.fout());
            Lines.circle(e.x, e.y, e.finpow() * 24f);
            Angles.randLenVectors(e.id, 5, 4f + e.finpow() * 20f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * 2.5f);
            });
        });

        omegaTurret = new LaserTurret("omega-turret") {{
            localizedName = "Омега";
            description = "Сверхтяжёлый стационарный аннигиляционный излучатель закрытого НИИ. Требует 10 минут на возведение, 30 000 энергии и 5 000 хладагента на один залп.";
            size = 16;
            health = 45000;
            armor = 30f;
            range = 1000f;
            rotateSpeed = 0.35f;
            shootCone = 3f;
            aimChangeSpeed = 0.25f;
            recoil = 10f;
            shake = 14f;
            reload = 3600f;        // 1 минута откат (60 сек * 60 тиков)
            shootDuration = 280f;  // Длительность луча как у Паруса (Vela)
            buildTime = 36000f;    // 10 минут постройки (600 сек * 60 тиков)
            liquidCapacity = 10000f;
            hasLiquids = true;
            hasPower = true;
            firingMoveSlowdown = 0.25f;
            env = Env.any;

            // Конденсатор на 30 000 энергии
            consumePowerBuffered(30000f);

            // Звуки как у меха Парус (Vela)
            chargeSound = Sounds.chargeVela;
            shootSound = Sounds.shootVela;
            loopSound = Sounds.beam;
            loopSoundVolume = 1.4f;
            smokeEffect = omegaSteam;

            shoot = new ShootPattern() {{
                firstShotDelay = 40f; // Точное время зарядки Паруса (40 тиков), без рассинхрона со звуком
            }};

            // Непрерывный луч в стиле Паруса (Vela), перекрашенный в бирюзовый спектр
            shootType = new ContinuousLaserBulletType(480f) {{
                length = 1020f;
                width = 14f;
                hitEffect = omegaHitFx;
                shootEffect = omegaSteam;
                chargeEffect = omegaCharge;
                drawSize = 1100f;
                incendChance = 0.6f;
                incendSpread = 10f;
                incendAmount = 2;
                shake = 3f;
                colors = new Color[]{
                    Color.valueOf("0066cc").a(0.55f),
                    Color.valueOf("00aaff").a(0.8f),
                    Color.valueOf("4df0ff"),
                    Color.white
                };
            }};

            drawer = new DrawTurret("omega-base") {{
                heatColor = Color.valueOf("4df0ff");
            }};

            buildType = () -> new LaserTurretBuild() {
                @Override
                public boolean hasConsume() {
                    // Требует 30 000 энергии в конденсаторе и 5 000 хладагента в баке
                    return power.status >= 0.999f && liquids.get(ModLiquids.refrigerant) >= 5000f;
                }

                @Override
                protected void shoot(BulletType type) {
                    // Списываем 30 000 энергии и 5 000 хладагента в начале залпа
                    power.status = 0f;
                    liquids.remove(ModLiquids.refrigerant, 5000f);
                    super.shoot(type);
                }
            };

            requirements(Category.turret, with(
                Items.copper, 1800,
                Items.lead, 1600,
                Items.graphite, 1200,
                Items.titanium, 1000,
                Items.thorium, 900,
                Items.silicon, 1100,
                Items.surgeAlloy, 650,
                Items.phaseFabric, 400
            ));
        }};

        // 5.2 РАСПАД (4x4, аналог Сияния с Эрекира, статус Облучение, стягивание частиц + волна каждые 2 сек, кастомный звук decay-emmition)
        Effect decayGatherFx = new Effect(28f, e -> {
            Draw.color(Color.valueOf("52ff6e"), Color.white, e.fin());
            for (int i = 0; i < 6; i++) {
                float angle = e.rotation + (i * 60f) + e.fin() * 35f;
                float dist = 22f * e.fout();
                float px = e.x + Angles.trnsx(angle, dist);
                float py = e.y + Angles.trnsy(angle, dist);
                Fill.circle(px, py, 1.8f * e.fslope());
            }
        });

        Effect decayBeamParticleFx = new Effect(36f, e -> {
            Draw.color(Color.valueOf("52ff6e"), Color.valueOf("a8ffb5"), e.fin());
            float sideOffset = (1f - e.fin()) * Mathf.sin(e.id * 1.7f) * 10f;
            float forward = e.fin() * 24f;
            float px = e.x + Angles.trnsx(e.rotation, forward) + Angles.trnsx(e.rotation + 90f, sideOffset);
            float py = e.y + Angles.trnsy(e.rotation, forward) + Angles.trnsy(e.rotation + 90f, sideOffset);
            Fill.circle(px, py, 1.5f * e.fslope());
        });

        Effect decayPulseWaveFx = new Effect(45f, e -> {
            Draw.color(Color.valueOf("52ff6e"), Color.valueOf("1b8a32"), e.fin());
            Draw.alpha(0.55f * e.fout());
            Lines.stroke(2.5f * e.fout());
            Lines.circle(e.x, e.y, e.finpow() * 28f);
            Draw.reset();
        });

        decayTurret = new ContinuousTurret("decay-turret") {{
            localizedName = "Распад";
            description = "Фокусированный изотопный излучатель. Непрерывно удерживает узкий зелёный луч на цели, вызывая радиационное поражение.";
            size = 4;
            health = 2600;
            range = 340f;
            rotateSpeed = 1.8f;
            aimChangeSpeed = 1.4f;
            shootCone = 5f;
            recoil = 1.5f;
            shake = 1f;
            liquidCapacity = 40f;
            shootSound = ModSounds.decayEmmition;
            loopSound = ModSounds.decayEmmition;
            loopSoundVolume = 0.85f;
            coolantMultiplier = 2.5f;
            env = Env.any;

            consumePower(28f);
            consumeCoolant(0.25f);

            shootEffect = decayGatherFx;

            shootType = new PointLaserBulletType() {
                {
                    damage = 95f;
                    buildingDamageMultiplier = 0.4f;
                    color = Color.valueOf("52ff6e");
                    status = ModStatuses.irradiated;
                    statusDuration = 240f;
                    oscScl = 0.5f;
                    oscMag = 0.3f;
                    hitEffect = Fx.none;
                    trailEffect = decayBeamParticleFx;
                    trailChance = 0.45f;
                }

                @Override
                public void update(Bullet b) {
                    super.update(b);
                    // Полупрозрачная круговая волна в точке попадания каждые 2 секунды (120 тиков)
                    if (b.timer(0, 120f)) {
                        decayPulseWaveFx.at(b.aimX, b.aimY);
                    }
                }
            };

            requirements(Category.turret, with(
                Items.copper, 350,
                Items.lead, 300,
                Items.silicon, 260,
                Items.titanium, 220,
                Items.thorium, 180,
                Items.plastanium, 110
            ));
        }};

        // 5.3 РАСПЛАВ (4x4, жидкостная турель на Термониуме, звуки Цунами, статус Дефлаграция)
        Effect rasplavShootFx = new Effect(26f, e -> {
            Draw.color(Color.valueOf("ff003f"), Color.valueOf("ff8800"), e.fin());
Angles.randLenVectors(e.id, 6, 4f + e.finpow() * 28f, e.rotation, 22f, (x, y) -> {
Fill.circle(e.x + x, e.y + y, e.fout() * 2.8f);
});
});
Effect rasplavHitFx = new Effect(32f, e -> {
Draw.color(Color.valueOf("ff003f"), Color.valueOf("ffaa00"), e.fin());
Lines.stroke(2f * e.fout());
Lines.circle(e.x, e.y, e.finpow() * 18f);
Angles.randLenVectors(e.id + 1, 7, 3f + e.finpow() * 22f, (x, y) -> {
Fill.circle(e.x + x, e.y + y, e.fout() * 2.4f);
});
});
rasplavTurret = new LiquidTurret("rasplav-turret") {{
localizedName = "Расплав";
description = "Тяжёлая струйная установка на базе НИИ. Заливает цели перегретым вязким термониумом на большой дистанции, вызывая дефлаграцию.";
size = 4;
health = 2200;
range = 300f;
reload = 2.5f;
recoil = 2f;
shootCone = 40f;
liquidCapacity = 80f;
inaccuracy = 4f;
targetAir = true;
targetGround = true;
shootSound = Sounds.shootLiquid; // Звук струи турели Цунами
shootEffect = rasplavShootFx;
env = Env.any;
ammo(
ModLiquids.thermonium, new LiquidBulletType(ModLiquids.thermonium) {{
speed = 6.5f;
lifetime = 47f;
damage = 14f;
knockback = 1.2f;
puddleSize = 10f;
orbSize = 4.5f;
status = ModStatuses.incineration; // Дефлаграция (250 урона/сек)
statusDuration = 300f;
drag = 0.005f;
hitEffect = rasplavHitFx;
despawnEffect = rasplavHitFx;
}}
);
requirements(Category.turret, with(
Items.copper, 320,
Items.lead, 280,
Items.graphite, 200,
Items.titanium, 180,
Items.plastanium, 120,
Items.silicon, 150
));
}};
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
