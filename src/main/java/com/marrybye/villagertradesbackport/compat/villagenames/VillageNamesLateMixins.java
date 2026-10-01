package com.marrybye.villagertradesbackport.compat.villagenames;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public class VillageNamesLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.villagertradesbackport.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if (loadedMods.contains("VillageNames")) {
            mixins.add("MixinFunctionsVN");
            mixins.add("MixinExtendedVillager");
        }
        return mixins;
    }
}
