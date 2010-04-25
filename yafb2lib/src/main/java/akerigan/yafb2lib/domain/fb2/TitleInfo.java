package akerigan.yafb2lib.domain.fb2;

import akerigan.yafb2lib.utils.xml.DomUtils;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic information about the fb2
 *
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 13.08.2008
 *         Time: 22:01:18
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TitleInfo {

    @XmlElement(name = "genre", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<String> genres = new ArrayList<String>();

    @XmlElement(name = "author", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<Author> authors = new ArrayList<Author>();

    @XmlElement(name = "book-title", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField bookTitle;

    @XmlAnyElement
    Element annotation;

    @XmlTransient
    String annotation2;

    @XmlElement(namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField keywords;

    @XmlElement(namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField date;

    @XmlElement(name = "coverpage", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    CoverPage coverPage;

    @XmlElement(name = "lang", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    String language;

    @XmlElement(name = "src-lang", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    String sourceLanguage;

    @XmlElement(name = "translator", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<Author> translators = new ArrayList<Author>();

    @XmlElement(name = "sequence", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<Sequence> sequences = new ArrayList<Sequence>();

    @XmlTransient
    int id;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String genre : genres) {
            sb.append("\n--- genre ---\n");
            sb.append(genre);
        }
        for (Author author : authors) {
            sb.append("\n--- author ---");
            sb.append(author);
        }
        sb.append("\n--- book title ---\n");
        sb.append(bookTitle);
        if (getAnnotation2() != null) {
            sb.append("\n--- annotation ---\n");
            sb.append(getAnnotation2());
        }
        if (keywords != null) {
            sb.append("\n--- keywords ---\n");
            sb.append(keywords);
        }
        if (date != null) {
            sb.append("\n--- date ---\n");
            sb.append(date);
        }
        if (coverPage != null) {
            sb.append("\n--- coverpage ---");
            sb.append(coverPage);
        }
        if (language != null) {
            sb.append("\n--- language ---\n");
            sb.append(language);
        }
        if (sourceLanguage != null) {
            sb.append("\n--- source language ---\n");
            sb.append(sourceLanguage);
        }
        for (Author translator : translators) {
            sb.append("\n--- translator ---");
            sb.append(translator);
        }
        for (Sequence sequence : sequences) {
            sb.append("\n--- sequence ---");
            sb.append(sequence);
        }
        return sb.toString();
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void addGenre(String genre) {
        genres.add(genre);
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

    public TextField getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(TextField bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Element getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Element annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation2() {
        if (annotation2 == null && annotation != null) {
            String text = DomUtils.getElementAsText(annotation, false);
            if (text != null) {
                annotation2 = text.trim().replaceAll("\\s+", " ");
            }
        }
        return annotation2;
    }

    public void setAnnotation2(String annotation2) {
        this.annotation2 = annotation2;
    }

    public TextField getKeywords() {
        return keywords;
    }

    public void setKeywords(TextField keywords) {
        this.keywords = keywords;
    }

    public TextField getDate() {
        return date;
    }

    public void setDate(TextField date) {
        this.date = date;
    }

    public CoverPage getCoverPage() {
        return coverPage;
    }

    public void setCoverPage(CoverPage coverPage) {
        this.coverPage = coverPage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public List<Author> getTranslators() {
        return translators;
    }

    public void setTranslators(List<Author> translators) {
        this.translators = translators;
    }

    public void addTranslator(Author translator) {
        translators.add(translator);
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
