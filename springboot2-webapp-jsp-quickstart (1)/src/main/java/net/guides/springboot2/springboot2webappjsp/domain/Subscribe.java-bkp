package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Table(name = "subscribe")
@Entity
public class Subscribe {
    @EmbeddedId
    private SubscribeId id;

    @Column(name = "time_start", nullable = false)
    private Instant timeStart;

    @Column(name = "time_end", nullable = false)
    private Instant timeEnd;

    public Subscribe() {

    }

    public Instant getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Instant timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Instant getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Instant timeStart) {
        this.timeStart = timeStart;
    }

    public SubscribeId getId() {
        return id;
    }

    public void setId(SubscribeId id) {
        this.id = id;
    }
}
