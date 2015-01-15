package de.tudresden.swt14ws18.modelTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tudresden.swt14ws18.util.Util;

public class UtilTest {
    @Test
    public void testIsBetween() {
        assertTrue(Util.isBetween(5, 0, 9));
        assertFalse(Util.isBetween(10, 0, 9));
    }

    @Test
    public void testUniqueArray() {
        assertTrue(Util.uniqueArray(1, 2, 3, 4, 5, 6));
        assertFalse(Util.uniqueArray(1, 2, 3, 4, 5, 1));
    }

    @Test
    public void testMatches() {
        assertEquals(3, Util.matches(new int[] { 1, 2, 3, 4, 5, 6 }, new int[] { 1, 2, 3, 7, 8, 9 }));
        assertEquals(3, Util.matches(new int[] { 1, 2, 3, 4, 5, 6 }, new int[] { 1, 2, 3, 7, 9 }));
        assertEquals(0, Util.matches(new int[] { 1, 2, 3, 4, 5, 6 }, new int[] { 40, 42, 43, 7, 8, 9 }));
        assertEquals(6, Util.matches(new int[] { 1, 2, 3, 4, 5, 6 }, new int[] { 6, 5, 4, 3, 2, 1 }));
    }
}
