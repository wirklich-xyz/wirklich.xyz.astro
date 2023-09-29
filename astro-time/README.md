# xyz.wirklich.astro:astro-time

The Julian Day (JD) is NOT a "Julian Date" and has nothing to do with the Julian Calendar. However, due to the
problems with calendar definitions in the past history, the calculations here are only precise if you use the
Gregorian calendar (confusing, if you think about it), which is the standard worldwide mostly since 1858.
<p>
The JD is a measure of counting days that is of fundamental importance in science, in particular for astronomy.
It is defined by the IAU (International Astronomical Union).
<p>
The JD counts days and fractions of days since 1. January −4712 (4713 BC), 12:00 Uhr UT. For example, the date of
14. September 2023, 13:12:03 Uhr UT corrsponds to JD 2460202.05003.
<p>
For historical reasons, the Julian Day starts each day at UT 12:00, thus, at noon.
<p>
Since the day-switching at noon is also confusing, in 1973 the IAU defined the "Modified Julian Date" (MJD) according to
MJD = JD - 2400000.5
, which has its zero-point 0 on 1858 November 17, 0:00 UT, see (IAU1973)[https://www.iau.org/static/resolutions/IAU1973_French.pdf].

The library provides JulianDay and JuliandModifiedDate in order to easily work 
in a standard java environment.  

