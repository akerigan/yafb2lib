package akerigan;

/**
 * Date: 25.04.2010
 * Time: 21:28:03
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class StringUtils {

    /**
     * Encode a byte array as a hex encoded string.
     */
    public static String hexencode(byte[] bs) {
        StringBuffer sb = new StringBuffer(bs.length * 2);
        for (byte element : bs) {
            int c = element & 0xFF;
            if (c < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(c));
        }

        return sb.toString();
    }
    
}
