package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private long id = 1;
    private HashMap<Long, TimeEntry> timeEntries = new HashMap();
    public TimeEntry create(TimeEntry timeEntry) {

        TimeEntry newEntry = new TimeEntry(this.id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        timeEntries.put(this.id ,newEntry);
        this.id++;
        return newEntry;
    }

    public TimeEntry find(long id) {
        if(!timeEntries.containsKey(id)) {
            return null;
        }
        return timeEntries.get(id);
    }

    @Override
    public List<TimeEntry> list() {
       return new ArrayList<>(timeEntries.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {

        if (!timeEntries.containsKey(id)) {
             return null;
        }

        TimeEntry updatedEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        timeEntries.put(id, updatedEntry);
        return updatedEntry;
    }

    @Override
    public void delete(long id) {
        timeEntries.remove(id);
    }


}
