package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 13.08.2008
 *         Time: 23:35:15
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Sequence {

    @XmlTransient
    int id;

    @XmlAttribute
    String name;

    @XmlAttribute
    int number;

    @XmlAttribute(name = "lang")
    String language;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append("\nname: ");
            sb.append(name);
        }
        if (number != 0) {
            sb.append("\nnumber: ");
            sb.append(number);
        }
        if (language != null) {
            sb.append("\nlanguage: ");
            sb.append(language);
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
