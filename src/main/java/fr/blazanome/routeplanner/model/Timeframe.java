package fr.blazanome.routeplanner.model;

public enum Timeframe {
    H8("8h-9h"),
    H9("9h-10h"),
    H10("10h-11h"),
    H11("11h-12h");

    public final String label;

    private Timeframe(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
