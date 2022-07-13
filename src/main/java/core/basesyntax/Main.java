package core.basesyntax;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int n = -336;
        Integer num = 16;
        int i = num.hashCode() & (n - 1);
        System.out.println(i);

        Map<Integer, Integer> map = new HashMap<>();
        map.put(-3136, 5);
        map.put(null, 2);
        map.put(null, 4);
        System.out.println(map.get(null));
    }
}
