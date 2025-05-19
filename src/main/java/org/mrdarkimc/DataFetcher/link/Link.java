package org.mrdarkimc.DataFetcher.link;

import org.mrdarkimc.DataFetcher.attributes.Attribute;
import org.mrdarkimc.DataFetcher.attributes.IAttribute;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Link {
    private static final String url = "https://budget.gov.ru/epbs/registry/ubpandnubp/data";
    private final Attribute from;
    private final Attribute to;
    private final Attribute[] extraAttributes;

    public Link(Attribute from, Attribute to, Attribute... extraAttributes) {
        this.from = from;
        this.to = to;
        this.extraAttributes = extraAttributes;
    }

    public Attribute getDateFrom() { //nullabe
        return from;
    }


    public Attribute getDateUpto() { //nullable
        return to;
    }

    public LinkBuilder newBuilder() {
        return new LinkBuilder();
    }

    public class LinkBuilder {
        private final Set<IAttribute> attributes = new LinkedHashSet<>();

        private LinkBuilder() {
            addAttribute(new Attribute("pageSize", String.valueOf(100))); //insert ddefault values
            if (from != null)
                addAttribute(from);
            if (to != null)
                addAttribute(to);
            addAttribute(new Attribute("pageNum", "1"));
            if (extraAttributes != null && extraAttributes.length > 0) {
                for (Attribute extraAttribute : extraAttributes) {
                    addAttribute(extraAttribute);
                }
            }
        }

        public LinkBuilder addAttribute(IAttribute param) {
            attributes.add(param);
            return this;
        }

        public LinkBuilder addAttribute(IAttribute param, boolean replace) {
            if (replace) {
                attributes.remove(param);
            }
            attributes.add(param);
            return this;
        }

        public LinkBuilder setPage(int pageNum) {
            Attribute attribute = new Attribute("pageNum", String.valueOf(pageNum));
            return addAttribute(attribute, true);
        }


        public String buildURL() {
            StringBuilder builder = new StringBuilder(url).append("?");

            Iterator<IAttribute> iterator = attributes.iterator();
            while (iterator.hasNext()) {
                IAttribute attribute = iterator.next();
                attribute.concat(builder);
                if (iterator.hasNext()) {
                    builder.append("&");
                }
            }

            return builder.toString();
        }
    }
}
