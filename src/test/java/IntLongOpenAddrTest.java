import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class IntLongOpenAddrTest {
    private static IntLongOpenAddrHashMap map;
    private static List<Integer> keys = Stream.iterate(123, n -> n + 17).limit((int)(pow(2, 20)*0.7)).collect(Collectors.toList());
    private static Random random = new Random();

    public static class WithBefore{
        @Before
        public void BeforeTest(){
            map = new IntLongOpenAddrHashMap();
            keys.forEach(k -> map.add(k, random.nextLong()));
        }

        @Test
        public void actualSizeTest(){
            assertEquals(map.size(), map.getEntrySet().size() - 1);
        }

        @Test
        public void addTest(){
            int expectedK = 21, expectedV = 202002;
            IntLongEntry expected = new IntLongEntry(expectedK, expectedV);
            map.add(expectedK, expectedV);
            assertTrue(map.getEntrySet().stream().anyMatch(e -> expected.equals(e)));
        }

        @Test
        public void getTest(){
            int expectedK = 21, expectedV = 202002;
            IntLongEntry expected = new IntLongEntry(expectedK, expectedV);
            map.add(expectedK, expectedV);
            assertEquals(expected, map.get(expectedK));
        }

        @Test
        public void removeTest() {
            int expectedK = 21, expectedV = 202002;
            map.add(expectedK, expectedV);
            map.remove(expectedK);
            assertNull(map.get(expectedK));
        }

        @After
        public void AfterTest(){
            map = new IntLongOpenAddrHashMap();
        }
    }

    public static class WithoutBefore {

        @Test(expected = IllegalArgumentException.class)
        public void IllegalArgumentConstructorTest() {
            new IntLongOpenAddrHashMap(0, 70);
        }

        @Test(expected = IllegalArgumentException.class)
        public void IllegalArgumentConstructorTest2() {
            new IntLongOpenAddrHashMap(3, 101);
        }

        @Test(expected = IllegalArgumentException.class)
        public void IllegalArgumentConstructorTest3() {
            new IntLongOpenAddrHashMap(3, 0);
        }

        @Test(expected = ResizeCeilException.class)
        public void ResizeCeilExceptionResizeTest() {
            map = new IntLongOpenAddrHashMap(28, 1);
            Stream.iterate(0, n -> n + 1).forEach(n -> map.add(n, random.nextLong()));
        }

        @Test
        public void ConstructorTest() {
            int expectedPower = 3, expectedLoadFactor = 90;
            map = new IntLongOpenAddrHashMap(expectedPower, expectedLoadFactor);
            assertEquals((int) pow(2, expectedPower), map.getEntryArraySize());
            assertEquals(expectedLoadFactor, map.getLoadFactor());
        }

        @Test
        public void ConstructorMaxArraySizeTest() {
            map = new IntLongOpenAddrHashMap(100, 75);
            assertEquals(Integer.MAX_VALUE + 1 >>> 3, map.getEntryArraySize());
        }

        @Test
        public void resizeTest() {
            map = new IntLongOpenAddrHashMap(3, 50);
            keys.stream().limit(5).forEach(x -> map.add(x, random.nextLong()));
            assertEquals((int) pow(2, 4), map.getEntryArraySize());
            keys.stream().skip(5).limit(5).forEach(x -> map.add(x, random.nextLong()));
            assertEquals((int) pow(2, 5), map.getEntryArraySize());
        }

        @Test
        public void addGetTest(){
            map = new IntLongOpenAddrHashMap();
            List<IntLongEntry> expected = keys.stream().map(k -> new IntLongEntry(k, random.nextLong())).collect(Collectors.toList());
            expected.forEach(e -> map.add(e.getKey(), e.getValue()));
            List<IntLongEntry> actual = keys.stream().map(k -> map.get(k)).collect(Collectors.toList());
            assertEquals(expected, actual);
        }

        @After
        public void AfterTest(){
            map = new IntLongOpenAddrHashMap();
        }
    }
}
