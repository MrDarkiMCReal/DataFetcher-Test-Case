package org.mrdarkimc.DataFetcher.link;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrdarkimc.DataFetcher.attributes.Attribute;

public class LinkTest {
    @Test
    public void testLinkGenerationCase1(){
        Attribute max = new Attribute("filtermaxloaddata","20.02.10");
        Attribute min = new Attribute("filterminloaddata","20.03.10");
        Link link = new Link(min,max);
        String s = link.newBuilder().buildURL();
        String premade = "https://budget.gov.ru/epbs/registry/ubpandnubp/data?pageSize=100&filterminloaddata=20.03.10&filtermaxloaddata=20.02.10&pageNum=1";
        Assertions.assertEquals(premade,s);
    }
    @Test
    public void testLinkGenerationCase2(){
        Attribute max = new Attribute("filtermaxloaddata","20.02.10");
        Link link1 = new Link(null, max);
        String s1 = link1.newBuilder().buildURL();
        String premade1 = "https://budget.gov.ru/epbs/registry/ubpandnubp/data?pageSize=100&filtermaxloaddata=20.02.10&pageNum=1";
        Assertions.assertEquals(premade1,s1);

    }
    @Test
    public void testLinkGenerationCase3(){
        Attribute min = new Attribute("filterminloaddata","20.03.10");

        Link link2 = new Link(min, null);
        String s2 = link2.newBuilder().buildURL();
        String premade2 = "https://budget.gov.ru/epbs/registry/ubpandnubp/data?pageSize=100&filterminloaddata=20.03.10&pageNum=1";
        Assertions.assertEquals(premade2,s2);
    }

    @Test
    public void testLinkGenerationCase4(){
        Link link3 = new Link(null, null); //все данные. ps такого в программе не предусмотрено, требуется хотябы 1 вводная данная
        String s3 = link3.newBuilder().buildURL();
        String premade3 = "https://budget.gov.ru/epbs/registry/ubpandnubp/data?pageSize=100&pageNum=1";
        Assertions.assertEquals(premade3,s3);
    }
    @Test
    public void testLinkGenerationCase5(){ //page test
        Attribute min = new Attribute("filterminloaddata","20.03.10");

        Link link2 = new Link(min, null);
        String s2 = link2.newBuilder().setPage(3).buildURL();
        String premade2 = "https://budget.gov.ru/epbs/registry/ubpandnubp/data?pageSize=100&filterminloaddata=20.03.10&pageNum=3";
        Assertions.assertEquals(premade2,s2);
    }
}
