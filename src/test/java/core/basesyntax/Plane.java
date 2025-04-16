package core.basesyntax;

import java.util.Objects;

public class Plane {
    private final String model;
    private final String color;

    public Plane(String model, String color) {
        this.model = model;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return Objects.equals(model, plane.model) &&
                Objects.equals(color, plane.color);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
