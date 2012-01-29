package textgame;

import java.util.*;
import java.lang.IllegalArgumentException;

/**
 * Represents some way in which items can be transformed to make new items.
 * 
 * input is a list of String item types which are consumed from the user's
 * inventory.
 * output is a list of Items which are added to the user's inventory
 *
 * @author ColinRothwell
 *
 * @see Item
 */
public class Transformation {
    private List<String> input;
	private List<Item> output;
	
	public Transformation(List<String> in, List<Item> out) {
        input = in;
        output = out;
	}
	
    public UserResponse performTransformation(ItemContainer player) throws IllegalArgumentException {
        ArrayList<Item> consumed = new ArrayList<Item>();
        for (String type : input) {
            Item it = player.itemOfType(type);
            if (it == null) {
                throw new IllegalArgumentException();
            }
            consumed.add(it);
        }

        for (Item rem : consumed) {
            player.removeItem(rem);
        }

        for (Item add : output) {
            player.addItem(add);
        }

        return UserResponse.message("Removed " + ItemContainer.describeItems(consumed) +
                ", and added " + ItemContainer.describeItems(output) + ".");
    }

    public List<String> getInput() {
        return input;
    }
	
	public List<Item> getOutput() {
		return output;
	}
}
