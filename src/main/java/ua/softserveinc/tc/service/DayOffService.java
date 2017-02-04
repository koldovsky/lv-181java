package ua.softserveinc.tc.service;

import ua.softserveinc.tc.entity.DayOff;

import java.time.LocalDate;
import java.util.List;

public interface DayOffService extends BaseService<DayOff>{

    DayOff findById(long id);

    boolean dayOffExist(String name, LocalDate startDate);

    void delete(long id);

    List<DayOff> findByNameOrStartDate(String name, LocalDate startDate);

    List<DayOff> getClosestDays();

    void sendDayOffInfo(DayOff dayOff);

    void createDayOffEvent(DayOff dayOff);

    void deleteDayOffEvent(String name);

}
