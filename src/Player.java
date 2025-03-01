public class Player {
    private int Balance;
    public int getBalance() {
        return Balance;
    }

    private Card Card1;
    public Card getCard1() {
        return Card1;
    }
    public void setCard1(Card card) {
        Card1 = card;
    }

    private Card Card2;
    public Card getCard2() {
        return Card2;
    }
    public void setCard2(Card card) {
        Card2 = card;
    }


    public Player() {
        Balance = 1_000;
    }

}
