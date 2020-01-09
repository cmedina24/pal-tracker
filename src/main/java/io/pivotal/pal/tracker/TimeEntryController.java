package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository repo;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {

        this.repo = timeEntryRepository;

        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = repo.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(repo.list().size());

        return new ResponseEntity<>(timeEntry, HttpStatus.CREATED);
    }
    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry temp = repo.find(timeEntryId);
        if(temp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();

        return new ResponseEntity<>(temp, HttpStatus.OK);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        actionCounter.increment();
        return new ResponseEntity<>(repo.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry temp = repo.update(timeEntryId, expected);
        if(temp == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        actionCounter.increment();
        return new ResponseEntity<>(temp, HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        repo.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(repo.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
