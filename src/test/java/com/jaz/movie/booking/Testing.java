package com.jaz.movie.booking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Testing {
    public static void main(String args[]) {
        /*try {

            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String currentDate = dateFormat.format(date);

            Calendar c = Calendar.getInstance();
            try {
                //Setting the date to the given date
                c.setTime(dateFormat.parse(customDateObj));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Number of Days to add
            c.add(Calendar.DAY_OF_MONTH, 7);
            //Date after adding the days to the given date
            String customDate = dateFormat.format(c.getTime());

            // Parses the date
            LocalDateTime dt1 = LocalDateTime.parse(currentDate);
            LocalDateTime dt2 = LocalDateTime.parse(customDate);
            System.out.println("Is Date 1 before Date 2: "
                    + dt1.isBefore(dt2));
        } catch (Exception e) {
            System.out.println("Invalid date");
        }*/
    }
}
