package core.basesyntax;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getValue(K key);

    int getSize();

    public static class Entry<K, V>{
        K key;
        V value;

        public Entry(K key, V value){
            this.key = key;
            this.value = value;
        }

        public K getKey(){
            return key;
        }

        public V getValue(){
            return value;
        }

        @Override
        public String toString(){
            return "[" + key + ", " + value + "]";
        }
    }
}
