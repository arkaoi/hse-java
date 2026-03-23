package hse.java.lectures.lesson7.dau;

import java.time.Clock;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DauServiceImplementation implements DauService {
    private final Clock clock;
    private volatile LocalDate current;

    private final ConcurrentHashMap<Integer, Set<Integer>> today = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Set<Integer>> yesterday = new ConcurrentHashMap<>();

    public DauServiceImplementation(Clock clock) {
        this.clock = clock;
        this.current = LocalDate.now(clock);
    }

    private void isNextDay() {
        LocalDate now = LocalDate.now(clock);

        if (!now.equals(current)) {
            synchronized (this) {
                if (!now.equals(current)) {
                    yesterday.clear();
                    yesterday.putAll(today);

                    today.clear();

                    current = now;
                }
            }
        }
    }

    @Override
    public void postEvent(Event event) {
        isNextDay();
        today.computeIfAbsent(event.authorId(), a -> ConcurrentHashMap.newKeySet()).add(event.userId());
    }

    @Override
    public Map<Integer, Long> getDauStatistics(List<Integer> authorIds) {
        isNextDay();

        Map<Integer, Long> result = new HashMap<>();

        for (Integer authorId : authorIds) {
            Set<Integer> users = yesterday.get(authorId);
            result.put(authorId, users == null ? 0L : (long) users.size());
        }

        return result;
    }

    @Override
    public Long getAuthorDauStatistics(int authorId) {
        return getDauStatistics(List.of(authorId)).get(authorId);
    }
}