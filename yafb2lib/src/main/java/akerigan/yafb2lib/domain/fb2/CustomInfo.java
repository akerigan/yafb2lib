package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.*;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 14.08.2008
 *         Time: 0:12:04
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomInfo {

    @XmlTransient
    int id;

    @XmlAttribute(name = "info-type")
    String infoType;

    @XmlAttribute(name = "lang")
    String language;

    @XmlValue
    String value;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (infoType != null) {
            sb.append("\ninfo type: ");
            sb.append(infoType);
        }
        if (language != null) {
            sb.append("\nlanguage: ");
            sb.append(language);
        }
        if (value != null) {
            sb.append("\nvalue: ");
            sb.append(value);
        }
        return sb.toString();
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
