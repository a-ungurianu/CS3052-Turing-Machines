public enum Move {
    Left, Right, Stay;

    public static Move fromSymbol(String symbol) {
        if(symbol.equals("L")) {
            return Move.Left;
        }
        if(symbol.equals("R")) {
            return Move.Right;
        }
        if(symbol.equals("S")) {
            return Move.Stay;
        }

        throw new IllegalArgumentException("Unknown move symbol :" + symbol);
    }
}
