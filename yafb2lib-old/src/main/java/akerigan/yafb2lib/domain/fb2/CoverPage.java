package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13.08.2008
 * Time: 23:23:24
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CoverPage {

    @XmlTransient
    int id;

    @XmlElement(name = "image", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<Image> images = new ArrayList<Image>();

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Image image : images) {
            sb.append("\n--- image ---");
            sb.append(image);
        }
        return sb.toString();
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
