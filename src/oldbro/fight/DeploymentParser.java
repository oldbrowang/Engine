package oldbro.fight;

import oldbro.util.XmlParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DeploymentParser extends XmlParser
{
    static String deploymentXml;

    static Node currentDeployment;

    static  {
        deploymentXml = "config/monsterDeployment.xml";
    }

    static void load()
    {
        currentDeployment = null;;
    }

    public static Deployment getDeployment()
    {
        if (currentDeployment == null) {
            currentDeployment = getFirstDeployment();
        } else {
            currentDeployment = currentDeployment.getNextSibling();
        }
        if (currentDeployment == null) {
            return null;
        }
        String deploymentName = currentDeployment.getNodeName();
        if (deploymentName == "#text" || deploymentName == "#comment") {
            return getDeployment();
        } else if (deploymentName == "delay") {
            return createDelayDeployment(currentDeployment);
        } else {
            return createMonsterDeployment(deploymentName, currentDeployment);
        }
    }

    public static Node getFirstDeployment()
    {
        Node root = getRootNode(deploymentXml);
        return getFirstChildNode(root);
    }

    public static DelayDeployment createDelayDeployment(Node item)
    {
        String durationStr = item.getTextContent();
        double duration = Double.parseDouble(durationStr);
        return new DelayDeployment(duration);
    }

    public static MonsterDeployment createMonsterDeployment(String deploymentName, Node deployment)
    {
        boolean isFree;
        if (deploymentName.endsWith("_")) {
            isFree = false;
            deploymentName = deploymentName.substring(0, deploymentName.length() - 1);
        } else {
            isFree = true;
        }
        String monsterClassName = getMonsterClassNameByDeploymentName(deploymentName);
        NodeList childNodes = deployment.getChildNodes();
        Node child;
        String childName;
        String childValue;
        int startX = 0;
        int startY = 0;
        int speed = 0;
        for (int i = 0; i < childNodes.getLength(); i++) {
            child = childNodes.item(i);
            childName = child.getNodeName();
            if (childName == "#text") {
                continue;
            }
            childValue = child.getTextContent();
            if (childName == "startX") {
                startX = Integer.parseInt(childValue);
            }
            if (childName == "startY") {
                startY = Integer.parseInt(childValue);
            }
            if (childName == "speed") {
                speed = Integer.parseInt(childValue);
            }
        }
        return new MonsterDeployment(monsterClassName, startX, startY, speed, isFree);
    }

    public static String getMonsterClassNameByDeploymentName(String deploymentName)
    {
        return deploymentName.substring(0, 1).toUpperCase()
                + deploymentName.substring(1, deploymentName.length()).toLowerCase();
    }
}
