package xyz.wirklich.astro.time;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class FractionOfDayTest {

    @Test
    void testFromFrac() {
        FractionOfDay foc = new FractionOfDay(0.5);
        assertThat("Hour of 0.5", foc.getHour(), is(12));
        assertThat("Minute of 0.5", foc.getMinute(), is(0));
        assertThat("Second of 0.5", foc.getSecond(), is(0));
        assertThat("Nano of 0.5", foc.getNanoSecond(), is(0));

        FractionOfDay foc2 = new FractionOfDay((3.+22./60+33./60/60+100./1e9/60/60)/24);
        assertThat("Hour of 0.5", foc2.getHour(), is(3));
        assertThat("Minute of 0.5", foc2.getMinute(), is(22));
        assertThat("Second of 0.5", foc2.getSecond(), is(33));
        assertThat("Nano of 0.5", foc2.getNanoSecond(), is(100));

    }

    @Test
    void testToFrac() {
        FractionOfDay foc = new FractionOfDay(12,0,0,0);
        assertThat("Fraction of 12:00:00", foc.getDayFraction(), is(0.5));

        FractionOfDay foc2 = new FractionOfDay(3,22,33,100);
        assertThat("Fraction of 3:22:33", foc2.getDayFraction(), is((3.+22./60+33./60/60+100./1e9/60/60)/24));

    }

}