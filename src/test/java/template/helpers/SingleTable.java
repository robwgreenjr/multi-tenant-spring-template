package template.helpers;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "single_table")
public class SingleTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String stringColumn;
    private Integer integerColumn;
    private Double doubleColumn;
    private String textColumn;
    private UUID uuidColumn;
    @Generated
    private Instant dateColumn;
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

    public String getStringColumn() {
        return stringColumn;
    }

    public void setStringColumn(String stringColumn) {
        this.stringColumn = stringColumn;
    }

    public Integer getIntegerColumn() {
        return integerColumn;
    }

    public void setIntegerColumn(Integer integerColumn) {
        this.integerColumn = integerColumn;
    }

    public Double getDoubleColumn() {
        return doubleColumn;
    }

    public void setDoubleColumn(Double doubleColumn) {
        this.doubleColumn = doubleColumn;
    }

    public String getTextColumn() {
        return textColumn;
    }

    public void setTextColumn(String textColumn) {
        this.textColumn = textColumn;
    }

    public Instant getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(Instant dateColumn) {
        this.dateColumn = dateColumn;
    }

    public UUID getUuidColumn() {
        return uuidColumn;
    }

    public void setUuidColumn(UUID uuidColumn) {
        this.uuidColumn = uuidColumn;
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
