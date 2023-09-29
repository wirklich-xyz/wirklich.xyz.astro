# xyy.wirklich.astro:astro-sun

Calculates properties of the sun location for any time and place on Earth. This is fit for astronomical use and
includes approximate corrections for the refractive index of the atmosphere. The calculation is cross-checked
against results obtained from the IAU.
<p>
The use of {@link JulianDay} and {@link ZonedDateTime} prevents typical time/date errors.
<p>
Quoting from NOAA: "
<i>
The approximations used in these programs are very good for years between 1800 and 2100. Results should still
be sufficiently accurate for the range from -1000 to 3000. Outside of this range, results may be given, but
the potential for error is higher."
</i>
