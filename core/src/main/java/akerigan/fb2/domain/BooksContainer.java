package akerigan.fb2.domain;

import java.util.List;

/**
 * Date: 06.05.2010
 * Time: 21:28:49
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BooksContainer {

    private int id;
    private String name;
    private long size;
    private boolean saved;

    private List<BookInfo> booksInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<BookInfo> getBooksInfo() {
        return booksInfo;
    }

    public void setBooksInfo(List<BookInfo> booksInfo) {
        this.booksInfo = booksInfo;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<BooksContainer>\n");
        sb.append("<id>");
        sb.append(id);
        sb.append("</id>\n");
        sb.append("<name>");
        sb.append(name);
        sb.append("</name>\n");
        sb.append("<size>");
        sb.append(size);
        sb.append("</size>\n");
        sb.append("<saved>");
        sb.append(saved);
        sb.append("</saved>\n");
        if (booksInfo != null) {
            sb.append("<booksInfo>");
            for (Object obj : booksInfo) {
                sb.append("<item>");
                sb.append(obj);
                sb.append("</item>\n");
            }
            sb.append("</booksInfo>\n");
        } else {
            sb.append("<booksInfo/>\n");
        }
        sb.append("</BooksContainer>\n");

        return sb.toString();
    }
}
