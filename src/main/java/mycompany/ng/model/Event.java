package mycompany.ng.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "EVENT_MSG")
    private String message;




    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EVENT_DATE")
    private Date date;

    public Event() {
    }

    public Event(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public Long getId() {
        return id;
    }
}
