package akerigan.yafb2lib.utils.file.test;

import akerigan.yafb2lib.utils.file.FileLister;
import akerigan.yafb2lib.utils.file.FilePatternType;

import java.io.File;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 18.03.2008
 *         Time: 21:15:38
 */
public class FilerTest {

    public static void main(String[] args) {
        FileLister lister = new FileLister();
        lister.addStartDir(new File("C:/Mobile/givc/download/fb2"));
        for (File file : lister.findFiles(FilePatternType.postfix, ".fb2.zip")) {
            System.out.println("file = " + file);
        }
    }

}
