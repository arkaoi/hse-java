package hse.java.lectures.lecture7;

import lombok.Setter;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

class MutableClock extends Clock {

    @Setter
    private Instant instant;
    private final ZoneId zone;

    public MutableClock(Instant instant, ZoneId zone) {
        this.instant = instant;
        this.zone = zone;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new MutableClock(instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }
}