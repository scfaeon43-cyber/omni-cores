package omnicore.world.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import omnicore.*;

public class LaserMatrixBlock extends Block {
    public float maxRange = 300f * 8f; // 300 клеток
    public float damagePerSecond = 2000f;

    public LaserMatrixBlock(String name) {
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        configurable = true;
        saveConfig = true;
        ambientSound = Sounds.loopPulse; // или Blocks.impactReactor.ambientSound
        ambientSoundVolume = 0.5f;

        config(Point2.class, (LaserMatrixBuild build, Point2 point) -> {
            build.tryLink(Point2.pack(point.x, point.y));
        });
        config(Integer.class, (LaserMatrixBuild build, Integer pos) -> {
            build.tryLink(pos);
        });
        configClear((LaserMatrixBuild build) -> {
            build.unlinkOut();
        });
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        Drawf.dashCircle(x * 8f + offset, y * 8f + offset, maxRange, Pal.accent);
    }

    public class LaserMatrixBuild extends Building {
        public int targetPos = -1;   // Кому передаём луч (исходящая связь)
        public int sourcePos = -1;   // От кого принимаем луч (входящая связь)

        public @Nullable LaserMatrixBuild getTarget() {
            if (targetPos == -1) return null;
            Building b = Vars.world.build(targetPos);
            return (b instanceof LaserMatrixBuild && b.team == team) ? (LaserMatrixBuild) b : null;
        }

        public @Nullable LaserMatrixBuild getSource() {
            if (sourcePos == -1) return null;
            Building b = Vars.world.build(sourcePos);
            return (b instanceof LaserMatrixBuild && b.team == team) ? (LaserMatrixBuild) b : null;
        }

        public void unlinkOut() {
            LaserMatrixBuild old = getTarget();
            if (old != null && old.sourcePos == pos()) {
                old.sourcePos = -1;
            }
            targetPos = -1;
        }

        public void unlinkIn() {
            LaserMatrixBuild src = getSource();
            if (src != null && src.targetPos == pos()) {
                src.targetPos = -1;
            }
            sourcePos = -1;
        }

        public void tryLink(int otherPos) {
            if (otherPos == -1 || otherPos == pos()) {
                unlinkOut();
                return;
            }
            Building b = Vars.world.build(otherPos);
            if (!(b instanceof LaserMatrixBuild) || b.team != team) return;
            LaserMatrixBuild other = (LaserMatrixBuild) b;

            // Если уже связаны с этим блоком — отключаем связь
            if (targetPos == otherPos) {
                unlinkOut();
                return;
            }

            // Проверка дистанции и свободного входящего слота у цели
            if (!within(other, maxRange)) return;
            if (other.getSource() != null && other.sourcePos != pos()) return;

            unlinkOut();
            targetPos = otherPos;
            other.sourcePos = pos();
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if (this == other) {
                configure(-1);
                return false;
            }
            if (other instanceof LaserMatrixBuild && other.team == team && within(other, maxRange)) {
                configure(other.pos());
                return false;
            }
            return true;
        }

        @Override
        public void remove() {
            unlinkOut();
            unlinkIn();
            super.remove();
        }

        public boolean isBeamActive() {
            LaserMatrixBuild target = getTarget();
            return target != null && efficiency > 0.9f && target.efficiency > 0.9f;
        }

        @Override
        public void updateTile() {
            LaserMatrixBuild target = getTarget();
            if (target == null) {
                targetPos = -1;
                return;
            }
            if (getSource() == null) {
                sourcePos = -1;
            }

            if (!isBeamActive()) return;

            float tickDamage = (damagePerSecond / 60f) * delta();
            float beamWidth = 10f;

            // Урон всем вражеским юнитам, задевшим луч
            float minX = Math.min(x, target.x) - beamWidth;
            float minY = Math.min(y, target.y) - beamWidth;
            float w = Math.abs(target.x - x) + beamWidth * 2f;
            float h = Math.abs(target.y - y) + beamWidth * 2f;

            Units.nearbyEnemies(team, minX, minY, w, h, unit -> {
                float dist = Intersector.distanceSegmentPoint(x, y, target.x, target.y, unit.x, unit.y);
                if (dist <= beamWidth + unit.hitSize * 0.5f) {
                    // 200% урона при наличии щита
                    float mult = unit.shield > 0f ? 2.0f : 1.0f;
                    unit.damage(tickDamage * mult);
                    if (Mathf.chanceDelta(0.25)) {
                        Fx.hitLaserColor.at(unit.x, unit.y, Color.valueOf("ff2a4b"));
                    }
                }
            });

            // Редкие случайные искры в сторону от луча (до 20 клеток = 160f, урон 20% = 400)
            if (Mathf.chanceDelta(0.035)) {
                float t = Mathf.random();
                float sx = Mathf.lerp(x, target.x, t);
                float sy = Mathf.lerp(y, target.y, t);
                float baseAngle = angleTo(target);
                float sideAngle = baseAngle + (Mathf.chance(0.5) ? 90f : -90f) + Mathf.range(35f);
                float sparkDamage = damagePerSecond * 0.2f; // 400 урона

                Lightning.create(team, Color.valueOf("ff3b5c"), sparkDamage, sx, sy, sideAngle, 20);
                Sounds.shootArc.at(sx, sy, Mathf.random(0.9f, 1.1f));
            }
        }

        @Override
        public void drawConfigure() {
            Drawf.dashCircle(x, y, maxRange, Pal.accent);
            Drawf.square(x, y, block.size * 4f + 2f, Pal.accent);
            LaserMatrixBuild target = getTarget();
            if (target != null) {
                Drawf.square(target.x, target.y, target.block.size * 4f + 2f, Color.valueOf("ff2a4b"));
            }
        }

        @Override
        public void draw() {
            super.draw();
            LaserMatrixBuild target = getTarget();
            if (target == null || !isBeamActive()) return;

            Draw.z(Layer.bullet);
            float pulse = Mathf.absin(Time.time, 2f, 1.8f);

            // Внешнее алое свечение
            Draw.color(Color.valueOf("ff1a3c"), 0.45f);
            Lines.stroke(8f + pulse);
            Lines.line(x, y, target.x, target.y);

            // Средний луч
            Draw.color(Color.valueOf("ff4d6d"), 0.85f);
            Lines.stroke(4f + pulse * 0.5f);
            Lines.line(x, y, target.x, target.y);

            // Белое ядро луча
            Draw.color(Color.white, 0.95f);
            Lines.stroke(1.6f);
            Lines.line(x, y, target.x, target.y);

            // Узлы на концах
            Draw.color(Color.valueOf("ff2a4b"));
            Fill.circle(x, y, 4.5f + pulse * 0.4f);
            Fill.circle(target.x, target.y, 4.5f + pulse * 0.4f);
            Draw.color(Color.white);
            Fill.circle(x, y, 2f);
            Fill.circle(target.x, target.y, 2f);

            Draw.reset();
        }

        @Override
        public Point2 config() {
            return Point2.unpack(targetPos);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(targetPos);
            write.i(sourcePos);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            targetPos = read.i();
            sourcePos = read.i();
        }
    }
}
