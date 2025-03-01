public enum CardRank {
    Ace,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King;

    public static CardRank fromInt(int x) {
        return switch (x) {
            case 2 -> Two;
            case 3 -> Three;
            case 4 -> Four;
            case 5 -> Five;
            case 6 -> Six;
            case 7 -> Seven;
            case 8 -> Eight;
            case 9 -> Nine;
            case 10 -> Ten;
            default -> Ace;
        };
    }

    public static CardRank fromString(String str) {
        return switch (str) {
            case "ace" -> Ace;
            case "jack" -> Jack;
            case "king" -> King;
            case "queen" -> Queen;
            default -> Ace;
        };
    }
}
