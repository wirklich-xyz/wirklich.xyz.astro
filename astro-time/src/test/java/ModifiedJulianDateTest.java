import org.junit.jupiter.api.Test;
import xyz.wirklich.astro.time.ModifiedJulianDate;

import java.time.ZonedDateTime;

import static java.time.ZoneId.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModifiedJulianDateTest {

    @Test
    void testCreate() {

        ModifiedJulianDate jd1 = new ModifiedJulianDate();
        assertEquals(jd1.getMjd(), 0);

        ModifiedJulianDate jd2 = new ModifiedJulianDate(1000);
        assertEquals(jd2.getMjd(), 1000);

        ModifiedJulianDate jd3 = new ModifiedJulianDate(ZonedDateTime.of(-4712, 1, 1, 0, 0, 0, 0, of("UTC+0")));
        assertEquals(jd3.getMjd(), 37.5 - 2_400_000.5);
    }

    @Test
    void testMjd() {
        ModifiedJulianDate jd2 = new ModifiedJulianDate(599_999.9);
        assertThat("MJD for JD 3000000.4 is not before 60000", jd2.getMjd(), lessThan(600_000.));
        assertThat("MJD for JD 3000000.4 is not before 60000", jd2.getJd().getJd(), lessThan(600_000.+2_400_000.5));

        ModifiedJulianDate jd3 = new ModifiedJulianDate(600_000.1);
        assertThat("MJD for JD 3000000.4 is not before 60000", jd3.getMjd(), greaterThan(600_000.));
        assertThat("MJD for JD 3000000.4 is not before 60000", jd3.getJd().getJd(), greaterThan(600_000.+2_400_000.5));

        jd3.setMjd(1000);
        assertThat("Set MJD", jd3.getMjd(), closeTo(1000, 0.0001));

    }

}