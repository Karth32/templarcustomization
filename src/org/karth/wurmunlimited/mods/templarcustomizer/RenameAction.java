package org.karth.wurmunlimited.mods.templarcustomizer;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.villages.Village;
import org.gotti.wurmunlimited.modsupport.actions.*;

import java.util.Arrays;
import java.util.List;

public class RenameAction implements ModAction, BehaviourProvider, ActionPerformer {

    private short actionId;
    private final ActionEntry actionEntry;
    Village vCheck;

    public RenameAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = new ActionEntryBuilder(actionId, "Nickname", "nicknaming", new int[] {47,36}).build();
        ModActions.registerAction(actionEntry);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Creature target) {
        if (subject != null && subject.getTemplateId() == 176 || subject.getTemplateId() == 315 || subject.getTemplateId() == NewItems.spiritContractID && target.getTemplate().getTemplateId() == 32 || target.getTemplate().getTemplateId() == 33) {
            return Arrays.asList(actionEntry);
        } else {
            return null;
        }
    }

    @Override
    public short getActionId() {
        return actionId;
    }

    public boolean action(Action action, Creature performer, Item subject, Creature target, short num, float counter) {
        if (target.getTemplate().getTemplateId() == 32 || target.getTemplate().getTemplateId() == 33) { // Sanity check, and a fallback if someone uses actions mod.
            vCheck = performer.getCitizenVillage();
            if (performer.getPower() >= 2 ) {
                RenameQuestion.send(performer, target, subject);
            } else {

                // Hopefully a workaround for the server modloader's non-handling of null variables.
                if (performer.getVillageId() == 0) {
                    performer.getCommunicator().sendNormalServerMessage("You may only rename spirit templars or spirit shadows that belong to your own deed!");
                    return true;
                }

                if (target.getVillageId() == performer.getVillageId()) {
                    if (vCheck.getMayor().getId() == performer.getWurmId()) {
                        RenameQuestion.send(performer, target, subject);
                    } else {
                        performer.getCommunicator().sendNormalServerMessage("You must be mayor to do this!");
                    }
                } else {
                    performer.getCommunicator().sendNormalServerMessage("You may only rename spirit templars or spirit shadows that belong to your own deed!");
                }
            }
        } else {
            performer.getCommunicator().sendNormalServerMessage("Nicknames may only be given to spirit templars or spirit shadows!");
        }

        return true;
    }

}