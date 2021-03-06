package textgame;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Stores the current state of the game. The player's state is saved in
 * <code>player</code> and the user can interact with it using
 * <code>listener</code>.
 *
 * @author Colin Rothwell
 * @author Lewis Brown
 *
 * @see Printer
 * @see Modifier
 *
 */
public class Game {
    private static String preamble = "!! John Fawcett's Mountain Biking Adventure ALPHA";

    private Printer printer;
    private Player player;

    private Game() {
        super();
        TransformationManager tm = new TransformationManager();
        player = new Player(makeMap(), tm);
        printer = new Printer(player);

        tm.addTransformation(_makeIncompleteSupervisionWork());
        tm.addTransformation(_makeSWTransform());
    }

    /**
     * Generates the game world, and returns the start room.
     *
     * @author Colin Rothwell
     */
    protected Room makeMap() {
        Room yourRoom = _makeYourRoom();
        Room library = _makeLibrary();
        Room johnsRoom = _makeJohnsRoom();
        Room corridor = _makeCorridor();

        yourRoom.addConnection(corridor);
        corridor.addConnection(yourRoom);
        library.addConnection(corridor);
        corridor.addConnection(library);
        johnsRoom.addConnection(corridor);
        corridor.addConnection(johnsRoom);

        return yourRoom;
    }

    /**
     * @author Colin Rothwell
     */
    protected Room _makeYourRoom() {
        Room r = new Room(0,
                "your room",
                "A small, dingy room. Sheets of paper cover the floor, along " +
                "with trays stolen from hall, complete with plates of moldering " +
                "food. One of the molds appears to twitch periodically, and the " +
                "radiator emits a worrying smell.");
        r.addItem(_makePen());
        r.addItem(_makePaper());
        r.addNPC(_makeLibrarian());
        return r;
    }

    protected Item _makePen() {
        return new Item("pen", "pen", "A nice pen");
    }

    protected Item _makePaper() {
        return new Item("paper", "paper", "A4, Margin, Lined. 80gsm, High quality.");
    }

    protected Transformation _makeIncompleteSupervisionWork() {
        List<String> in = new ArrayList<String>();
        in.add("pen");
        in.add("paper");
        List<Item> out = new ArrayList<Item>();
        out.add(new Item("isw", "incomplete supervision work", 
                    "you tried your best, but you just don't know enough to " +
                    "complete this supervision work"));
        return new Transformation(in, out);
    }

    protected Transformation _makeSWTransform() {
        List<String> in = new ArrayList<String>();
        in.add("isw");
        in.add("textbook");
        List<Item> out = new ArrayList<Item>();
        out.add(_makeSupervisionWork());
        return new Transformation(in, out);
    }

    /**
     * @author Colin Rothwell
     */
    protected Room _makeLibrary() {
        Room r = new Room(1,
                "the library",
                "The shelves locked as a result of recent vandalism, borrowable " +
                "only with the express permission of the ferocious librarian, " +
                "a few flickering bulbs cast a dingy light over the masses of " +
                "silently oppressed students, bent and labouring at their desks. ");
        r.addNPC(_makeLibrarian());
        return r;
    }

    /**
     * @author Colin Rothwell
     */
    protected NPC _makeLibrarian() {
        ConversationState start = new ConversationState(
                "Well?! What do you want?! Can't you see I'm busy!");

        ConversationState askForBook = new ConversationState(
                "Hmm, I think I do have something like that. But if you don't " +
                "bring it back by the due date, I will reach into your chest, " +
                "break off your ribs, and skewer you with them.");

        start.putSpeech("Do you have a book that could help me with this piece of " +
                "very difficult supervision work, please?", askForBook);

        ConversationState end = new ConversationState(
                "There you go. Remember what I said about returns!");

        askForBook.putAcceptableItem(_makeTextBook(), end);

        return new NPC(0, "librarian", //"The Ferocious Librarian", 
                "The intimidating librarian. She appears to be busy tending to " +
                "the books. You'd better have a good reason to talk to her.",
                start);
    }

    protected Item _makeTextBook() {
        return new Item("textbook", "textbook", "Algorithms and Shiz, Yo! 3rd Edition");
    }

    /**
     * @author Colin Rothwell
     */
    protected Room _makeJohnsRoom() {
        Room r = new Room(2,
                "47E, John's room",
                "47E, The legendary home of the legendary John Fawcett. The " +
                "walls are lined with skulls of students who have failed to " +
                "hand in their supervision work on time, in various states of " +
                "decay. A large television sits on the floor. You have never " +
                "quite worked up the courage to ask what it's actually for.");
        r.addNPC(_makeJohn());
        return r;
    }

    /**
     * @author Colin Rothwell
     */
    protected NPC _makeJohn() {
        ConversationState start = new ConversationState("Where is your " +
                "supervision work?! You can't come on my famous MOUNTAIN " +
                "BIKING EXPEDITION if you don't hand in your supervision work!");

        ConversationState win = new ConversationState("This is excellent work! " +
                "Let's go mountain biking!");
        win.setWinningState(true);

        start.putGiveableItem(_makeSupervisionWork(), win);

        return new NPC(1, "John", 
                "Dr. John Fawcett, MA PhD (Cantab). " + 
                "The legendary DoS for Churchill College CompScis. He is " +
                "rumoured to never leave any survivors. Surprisingly keen on " +
                "mountain biking.", start);
    }

    protected Item _makeSupervisionWork() {
        return new Item("work", "Supervision Work", "This should get you that elusive first!");
    }

    /**
     * @author Colin Rothwell
     */
    protected Room _makeCorridor() {
        return new Room(3,
                "the corridor",
                "An angry corridor. Its vicious snaps and snarls make you jump " +
                "as you creep from one destination to the next.");
    }

    public static void main(String [] args) {
        BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
        Game game = new Game();
        System.out.println(preamble);
        game.printer.printState();
        boolean speaking = false;
        Command command = null;
        UserResponse resp = null;
        while (true) {
            if (!speaking) {
                System.out.print(">> ");
                try {
                    command = new Command(rdr.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                resp = command.execute(game.player);
            }

            game.printer.printResponse(resp);

            switch (resp.getType()) {
                case CONVERSATION:
                    speaking = true;
                    ConversationResponse cresp = (ConversationResponse)resp;

                    if (!cresp.hasActions()) {
                        speaking = false;
                        break;
                    }

                    System.out.print("in conversation >> ");

                    int choice = -1;
                    try {
                        choice = Integer.parseInt(rdr.readLine());
                    } catch (IOException ex) {
                        System.out.println("That is not a valid choice! Try again.");
                    }
                    if (choice == cresp.getHighestAction()) {
                        speaking = false;
                        break;
                    }
                    ConversationResult crslt = cresp.performAction(choice);
                    switch (crslt.getType()) {
                        case GOTITEM:
                            game.player.addItem(crslt.getItem());
                            break;
                        case GAVEITEM:
                            game.player.removeItem(crslt.getItem());
                            break;
                    }
                    resp = game.player.talkTo(cresp.getPartner().getName());
                    break;
                case WIN:
                case QUIT:
                    return;
            }
        }
    }
}
