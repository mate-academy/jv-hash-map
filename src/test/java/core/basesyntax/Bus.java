package core.basesyntax;

import java.util.Objects;

public class Bus {
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

    public Bus(String model, String color) {
        this.model = model;
        this.color = color;
    }

    public Bus() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bus bus = (Bus) obj;
        return Objects.equals(model, bus.model) && Objects.equals(color, bus.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, color);
    }
}
