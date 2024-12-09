package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size; // Поточний розмір мапи (кількість елементів)
    private Node<K, V>[] table; // Масив, що зберігає вузли мапи
    private int capacity = 16; // Початковий розмір таблиці
    private double loadFactor = 0.75; // Фактор навантаження для розширення таблиці
    private int threshold = (int) (capacity * loadFactor);
    // Поріг, при якому потрібно розширювати таблицю

    // Статичний клас для представлення елементів мапи (ключ-значення)
    static class Node<K, V> {
        private final K key; // Ключ елемента
        private V value; // Значення елемента
        private Node<K, V> next; // Посилання на наступний вузол у випадку колізій

        public Node(K key, V value) {
            this.key = key; // Ініціалізація ключа
            this.value = value; // Ініціалізація значення
            this.next = null; // Ініціалізація посилання на наступний вузол як null
        }
    }

    // Метод для обчислення індексу в таблиці за допомогою хеш-коду ключа
    public int getIndex(K key) {
        if (key == null) { // Якщо ключ дорівнює null, то повертаємо 0
            return 0;
        }
        int hashCode = key.hashCode(); // Отримуємо хеш-код ключа
        return Math.abs(hashCode) % capacity;
        // Повертаємо індекс після обчислення модуля та гарантуємо, що він буде додатнім
    }

    // Метод для вставки елемента в хеш-мапу
    @Override
    public void put(K key, V value) {
        // Якщо розмір більше порогу, подвоюємо розмір таблиці
        if (size >= threshold) {
            capacity = capacity * 2; // Подвоюємо місткість таблиці
            threshold = (int) (capacity * loadFactor); // Перераховуємо поріг для нової місткості
            Node<K, V>[] newTable = new Node[capacity];
            // Створюємо нову таблицю з подвоєним розміром

            // Переміщаємо елементи в нову таблицю
            for (int i = 0; i < table.length; i++) { // Перебираємо стару таблицю
                Node<K, V> current = table[i];
                // Отримуємо поточний вузол
                while (current != null) {
                    // Перебираємо всі вузли, що знаходяться на поточному індексі
                    int newHash = (current.key == null) ? 0
                            : Math.abs(current.key.hashCode() % capacity);
                    // Обчислюємо новий індекс для поточного вузла
                    Node<K, V> next = current.next; // Запам'ятовуємо наступний вузол
                    current.next = newTable[newHash];
                    // Встановлюємо зв'язок з новим ланцюгом на новому індексі
                    newTable[newHash] = current; // Поміщаємо поточний вузол в нову таблицю
                    current = next; // Переходимо до наступного вузла
                }
            }
            table = newTable; // Оновлюємо таблицю
        }

        // Створюємо новий вузол
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key); // Отримуємо індекс для нового елемента

        // Перевіряємо, чи є елемент з таким ключем
        Node<K, V> current = table[index];
        while (current != null) { // Перебираємо всі елементи в ланцюзі
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                // Якщо знайдений вузол з таким самим ключем
                current.value = value; // Оновлюємо значення
                return; // Виходимо, так як елемент вже знайдений
            }
            current = current.next; // Переходимо до наступного вузла в ланцюзі
        }

        // Якщо елемент з таким ключем не знайдений, додаємо новий елемент в ланцюг
        newNode.next = table[index]; // Встановлюємо новий вузол на початок ланцюга
        table[index] = newNode; // Оновлюємо таблицю, додаючи новий елемент

        size++; // Збільшуємо розмір мапи
    }

    // Метод для отримання значення за ключем
    @Override
    public V getValue(K key) {
        int index = getIndex(key); // Отримуємо індекс за допомогою ключа
        Node<K, V> current = table[index]; // Отримуємо перший вузол на цьому індексі
        while (current != null) { // Перебираємо ланцюг вузлів
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                // Якщо знайдено вузол з таким самим ключем
                return current.value; // Повертаємо значення
            }
            current = current.next; // Переходимо до наступного вузла
        }
        return null; // Якщо елемент не знайдений, повертаємо null
    }

    // Метод для отримання розміру мапи
    @Override
    public int getSize() {
        return size; // Повертаємо поточний розмір мапи
    }
}
