package com.jaz.movie.booking.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {

    /**
     * <p>
     * Convert the 2 dates and given the minutes.
     * </p>
     *
     * @param dt1
     * @return
     */
    public static int findMinutes(Date dt1) {
        log.info("#findMinutes : Date: " + dt1);
        Date dt2 = new Date();
        long diff = dt2.getTime() - dt1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        log.info("#findMinutes : diffMinutes: " + (int) diffMinutes);
        return (int) diffMinutes;
    }

    /**
     * <p>
     * It will validate the ticket booking date is valid or not
     * If the date is past it will throw custom exception
     * If the date is more than 1 week then it will throw custom exception
     * If the date is current and within 1 week it will pass this method and return true.
     * </p>
     *
     * @param ticketDate
     * @param ticketDate
     * @return
     */
    public static boolean isTicketDateValid(Date ticketDate) {
        boolean isSuccess = false;
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateObj = dateFormat.format(date);
            log.info("Current date: " + currentDateObj);
            Calendar c = Calendar.getInstance();
            try {
                //Setting the date to the given date
                c.setTime(dateFormat.parse(dateFormat.format(ticketDate)));
            } catch (ParseException e) {
                log.info("ticketDate : " + ticketDate);
                log.error("ParseException Date: " + e.toString());
                isSuccess = false;
            }
            //Date after adding the days to the given date
            String customDateObj = dateFormat.format(c.getTime());
            log.info("currentDateObj : " + currentDateObj + "customDateObj : " + customDateObj);
            // Parses the date
            LocalDate dt1 = LocalDate.parse(currentDateObj);
            LocalDate dt2 = LocalDate.parse(customDateObj);
            log.info("dt1 : " + dt1 + " dt2 : " + dt2);
            if (dt1.isEqual(dt2) || dt1.isBefore(dt2)) {
                Period period = Period.between(dt1, dt2);
                int diff = Math.abs(period.getDays());
                log.info("diff : " + diff);
                if (7 >= diff) {
                    isSuccess = true;
                }
            }
        } catch (Exception e) {
            log.error("Invalid Date Exception: " + e.toString());
            isSuccess = false;
        }
        log.info("Is date Success : " + isSuccess);
        return isSuccess;
    }
}
