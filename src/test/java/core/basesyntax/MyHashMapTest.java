package core.basesyntax;

import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

public class MyHashMapTest {
    private static final Car firstCar = new Car("Audi", "black");
    private static final Car secondCar = new Car("Bmw", "red");
    private static final Car thirdCar = new Car("Mercedes", "grey");

    private static final Car sameFirstCar = new Car("Audi", "black");
    private static final Car sameSecondCar = new Car("Bmw", "red");
    private static final Car sameThirdCar = new Car("Mercedes", "grey");

    private static final Plane firstPlane = new Plane("Mria", "white");
    private static final Plane secondPlane = new Plane("Boing", "white");
    private static final Plane thirdPlane = new Plane("F3T", "grey");

    private static final Bus firstBus = new Bus("FirstBus", "white");
    private static final Bus secondBus = new Bus("SecondBus", "white");
    private static final Bus thirdBus = new Bus("ThirdBus", "grey");

    @Test
    public void getByNonExistedKey() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        Assert.assertNull("Test failed! If key doesn't exist, we should return null.",
                myHashMap.getValue(firstCar));
    }

    @Test
    public void putAndGetOk() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstCar, 3);
        myHashMap.put(secondCar, 5);
        myHashMap.put(thirdCar, 1);

        Assert.assertEquals("Test failed! The size isn't correct. Expected 3 but was "
                + myHashMap.getSize(), 3, myHashMap.getSize());

        Integer firstActualValue = myHashMap.getValue(firstCar);
        Integer secondActualValue = myHashMap.getValue(secondCar);
        Integer thirdActualValue = myHashMap.getValue(thirdCar);
        Assert.assertEquals("Test failed! HashMap expects to contain value 3 for key `firstCar`,"
                + " but was " + firstActualValue, Integer.valueOf(3), firstActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 5 for key `secondCar`,"
                + " but was " + secondActualValue, Integer.valueOf(5), secondActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 1 for key `thirdCar`,"
                + " but was " + thirdActualValue, Integer.valueOf(1), thirdActualValue);
    }

    @Test
    public void putTheSameElement() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstCar, 3);
        myHashMap.put(secondCar, 5);
        myHashMap.put(thirdCar, 1);
        myHashMap.put(sameFirstCar, 3);
        myHashMap.put(sameSecondCar, 5);
        myHashMap.put(sameThirdCar, 1);

        Assert.assertEquals("Test failed! We should add checking if the same element "
                + "exists in the map", 3, myHashMap.getSize());

        Integer firstActualValue = myHashMap.getValue(firstCar);
        Integer secondActualValue = myHashMap.getValue(secondCar);
        Integer thirdActualValue = myHashMap.getValue(thirdCar);
        Assert.assertEquals("Test failed! HashMap expects to contain value 3 for key `firstCar`,"
                + " but was " + firstActualValue, Integer.valueOf(3), firstActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 5 for key `secondCar`,"
                + " but was " + secondActualValue, Integer.valueOf(5), secondActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 1 for key `thirdCar`,"
                + " but was " + thirdActualValue, Integer.valueOf(1), thirdActualValue);
    }

    @Test
    public void putAndGetByNullKey() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(null, 3);
        Integer firstActualValue = myHashMap.getValue(null);
        Assert.assertEquals("Test failed! HashMap expects to contain value 3 for null key,"
                + " but was " + firstActualValue, Integer.valueOf(3), firstActualValue);
        Assert.assertEquals("Test failed! The size isn't correct. Expected 1 but was "
                + myHashMap.getSize(), 1, myHashMap.getSize());
        myHashMap.put(null, 5);
        Integer secondActualValue = myHashMap.getValue(null);
        Assert.assertEquals("Test failed! HashMap expects to contain value 5  for null key,"
                + " but was " + secondActualValue, Integer.valueOf(5), secondActualValue);
        Assert.assertEquals("Test failed! The size isn't correct. Expected 1 but was "
                + myHashMap.getSize(), 1, myHashMap.getSize());
    }

    @Test
    public void putAndGetWithCollision() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 3);
        myHashMap.put(secondPlane, 5);
        myHashMap.put(thirdPlane, 1);

        Assert.assertEquals("Test failed! The size isn't correct. Expected 3 but was "
                + myHashMap.getSize(), 3, myHashMap.getSize());

        Integer firstActualValue = myHashMap.getValue(firstPlane);
        Integer secondActualValue = myHashMap.getValue(secondPlane);
        Integer thirdActualValue = myHashMap.getValue(thirdPlane);
        Assert.assertEquals("Test failed! HashMap expects to contain value 3 for key `firstPlane`,"
                + " but was " + firstActualValue, Integer.valueOf(3), firstActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 5 for key `secondPlane`,"
                + " but was " + secondActualValue, Integer.valueOf(5), secondActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 1 for key `thirdPlane`,"
                + " but was " + thirdActualValue, Integer.valueOf(1), thirdActualValue);
    }

    @Test
    public void putAndGetWithCollisionAndReplaceValue() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 3);
        myHashMap.put(secondPlane, 5);
        myHashMap.put(thirdPlane, 1);
        myHashMap.put(firstPlane, 4);

        Assert.assertEquals("Test failed! The size isn't correct. Expected 3 but was "
                + myHashMap.getSize(), 3, myHashMap.getSize());

        Integer firstActualValue = myHashMap.getValue(firstPlane);
        Integer secondActualValue = myHashMap.getValue(secondPlane);
        Integer thirdActualValue = myHashMap.getValue(thirdPlane);
        Assert.assertEquals("Test failed! HashMap expects to contain value 3 for key `firstPlane`,"
                + " but was " + firstActualValue, Integer.valueOf(4), firstActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 5 for key `secondPlane`,"
                + " but was " + secondActualValue, Integer.valueOf(5), secondActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 1 for key `thirdPlane`,"
                + " but was " + thirdActualValue, Integer.valueOf(1), thirdActualValue);
    }

    @Test
    public void putAndGetByNullKeyWithCollision() {
        MyMap<Bus, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstBus, 3);
        myHashMap.put(null, 4);
        myHashMap.put(secondBus, 5);
        myHashMap.put(null, 10);
        myHashMap.put(thirdBus, 1);

        Assert.assertEquals("Test failed! The size isn't correct. Expected 4 but was "
                + myHashMap.getSize(), 4, myHashMap.getSize());

        Integer firstActualValue = myHashMap.getValue(firstBus);
        Integer secondActualValue = myHashMap.getValue(secondBus);
        Integer thirdActualValue = myHashMap.getValue(thirdBus);
        Integer fourthActualValue = myHashMap.getValue(null);
        Assert.assertEquals("Test failed! HashMap expects to contain value 3 for key `firstBus`,"
                + " but was " + firstActualValue, Integer.valueOf(3), firstActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 5 for key `secondBus`,"
                + " but was " + secondActualValue, Integer.valueOf(5), secondActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 1 for key `thirdBus`,"
                + " but was " + thirdActualValue, Integer.valueOf(1), thirdActualValue);
        Assert.assertEquals("Test failed! HashMap expects to contain value 10 for null key,"
                + " but was " + fourthActualValue, Integer.valueOf(10), fourthActualValue);
    }

    @Test
    public void putAndGetTheOverriddenValueByKey() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstCar, 3);
        Assert.assertEquals("Test failed! The size isn't correct. Expected 1 but was "
                + myHashMap.getSize(), 1, myHashMap.getSize());
        Integer firstActualValue = myHashMap.getValue(firstCar);
        Assert.assertEquals("Test failed! HashMap expects to contain value 3 for key `firstCar`,"
                + " but was " + firstActualValue, Integer.valueOf(3), firstActualValue);
        myHashMap.put(firstCar, 5);
        Assert.assertEquals("Test failed! The size isn't correct. Expected 1 but was "
                + myHashMap.getSize(), 1, myHashMap.getSize());
        Integer secondActualValue = myHashMap.getValue(firstCar);
        Assert.assertEquals("Test failed! HashMap expects to contain value 5  for key `firstCar`,"
                + " but was " + secondActualValue, Integer.valueOf(5), secondActualValue);
    }

    @Test
    public void removeFromHashWithoutCollisions() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstCar, 1000);
        myHashMap.put(secondCar, 1500);
        myHashMap.put(thirdCar, 2000);
        Integer firstActualValue = myHashMap.remove(secondCar);
        Assert.assertEquals("Test failed! Hash should return the value of removed key-value pair.",
                Integer.valueOf(1500), firstActualValue);
        Assert.assertEquals("Test failed! Hash size should decrease after key-value pair remove.",
                2, myHashMap.getSize());
        Assert.assertEquals("Test failed! Hash should still contain the other key-value pairs "
                + "which were not removed.", Integer.valueOf(1000), myHashMap.getValue(firstCar));
        Assert.assertEquals("Test failed! Hash should still contain the other key-value pairs "
                + "which were not removed.", Integer.valueOf(2000), myHashMap.getValue(thirdCar));
        Assert.assertNull("Test failed! Hash should not contain the removed key-value pair "
                + "after remove.", myHashMap.getValue(secondCar));
    }

    @Test
    public void removeFromHashWithCollisions() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 1000);
        myHashMap.put(secondPlane, 1500);
        myHashMap.put(thirdPlane, 2000);
        Integer firstActualValue = myHashMap.remove(secondPlane);
        Assert.assertEquals("Test failed! Hash should return the value of removed key-value pair.",
                Integer.valueOf(1500), firstActualValue);
        Assert.assertEquals("Test failed! Hash size should decrease after key-value pair remove.",
                2, myHashMap.getSize());
        Assert.assertEquals("Test failed! Hash should still contain the other key-value pairs "
                + "which were not removed.", Integer.valueOf(1000), myHashMap.getValue(firstPlane));
        Assert.assertEquals("Test failed! Hash should still contain the other key-value pairs "
                + "which were not removed.", Integer.valueOf(2000), myHashMap.getValue(thirdPlane));
        Assert.assertNull("Test failed! Hash should not contain the removed key-value pair "
                + "after remove.", myHashMap.getValue(secondPlane));
    }

    @Test
    public void removeFromEmptyHashReturnsNull() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        Assert.assertNull("Test failed! Hash should return null if it doesn't contain any key-value pair.",
                myHashMap.remove(firstPlane));
    }

    @Test
    public void removeByNonExistentKeyReturnsNull() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 1000);
        myHashMap.put(secondPlane, 1500);
        Assert.assertNull("Test failed! Hash should return null if it doesn't contain the key-value pair.",
                myHashMap.remove(thirdPlane));
    }

    @Test
    public void getKeysReturnsCorrectListOfKeysWithoutCollisions() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstCar, 1000);
        myHashMap.put(secondCar, 1500);
        myHashMap.put(thirdCar, 2000);
        List<Car> listOfKeys = myHashMap.getKeys();
        Assert.assertEquals("Test failed! List of keys must have correct size.",
                3, listOfKeys.size());
        Assert.assertTrue("Test failed! List of keys must have correct keys.",
                listOfKeys.contains(firstCar));
        Assert.assertTrue("Test failed! List of keys must have correct keys.",
                listOfKeys.contains(secondCar));
        Assert.assertTrue("Test failed! List of keys must have correct keys.",
                listOfKeys.contains(thirdCar));
    }

    @Test
    public void getKeysReturnsCorrectListOfKeysWithCollisions() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 1000);
        myHashMap.put(secondPlane, 1500);
        myHashMap.put(thirdPlane, 2000);
        List<Plane> listOfKeys = myHashMap.getKeys();
        Assert.assertEquals("Test failed! List of keys must have correct size.",
                3, listOfKeys.size());
        Assert.assertTrue("Test failed! List of keys must have correct keys.",
                listOfKeys.contains(firstPlane));
        Assert.assertTrue("Test failed! List of keys must have correct keys.",
                listOfKeys.contains(secondPlane));
        Assert.assertTrue("Test failed! List of keys must have correct keys.",
                listOfKeys.contains(thirdPlane));
    }

    @Test
    public void getKeysReturnsEmptyList() {
        List<Plane> expectedListOfKeys = List.of();
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        List<Plane> listOfKeys = myHashMap.getKeys();
        Assert.assertEquals("Test failed! Expected hash to return empty list of keys.",
                expectedListOfKeys, listOfKeys);
    }

    @Test
    public void getValuesReturnsCorrectListOfKeysWithoutCollisions() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstCar, 1000);
        myHashMap.put(secondCar, 1500);
        myHashMap.put(thirdCar, 2000);
        List<Integer> listOfValues = myHashMap.getValues();
        Assert.assertEquals("Test failed! List of keys must have correct size.",
                3, listOfValues.size());
        Assert.assertTrue("Test failed! List of keys must have correct values.",
                listOfValues.contains(1000));
        Assert.assertTrue("Test failed! List of keys must have correct values.",
                listOfValues.contains(1500));
        Assert.assertTrue("Test failed! List of keys must have correct values.",
                listOfValues.contains(2000));
    }

    @Test
    public void getValuesReturnsCorrectListOfKeysWithCollisions() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 1000);
        myHashMap.put(secondPlane, 1500);
        myHashMap.put(thirdPlane, 2000);
        List<Integer> listOfValues = myHashMap.getValues();
        Assert.assertEquals("Test failed! List of keys must have correct size.",
                3, listOfValues.size());
        Assert.assertTrue("Test failed! List of keys must have correct values.",
                listOfValues.contains(1000));
        Assert.assertTrue("Test failed! List of keys must have correct values.",
                listOfValues.contains(1500));
        Assert.assertTrue("Test failed! List of keys must have correct values.",
                listOfValues.contains(2000));
    }

    @Test
    public void getValuesReturnsEmptyList() {
        List<Integer> expectedListOfValues = List.of();
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        List<Integer> listOfValues = myHashMap.getValues();
        Assert.assertEquals("Test failed! Expected hash to return empty list of values.",
                expectedListOfValues, listOfValues);
    }

    @Test
    public void isEmptyReturnsTrueFromEmptyHash() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        Assert.assertTrue("Test failed! Empty hash should return true.",
                myHashMap.isEmpty());
    }

    @Test
    public void isEmptyReturnsFalseFromNonEmptyHash() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 1000);
        Assert.assertFalse("Test failed! Non-empty hash should return false.",
                myHashMap.isEmpty());
    }

    @Test
    public void checkTheHashMapIncrease() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 1000; i++) {
            Car car = new Car("model_" + i, "color_" + i);
            myHashMap.put(car, i);
        }
        Assert.assertEquals("Test failed! The size isn't correct. Expected 1000 but was "
                + myHashMap.getSize(), 1000, myHashMap.getSize());
        for (int i = 0; i < 1000; i++) {
            Assert.assertEquals(Integer.valueOf(i),
                    myHashMap.getValue(new Car("model_" + i, "color_" + i)));
        }
    }

    @Test
    public void getSizeOfEmptyHashMap() {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        Assert.assertEquals("Test failed! The size isn't correct. Expected 0 but was "
                + myHashMap.getSize(), 0, myHashMap.getSize());
    }

    @Test
    public void getSizeWithCollision() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstPlane, 3);
        myHashMap.put(secondPlane, 5);
        Assert.assertEquals("Test failed! The size isn't correct. Expected 2 but was "
                + myHashMap.getSize(), 2, myHashMap.getSize());
    }

    @Test
    public void getSizeWithCollisionAtZeroPosition() {
        MyMap<Bus, Integer> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 1000; i++) {
            Bus bus = new Bus("model_" + i, "color_" + i);
            myHashMap.put(bus, i);
        }
        Assert.assertEquals("Test failed! The size isn't correct. Expected 1000 but was "
                + myHashMap.getSize(), 1000, myHashMap.getSize());
        for (int i = 0; i < 1000; i++) {
            Assert.assertEquals(Integer.valueOf(i),
                    myHashMap.getValue(new Bus("model_" + i, "color_" + i)));
        }
    }

    @Test
    public void getSizeWithCollisionAtFirstPosition() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 1000; i++) {
            Plane plane = new Plane("model_" + i, "color_" + i);
            myHashMap.put(plane, i);
        }
        Assert.assertEquals("Test failed! The size isn't correct. Expected 1000 but was "
                + myHashMap.getSize(), 1000, myHashMap.getSize());
        for (int i = 0; i < 1000; i++) {
            Assert.assertEquals(Integer.valueOf(i),
                    myHashMap.getValue(new Plane("model_" + i, "color_" + i)));
        }
    }

    @Test
    public void existOnlyOneArrayFieldTest() {
        MyHashMap<?, ?> hashMap = new MyHashMap<>();
        Field[] declaredFields = hashMap.getClass().getDeclaredFields();
        int count = 0;
        for (Field field : declaredFields) {
            if (field.getType().isArray()) {
                count++;
            }
        }
        Assert.assertEquals("Class MyHashMap shouldn't consist more then one array as a field",
                1, count);
    }

    @Test
    public void checkArrayLengthAfterResizingTest() throws IllegalAccessException {
        MyMap<Car, Integer> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 14; i++) {
            Car car = new Car("model_" + i, "color_" + i);
            myHashMap.put(car, i);
        }
        for (int i = 0; i < 14; i++) {
            Assert.assertEquals(Integer.valueOf(i),
                    myHashMap.getValue(new Car("model_" + i, "color_" + i)));
        }
        Field[] declaredFields = myHashMap.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType().isArray()) {
                field.setAccessible(true);
                int length = Array.getLength(field.get(myHashMap));
                Assert.assertEquals("After first resizing, length of array should be " + 32,
                        32, length);
            }
        }
    }
}
