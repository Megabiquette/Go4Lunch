package com.albanfontaine.go4lunch;


import com.albanfontaine.go4lunch.Utils.Utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UnitTest {

    // Converts a Date object to a String in "yyyy-MM-dd" format
    @Test
    public void dateFormattingTest(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 4,24); // 24th of May 2019
        Date date = calendar.getTime();

        assertEquals("2019-05-24", Utils.getFormattedDate(date));
    }

    // Converts a time from HHMM format to HH:MM format
    @Test
    public void formatHoursTest(){
        String hours = "1200";

        assertEquals("12:00", Utils.formatHours(hours));
    }

}
