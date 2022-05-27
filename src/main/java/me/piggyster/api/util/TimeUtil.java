package me.piggyster.api.util;

public class TimeUtil {

    public static String format(long ms) {
        ms = ms / 1000;
        long remainder = ms % 86400;

        long weeks = ms / 604800;
        long days 	= ms / 86400;
        long hours 	= remainder / 3600;
        long minutes	= (remainder / 60) - (hours * 60);
        long seconds	= (remainder % 3600) - (minutes * 60);


        String fWeeks   = (weeks > 0    ? " " + weeks + " week"     + (weeks > 1 ? "s" : "")    : "");
        String fDays 	= (days > 0 	? " " + days + " day" 		+ (days > 1 ? "s" : "") 	: "");
        String fHours 	= (hours > 0 	? " " + hours + " hour" 	+ (hours > 1 ? "s" : "") 	: "");
        String fMinutes = (minutes > 0 	? " " + minutes + " minute"	+ (minutes > 1 ? "s" : "") 	: "");
        String fSeconds = (seconds > 0 	? " " + seconds + " second"	+ (seconds > 1 ? "s" : "") 	: "");

        return fWeeks + fDays + fHours +
                fMinutes + fSeconds;
    }
}
