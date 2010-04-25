package akerigan.yafb2lib.utils.xml;

import org.w3c.dom.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 18.09.2008
 *         Time: 22:28:27
 */
public class DomUtils {

    public static String getText(Element element) {
        if (element != null) {
            try {
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                StringWriter sw = new StringWriter();
                transformer.transform(new DOMSource(element), new StreamResult(sw));
                return sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getElementAsText(Element element, boolean includeCurrent) {
        StringBuilder sb = new StringBuilder();
        if (includeCurrent) {
            sb.append("<");
            sb.append(element.getTagName());
        }
        StringBuilder sba = new StringBuilder();
        StringBuilder sbt = new StringBuilder();
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    sbt.append(getElementAsText((Element) node, true));
                    break;
                case Node.TEXT_NODE:
                    sbt.append(node.getTextContent().trim());
                    break;
                case Node.ATTRIBUTE_NODE:
                    sba.append(" ");
                    sba.append(getAttributeAsText((Attr) node));
                    break;
            }
        }
        if (includeCurrent && sba.length() != 0) {
            sb.append(sba);
        }
        if (includeCurrent) {
            if (sbt.length() != 0) {
                sb.append(">");
                sb.append(sbt);
                sb.append("</");
                sb.append(element.getTagName());
                sb.append(">");
            } else {
                sb.append("/>");
            }
        } else {
            sb.append(sbt);
        }

        return sb.toString();
    }

    public static String getAttributeAsText(Attr attr) {
        StringBuilder sb = new StringBuilder();
        sb.append(attr.getName());
        sb.append("=\"");
        sb.append(attr.getValue());
        sb.append("\"");
        return sb.toString();
    }
}
