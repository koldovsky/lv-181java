package ua.softserveinc.tc.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ua.softserveinc.tc.constants.DayOffConstants;
import ua.softserveinc.tc.util.SimpleRoomSerializer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = DayOffConstants.Entity.TABLENAME)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class DayOff {

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = DayOffConstants.Entity.ID_DAY_OFF)
    private Long id;

    @Column(name = DayOffConstants.Entity.NAME)
    private String name;

    @Column(name = DayOffConstants.Entity.START_DATE)
    private LocalDate startDate;

    @Column(name = DayOffConstants.Entity.END_DATE)
    private LocalDate endDate;

    @ManyToMany(mappedBy = "daysOff", fetch = FetchType.LAZY)
    @JsonSerialize(using = SimpleRoomSerializer.class)
    Set<Room> rooms;

    /**
     * Deletes {@link DayOff} instance and avoids
     * throwing DataIntegrityViolationException.
     */
    @PreRemove
    private void removeGroupsFromUsers() {
        for (Room room : rooms) {
            room.getDaysOff().remove(this);
        }
    }
}
