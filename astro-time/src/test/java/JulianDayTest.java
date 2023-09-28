import xyz.wirklich.astro.time.JulianDay;

import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static java.time.ZoneId.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JulianDayTest {

    @Test
    void testCreate() {

        JulianDay jd1 = new JulianDay();
        assertEquals(jd1.getJd(), 0);

        JulianDay jd2 = new JulianDay(1000);
        assertEquals(jd2.getJd(), 1000);

        JulianDay jd3 = new JulianDay(ZonedDateTime.of(-4712, 1, 1, 0, 0, 0, 0, of("UTC+0")));
        assertEquals(jd3.getJd(), 37.5); // THIS IS "WRONG" because of the need for Julian Calendar calculations before 1858
    }

    @Test
    void testJd() {
        JulianDay jd1 = new JulianDay(ZonedDateTime.of(1858, 11, 17, 0, 0, 0, 0, of("UTC+0")));
        assertThat("Check JD of 17.11.1858 00:00:00UT", jd1.getJd(), is(2400000.5));

        // 15. Okt. 1582greg. 	00:00 UT ->  2299160.500
        JulianDay jd2 = new JulianDay(ZonedDateTime.of(1582, 10, 15, 0, 0, 0, 0, of("UTC+0")));
        assertThat("Check JD of 15.10.1582 00:00:00UT", jd2.getJd(), is(2299160.5));

        //      1. Jan. 2000 12:00 TT -> 2451545.00000
        JulianDay jd3 = new JulianDay(ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, of("UTC+0")));
        assertThat("Check JD of 1.1.2000 12:00:00UT", jd3.getJd(), is(2451545.00000));

        //      31. Dez. 1899 	19:31:28 TT  2415020.31352
        JulianDay jd4 = new JulianDay(ZonedDateTime.of(1899, 12, 31, 19, 31, 28, 0, of("UTC+0")));
        assertThat("Check JD of 31.12.1899 19:31:28UT", jd4.getJd(), closeTo(2415020.31352, 0.0001));

        // 7/22/2022 1 UT
        JulianDay jd5 = new JulianDay(ZonedDateTime.of(2022, 7, 22, 1, 0, 0, 0, of("UTC+0")));
        assertThat("Check JD of 22.07.2022 01:00:00UT", jd5.getJd(), closeTo(2459782.54, 0.01));
        assertThat("JD Century", jd5.getJulianCenturyJ2000(), closeTo(0.22553160,0.00001));
    }


    @Test
    void testMjd() {
        JulianDay jd1 = new JulianDay(0);
        assertThat("MJD for JD -2400000.5", jd1.getMjd(), is(-2_400_000.5));

        JulianDay jd2 = new JulianDay(3_000_000.4);
        assertThat("MJD for JD 3000000.4 is not before 60000", jd2.getMjd(), lessThan(600_000.));

        JulianDay jd3 = new JulianDay(3_000_000.6);
        assertThat("MJD for JD 3000000.4 is not before 60000", jd3.getMjd(), greaterThan(600_000.));
    }

    @Test
    void testDate() {
        ZonedDateTime testDate1 = ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, of("UTC"));
        JulianDay jd1 = new JulianDay(2_451_545.000_00);
        assertThat("The Date for JD 2451545", jd1.getDate(), equalTo(testDate1));

        ZonedDateTime testDate2 = ZonedDateTime.of(2023, 9, 20, 11, 5, 59, (int) (0.135985374e9), of("UTC"));
        JulianDay jd2 = new JulianDay(2460207.96249);
        assertThat("The Date for JD 2451545", jd2.getDate(), equalTo(testDate2));
    }

}