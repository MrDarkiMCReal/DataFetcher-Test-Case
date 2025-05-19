package org.mrdarkimc.DataFetcher.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.function.Supplier;

public enum DataTypes {
    JSON(".json", ObjectMapper::new), XML(".xml", XmlMapper::new);
    private final String fileFormat;
    private final Supplier<ObjectMapper> mapper;

    DataTypes(String fileFormat, Supplier<ObjectMapper> mapper) {
        this.fileFormat = fileFormat;
        this.mapper = mapper;
    }

    public String getFormat() {
        return fileFormat;
    }

    public ObjectMapper getMapper() {
        return mapper.get();
    }
}
