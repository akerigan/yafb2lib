package akerigan.yafb2lib.utils.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author ���� ��������� (akerigan@gmail.com)
 *         Date: 18.03.2008
 *         Time: 20:48:15
 */
public class PostfixFilenameFilter implements FilenameFilter {
    private String postfix;

    public PostfixFilenameFilter(String postfix) {
        this.postfix = postfix;
    }

    public boolean accept(File dir, String name) {
        if (name.endsWith(postfix)) {
            return true;
        } else {
            return false;
        }
    }
}
