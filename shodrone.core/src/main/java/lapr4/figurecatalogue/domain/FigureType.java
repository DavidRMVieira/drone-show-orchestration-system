package lapr4.figurecatalogue.domain;

public enum FigureType {
    STATIC,
    DYNAMIC;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
