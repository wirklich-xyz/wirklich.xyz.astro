package xyz.wirklich.astro.time;

import static java.lang.Math.floor;

public class FractionOfDay {

    private final double dayFraction;
    private final int hour;
    private final int minute;
    private final int second;
    private final int nanoSecond;

    public FractionOfDay(double dayFraction) {
        this.dayFraction = dayFraction;
        double hourInDay = dayFraction * 24;
        this.hour = (int) floor(hourInDay);
        double minuteInHour = (hourInDay - hour) * 60;
        this.minute = (int) floor(minuteInHour);
        double secondFrac = (minuteInHour - minute) * 60;
        this.second = (int) floor(secondFrac);
        this.nanoSecond = (int) ((secondFrac - second) * 1e9);

    }

    public FractionOfDay(int hour, int minute, int second, int nanoSecond) {
        this.dayFraction = (hour + minute / 60 + second / 60 / 60 + nanoSecond / 60 / 60 / 1e9) / 24;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.nanoSecond = nanoSecond;
    }

    public double getDayFraction() {
        return dayFraction;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getNanoSecond() {
        return nanoSecond;
    }
}
