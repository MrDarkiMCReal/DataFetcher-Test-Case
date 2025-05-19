package org.mrdarkimc.DataFetcher.attributes;

import org.mrdarkimc.DataFetcher.Validator;
import org.mrdarkimc.DataFetcher.exceptions.DuplicationArgsException;

public class AttributeUtils {
    public static Attribute[] getAttributesFromString(String userInput) throws IllegalArgumentException {
        String[] args = userInput.split(" ");
        return fillAttributes(args);
    }

    private static Attribute[] fillAttributes(String... input) throws IllegalArgumentException {
        Attribute[] attributes = new Attribute[input.length / 2];
        for (int i = 0; i < input.length - 1; i = i + 2) {
            String command = input[i];
            String arg = input[i + 1];
            if (System.getProperty("NoValidation") ==null){
                Validator.validateDateFormat(arg);
            }
            attributes[i != 0 ? i / 2 : i] = new Attribute(command, arg);
        }
        return attributes;
    }

    public static Attribute tryFindFromKey(String key, Attribute... attributes) {
        int count = 0;
        Attribute attribute = null;
        for (Attribute atb : attributes) {
            if (atb.getKey().equals(key)) {
                count++;
                attribute = atb;
                if (count > 1) throw new DuplicationArgsException();
            }
        }
        return attribute;
    }
}
