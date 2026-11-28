package omnicore.content;

import arc.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.type.*;

public class ModTechTree {
    private static final Seq<TechNode> modNodes = new Seq<>();

    public static void load() {
        modNodes.clear();

        // Находим ванильные стартовые ядра обеих планет
        TechNode serpuloShard = TechTree.all.find(t -> t.content == Blocks.coreShard);
        TechNode erekirBastion = TechTree.all.find(t -> t.content == Blocks.coreBastion);

        // Прикрепляем единую ветку мода к обоим деревьям
        if (serpuloShard != null) buildBranch(serpuloShard);
        if (erekirBastion != null) buildBranch(erekirBastion);

        // Синхронизируем частичный прогресс изучения между деревьями Серпуло и Эрекира
        Events.on(ResearchEvent.class, e -> {
            TechNode source = modNodes.find(n -> n.content == e.content);
            if (source == null) return;

            for (TechNode target : modNodes) {
                if (target != source && target.content == e.content) {
                    for (int i = 0; i < Math.min(source.finishedRequirements.length, target.finishedRequirements.length); i++) {
                        target.finishedRequirements[i].amount = source.finishedRequirements[i].amount;
                    }
                    target.save();
                }
            }
        });
    }

    private static void buildBranch(TechNode parent) {
        // Корень всей ветки мода — Ядро: Восход
        TechNode voskhod = addNode(parent, ModBlocks.coreVoskhod);

        // 1. Командный юнит ядра
        addNode(voskhod, ModUnits.proton);

        // 2. Ветка ресурсов, жидкостей и производства
        TechNode refComp = addNode(voskhod, ModBlocks.refrigerantCompressor);
        addProduceNode(refComp, ModLiquids.refrigerant);

        TechNode thermMixer = addNode(refComp, ModBlocks.thermoniumMixer);
        addProduceNode(thermMixer, ModLiquids.thermonium);

        TechNode antiFactory = addNode(refComp, ModBlocks.antimatterFactory);
        addProduceNode(antiFactory, ModItems.antimatter);

        TechNode matterSynth = addNode(refComp, ModBlocks.matterSynthesizer);
        addNode(matterSynth, ModBlocks.liquidSynthesizer);

        // 3. Ветка логистики, бурения и энергетики
        TechNode transmitter = addNode(voskhod, ModBlocks.beamEnergyTransmitter);
        addNode(transmitter, ModBlocks.electromagneticConveyor);
        addNode(transmitter, ModBlocks.electromagneticDrill);

        TechNode hydro = addNode(transmitter, ModBlocks.hydroPowerPlant);
        addNode(hydro, ModBlocks.antimatterReactor);

        // 4. Ветка обороны и турелей
        TechNode mine = addNode(voskhod, ModBlocks.electroMine);

        TechNode rasplav = addNode(mine, ModBlocks.rasplavTurret);
        TechNode decay = addNode(rasplav, ModBlocks.decayTurret);
        addNode(decay, ModBlocks.omegaTurret);
    }

    private static TechNode addNode(TechNode parent, UnlockableContent content) {
        TechNode node = new TechNode(parent, content, content.researchRequirements());
        modNodes.add(node);
        return node;
    }

    private static TechNode addProduceNode(TechNode parent, UnlockableContent content) {
        TechNode node = addNode(parent, content);
        node.objectives.add(new Objectives.Produce(content));
        return node;
    }
}
