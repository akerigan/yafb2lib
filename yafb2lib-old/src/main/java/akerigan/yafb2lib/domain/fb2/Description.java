package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13.08.2008
 * Time: 21:47:49
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Description {

    @XmlElement(name = "title-info", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TitleInfo titleInfo = new TitleInfo();

    @XmlElement(name = "document-info", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    DocumentInfo documentInfo = new DocumentInfo();

    @XmlElement(name = "publish-info", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    PublishInfo publishInfo = new PublishInfo();

    @XmlElement(name = "custom-info", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<CustomInfo> customInfos = new ArrayList<CustomInfo>();

    @XmlTransient
    int id;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== title info ===");
        sb.append(titleInfo);
        sb.append("\n=== document info ===");
        sb.append(documentInfo);
        sb.append("\n=== publish info ===");
        sb.append(publishInfo);
        for (CustomInfo customInfo : customInfos) {
            sb.append("\n=== custom info ===");
            sb.append(customInfo);
        }
        return sb.toString();
    }

    public Description() {
    }

    public TitleInfo getTitleInfo() {
        return titleInfo;
    }

    public void setTitleInfo(TitleInfo titleInfo) {
        this.titleInfo = titleInfo;
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    public PublishInfo getPublishInfo() {
        return publishInfo;
    }

    public void setPublishInfo(PublishInfo publishInfo) {
        this.publishInfo = publishInfo;
    }

    public List<CustomInfo> getCustomInfos() {
        return customInfos;
    }

    public void setCustomInfos(List<CustomInfo> customInfos) {
        this.customInfos = customInfos;
    }

    public void addCustomInfo(CustomInfo customInfo) {
        customInfos.add(customInfo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
