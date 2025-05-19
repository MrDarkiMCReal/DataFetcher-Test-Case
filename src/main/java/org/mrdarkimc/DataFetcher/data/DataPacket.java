package org.mrdarkimc.DataFetcher.data;

import com.fasterxml.jackson.databind.JsonNode;
import org.mrdarkimc.DataFetcher.link.Link;

import java.util.List;

public class DataPacket {
    private Link link;
    private List<JsonNode> list;

    public DataPacket(Link link, List<JsonNode> list) {
        this.link = link;
        this.list = list;
    }

    public List<JsonNode> getList() {
        return list;
    }


    public Link getLink() {
        return link;
    }
}
