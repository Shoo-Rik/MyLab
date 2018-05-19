package com.shoorik.timesheet.interfaces;

import java.util.Date;

public interface IDataStorage {

    Date getStartTime(Date startDate);
    Date getEndTime(Date endDate);

    void setStartTime(Date startDateTime);
    void setEndTime(Date endDateTime);
}
