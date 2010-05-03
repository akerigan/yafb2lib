package akerigan.fb2.domain;

/**
 * Date: 03.05.2010
 * Time: 21:13:54
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookFile {

    private int id;
    private String sha1;
    private String container;
    private String file;
    private int size;

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

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
