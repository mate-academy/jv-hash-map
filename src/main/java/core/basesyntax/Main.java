package core.basesyntax;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Book book1 = new Book("Harry", 2012);
        Book book2 = new Book("Harry", 2015);
        Book book3 = new Book("Not Harry", 2010);
        MyHashMap<Book, Integer> map = new MyHashMap<>();
        map.put(book1, book1.release);
        map.put(book2, book2.release);
        map.put(book3, book3.release);

        System.out.println(map.getValue(book2));
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.equals(obj)) {
            return true;
        }
        return false;
    }
}
class Book {
    String title;
    int release;

    public Book(String title, int release) {
        this.title = title;
        this.release = release;
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 17 * res + release;
        res = 17 * res + title.hashCode();
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
