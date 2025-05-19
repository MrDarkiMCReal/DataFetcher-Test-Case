package org.mrdarkimc.DataFetcher.filemanager;

import org.mrdarkimc.DataFetcher.attributes.Attribute;
import org.mrdarkimc.DataFetcher.Main;
import org.mrdarkimc.DataFetcher.data.DataPacket;

import java.util.List;

public class FileNamingStrategy {
    public static String getNamingForDataFile(DataPacket dataPacket, String fileFormat){
        return defineName(dataPacket,fileFormat,true);
    }
    public static String getNamingForZipFile(DataPacket dataPacket){
        return defineName(dataPacket,".zip",false);
    }
    private static String defineName(DataPacket data, String fileFormat, boolean includePagination) {
        Attribute from = data.getLink().getDateFrom();
        Attribute to = data.getLink().getDateUpto();

        StringBuilder builder = new StringBuilder();
        builder.append(Main.name).append("_");
        if (from != null) {
            builder.append(from.getValue());
        }
        builder.append("-");
        if (to != null) {
            builder.append(to.getValue());
        }
        if (includePagination){
            builder.append("_");
            builder.append("Page-").append("%s");
        }
        builder.append(fileFormat);
        return builder.toString();
    }
}
