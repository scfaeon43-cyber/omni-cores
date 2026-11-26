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
    public static UnitType proton;

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
            mineSpeed = 100f; // 10 единиц в секунду
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
                x = 6f;
                y = 2f;
                mirror = true;
                reload = 67.5f; 
                rotate = false;
                shootCone = 360f;
                baseRotation = -75f;
                shootSound = UnitTypes.tecta.weapons.first().shootSound;


                shoot = new ShootSpread() {{
                    shots = 4;
                    spread = 10f;
                }};

                bullet = new BasicBulletType(8.5f, 12f) {{
                    lifetime = 64f;
                    width = 6f;
                    height = 12f;

                    weaveScale = 4f;
                    weaveMag = 3.5f;
                    homingPower = 0.2f;
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
}
}
