package textgame;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

public class ConversationResponse extends UserResponse {
    private boolean winningResponse;
    public UserResponseType getType() { 
        if (winningResponse) {
            return UserResponseType.WIN;
        }
        else {
            return UserResponseType.CONVERSATION;
        }
    }

    private int highestAction;
    public int getHighestAction() {
        return highestAction;
    }
    private String reply;
    private NPC partner;
    public NPC getPartner() {
        return partner;
    }
    private Map<Integer, String> speeches;
    private Map<Integer, Item> acceptableItems;
    private Map<Integer, Item> giveableItems;

    public ConversationResponse(NPC p, String rep, Boolean win,
            Set<String> spcs, Set<Item> acc, Set<Item> giv) {
        partner = p;
        reply = rep;
        winningResponse = win;

        speeches = new HashMap<Integer, String>();
        acceptableItems = new HashMap<Integer, Item>();
        giveableItems = new HashMap<Integer, Item>();

        int i = 1;
        for (String s: spcs) {
            speeches.put(i, s);
            ++i;
        }
        for (Item itm: acc) {
            acceptableItems.put(i, itm);
            ++i;
        }
        for (Item itm: giv) {
            giveableItems.put(i, itm);
            ++i;
        }
        highestAction = i;
    }

    public boolean hasActions() {
        return !(speeches.isEmpty() &&
                acceptableItems.isEmpty() &&
                giveableItems.isEmpty());
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("They say \"");
        sb.append(reply);
        sb.append("\".");
        if (!hasActions()) {
            return sb.toString();
        }

        sb.append("\n\nYou Can...\n\n");
        for (int i = 1; i < highestAction; ++i) {
            sb.append(i);
            sb.append(". ");
            sb.append(getActionString(i));
            sb.append("\n");
        }
        sb.append(highestAction);
        sb.append(". Stop Talking");
        return sb.toString();
    }
 
    public String getActionString(int i) {
        if (speeches.containsKey(i)) {
            return "Say \"" + speeches.get(i) + "\"";
        }
        else if (acceptableItems.containsKey(i)) {
            return "Take \"" + acceptableItems.get(i).getName() + "\"";
        }
        else if (giveableItems.containsKey(i)) {
            return "Give \"" + giveableItems.get(i).getName() + "\"";
        }
        else {
            return null;
        }
    }

    public ConversationResult performAction(int i) throws IllegalArgumentException {
        if (speeches.containsKey(i)) {
            partner.talk(speeches.get(i));
            return ConversationResult.spoke();
        }
        else if (acceptableItems.containsKey(i)) {
            partner.getItem(acceptableItems.get(i));
            return ConversationResult.gotItem(acceptableItems.get(i));
        }
        else if (giveableItems.containsKey(i)) {
            partner.giveItem(giveableItems.get(i));
            return ConversationResult.gaveItem(giveableItems.get(i));
        }
        throw new IllegalArgumentException("No action with that number.");
    }
}

