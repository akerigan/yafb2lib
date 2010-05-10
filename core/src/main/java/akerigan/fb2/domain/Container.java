package akerigan.fb2.domain;

import java.util.List;

/**
 * Date: 06.05.2010
 * Time: 21:28:49
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class Container {

    private int id;
    private String name;
    private long size;
    private boolean saved;

    private List<Book> books;

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

    public List<Book> getBooksInfo() {
        return books;
    }

    public void setBooksInfo(List<Book> books) {
        this.books = books;
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
        sb.append("<Container>\n");
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
        if (books != null) {
            sb.append("<books>");
            for (Object obj : books) {
                sb.append("<item>");
                sb.append(obj);
                sb.append("</item>\n");
            }
            sb.append("</books>\n");
        } else {
            sb.append("<books/>\n");
        }
        sb.append("</Container>\n");

        return sb.toString();
    }
}
