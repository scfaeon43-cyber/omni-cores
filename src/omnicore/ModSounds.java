package omnicore;

import arc.audio.*;
import mindustry.*;

public class ModSounds {
    public static Sound
        protonShoot = new Sound(),
        neutronCharge = new Sound(),
        neutronShoot = new Sound(),
        shockSphereExplosive = new Sound(),
        lasMatrixEmmition = new Sound();

    public static void load() {
        protonShoot = Vars.tree.loadSound("proton-shoot");
        neutronCharge = Vars.tree.loadSound("neutron-charge");
        neutronShoot = Vars.tree.loadSound("neutron-shoot");
        shockSphereExplosive = Vars.tree.loadSound("shock-sphere-explosive");
        lasMatrixEmmition = Vars.tree.loadSound("las-matrix-emmition");
    }
}
