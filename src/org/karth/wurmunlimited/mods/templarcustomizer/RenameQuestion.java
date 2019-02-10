package org.karth.wurmunlimited.mods.templarcustomizer;

import com.wurmonline.server.Items;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.questions.Question;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.karth.wurmunlimited.mods.templarcustomizer.BadWords.badWordCheck;

public class RenameQuestion implements ModQuestion {

    private final Creature performer;
    private final Creature renameTarget;
    private final Item subject;
    private static Logger logger;



    private RenameQuestion(Creature performer, Creature renameTarget, Item subject) {
        this.performer = performer;
        this.renameTarget = renameTarget;
        this.subject = subject;
        logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    public void sendQuestion(Question question) {
        final StringBuilder buf = new StringBuilder(ModQuestions.getBmlHeader(question));

        //performer.getCommunicator().sendNormalServerMessage("Debug: renameTarget.getTemplateId() = " + renameTarget.getTemplateId());
        //performer.getCommunicator().sendNormalServerMessage("Debug: performer.getTemplateId() = " + performer.getTemplateId());

        buf.append("harray{input{text=\'" + renameTarget.getName() + "\'; id=\'data1\'; maxchars=\'40\'}}");
        buf.append("text{text=\'\'}");
        buf.append("text{text=\'If you leave this blank the guard will revert to its\'}");
        buf.append("text{text=\'default name.\'}");
        buf.append("text{text=\'\'}");
        if (renameTarget.getSex() == 0) {
            buf.append("radio{ group=\'sex\'; id=\'sexmale\';text=\'Male\';selected=\'true\'}");
            buf.append("radio{ group=\'sex\'; id=\'sexfemale\';text=\'Female\'}");
        } else {
            buf.append("radio{ group=\'sex\'; id=\'sexmale\';text=\'Male\'}");
            buf.append("radio{ group=\'sex\'; id=\'sexfemale\';text=\'Female\';selected=\'true\'}");
        }
        buf.append("text{text=\'\'}");
        buf.append(ModQuestions.createAnswerButton2(question, "Confirm"));

        question.getResponder().getCommunicator().sendBml(300, 200, false, true, buf.toString(), 200, 200, 200, question.getTitle());

    }

    @Override
    public void answer(Question question, Properties answer) {
        String renameText;
        String renameText2;
        String guardGender;
        renameText = answer.getProperty("data1");
        renameText2 = answer.getProperty("data1");
        guardGender = answer.getProperty("sex");
        boolean toDestroy = true;

        renameText += "-x"; // Modloader doesn't like null variables being passed around. This is to ensure renameText is never null... I hope

        if (subject.getTemplateId() == 176 || subject.getTemplateId() == 315) { toDestroy = false; } // Don't want to destroy GM wands!

        if (guardGender.equals("sexmale") && renameTarget.getSex() == 1) {
            renameTarget.setSex((byte) 0);

        } else if (guardGender.equals("sexfemale") && renameTarget.getSex() == 0) {
            renameTarget.setSex((byte) 1);
        }

        if(Objects.equals(renameText, "-x")) {
            renameText = renameTarget.getTemplate().getName();
            performer.getCommunicator().sendNormalServerMessage("You didn't specify a nickname. The name will be reverted to " + renameText + ".");
            renameTarget.setName(renameText);
            renameTarget.setVisible(false);
            renameTarget.setVisible(true);
            try {
                renameTarget.getStatus().save(); // Force the server to save the status of the templar.
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to update creature " + renameTarget.getTemplate().getName() + " WurmID: (" + renameTarget.getWurmId() + ") in the database!");
            }
            if (toDestroy) { Items.destroyItem(subject.getWurmId()); }
            return;
        }

        if (!containsIllegalCharacters(renameText2, performer)) {
            if (!badWordCheck(renameText2)) {
                performer.getCommunicator().sendNormalServerMessage("Your " + renameTarget.getName() + " shall now be called " + renameText2 + "!");
                renameTarget.setName(renameText2);
                // There's probably a better method to force the client to update the NPC's name. But I can't find it.
                //renameTarget.blinkTo(tXPos, tYPos, renameTarget.getLayer(), renameTarget.getFloorLevel());
                // Found it!
                renameTarget.setVisible(false);
                renameTarget.setVisible(true);
                try {
                    renameTarget.getStatus().save(); // Force the server to save the status of the templar.
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Failed to update creature " + renameTarget.getTemplate().getName() + " WurmID: (" + renameTarget.getWurmId() + ") in the database!");
                }
                if (toDestroy) { Items.destroyItem(subject.getWurmId());}
            } else {
                performer.getCommunicator().sendNormalServerMessage("Your name contained an inappropriate word.");
            }
        }

    }

    public static void send(Creature performer, Creature renameTarget, Item subject) {
        ModQuestions.createQuestion(performer, "Give nickname", "What nickname shall you give this templar?", -10, new RenameQuestion(performer, renameTarget, subject)).sendQuestion();
    }

    private boolean containsIllegalCharacters(String text, Creature performer) {
        char[] chars = text.toCharArray();
        boolean toReturn = false;

        if (text.length() > 39) {
            performer.getCommunicator().sendNormalServerMessage("The nickname must be less than 40 characters.");
            return true;
        }

        if (Character.isWhitespace(text.charAt(0))) {
            performer.getCommunicator().sendNormalServerMessage("The nickname must not start with a space.");
            return true;
        }

        for (int x = 0; x < chars.length; x++) {
            if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ".indexOf(chars[x]) < 0 )
                if (!toReturn) {
                    performer.getCommunicator().sendNormalServerMessage("The nickname may only contain uppercase letters, lowercase letters, numbers and spaces.");
                    toReturn = true;
                }

        }

        return toReturn;
    }




}