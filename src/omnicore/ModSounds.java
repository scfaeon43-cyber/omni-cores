package omnicore;

import arc.audio.*;
import mindustry.*;

public class ModSounds {
    public static Sound
        protonShoot = new Sound(),
        lasMatrixEmmition = new Sound(),
        decayEmmition = new Sound();

    public static void load() {
        protonShoot = Vars.tree.loadSound("protonShoot");
        lasMatrixEmmition = Vars.tree.loadSound("lasMatrixEmmition");
        decayEmmition = Vars.tree.loadSound("decay-emmition");
    }
}
