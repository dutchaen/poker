import java.util.*;

public class Game {

    private static Player LocalPlayer;
    public static Player getLocalPlayer() {
        return LocalPlayer;
    }

    private static Player EnemyPlayer;
    public static Player getEnemyPlayer() {
        return EnemyPlayer;
    }

    private static List<Card> Deck;
    public static List<Card> getDeck() {
        return Deck;
    }

    private static int Bet;
    public static int getBet() {
        return Bet;
    }
    public static void setBet(int bet) {
        Bet = bet;
    }

    // todo
    public static boolean canCheck() {
        return false;
    }

    private static int Pot;
    public static int getPot() {
        return Pot;
    }

    private static GameState State;
    public static GameState getState() {
        return State;
    }

    private static Card[] CommunityCards;
    public static Card[] getCommunityCards() {
        return CommunityCards;
    }

    public static void init() {
        LocalPlayer = new Player();
        EnemyPlayer = new Player();

        initDeck();
        CommunityCards = null;
    }

    private static void initDeck() {
        Deck = new ArrayList<>();

        for (CardRank rank : CardRank.values()) {
            for (CardSuit suit : CardSuit.values()) {
                Deck.add(new Card(rank, suit));
            }
        }

        Collections.shuffle(Deck);
    }

    public static void resetGame() {
        Pot = 0;
        State = GameState.Flop;

        if (CommunityCards != null) {
            Deck.addAll(Arrays.asList(CommunityCards));
            Collections.shuffle(Deck);
        }

        Deck.add(LocalPlayer.getCard1());
        Deck.add(LocalPlayer.getCard2());

        Deck.add(EnemyPlayer.getCard1());
        Deck.add(EnemyPlayer.getCard2());


        CommunityCards = new Card[5];
        for (int i = 0; i < 5; i++) {
            CommunityCards[i] = getNextCard();
        }

        Collections.shuffle(Deck);

        LocalPlayer.setCard1(getNextCard());
        LocalPlayer.setCard2(getNextCard());

        EnemyPlayer.setCard1(getNextCard());
        EnemyPlayer.setCard2(getNextCard());

        Collections.shuffle(Deck);
    }

    private static Card getNextCard() {
        return Deck.remove(0);
    }

}
