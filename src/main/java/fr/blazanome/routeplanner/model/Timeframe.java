package fr.blazanome.routeplanner.model;

public enum Timeframe {
    H8(8, "8h-9h"),
    H9(9, "9h-10h"),
    H10(10, "10h-11h"),
    H11(11, "11h-12h");

    private final int start;
    private final String label;

    private Timeframe(int start, String label) {
        this.start = start;
        this.label = label;
    }

    public int getStart() {
        return this.start;
    }

    @Override
    public String toString() {
        return this.label;
    }

    public String getLabel() {
        return this.label;
    }
}
