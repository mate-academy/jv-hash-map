package core.basesyntax;

import java.util.Objects;

public class Car {

    private final String model;
    private final String color;

    public Car(String model, String color) {
        this.model = model;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(model, car.model) &&
                Objects.equals(color, car.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, color);
    }
}
