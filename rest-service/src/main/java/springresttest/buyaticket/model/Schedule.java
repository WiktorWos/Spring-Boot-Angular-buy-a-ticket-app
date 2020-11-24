package springresttest.buyaticket.model;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Schedule {
    @NotNull(message = "Please provide daily schedule")
    private Map<DayOfWeek, List<LocalTime>> connectionDailySchedule;

    public Schedule() {
    }

    public Schedule(Map<DayOfWeek, List<LocalTime>> connectionDailySchedule) {
        this.connectionDailySchedule = connectionDailySchedule;
    }

    public Map<DayOfWeek, List<LocalTime>> getConnectionDailySchedule() {
        return connectionDailySchedule;
    }

    public void setConnectionDailySchedule(Map<DayOfWeek, List<LocalTime>> connectionDailySchedule) {
        this.connectionDailySchedule = connectionDailySchedule;
    }
}
