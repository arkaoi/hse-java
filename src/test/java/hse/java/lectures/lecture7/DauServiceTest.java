package hse.java.lectures.lecture7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import hse.java.lectures.lesson7.dau.*;

@Tag("Dau")
class DauServiceTest {

    private MutableClock clock;
    private DauServiceImplementation service;

    private static final ZoneId ZONE = ZoneOffset.UTC;

    @BeforeEach
    void setUp() {
        clock = new MutableClock(LocalDate.of(2026, 3, 21).atStartOfDay(ZONE).toInstant(), ZONE);

        service = new DauServiceImplementation(clock);
    }

    private void setDate(LocalDate date) {
        clock.setInstant(date.atStartOfDay(ZONE).toInstant());
    }

    @Test
    void singleUser_singleClick() {
        setDate(LocalDate.of(2026, 3, 20));
        service.postEvent(new Event(1, 100));

        setDate(LocalDate.of(2026, 3, 21));

        Long result = service.getAuthorDauStatistics(100);

        assertEquals(1L, result);
    }

    @Test
    void sameUser_multipleClicks() {
        setDate(LocalDate.of(2026, 3, 20));

        service.postEvent(new Event(1, 100));
        service.postEvent(new Event(1, 100));
        service.postEvent(new Event(1, 100));

        setDate(LocalDate.of(2026, 3, 21));

        assertEquals(1L, service.getAuthorDauStatistics(100));
    }

    @Test
    void multipleUsers() {
        setDate(LocalDate.of(2026, 3, 20));

        service.postEvent(new Event(1, 100));
        service.postEvent(new Event(2, 100));
        service.postEvent(new Event(3, 100));

        setDate(LocalDate.of(2026, 3, 21));

        assertEquals(3L, service.getAuthorDauStatistics(100));
    }

    @Test
    void differentAuthors() {
        setDate(LocalDate.of(2026, 3, 20));

        service.postEvent(new Event(1, 100));
        service.postEvent(new Event(2, 200));

        setDate(LocalDate.of(2026, 3, 21));

        Map<Integer, Long> stats = service.getDauStatistics(List.of(100, 200));

        assertEquals(1L, stats.get(100));
        assertEquals(1L, stats.get(200));
    }

    @Test
    void emptyClick() {
        Map<Integer, Long> stats = service.getDauStatistics(List.of(100));

        assertEquals(0L, stats.get(100));
    }

    @Test
    void isTodayClicks() {
        service.postEvent(new Event(1, 100));

        assertEquals(0L, service.getAuthorDauStatistics(100));
    }

    @Test
    void isDataClear() {
        setDate(LocalDate.of(2026, 3, 20));
        service.postEvent(new Event(1, 100));

        setDate(LocalDate.of(2026, 3, 21));
        service.postEvent(new Event(2, 100));

        assertEquals(1L, service.getAuthorDauStatistics(100));

        setDate(LocalDate.of(2026, 3, 22));
        service.postEvent(new Event(3, 100));

        assertEquals(1L, service.getAuthorDauStatistics(100));
    }
}