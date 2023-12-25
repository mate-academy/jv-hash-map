package core.basesyntax;

import java.util.Objects;

public class Car {

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

    public Car(String model, String color) {
        this.model = model;
        this.color = color;
    }

    public Car() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() == this.getClass()) {
            Car object = (Car) o;
            return ((this.color != null && object.color != null)
                    ? this.color.equals(object.color) : (this.color == null && object.color == null))
                    && ((this.model != null && object.model != null)
                    ? this.model.equals(object.model) : (this.model == null && object.model == null));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17 * (this.color == null ? 0 : color.hashCode()) + (this.model == null ? 0 : model.hashCode());
    }
}
