package template.global.models;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Objects;
import java.util.TimeZone;

public class MetaData {
    private String timestamp;

    public MetaData() {
        this.timestamp = new DateTime(System.currentTimeMillis(),
            DateTimeZone.forTimeZone(TimeZone.getTimeZone("EST"))).toString();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaData metaData = (MetaData) o;
        return Objects.equals(timestamp, metaData.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }
}
