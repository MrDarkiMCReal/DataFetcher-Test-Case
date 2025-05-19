package org.mrdarkimc.DataFetcher.attributes;

import java.util.Objects;
public class Attribute implements IAttribute {
    private String key;
    private String value;

    public Attribute(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Attribute attribute = (Attribute) object;
        return Objects.equals(key, attribute.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public void concat(StringBuilder linkBuilder) {
        linkBuilder.append(key).append("=").append(value);
    }
}
