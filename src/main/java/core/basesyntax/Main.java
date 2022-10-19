package core.basesyntax;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        int a = 4;
        System.out.println(a << 1);
        int b = 18;
        int hash = b % 16;
        int index = hash & (16 - 1);
        System.out.println(b);
        System.out.println(hash);
        System.out.println(index);
    }
}
