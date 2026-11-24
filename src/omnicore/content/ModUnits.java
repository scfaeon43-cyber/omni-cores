package omnicore.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import omnicore.*;

public class ModUnits {
    public static UnitType proton, electron, neutron;

    public static void load() {
        // ==========================================
        // 1. ПРОТОН (Командный корабль ядра "Горизонт")
        // ==========================================
        proton = new UnitType("proton") {{
            localizedName = "Протон";
            description = "Экспериментальный командный корабль НИИ. Оснащён генератором силового поля, импульсным ремкомплектом и четырёхканальным дугомётом, разгоняющим темп стрельбы при непрерывном огне.";
            constructor = UnitEntity::create;
            controller = u -> new BuilderAI();
            flying = true;
            lowAltitude = true;
            speed = 4.6f; // Как у Гаммы
            accel = 0.12f;
            drag = 0.05f;
            rotateSpeed = 7f;
            health = 1600f;
            armor = 8f;
            hitSize = 18f;
            itemCapacity = 1000;
            buildSpeed = 2.8f;
            mineSpeed = 10f; // 10 единиц в секунду
            mineTier = 5;    // Копает любые руды
            mineWalls = true;
            buildBeamOffset = 8f;
            coreUnitDock = true;
            isEnemy = false;

            abilities.add(
                new ForceFieldAbility(65f, 2.5f, 600f, 300f),
                new RepairFieldAbility(180f, 180f, 110f) {{
                    healEffect = Fx.healWaveDynamic;
                    activeEffect = Fx.healWave;
                }}
            );

            weapons.add(new Weapon("neutron-arc-gun") {{
                x = 0f;
                y = 4f;
                mirror = false;
                reload = 45f; // Базовый интервал, при разгоне в 3 раза чаще (+200%)
                rotate = true;
                rotateSpeed = 10f;
                shootSound = ModSounds.protonShoot;

                // Разгон за 5 секунд (300 тиков) -> +200% к скорострельности
                shootWarmupSpeed = 1f / 300f;
                minWarmup = 0f;
                linearWarmup = true;

                shoot = new ShootSpread() {{
                    shots = 4;
                    spread = 22f;
                }};

                bullet = new BasicBulletType(8.5f, 12f) {{
                    lifetime = 32f;
                    width = 6f;
                    height = 12f;
                    buildingDamageMultiplier = 0.25f; // -75% урона по постройкам

                    weaveScale = 4f;
                    weaveMag = 3.5f;
                    homingPower = 0.14f;
                    homingRange = 180f;

                    frontColor = Color.white;
                    backColor = Color.valueOf("52ff6e");
                    trailColor = Color.valueOf("52ff6e");
                    trailWidth = 2.2f;
                    trailLength = 10;

                    // Распадается на 3 небольшие молнии в финале полёта
                    lightning = 3;
                    lightningLength = 7;
                    lightningDamage = 12f;
                    lightningColor = Color.valueOf("52ff6e");

                    // Звук попадания: небольшой взрыв + молнии
                    hitSound = Sounds.shootArc;
                    despawnSound = Sounds.explosion;

                    collidesTeam = true;
                    healAmount = 45f;
                    hitEffect = Fx.hitMeltHeal;
                    despawnEffect = Fx.hitMeltHeal;
                }};
            }});
        }};

        // ==========================================
        // 2. ЭЛЕКТРОН (Командный корабль ядра "Зенит")
        // ==========================================
        electron = new UnitType("electron") {{
            localizedName = "Электрон";
            description = "Скоростной страж поддержки НИИ. Выпускает плотные очереди дуговых разрядов, мгновенно поражающих цели и восстанавливающих союзные укрепления.";
            constructor = UnitEntity::create;
            controller = u -> new BuilderAI();
            flying = true;
            lowAltitude = true;
            speed = 4.6f; // Как у Гаммы
            accel = 0.12f;
            drag = 0.05f;
            rotateSpeed = 7f;
            health = 1600f;
            armor = 8f;
            hitSize = 18f;
            itemCapacity = 1000;
            buildSpeed = 2.8f;
            mineSpeed = 10f; // 10 единиц в секунду
            mineTier = 5;
            mineWalls = true;
            buildBeamOffset = 8f;
            coreUnitDock = true;
            isEnemy = false;

            abilities.add(
                new ForceFieldAbility(65f, 2.5f, 600f, 300f),
                new RepairFieldAbility(180f, 180f, 110f) {{
                    healEffect = Fx.healWaveDynamic;
                    activeEffect = Fx.healWave;
                }}
            );

            weapons.add(new Weapon("electron-burst-gun") {{
                x = 0f;
                y = 4f;
                mirror = false;
                reload = 150f; // 0.5 сек очередь + 2 сек откат
                rotate = true;
                rotateSpeed = 12f;
                inaccuracy = 8f;
                shootSound = Sounds.shootArc;

                // 20 дуг за 0.5 секунды (30 тиков)
                shoot = new ShootPattern() {{
                    shots = 20;
                    shotDelay = 1.5f;
                }};

                bullet = new LightningBulletType() {{
                    damage = 10f;
                    buildingDamageMultiplier = 0.25f; // -75% урона по постройкам
                    lightningLength = 26;
                    lightningLengthRand = 6;
                    lightningColor = Color.valueOf("4df0ff");
                    collidesTeam = true;
                    healAmount = 35f;
                    status = StatusEffects.shocked;
                    statusDuration = 90f;
                }};
            }});
        }};

        // ==========================================
        // 3. НЕЙТРОН (Командный корабль ядра "Восход")
        // ==========================================
        neutron = new UnitType("neutron") {{
            localizedName = "Нейтрон";
            description = "Тяжёлый осадный страж НИИ. Выпускает нестабильную шаровую молнию с вращающимися плазменными лезвиями, испепеляющую врагов и ремонтирующую союзные постройки.";
            constructor = UnitEntity::create;
            controller = u -> new BuilderAI();
            flying = true;
            lowAltitude = true;
            speed = 4.6f; // Как у Гаммы
            accel = 0.12f;
            drag = 0.05f;
            rotateSpeed = 7f;
            health = 1600f;
            armor = 8f;
            hitSize = 18f;
            itemCapacity = 1000;
            buildSpeed = 2.8f;
            mineSpeed = 10f; // 10 единиц в секунду
            mineTier = 5;
            mineWalls = true;
            buildBeamOffset = 8f;
            coreUnitDock = true;
            isEnemy = false;

            abilities.add(
                new ForceFieldAbility(65f, 2.5f, 600f, 300f),
                new RepairFieldAbility(180f, 180f, 110f) {{
                    healEffect = Fx.healWaveDynamic;
                    activeEffect = Fx.healWave;
                }}
            );

            Effect explosionFx = new Effect(40f, e -> {
                Draw.color(Color.valueOf("4df0ff"), Color.white, e.fin());
                Lines.stroke(3.5f * e.fout());
                Lines.circle(e.x, e.y, e.finpow() * 68f);
                Draw.color(Color.valueOf("0088ff"), 0.55f * e.fout());
                Fill.circle(e.x, e.y, e.fout() * 34f);
            });

            BasicBulletType ballLightning = new BasicBulletType(2.2f, 150f) {
                final float bladeLength = 42f;
                final float bladeOffset = 7f;
                final float bladeDamage = 25f;

                {
                    lifetime = 210f;
                    splashDamage = 1000f; // Финальный взрыв ~1000 урона
                    splashDamageRadius = 64f;
                    buildingDamageMultiplier = 0.25f; // -75% урона по постройкам
                    hitSize = 14f;
                    pierce = false;
                    status = StatusEffects.shocked;
                    statusDuration = 240f;
                    despawnEffect = explosionFx;
                    hitEffect = explosionFx;
                    hitSound = ModSounds.shockSphereExplosive;
                    despawnSound = ModSounds.shockSphereExplosive;
                }

                @Override
                public void update(Bullet b) {
                    super.update(b);

                    // Каждые 0.5 сек (30 тиков) бьёт всех врагов и чинит союзные постройки
                    if (b.timer(0, 30f)) {
                        boolean struck = false;

                        // Молнии по всем врагам в радиусе 95
                        Units.nearbyEnemies(b.team, b.x - 95f, b.y - 95f, 190f, 190f, target -> {
                            if (target.within(b.x, b.y, 95f)) {
                                Lightning.create(b.team, Color.valueOf("4df0ff"), 125f, b.x, b.y, b.angleTo(target), 15);
                                target.apply(StatusEffects.shocked, 180f);
                            }
                        });
                        if (Units.closestEnemy(b.team, b.x, b.y, 95f, u -> true) != null) {
                            struck = true;
                        }

                        // Ремонтные молнии по всем повреждённым союзным зданиям в радиусе 95
                        float r = 95f;
                        int minX = Math.max(0, (int)((b.x - r) / 8f));
int maxX = (int)((b.x + r) / 8f);
int minY = Math.max(0, (int)((b.y - r) / 8f));
int maxY = (int)((b.y + r) / 8f);
for (int tx = minX; tx <= maxX; tx++) {
for (int ty = minY; ty <= maxY; ty++) {
Building build = mindustry.Vars.world.build(tx, ty);
if (build != null && build.team == b.team && build.damaged() && build.within(b.x, b.y, r)) {
build.heal(130f);
Fx.healBlockFull.at(build.x, build.y, build.block.size, Color.valueOf("52ff6e"));
Lightning.create(b.team, Color.valueOf("52ff6e"), 0f, b.x, b.y, b.angleTo(build), 12);
struck = true;
}
}
}
if (struck) {
Sounds.shootArc.at(b.x, b.y);
}
}
// Урон плазменными лезвиями при контакте
if (b.timer(1, 5f)) {
Units.nearbyEnemies(b.team, b.x - bladeLength - 10f, b.y - bladeLength - 10f,
(bladeLength + 10f) * 2f, (bladeLength + 10f) * 2f, unit -> {
if (unit.within(b.x, b.y, bladeLength + unit.hitSize * 0.5f)) {
unit.damage(bladeDamage);
unit.apply(StatusEffects.shocked, 120f);
}
});
}
}
@Override
public void hit(Bullet b, float x, float y) {
for (int i = 0; i < 8; i++) {
Lightning.create(b.team, Color.valueOf("4df0ff"), 120f, b.x, b.y, i * 45f + Mathf.random(15f), 18);
}
super.hit(b, x, y);
}
@Override
public void draw(Bullet b) {
float angle1 = Time.time * 4.5f;
float angle2 = -Time.time * 4.5f;
Draw.z(Layer.bullet);
for (float rot : new float[]{angle1, angle2}) {
for (int side : new int[]{-1, 1}) {
float perpX = Angles.trnsx(rot + 90f, bladeOffset * side);
float perpY = Angles.trnsy(rot + 90f, bladeOffset * side);
float x1 = b.x + perpX - Angles.trnsx(rot, bladeLength);
float y1 = b.y + perpY - Angles.trnsy(rot, bladeLength);
float x2 = b.x + perpX + Angles.trnsx(rot, bladeLength);
float y2 = b.y + perpY + Angles.trnsy(rot, bladeLength);
Draw.color(Color.valueOf("0088ff"), 0.55f);
Lines.stroke(3.8f);
Lines.line(x1, y1, x2, y2);
Draw.color(Color.white, 0.95f);
Lines.stroke(1.4f);
Lines.line(x1, y1, x2, y2);
}
}
float pulse = Mathf.absin(Time.time, 2f, 2.5f);
Draw.color(Color.valueOf("0088ff"), 0.6f);
Fill.circle(b.x, b.y, 12f + pulse);
Draw.color(Color.valueOf("4df0ff"));
Fill.circle(b.x, b.y, 8f + pulse * 0.6f);
Draw.color(Color.white);
Fill.circle(b.x, b.y, 4.5f);
Draw.reset();
}
};
weapons.add(new Weapon("neutron-sphere-launcher") {{
x = 0f;
y = 5f;
mirror = false;
rotate = true;
reload = 600f; // 1 выстрел в 10 секунд
chargeSound = ModSounds.neutronCharge;
shootSound = ModSounds.neutronShoot;
shootStatus = StatusEffects.slow; // Замедление при выстреле
shootStatusDuration = 110f;
shoot = new ShootPattern() {{
firstShotDelay = 90f; // 1.5 сек зарядка как у Ворона
}};
bullet = ballLightning;
ballLightning.chargeEffect = Fx.lightningCharge;
}});
}};
}
}
