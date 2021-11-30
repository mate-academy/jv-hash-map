package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;

    private int size;

    private final float loadFactor;

    private int threshold;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        } //Bitwise exclusive OR(returns 0 if both bits are the same)

        public final boolean equals(Object o) {

            if (o == this) {
                return true;
            }

            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        boolean rewriteExisting = true;
        Node<K,V>[] tab = table;
        int tabLen = 0;
        if (tab != null) {
            tabLen = tab.length;
        }
        Node<K,V> curr;
        int i;
        if (tab == null || tabLen == 0) {
            tab = resize();
            tabLen = tab.length;
        }
        //Bitwise AND (&) returns 1 if and only if both bits are 1, else returns 0
        i = (tabLen - 1) & hash;//index of bucket where to put value
        curr = tab[i];//посмотрим что там было в бакете до записи в него
        if (curr == null) { //если по данному индексу в table еще нет ноды
            tab[i] = newNode(hash, key, value, null);
        } else { //если нода уже есть
            Node<K,V> existing = null; // existing mapping for key
            if (curr.hash == hash && (curr.key == key || (key != null && key.equals(curr.key)))) {
                existing = curr;//если key и hash совпадают, то скопируем curr в existing
            } else { //проверим на совпадение по key и hash всю цепочку нод .next от curr до конца
                Node<K,V> next;
                while (true) { //вечный цикл (мы не знаем сколько всего нод в списке)
                    next = curr.next;
                    if (next == null) { //значит проверили все ноды и совпадения не выявлено
                        curr.next = newNode(hash, key, value, null);
                        //дописываем в конец списка новую ноду
                        break;//и прерываем цикл
                    }
                    //если на предыдущих итерациях не сработал break в выше расположенном if(),
                    //значит до конца списка еще не дошли и сечас мы проверим у next его key и hash
                    if (next.hash == hash && (next.key == key
                            || (key != null && key.equals(next.key)))) {
                        existing = next;//если key и hash совпадают, то скопируем next в existing
                        break;//и прерываем цикл
                    }
                    curr = next;//переходим к следующей ноде, но уже на следующей итерации
                }
            }
            if (existing != null) { //если с таким key и hash уже была запись в списке
                //то перезапишем value
                V oldValue = existing.value;//скопируем старое значение в oldValue
                if (rewriteExisting || oldValue == null) {
                    //только если rewriteExisting==true или oldValue==null
                    existing.value = value;//запишем в .value новое значение
                }
                return;
            }
        }
        if (++size > threshold) { //если после увеличения на 1 size > threshold
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> e = getNode(hash(key), key);
        return e == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    } //^ Bitwise exclusive OR (returns 0 if both bits are the same, else returns 1)
    //>>> Unsigned Right Shift Operator (11110000 >>> 2 = 00111100)

    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap;
        int newThr = 0;
        if (oldCap > 0) {
            newCap = oldCap << 1; //double capacity
            if (oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1; // double threshold
            } else if (oldThr > 0) { //oldCap=0,oldThr>0 (initial capacity was placed in threshold)
                newCap = oldThr;
            }
        } else { //oldCap=0,oldThr=0 (zero initial threshold signifies using defaults)
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * newCap);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) { //нужно перенести все элементы из oldTab в newTab
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> oldTabNode = oldTab[j];
                if (oldTabNode != null) {
                    oldTab[j] = null;
                    if (oldTabNode.next == null) { //если oldTabNode единственнный в бакете
                        int i = (newCap - 1) & oldTabNode.hash; //индекс бакета в newTab
                        newTab[i] = oldTabNode;
                    } else { //oldTabNode не единственнный в бакете        preserve order
                        Node<K,V> loHead = null;
                        Node<K,V> loTail = null;
                        Node<K,V> hiHead = null;
                        Node<K,V> hiTail = null;
                        Node<K,V> next;
                        do {
                            next = oldTabNode.next;
                            if ((oldTabNode.hash & oldCap) == 0) {
                                //hash=xXxxxx & oldCap=010000//2^4=16
                                //X=0||1 result of & = 000000||010000
                                //distribute them into "hi" and "lo" bin,
                                // depending on the significant bit inside the hash
                                if (loTail == null) { //first time loTail==null will be true
                                    loHead = oldTabNode;
                                } else { //loTail != null
                                    loTail.next = oldTabNode;
                                }
                                loTail = oldTabNode;
                            } else { //placing oldTabNode into "hi" bin
                                if (hiTail == null) {
                                    hiHead = oldTabNode;
                                } else {
                                    hiTail.next = oldTabNode;
                                }
                                hiTail = oldTabNode;
                            }
                            oldTabNode = next;
                        } while (oldTabNode != null);
                        //после того как мы переписали все ссылки .next от loHead до loTail
                        //и от hiHead до hiTail
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab;
        int n;
        if ((tab = table) != null && (n = tab.length) > 0) {
            Node<K,V> first = tab[(n - 1) & hash];
            K k = first.key;
            Node<K,V> e;
            if (first.hash == hash && (k == key || (key != null && key.equals(k)))) {
                return first;
            }
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

}
