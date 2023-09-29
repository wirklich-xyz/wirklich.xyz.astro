package xyz.wirklich.astro.sun;

import xyz.wirklich.astro.time.JulianDay;

import java.time.ZonedDateTime;

import static java.lang.Math.*;

/**
 * // https://www.youtube.com/watch?v=puIBnXxTOR8
 * // 22 July 2009 um 13:30 Sonne auf Meridian-Linie in Dom
 *
 * @author Ralf Ulrich
 */
public class SolarLocation {

    JulianDay jd; // the "day number" matters most

    double latitude = 0; //  (+ to N)
    double longitude = 0; //(+ to E)

    public SolarLocation(double latitude, double longitude, JulianDay jd) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.jd = jd;
    }

    public SolarLocation(double latitude, double longitude, ZonedDateTime date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.jd = new JulianDay(date);
    }

    /**
     * @return The mean longitude of the sun in degrees
     */
    public double geomagneticMeanLongitudeSun() {
        double jdC = jd.getJulianCenturyJ2000();
        return (280.46646 + jdC * (36000.76983 + jdC * 0.0003032)) % 360;
    }

    /**
     * @return In units of [degree]
     */
    public double geomagneticMeanAnomalySun() { // deg
        double jdC = jd.getJulianCenturyJ2000();
        return 357.52911 + jdC * (35999.05029 - 0.0001537 * jdC);
    }

    /**
     * @return In units of [degree]
     */
    public double eccentricityEarthOrbit() { // deg
        double jdC = jd.getJulianCenturyJ2000();
        return 0.016708634 - jdC * (0.000042037 + 0.0000001267 * jdC);
    }

    /**
     * @return
     */
    public double sunEqOfCtr() {
        double jdC = jd.getJulianCenturyJ2000();
        return sin(toRadians(geomagneticMeanAnomalySun())) * (1.914602 - jdC * (0.004817 + 0.000014 * jdC)) + sin(toRadians(2 * geomagneticMeanAnomalySun())) * (0.019993 - 0.000101 * jdC) + sin(toRadians(3 * geomagneticMeanAnomalySun())) * 0.000289;
    }

    public double sunTrueLong() { // deg
        return sunEqOfCtr() + geomagneticMeanLongitudeSun();
    }

    public double sunTrueAnomaly() { // deg
        return sunEqOfCtr() + geomagneticMeanAnomalySun();
    }

    /**
     * @return In units of [AU]
     */
    public double sunRadVector() { // AU
        double ecc = eccentricityEarthOrbit();
        return (1.000001018 * (1 - ecc * ecc)) / (1 + ecc * cos(toRadians(sunTrueAnomaly())));
    }

    /**
     * @return In units of [degree]
     */
    public double sunApparentLongitude() { // deg
        double jdC = jd.getJulianCenturyJ2000();
        return sunTrueLong() - 0.00569 - 0.00478 * sin(toRadians(125.04 - 1934.136 * jdC));
    }

    /**
     * @return In units of [degree]
     */
    public double meanObliquEcliptic() { // deg
        double jdC = jd.getJulianCenturyJ2000();
        return 23 + (26 + ((21.448 - jdC * (46.815 + jdC * (0.00059 - jdC * 0.001813)))) / 60) / 60;
    }

    /**
     * @return In units of [degree]
     */
    public double obliqueCorrected() { // deg
        double jdC = jd.getJulianCenturyJ2000();
        return meanObliquEcliptic() + 0.00256 * cos(toRadians(125.04 - 1934.136 * jdC));
    }

    /**
     * @return In units of [degree]
     */
    public double sunRtAscend() { // deg
        double appLong = toRadians(sunApparentLongitude()); // [P]
        double obliq = toRadians(obliqueCorrected()); // [R]
        return toDegrees(atan2(cos(obliq) * sin(appLong), cos(appLong)));
    }

    /**
     * @return In units of [degree]
     */
    public double sunDeclination() { // deg
        return toDegrees(asin(sin(toRadians(obliqueCorrected())) * sin(toRadians(sunApparentLongitude()))));
    }

    /**
     * This is internal only.
     */
    private double varY() {
        return pow(tan(toRadians(obliqueCorrected() / 2)), 2);
    }

    /**
     * @return In units of [minute]
     */
    private double eqOfTime() { // minutes
        double geomMeanLong = toRadians(geomagneticMeanLongitudeSun()); // [I]
        double ecc = eccentricityEarthOrbit();                          // [K]
        double geoMeanAnom = toRadians(geomagneticMeanAnomalySun());    // [J]
        double vY = varY();                                             // [U]
        return 4 * toDegrees((vY * sin(2 * geomMeanLong) - 2 * ecc * sin(geoMeanAnom) + 4 * ecc * vY * sin(geoMeanAnom) * cos(2 * geomMeanLong) - 0.5 * vY * vY * sin(4 * geomMeanLong) - 1.25 * ecc * ecc * sin(2 * geoMeanAnom)));
    }

    /**
     * @return In units of [degree]
     */
    public double haSunrise() { // deg
        return toDegrees(acos(cos(toRadians(90.833)) / (cos(toRadians(getLatitude())) * cos(toRadians(sunDeclination()))) - tan(toRadians(getLatitude())) * tan(toRadians(sunDeclination()))));
    }

    /**
     * @return In local time as fraction of a day (24h/1d)
     */
    public double solarNoon() { // LST
        double longi = getLongitude();
        double eqT = eqOfTime();
        return (720 - 4 * longi - eqT /* + getTimeZone() * 60*/) / 1440;
    }

    /**
     * @return In local time as fraction of a day (24h/1d)
     */
    public double sunrise() { // LST
        return solarNoon() - haSunrise() * 4 / 1440;
    }

    /**
     * @return In local time as fraction of a day (24h/1d)
     */
    public double sunset() { // LST
        return solarNoon() + haSunrise() * 4 / 1440;
    }

    /**
     * @return In units of [minute]
     */
    public double sunlightDuration() { // minutes
        return haSunrise() * 8;
    }


    /**
     * Further note, times here are always in JD/TT. This is UTC+0.
     *
     * @return time as fraction of 24h/1d, thus, 0.5 is noon/12:00.
     */
    private double getTimePastMidnight() {
        double T = jd.getJd() + 0.5; // shift by half day.
        return T - floor(T); // time as fraction of 24h / 1d
    }

    /**
     * @return In units of fractions of a day 24h/1d
     */
    public double trueSolarTime() {
        return (getTimePastMidnight() * 1440 + eqOfTime() + 4 * getLongitude()) % 1440;
    }

    /**
     * @return In units of [degree]
     */
    public double hourAngle() {
        double tt = trueSolarTime();
        return (tt / 4 < 0 ? tt / 4 + 180
                : tt / 4 - 180);
    }

    /**
     * @return In units of [degree]
     */
    public double solarZenithAngle() {
        return toDegrees(acos(sin(toRadians(getLatitude())) * sin(toRadians(sunDeclination())) + cos(toRadians(getLatitude())) * cos(toRadians(sunDeclination())) * cos(toRadians(hourAngle()))));
    }

    /**
     * @return In units of [degree]
     */
    public double solarElevation() { // deg
        return 90 - solarZenithAngle();
    }

    /**
     * @return In units of [degree]
     */
    public double refractionCorrection() { // deg
        double elevation = solarElevation();
        if (elevation > 85) return 0;
        if (elevation > 5) return (58.1 / tan(toRadians(elevation)) - 0.07 / pow(tan(toRadians(elevation)), 3)
                + 0.000086 / pow(tan(toRadians(elevation)), 5)) / 3600;
        if (elevation > -0.575)
            return (1735 + elevation * (-518.2 + elevation * (103.4 + elevation * (-12.79 + elevation * 0.711)))) / 3600;
        return -20.772 / tan(toRadians(elevation)) / 3600;
    }

    /**
     * @return In units of [degree]
     */
    public double solarElevationCorrected() { // deg
        return solarElevation() + refractionCorrection();
    }

    /**
     * @return In units of [degree] cw from N
     */
    public double solarAzimuth() { // deg cw from N
        double lat = toRadians(getLatitude());
        double zenith = toRadians(solarZenithAngle());
        double decl = toRadians(sunDeclination());
        if (hourAngle() > 0)
            return (toDegrees(acos(((sin(lat) * cos(zenith)) - sin(decl)) / (cos(lat) * sin(zenith)))) + 180) % 360;
        return (540 - toDegrees(acos(((sin(lat) * cos(zenith)) - sin(decl)) / (cos(lat) * sin(zenith))))) % 360;
    }


    // -----------------------------------------------
    // getters and setters

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
