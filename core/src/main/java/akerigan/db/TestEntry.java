package akerigan.db;

/**
 * User: Vlad Vinichenko (akerigan@gmail.com)
 * Date: 27.04.2010
 * Time: 22:49:07
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
        return "TestEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
