package se.group14.foodfinder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    private MainActivity mainActivity;
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void mockitoTest() {

        List mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        LinkedList linkedMocked = mock(LinkedList.class);

        when(mockedList.get(0)).thenReturn("first");

        System.out.println(mockedList.get(0));

        System.out.println(mockedList.get(999));

        System.out.println("Hej Elias, nu är testet slut.");


    }

    public void onCreate () {
        ExampleUnitTest unitTest = new ExampleUnitTest();
        System.out.println("#####");
        unitTest.mockitoTest();
    }
}