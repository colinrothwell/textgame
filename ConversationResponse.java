package textgame;

import java.util.Set;
import java.util.StringBuilder;

public class ConversationResponse {
    public UserResponseType getType() { return UserResponseType.CONVERSATION; }

    private String reply;
    private Set<String> speeches;
    private Set<Item> acceptableItems;
    private Set<Item> givableItems;

    public ConversationResponse(String rep, Set<String> spcs, Set<Item> acc, Set<Item> giv) {
        reply = rep;
        speeches = spcs;
        acceptableItems = acc;
        giveableItems = giv;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder(reply);
        if (speeches.isEmpty() && acceptableItems.isEmpty() && giveableItems.isEmpty()) {
            return sb.toString();
        }

        sb.append("\nYou Can...");
        int item = 1;
        for (String s: speeches) {
            sb.append(item);
            sb.append(". Say, \"");
            sb.append(s);
            sb.append("\".\n");
            ++item;
        }
        for (Item i: acceptableItems) {
            sb.append(item);
            sb.append(". Take, \"");
            sb.append(i.getName());
            sb.append("\".\n");
            ++item;
        }
        for (Item i: giveableItems) {
            sb.append(item);
            sb.append(". Give, \"");
            sb.append(i.getName());
            sb.append("\".\n");
            ++item;
        }
        return sb.toString();
    }
}

