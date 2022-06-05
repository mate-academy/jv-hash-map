package core.basesyntax;

public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int hashCode() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !o.getClass().equals(User.class)) {
            return false;
        }
        User current = (User) o;
        return current.age == this.age
                && (current.name == this.name || current.name != null && current.name.equals(this.name));
    }

    @Override
    public String toString() {
        return name + " " + age;
    }
}
