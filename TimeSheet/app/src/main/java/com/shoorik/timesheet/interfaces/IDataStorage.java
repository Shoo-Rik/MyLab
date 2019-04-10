package com.shoorik.timesheet.interfaces;

import java.util.Date;

public interface IDataStorage {

    Date getStartTime(Date date);
    Date getEndTime(Date date);

    void setStartTime(Date startDateTime);
    void setEndTime(Date endDateTime);
}
