import org.junit.jupiter.api.Test;
import xyz.wirklich.astro.sun.SolarLocation;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;


class SolarLocationTest {


    @Test
    void testCalc() {
        SolarLocation sl = new SolarLocation(30, 10, ZonedDateTime.of(2022, 7, 22, 1, 0, 0, 0, ZoneId.of("UTC")));

        // Date	     Time (past local midnight)	Julian Day	Julian Century
        // 7/22/2022 1:00:00	                2459782,54	0,22553160

        // Geom Mean Long Sun (deg)	Geom Mean Anom Sun (deg)	Eccent Earth Orbit	Sun Eq of Ctr	Sun True Long (deg)	Sun True Anom (deg)	Sun Rad Vector (AUs)
        // 119,78	                     8476,45	                0,02	            -0,53	          119,25	    8475,92	            1,02

        assertThat("Check Mean Long", sl.geomagneticMeanLongitudeSun(), closeTo(119.78, 0.01));
        assertThat("Check Mean Anom", sl.geomagneticMeanAnomalySun(), closeTo(8476.45, 0.01));
        assertThat("Earth Eccent", sl.eccentricityEarthOrbit(), closeTo(0.02, 0.01));
        assertThat("Sun Eq Ctr", sl.sunEqOfCtr(), closeTo(-0.53, 0.01));
        assertThat("Sun True Long", sl.sunTrueLong(), closeTo(119.25, 0.01));
        assertThat("Sun True Anom", sl.sunTrueAnomaly(), closeTo(8475.92, 0.01));

        //  Sun App Long (deg)	Mean Obliq Ecliptic (deg)	Obliq Corr (deg)	Sun Rt Ascen (deg)	Sun Declin (deg)	var y	Eq of Time (minutes)
        //  119,24	            23,44	                    23,44	            121,39	            20,31	            0,04	-6,47
        assertThat("App Long", sl.sunApparentLongitude(), closeTo(119.24, 0.01));
        assertThat("Mean obliq Eclip", sl.meanObliquEcliptic(), closeTo(23.44, 0.01));
        assertThat("Obliq Corr", sl.obliqueCorrected(), closeTo(23.44, 0.01));
        assertThat("Sun Rt Ascen", sl.sunRtAscend(), closeTo(121.39, 0.01));
        assertThat("Sun Declin", sl.sunDeclination(), closeTo(20.31, 0.01));


        // HA Sunrise (deg)	Solar Noon (LST)	Sunrise Time (LST)	Sunset Time (LST)	Sunlight Duration (minutes)	True Solar Time (min)	Hour Angle (deg)
        //  103,39	        11:26:28	        4:32:54	            18:20:01	        827,12	                    93,53	                    -156,62
        assertThat("HA sunrise", sl.haSunrise(), closeTo(103.39, 0.01));
        assertThat("solar noon", sl.solarNoon(), closeTo(0.4767, 0.01));
        assertThat("sunrise", sl.sunrise(), closeTo(0.1895, 0.01));
        assertThat("sunset", sl.sunset(), closeTo(0.7639, 0.01));
        assertThat("duration", sl.sunlightDuration(), closeTo(827.12, 0.01));
        assertThat("True Solar time", sl.trueSolarTime(), closeTo(93.53, 0.01));
        assertThat("Hour Angle", sl.hourAngle(), closeTo(-156.62, 0.01));

        // 	Solar Zenith Angle (deg)	Solar Elevation Angle (deg)	Approx Atmospheric Refraction (deg)	Solar Elevation corrected for atm refraction (deg)	Solar Azimuth Angle (deg cw from N)
        //           124,89	            -34,89	                        0,01	                            -34,88	                                            26,98
        assertThat("Solar Zenith Angle", sl.solarZenithAngle(), closeTo(124.89, 0.01));
        assertThat("Solar Elevation Angle", sl.solarElevation(), closeTo(-34.89, 0.01));
        assertThat("Refractive correction", sl.refractionCorrection(), closeTo(0.008, 0.001));
        assertThat("Solar Elevation Corrected", sl.solarElevationCorrected(), closeTo(-34.88, 0.01));
        assertThat("Solar Azimuth Angle", sl.solarAzimuth(), closeTo(26.98, 0.01));
    }
}