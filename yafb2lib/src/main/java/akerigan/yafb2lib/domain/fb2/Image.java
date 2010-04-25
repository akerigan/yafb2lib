package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 13.08.2008
 *         Time: 23:25:02
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {

    @XmlTransient
    int id;

    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    String type;

    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    String href;

    @XmlAttribute
    String alt;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type != null) {
            sb.append("\ntype: ");
            sb.append(type);
        }
        if (href != null) {
            sb.append("\nhref: ");
            sb.append(href);
        }
        if (alt != null) {
            sb.append("\nalt: ");
            sb.append(alt);
        }
        return sb.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
