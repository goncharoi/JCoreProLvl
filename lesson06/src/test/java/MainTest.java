import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
class MainTest {

    @org.junit.jupiter.api.Test
    void first0() {
        try {
            Assertions.assertArrayEquals(new Integer[]{1, 2, 3}, Main.first(new Integer[]{4, 1, 2, 3}));
        } catch (EmptyArrayException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void first1() {
        try {
            Assertions.assertArrayEquals(new Integer[]{2, 3}, Main.first(new Integer[]{1, 4, 2, 3}));
        } catch (EmptyArrayException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void firstMulti() {
        try {
            Assertions.assertArrayEquals(new Integer[]{2, 3}, Main.first(new Integer[]{1, 4, 4, 1, 4, 2, 3}));
        } catch (EmptyArrayException e) {
            e.printStackTrace();
        }
    }
    @org.junit.jupiter.api.Test
    void firstLast() {
        try {
            Assertions.assertArrayEquals(new Integer[0], Main.first(new Integer[]{1, 2, 3, 4}));
        } catch (EmptyArrayException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void firstEmptyArrayException() {
        Assertions.assertThrows(EmptyArrayException.class, () -> Main.first(new Integer[0]));
    }

    @org.junit.jupiter.api.Test
    void firstRuntimeException() {
        Assertions.assertThrows(RuntimeException.class, () -> Main.first(new Integer[]{1, 2, 3}));
    }

    @Test
    void second0() {
        Assertions.assertEquals(false, Main.second(new Integer[0]));
    }

    @Test
    void secondOnly1() {
        Assertions.assertEquals(false, Main.second(new Integer[]{1,1,1}));
    }

    @Test
    void secondOnly4() {
        Assertions.assertEquals(false, Main.second(new Integer[]{4,4,4,4,4,4}));
    }

    @Test
    void secondTrue() {
        Assertions.assertEquals(true, Main.second(new Integer[]{4,4,1,4,1,4}));
    }

    @Test
    void secondIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Main.second(new Integer[]{1,2,4,1,3,4,4,1}));
    }
}