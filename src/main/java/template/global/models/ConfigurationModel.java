package template.global.models;

import java.util.Objects;

public class ConfigurationModel {
    private String key;
    private String value;
    private Boolean hashed;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getHashed() {
        return hashed;
    }

    public void setHashed(Boolean hashed) {
        this.hashed = hashed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationModel that = (ConfigurationModel) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value) &&
            Objects.equals(hashed, that.hashed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, hashed);
    }
}
