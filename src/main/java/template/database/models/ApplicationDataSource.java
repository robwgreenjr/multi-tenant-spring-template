package template.database.models;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Objects;

public class ApplicationDataSource {
    private Instant lastConnectionTime;
    private HikariDataSource dataSource;

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getDataConnection() throws SQLException {
        this.lastConnectionTime = Instant.now();

        return this.dataSource.getConnection();
    }

    public Instant getLastConnectionTime() {
        return lastConnectionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationDataSource that = (ApplicationDataSource) o;
        return
            Objects.equals(lastConnectionTime, that.lastConnectionTime) &&
                Objects.equals(dataSource, that.dataSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastConnectionTime, dataSource);
    }

    @Override
    public String toString() {
        return "ApplicationDataSource{" +
            "lastConnectionTime=" + lastConnectionTime +
            ", dataSource=" + dataSource +
            '}';
    }
}
