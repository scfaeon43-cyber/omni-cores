package omnicore.content;

import arc.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.game.EventType.*;
import mindustry.type.*;

import static mindustry.type.ItemStack.*;

public class ModTechTree {
    public static TechNode root;

    public static void load() {
        root = TechTree.nodeRoot("omni-cores", ModBlocks.coreHorizon, true, () -> {
            // Ветка ядер и командных юнитов
            TechTree.node(ModUnits.proton, () -> {});
            TechTree.node(ModBlocks.coreZenith, () -> {
                TechTree.node(ModUnits.electron, () -> {});
                TechTree.node(ModBlocks.coreVoskhod, () -> {
                    TechTree.node(ModUnits.neutron, () -> {});
                });
            });

            // Ветка ресурсов, жидкостей и производства
            TechTree.node(ModBlocks.refrigerantCompressor, () -> {
                TechTree.nodeProduce(ModLiquids.refrigerant, () -> {});
                TechTree.node(ModBlocks.thermoniumMixer, () -> {
                    TechTree.nodeProduce(ModLiquids.thermonium, () -> {});
                });
                TechTree.node(ModBlocks.antimatterFactory, () -> {
                    TechTree.nodeProduce(ModItems.antimatter, () -> {});
                });
                TechTree.node(ModBlocks.matterSynthesizer, () -> {
                    TechTree.node(ModBlocks.liquidSynthesizer, () -> {});
                });
            });

            // Ветка логистики, бурения и энергетики
            TechTree.node(ModBlocks.beamEnergyTransmitter, () -> {
                TechTree.node(ModBlocks.electromagneticConveyor, () -> {});
                TechTree.node(ModBlocks.electromagneticDrill, () -> {});
                TechTree.node(ModBlocks.hydroPowerPlant, () -> {
                    TechTree.node(ModBlocks.antimatterReactor, () -> {});
                });
            });

            // Ветка обороны и турелей
            TechTree.node(ModBlocks.electroMine, () -> {
                TechTree.node(ModBlocks.laserMatrix, () -> {});
                TechTree.node(ModBlocks.rasplavTurret, () -> {
                    TechTree.node(ModBlocks.decayTurret, () -> {
                        TechTree.node(ModBlocks.omegaTurret, () -> {});
                    });
                });
            });
        });

        // Подключаем ветку ко всем планетам (включая планеты из других модов, например Миданту)
        Events.on(ContentInitEvent.class, e -> {
            for (Planet planet : Vars.content.planets()) {
                root.addPlanet(planet);
            }
        });
    }
}
