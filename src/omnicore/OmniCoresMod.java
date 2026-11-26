package omnicore;

import arc.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import omnicore.content.*;

public class OmniCoresMod extends Mod {

    

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
