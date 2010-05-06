package akerigan.fb2.domain;

import java.util.Map;

/**
 * Date: 17.04.2010
 * Time: 16:41:37
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookInfo {

    private int id;
    private int container;
    private String sha1;
    private String name;
    private long size;
    private Map<String, String> description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public int getContainer() {
        return container;
    }

    public void setContainer(int container) {
        this.container = container;
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

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<BookInfo>\n");
        sb.append("<id>");
        sb.append(id);
        sb.append("</id>\n");
        sb.append("<container>");
        sb.append(container);
        sb.append("</container>\n");
        sb.append("<sha1>");
        sb.append(sha1);
        sb.append("</sha1>\n");
        sb.append("<name>");
        sb.append(name);
        sb.append("</name>\n");
        sb.append("<size>");
        sb.append(size);
        sb.append("</size>\n");
        if (description != null) {
            sb.append("<description>");
            for (Object key : description.keySet()) {
                sb.append("<entry key=\"");
                sb.append(key);
                sb.append("\">");
                sb.append(description.get(key));
                sb.append("</entry>\n");
            }
            sb.append("</description>\n");
        } else {
            sb.append("<description/>\n");
        }
        sb.append("</BookInfo>\n");

        return sb.toString();
    }
}
