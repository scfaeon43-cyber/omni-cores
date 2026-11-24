package omnicore;

import arc.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import omnicore.content.*;

public class OmniCoresMod extends Mod {

    public OmniCoresMod() {
        Events.on(ClientLoadEvent.class, e -> {
            ModSounds.load();
        });
    }

    @Override
    public void loadContent() {
        ModItems.load();
        ModLiquids.load();
        ModStatuses.load();
        ModUnits.load();
        ModBlocks.load();
        ModTechTree.load();
    }
}
