package xyz.wirklich.astro.time;


import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Ralf Ulrich
 * <p>
 * ModifiedJulianDate as a utility for java.time.
 */
public class ModifiedJulianDate {

    private static final double mjdRef = 2_400_000.5;
    private static final ZonedDateTime mjdRefDate = ZonedDateTime.of(1858, 11, 17, 0, 0, 0, 0, ZoneId.of("UTC+0"));

    private JulianDay jd;

    public ModifiedJulianDate() {
        this.jd = new JulianDay(mjdRef);
    }

    public ModifiedJulianDate(JulianDay jd) {
        this.jd = jd;
    }

    public ModifiedJulianDate(double mjd) {
        jd = new JulianDay(mjd + mjdRef);
    }

    public ModifiedJulianDate(ZonedDateTime date) {
        jd = new JulianDay(date);
    }

    public double getMjd() {
        return jd.getJd() - mjdRef;
    }

    public void setMjd(double mjd) {
        this.jd.setJd(mjd + mjdRef);
    }


    // ---------------------------------------------
    // getter and setter

    public JulianDay getJd() {
        return jd;
    }

    public void setJd(JulianDay jd) {
        this.jd = jd;
    }
}
