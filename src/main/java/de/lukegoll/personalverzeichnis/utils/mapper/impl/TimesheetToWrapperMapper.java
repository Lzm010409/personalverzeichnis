package de.lukegoll.personalverzeichnis.utils.mapper.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import de.lukegoll.personalverzeichnis.domain.entities.TimesheetWrapper;
import de.lukegoll.personalverzeichnis.utils.mapper.EntityMapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Wrapper welcher von einer {@linkplain List<Timesheet>} nach {@linkplain TimesheetWrapper} mappt
 */
public class TimesheetToWrapperMapper implements EntityMapper<TimesheetWrapper, List<Timesheet>> {

    @Override
    public TimesheetWrapper mapTo(List<Timesheet> timesheets) {
        if (timesheets == null || timesheets.isEmpty()) {
            return null;
        }
        LocalDateTime start = findMax(timesheets.stream().map(Timesheet::getStartTime).toList());

        LocalDateTime end = findMin(timesheets.stream().map(Timesheet::getEndTime).toList());

        int hours = 0;
        int minutes = 0;
        if (start != null && end != null) {
            hours = end.getHour() - start.getHour();
            minutes = end.getMinute() > start.getMinute() ? end.getMinute() - start.getMinute() : start.getMinute() - end.getMinute();
        }
        TimesheetWrapper timesheetWrapper = new TimesheetWrapper(timesheets, start, end, hours, minutes);
        return timesheetWrapper;
    }

    @Override
    public List<Timesheet> mapFrom(TimesheetWrapper timesheetWrapper) {
        return null;
    }


    private LocalDateTime findMax(List<LocalDateTime> times) {
        if (times == null || times.isEmpty()) {
            return null;
        }
        LocalDateTime max = times.get(0);
        for (LocalDateTime time : times) {
            if (time == null) continue;
            if (time.isAfter(max)) {
                max = time;
            }
        }
        return max;
    }

    private LocalDateTime findMin(List<LocalDateTime> times) {
        if (times == null || times.isEmpty()) {
            return null;
        }
        LocalDateTime min = times.get(0);
        for (LocalDateTime time : times) {
            if (time == null) continue;
            if (time.isBefore(min)) {
                min = time;
            }
        }
        return min;
    }

}
