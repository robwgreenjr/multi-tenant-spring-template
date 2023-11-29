package template.database.helpers;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "join_table")
public class JoinTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @jakarta.persistence.JoinTable(name = "single_table_join",
        joinColumns = {@JoinColumn(name = "join_table_id")},
        inverseJoinColumns = {@JoinColumn(name = "single_table_id")})
    private Set<SingleTable> singleTables = new HashSet<>();
    @Generated
    private Instant createdOn;
    @Generated
    private Instant updatedOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SingleTable> getSingleTables() {
        return singleTables;
    }

    public void setSingleTables(
        Set<SingleTable> singleTables) {
        this.singleTables = singleTables;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }
}
