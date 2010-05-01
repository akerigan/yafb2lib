package akerigan.yafb2lib.domain.fb2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13.08.2008
 * Time: 22:47:48
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Author {

    @XmlElement(name = "first-name", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField firstName;

    @XmlElement(name = "middle-name", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField middleName;

    @XmlElement(name = "last-name", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField lastName;

    @XmlElement(name = "nickname", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    TextField nickName;

    @XmlElement(name = "home-page", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<String> homePages = new ArrayList<String>();

    @XmlElement(name = "email", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    List<String> emails = new ArrayList<String>();

    @XmlElement(name = "id", namespace = "http://www.gribuser.ru/xml/fictionbook/2.0")
    String fb2Id;

    @XmlTransient
    int id;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append("\nfirst name: ");
            sb.append(firstName);
        }
        if (middleName != null) {
            sb.append("\nmiddle name: ");
            sb.append(middleName);
        }
        if (lastName != null) {
            sb.append("\nlast name: ");
            sb.append(lastName);
        }
        if (nickName != null) {
            sb.append("\nnick name: ");
            sb.append(nickName);
        }
        for (String homePage : homePages) {
            sb.append("\nhome page: ");
            sb.append(homePage);
        }
        for (String email : emails) {
            sb.append("\nemail: ");
            sb.append(email);
        }
        if (fb2Id != null) {
            sb.append("\nbook fb2Id: ");
            sb.append(fb2Id);
        }
        return sb.toString();
    }

    public TextField getFirstName() {
        return firstName;
    }

    public void setFirstName(TextField firstName) {
        this.firstName = firstName;
    }

    public TextField getMiddleName() {
        return middleName;
    }

    public void setMiddleName(TextField middleName) {
        this.middleName = middleName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public void setLastName(TextField lastName) {
        this.lastName = lastName;
    }

    public TextField getNickName() {
        return nickName;
    }

    public void setNickName(TextField nickName) {
        this.nickName = nickName;
    }

    public List<String> getHomePages() {
        return homePages;
    }

    public void setHomePages(List<String> homePages) {
        this.homePages = homePages;
    }

    public void addHomePage(String homePage) {
        homePages.add(homePage);
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void addEmail(String email) {
        emails.add(email);
    }

    public String getFb2Id() {
        return fb2Id;
    }

    public void setFb2Id(String fb2Id) {
        this.fb2Id = fb2Id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
