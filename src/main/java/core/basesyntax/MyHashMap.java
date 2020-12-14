package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int LOAD_FACTOR = 2;
    private Node<K, V>[] array;
    private static int capacity;
    private int size = 0;
    private int threshold;

    MyHashMap() {
        array = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
        size++;
    }

    private void putValue(int position, K key, V value) {
        Node<K, V>[] tempArray;
        int tempHash = hash(key);

        //в случае если мапа пустая
        if (array == null || array.length == 0) {
            array = resize();
        }
        /* в случае, если мапа не пустая и два варианта
        1. Если ячейка массива пустая, добавим.
        2. Если ячейка массива не пустая, добавим.
         */
        if (array[tempHash] == null) {
            array[tempHash] = new Node<>(tempHash, key, value, null);
        } else {
            // Нужна доп проверка, а есть ли уже этот ключ TODO
            Node<K, V> tempNode = array[tempHash];
            while (tempNode.next != null) {
                tempNode = tempNode.next;
            }
            tempNode = new Node<>(tempHash, key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] tempArray = array;
        array = new Node[tempArray.length * LOAD_FACTOR];
        for (Node<K, V> tempNode : tempArray) {
            if (tempNode != null) {
                put(tempNode.key, tempNode.value);
                if (tempNode.next != null) {

                }
            }
        }


        return array;
    }

    private Node<K, V> takeLastNode(Node<K, V> node) {
        while (node.next != null) {
            node = node.next;
        }
        return node;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }


    }
}
/*
Хешмапа
1. Имеет поле Node[] - размером в 16 👌
a) Node имеет поле int hash  👌
2. Внутри метода put, есть метод который считает хеш ключа(правка в Б), который передается
подпункт по put, вызывает метод putVal(hash(key), key, value ... ) остальное вряд ли нужно 👌
а)   в случае null = 0 || key.hashcode() ^ >>> 16 (скорее всего просто делиться по остатку на 16) 👌
б) (!) Вычисляет не хеш, а место в массиве куда этот key можно закинуть. 👌
с) Пункт хеш понадобиться для get(key) (мы вычисляем по этому значению). 👌
3. Есть внутренний класс Node(Нагло скопировал) 👌
a) Создается нода, в которую мы помещаем ключ/value/hash по hash позиции. 👌
б) В случае put, если key с одинаковым хешем equals key с уже готовым ключем, второй теряется в небытие ;(
4. В методе put, вызываем метод putValue(hash, key, value) 👌
В ресайзе
1. Мы проверяем что основная таблица (array) в нулл или length == 0
В случае тру, мы инициализируем через resize() изничальную хешмапу. и где-то запоминаем длину(а надо?)
а) Метод ресайз возвращает массив нового.
Подпункты по ресайзу:
1) Проверяем старое значение capacity
В случае если capactity == 0 && old thr == 0 TODO
capacity = 16
newThr = load factor * 16
thr нужен, чтоб определиться когда нам нужно делать resize()
if(++size



 */