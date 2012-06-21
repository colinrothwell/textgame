/**
 * A discriminated union for the result of a conversation
 *
 * @author Colin Rothwell
 */
package textgame;

public class ConversationResult {
    private ConversationResultType typ;
    public ConversationResultType getType() {
        return typ;
    }

    private Item item;
    public Item getItem() {
        return item;
    }

    private ConversationResult(ConversationResultType t, Item i) {
        typ = t;
        item = i;
    }

    public static ConversationResult spoke() {
        return new ConversationResult(ConversationResultType.SPOKE, null);
    }

    public static ConversationResult gaveItem(Item i) {
        return new ConversationResult(ConversationResultType.GAVEITEM, i);
    }

    public static ConversationResult gotItem(Item i) {
        return new ConversationResult(ConversationResultType.GOTITEM, i);
    }
}
