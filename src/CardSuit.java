public enum CardSuit {
    Hearts,
    Diamonds,
    Clubs,
    Spades;

    public static CardSuit fromString(String str) {
        return switch (str) {
            case "hearts" -> Hearts;
            case "diamonds" -> Diamonds;
            case "clubs" -> Clubs;
            case "spades" -> Spades;
            default -> Hearts;
        };
    }
}
