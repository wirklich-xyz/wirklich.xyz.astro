/*
   Copyright 2023, Ralf Ulrich, ralf.m.ulrich@gmail.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package xyz.wirklich.astro.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.lang.Math.floor;


/**
 * @author Ralf Ulrich
 * <p>
 * The Julian Day (JD) is NOT a "Julian Date" and has nothing to do with the Julian Calendar. However, due to the
 * problems with calendar definitions in the past history, the calculations here are only precise if you use the
 * Gregorian calendar, which is the standard woldwide mostly since 1858.
 * <p>
 * The JD is a measure of counting days that is of fundamental importance in science, in particular for astronomy.
 * It is defined by the IAU (International Astronomical Union).
 * <p>
 * The JD counts days and fractions of days since 1. January −4712 (4713 BC), 12:00 Uhr UT. For example, the date of
 * 14. September 2023, 13:12:03 Uhr UT corrsponds to JD 2460202.05003.
 * <p>
 * For historical reasons, the Julian Day starts each day at UT 12:00, thus, at noon.
 * <p>
 * Since the day-switching at noon is also confusing, in 1973 the IAU defined the "Modified Julian Date" (MJD) according to
 * MJD = JD - 2400000.5
 * , which has its zero-point 0 on 1858 November 17, 0:00 UT (https://www.iau.org/static/resolutions/IAU1973_French.pdf).
 */

public class JulianDay {

    private static final double mjdRef = 2_400_000.5;
    private static final ZonedDateTime mjdRefDate = ZonedDateTime.of(1858, 11, 17, 0, 0, 0, 0, ZoneId.of("UTC+0"));
    private double jd;

    public JulianDay() {
        jd = 0;
    }

    public JulianDay(double jd) {
        this.jd = jd;
    }

    public JulianDay(ZonedDateTime date) {
        setDate(date);
    }

    public double getJd() {
        return jd;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    public ZonedDateTime getDate() {

        // this is all for gregorian (modern) calendar

        double Z = floor(jd + 0.5); // MJD day number
        double F = jd + 0.5 - Z; // MJD fractional day number

        double alpha = floor((Z - 1_867_216.25) / 36_524.25);
        double A = Z + 1 + alpha - floor(alpha / 4);

        double B = A + 1524;
        int C = (int) floor((B - 122.1) / 365.25);
        int D = (int) floor(365.25 * C);
        int E = (int) floor((B - D) / 30.6001);

        double dayFrac = B - D - floor(30.6001 * E) + F;
        int day = (int) dayFrac;

        double hourInDay = (dayFrac - day) * 24;
        int hour = (int) floor(hourInDay);

        double minuteInHour = (hourInDay - hour) * 60;
        int minute = (int) floor(minuteInHour);

        double secondFrac = (minuteInHour - minute) * 60;
        int second = (int) floor(secondFrac);

        int nano = (int) ((secondFrac - second) * 1e9);

        if (E <= 13) {
            return ZonedDateTime.of(C - 4716, E - 1, day, hour, minute, second, nano, ZoneId.of("UTC+0"));
        }
        return ZonedDateTime.of(C - 4715, E - 13, day, hour, minute, second, nano, ZoneId.of("UTC+0"));
    }

    public void setDate(ZonedDateTime date) {

        ZonedDateTime utc = date.withZoneSameInstant(ZoneId.of("UTC"));
        int month = utc.getMonthValue();
        int year = utc.getYear();
        if (month <= 2) {
            year -= 1;
            month += 12;
        }
        double day = utc.getDayOfMonth();

        double hour = utc.getHour();
        double minute = utc.getMinute();
        double second = utc.getSecond();
        double nano = utc.getNano();
        double dayFrac = (hour + minute / 60 + second / 60 / 60 + nano / 60 / 60 / 1e9) / 24;

        // this is all for gregorian (modern) calendar
        double B = 2 - floor(year / 100) + floor(year / 400);
        this.jd = floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + day + dayFrac + B - 1524.5;
    }

    public double getMjd() {
        return jd - mjdRef;
    }

    public void setMjd(double mjd) {
        this.jd = mjd - mjdRef;
    }

    /**
     * @return This is the fractional century in the J2000 epoch.
     */
    public double getJulianCenturyJ2000() {
        return ((jd - 2451545) / 36525);
    }
}

