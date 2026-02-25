package core.basesyntax;

import java.util.Objects;

public class Plane {
    private static final int INITIAL_HASH = 17;
    private static final int MULTIPLIER = 31;

    private String model;
    private String color;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Plane(String model, String color) {
        this.model = model;
        this.color = color;
    }

    public Plane() {
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
        int result = INITIAL_HASH;
        result = MULTIPLIER * result + (model == null ? 0 : model.hashCode());
        result = MULTIPLIER * result + (color == null ? 0 : color.hashCode());

        return result;
    }
}
