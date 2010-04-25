package akerigan.yafb2lib.domain.fb2;

import akerigan.yafb2lib.utils.xml.DomUtils;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ���� ��������� (akerigan@gmail.com)
 *         Date: 13.08.2008
 *         Time: 23:45:23
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentInfo {

    @XmlElement(name = "author", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<Author> authors = new ArrayList<Author>();

    @XmlElement(name = "program-used", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField programUsed;

    @XmlElement(namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField date;

    @XmlElement(name = "src-url", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<String> sourceURLs = new ArrayList<String>();

    @XmlElement(name = "src-ocr", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField sourceOCR;

    @XmlElement(name = "id", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    String fb2Id;

    @XmlElement(namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    String version;

    @XmlAnyElement
    Element history;

    @XmlTransient
    String history2;

    @XmlTransient
    int id;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Author author : authors) {
            sb.append("\n--- author ---");
            sb.append(author);
        }
        if (programUsed != null) {
            sb.append("\n--- program used ---\n");
            sb.append(programUsed);
        }
        if (date != null) {
            sb.append("\n--- date ---\n");
            sb.append(date);
        }
        for (String sourceURL : sourceURLs) {
            sb.append("\n--- source URL ---\n");
            sb.append(sourceURL);
        }
        if (sourceOCR != null) {
            sb.append("\n--- source OCR ---\n");
            sb.append(sourceOCR);
        }
        if (fb2Id != null) {
            sb.append("\n--- fb2 id ---\n");
            sb.append(fb2Id);
        }
        if (version != null) {
            sb.append("\n--- version ---\n");
            sb.append(version);
        }
        if (getHistory2() != null) {
            sb.append("\n--- history ---\n");
            sb.append(getHistory2());
        }
        return sb.toString();
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public TextField getProgramUsed() {
        return programUsed;
    }

    public void setProgramUsed(TextField programUsed) {
        this.programUsed = programUsed;
    }

    public TextField getDate() {
        return date;
    }

    public void setDate(TextField date) {
        this.date = date;
    }

    public List<String> getSourceURLs() {
        return sourceURLs;
    }

    public void setSourceURLs(List<String> sourceURLs) {
        this.sourceURLs = sourceURLs;
    }

    public void addSourceURLs(String sourceURL) {
        sourceURLs.add(sourceURL);
    }

    public TextField getSourceOCR() {
        return sourceOCR;
    }

    public void setSourceOCR(TextField sourceOCR) {
        this.sourceOCR = sourceOCR;
    }

    public String getFb2Id() {
        return fb2Id;
    }

    public void setFb2Id(String fb2Id) {
        this.fb2Id = fb2Id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Element getHistory() {
        return history;
    }

    public void setHistory(Element history) {
        this.history = history;
    }

    public String getHistory2() {
        if (history2 == null && history != null) {
            String text = DomUtils.getElementAsText(history, false);
            if (text != null) {
                history2 = text.trim().replaceAll("\\s+", " ");
            }
        }
        return history2;
    }

    public void setHistory2(String history2) {
        this.history2 = history2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
