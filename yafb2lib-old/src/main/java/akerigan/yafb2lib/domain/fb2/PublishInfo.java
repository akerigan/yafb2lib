package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 14.08.2008
 * Time: 0:04:48
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PublishInfo {

    @XmlElement(name = "book-name", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField bookName;

    @XmlElement(name = "publisher", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField publisher;

    @XmlElement(name = "city", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField city;

    @XmlElement(name = "year", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    String year;

    @XmlElement(name = "isbn", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField isbn;

    @XmlElement(name = "sequence", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<Sequence> sequences = new ArrayList<Sequence>();

    @XmlTransient
    int id;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (bookName != null) {
            sb.append("\n--- book name ---\n");
            sb.append(bookName);
        }
        if (publisher != null) {
            sb.append("\n--- publisher ---\n");
            sb.append(publisher);
        }
        if (city != null) {
            sb.append("\n--- city ---\n");
            sb.append(city);
        }
        if (year != null) {
            sb.append("\n--- year ---\n");
            sb.append(year);
        }
        if (isbn != null) {
            sb.append("\n--- isbn ---\n");
            sb.append(isbn);
        }
        for (Sequence sequence : sequences) {
            sb.append("\n--- sequence ---");
            sb.append(sequence);
        }
        return sb.toString();
    }

    public TextField getBookName() {
        return bookName;
    }

    public void setBookName(TextField bookName) {
        this.bookName = bookName;
    }

    public TextField getPublisher() {
        return publisher;
    }

    public void setPublisher(TextField publisher) {
        this.publisher = publisher;
    }

    public TextField getCity() {
        return city;
    }

    public void setCity(TextField city) {
        this.city = city;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public TextField getIsbn() {
        return isbn;
    }

    public void setIsbn(TextField isbn) {
        this.isbn = isbn;
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    public void setSequences(List<Sequence> sequences) {
        this.sequences = sequences;
    }

    public void addSequence(Sequence sequence) {
        sequences.add(sequence);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
