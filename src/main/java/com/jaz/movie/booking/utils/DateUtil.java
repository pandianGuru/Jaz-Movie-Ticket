package com.jaz.movie.booking.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    /**
     * @param dt1
     * @return
     */
    public static int findMinutes(Date dt1) {
        Date dt2 = new Date();
        long diff = dt2.getTime() - dt1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        return (int) diffMinutes;
    }

    /**
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

            Calendar c = Calendar.getInstance();
            try {
                //Setting the date to the given date
                c.setTime(dateFormat.parse(dateFormat.format(ticketDate)));
            } catch (ParseException e) {
                e.printStackTrace();
                isSuccess = false;
            }
            //Date after adding the days to the given date
            String customDateObj = dateFormat.format(c.getTime());

            // Parses the date
            LocalDate dt1 = LocalDate.parse(currentDateObj);
            LocalDate dt2 = LocalDate.parse(customDateObj);

                Period period = Period.between(dt1, dt2);
                int diff = Math.abs(period.getDays());
                if(7 >= diff) {
                    isSuccess = true;
                }

        } catch (Exception e) {
            System.out.println("Invalid date");
            isSuccess = false;
        }
        return isSuccess;
    }
}
