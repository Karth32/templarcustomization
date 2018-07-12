package org.karth.wurmunlimited.mods.templarcustomizer;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import org.karth.wurmunlimited.mods.templarcustomizer.RenameAction;

public class TemplarCustomizer implements WurmServerMod, Configurable, PreInitable, Initable, ServerStartedListener, ItemTemplatesCreatedListener {

    private static Logger logger;
    public static boolean bKarthDebug = false;

    @Override
    public void configure(Properties properties) {
        logger = Logger.getLogger(this.getClass().getName());

        if (bKarthDebug) {
            logger.log(Level.INFO, "Debug mode is enabled.");
        }
    }

    @Override
    public void preInit() {

        ModActions.init();

    }

    @Override
    public void init() {
    }

    @Override
    public void onItemTemplatesCreated() {
        try {
            logger.log(Level.INFO, "Registering items");
            NewItems.register();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onServerStarted() {
        logger.log(Level.INFO, "Registering actions");
        //ModActions.registerAction(new RandomTeleportAction());
        ModActions.registerAction(new RenameAction());
        logger.log(Level.INFO, "Registering creation entries");
        //CreationEntryCreator.createSimpleEntry(SkillList.MIND_LOGICAL, ItemList.paperSheet, ItemList.blood, NewItems.spiritContractID, false, false, 0.0f, false, false, CreationCategories.WRITING);
        AdvancedCreationEntry spiritContract = CreationEntryCreator.createAdvancedEntry(SkillList.PAPYRUSMAKING, ItemList.paperSheet, ItemList.blood, NewItems.spiritContractID, false, false, 0.0f, false, false, CreationCategories.WRITING);
        spiritContract.addRequirement(new CreationRequirement(1, ItemList.paperSheet, 2, true));
        spiritContract.addRequirement(new CreationRequirement(2, ItemList.shaft, 1, true));
    }
}