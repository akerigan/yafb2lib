package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.*;

/**
 * @author ���� ��������� (akerigan@gmail.com)
 *         Date: 13.08.2008
 *         Time: 22:54:52
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TextField {

    @XmlTransient
    int id;

    @XmlAttribute(name = "lang")
    String lang;

    @XmlValue
    String value;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (value != null) {
            sb.append(value);
            if (lang != null) {
                sb.append("(");
                sb.append(lang);
                sb.append(")");
            }
        }
        return sb.toString();
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
