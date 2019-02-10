package org.karth.wurmunlimited.mods.templarcustomizer;

import com.wurmonline.server.items.*;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewItems {

    private static Logger logger;

    public static int spiritContractID;
    public static ItemTemplate spiritContract;

    static void register() throws IOException {
        logger = Logger.getLogger("Karth.NewItems");

        spiritContract = new ItemTemplateBuilder("karth.spiritcontract")
                .name("spirit contract", "spirit contracts", "A contract written with the blood of a powerful creature. The wording is such to allow a mayor to nickname a spirit templar on his or her own deed.")
                .imageNumber((short) 640)
                .weightGrams(100)
                .dimensions(1, 3, 20)
                .decayTime((long) 3024001) //This is the same as a papyrus sheet
                .value(50)
                .material(ItemMaterials.MATERIAL_PAPER)
                .modelName("model.artifact.scrollbind.")
                .difficulty((float) 50.0)
                .behaviourType((short) 1)
                .itemTypes( new short[] {
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_NORENAME,
                        ItemTypes.ITEM_TYPE_NO_IMPROVE
                })
                .build();

        spiritContractID = spiritContract.getTemplateId();

    }

}