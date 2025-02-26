package com.sheath.bettermining;

import com.sheath.bettermining.init.EnchantmentInit;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bettermining implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Better Mining");

    public static final String MOD_ID = "bettermining";

    @Override
    public void onInitialize() {
        LOGGER.info("Loading...");

        EnchantmentInit.register();

    }

    public static Identifier id(String path) {return Identifier.of(MOD_ID,path);}

}
