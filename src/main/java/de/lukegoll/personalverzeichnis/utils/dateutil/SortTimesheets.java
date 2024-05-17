package de.lukegoll.personalverzeichnis.utils.dateutil;

import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class SortTimesheets {


    public static List<List<Timesheet>> sortTimesheets(List<Timesheet> timesheets) {
        if (timesheets == null || timesheets.isEmpty()) return null;
        List<List<Timesheet>> lists = new ArrayList<>();
        int index = 0;
        for (Timesheet t : timesheets) {
            if (timesheets.indexOf(t) <= index && index>0) continue;
            List<Timesheet> tempList = new ArrayList<>();
            tempList.add(t);
            LocalDateTime start = t.getStartTime();
            int year = start.getYear();
            int month = start.getMonth().getValue();
            int day = start.getDayOfMonth();
            int tempIndex = index + 1;
            for (int i = index + 1; i < timesheets.size(); i++) {
                Timesheet temp = timesheets.get(i);
                LocalDateTime tempStart = temp.getStartTime();
                int tempYear = tempStart.getYear();
                int tempMonth = tempStart.getMonth().getValue();
                int tempDay = tempStart.getDayOfMonth();
                if (year == tempYear && month == tempMonth && day == tempDay) {
                    tempList.add(temp);
                } else if (tempDay > day) {
                    break;
                }
                tempIndex = i;
            }
            index = tempIndex;
            lists.add(tempList);
        }
        return lists;
    }


}

