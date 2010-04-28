package akerigan.db;

/**
 * Date: 27.04.2010
 * Time: 22:49:07
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class TestEntry {
    private int id;
    private String name;

    TestEntry() {
    }

    public TestEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<TestEntry>\n");
        sb.append("<id>");
        sb.append(id);
        sb.append("</id>\n");
        sb.append("<name>");
        sb.append(name);
        sb.append("</name>\n");
        sb.append("</TestEntry>\n");

        return sb.toString();
    }
}
