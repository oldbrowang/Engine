package oldbro.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlParser
{
    public static Node getRootNode(String fileName)
    {
        Node root = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            root = getFirstChildNode(doc);
        } catch (Exception e) {
            System.out.println(e);
        }
        return root;
    }

    public static Node getFirstChildNode(Node parentNode)
    {
        return parentNode.getChildNodes().item(0);
    }
}