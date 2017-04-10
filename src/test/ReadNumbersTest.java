package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stc5.lab1.ReadNumbers;
import stc5.lab1.SumContainer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by sergey on 10.04.17.
 */
class ReadNumbersTest {
    private static int magicKey;
    private static SumContainer sc;

    @BeforeAll
    public static void init() {
        magicKey = 5;
        sc = new SumContainer();
    }
}