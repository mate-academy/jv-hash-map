package core.basesyntax;

import org.junit.Assert;
import org.junit.Test;

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
        MyMap<Car, Integer> myHashMap = new MyHashMap();
        Assert.assertNull("Test failed! If key doesn't exist, we shoud return null.",
                myHashMap.getValue(firstCar));
    }

    @Test
    public void putAndGetOk() {
        MyMap<Car, Integer> myHashMap = new MyHashMap();
        myHashMap.put(firstCar, 3);
        myHashMap.put(secondCar, 5);
        myHashMap.put(thirdCar, 1);

        Assert.assertEquals("Test failed! The size isn't correct",
                3, myHashMap.getSize());
        Assert.assertEquals("Test failed, result should be 3.",
                Integer.valueOf(3), myHashMap.getValue(firstCar));
        Assert.assertEquals("Test failed, result should be 5.",
                Integer.valueOf(5), myHashMap.getValue(secondCar));
        Assert.assertEquals("Test failed, result should be 1.",
                Integer.valueOf(1), myHashMap.getValue(thirdCar));
    }

    @Test
    public void putTheSameElement() {
        MyMap<Car, Integer> myHashMap = new MyHashMap();
        myHashMap.put(firstCar, 3);
        myHashMap.put(secondCar, 5);
        myHashMap.put(thirdCar, 1);
        myHashMap.put(sameFirstCar, 3);
        myHashMap.put(sameSecondCar, 5);
        myHashMap.put(sameThirdCar, 1);

        Assert.assertEquals("Test failed! We should add checking if the same element " +
                "exist in the map", 3, myHashMap.getSize());
        Assert.assertEquals("Test failed, result should be 3.",
                Integer.valueOf(3), myHashMap.getValue(firstCar));
        Assert.assertEquals("Test failed, result should be 5.",
                Integer.valueOf(5), myHashMap.getValue(secondCar));
        Assert.assertEquals("Test failed, result should be 1.",
                Integer.valueOf(1), myHashMap.getValue(thirdCar));
    }

    @Test
    public void putAndGetByNullKey() {
        MyMap<Car, Integer> myHashMap = new MyHashMap();
        myHashMap.put(null, 3);
        Assert.assertEquals("Test failed, result should be 3.",
                Integer.valueOf(3), myHashMap.getValue(null));
        Assert.assertEquals("Test failed! The size isn't correct",
                1, myHashMap.getSize());
        myHashMap.put(null, 5);
        Assert.assertEquals("Test failed, result should be 5.",
                Integer.valueOf(5), myHashMap.getValue(null));
        Assert.assertEquals("Test failed! The size isn't correct",
                1, myHashMap.getSize());
    }

    @Test
    public void putAndGetWithCollision() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap();
        myHashMap.put(firstPlane, 3);
        myHashMap.put(secondPlane, 5);
        myHashMap.put(thirdPlane, 1);

        Assert.assertEquals("Test failed! The size isn't correct",
                3, myHashMap.getSize());
        Assert.assertEquals("Test failed, result should be 3.",
                Integer.valueOf(3), myHashMap.getValue(firstPlane));
        Assert.assertEquals("Test failed, result should be 5.",
                Integer.valueOf(5), myHashMap.getValue(secondPlane));
        Assert.assertEquals("Test failed, result should be 1.",
                Integer.valueOf(1), myHashMap.getValue(thirdPlane));
    }

    @Test
    public void putAndGetByNullKeyWithCollision() {
        MyMap<Bus, Integer> myHashMap = new MyHashMap();
        myHashMap.put(firstBus, 3);
        myHashMap.put(null, 4);
        myHashMap.put(secondBus, 5);
        myHashMap.put(null, 10);
        myHashMap.put(thirdBus, 1);
        Assert.assertEquals("Test failed! The size isn't correct",
                4, myHashMap.getSize());
        Assert.assertEquals("Test failed, result should be 3.",
                Integer.valueOf(3), myHashMap.getValue(firstBus));
        Assert.assertEquals("Test failed, result should be 5.",
                Integer.valueOf(5), myHashMap.getValue(secondBus));
        Assert.assertEquals("Test failed, result should be 1.",
                Integer.valueOf(1), myHashMap.getValue(thirdBus));
        Assert.assertEquals("Test failed, result should be 10.",
                Integer.valueOf(10), myHashMap.getValue(null));
    }

    @Test
    public void putAndGetTheOverriddenValueByKey() {
        MyMap<Car, Integer> myHashMap = new MyHashMap();
        myHashMap.put(firstCar, 3);
        Assert.assertEquals("Test failed! The size isn't correct",
                1, myHashMap.getSize());
        Assert.assertEquals("Test failed, result should be 3.",
                Integer.valueOf(3), myHashMap.getValue(firstCar));

        myHashMap.put(firstCar, 5);
        Assert.assertEquals("Test failed! The size isn't correct",
                1, myHashMap.getSize());
        Assert.assertEquals("Test failed, result should be 5.",
                Integer.valueOf(5), myHashMap.getValue(firstCar));
    }

    @Test
    public void checkTheHashMapIncrease() {
        MyMap<Car, Integer> myHashMap = new MyHashMap();
        for (int i = 0; i < 1000; i++) {
            Car car = new Car("model_" + i, "color_" + i);
            myHashMap.put(car, i);
        }
        Assert.assertEquals("Test failed! The size isn't correct",
                1000, myHashMap.getSize());
        for (int i = 0; i < 1000; i++) {
            Assert.assertSame(i, myHashMap.getValue(new Car("model_" + i, "color_" + i)));
        }
    }

    @Test
    public void getSizeOfEmptyHashMap() {
        MyMap<Car, Integer> myHashMap = new MyHashMap();
        Assert.assertEquals("Test failed! The size isn't correct",
                0, myHashMap.getSize());
    }

    @Test
    public void getSizeWithCollision() {
        MyMap<Plane, Integer> myHashMap = new MyHashMap();
        myHashMap.put(firstPlane, 3);
        myHashMap.put(secondPlane, 5);
        Assert.assertEquals("Test failed! The size isn't correct",
                2, myHashMap.getSize());
    }
}
