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
        LocalDateTime start = timesheets.stream().map(Timesheet::getStartTime)
                .min(LocalDateTime::compareTo).get();
        LocalDateTime end = timesheets.stream().map(Timesheet::getEndTime)
                .max(LocalDateTime::compareTo).get();

        int hours = end.getHour() - start.getHour();
        int minutes = end.getMinute() > start.getMinute() ? end.getMinute() - start.getMinute() : start.getMinute() - end.getMinute();
        TimesheetWrapper timesheetWrapper = new TimesheetWrapper(timesheets, start, end, hours, minutes);
        return timesheetWrapper;
    }

    @Override
    public List<Timesheet> mapFrom(TimesheetWrapper timesheetWrapper) {
        return null;
    }


}
