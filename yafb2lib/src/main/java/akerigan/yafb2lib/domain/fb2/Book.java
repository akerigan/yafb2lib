package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.*;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 14.08.2008
 *         Time: 0:24:02
 */
@XmlRootElement(name = "FictionBook", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class Book {

    @XmlTransient
    int id;

    @XmlElement(name = "description", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    Description description = new Description();

    @XmlTransient
    String fileName;

    public Book() {
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
