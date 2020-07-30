package com.securenative.agent.snpackage;

import com.securenative.agent.utils.Utils;
import com.securenative.agent.models.Dependency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class PackageManager {
    private static final Logger logger = Logger.getLogger(PackageManager.class.getName());
    private static Document readPackageFile(String filePath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(filePath));
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            logger.fine(String.join("Could not parse pom file; ", e.toString()));
        }

        return document;
    }

    private static Dependency[] parseDependencies(NodeList nodeList) {
        Dependency[] dependencies = new Dependency[nodeList.getLength()];

        int j = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                String name = eElement.getElementsByTagName("groupId").item(0).getTextContent().concat(
                        ":").concat(eElement.getElementsByTagName("artifactId").item(0).getTextContent());
                dependencies[j] = new Dependency(name, eElement.getElementsByTagName("version").item(0).getTextContent());
                j += 1;
            }
        }

        return dependencies;
    }

    private static String parseParent(NodeList nodeList, String key) {
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) node;
            return eElement.getAttribute(key);
        }

        return "";
    }

    public static PackageItem getPackage(String packageFilePath) {
        Document document = readPackageFile(packageFilePath);

        NodeList deps = document.getElementsByTagName("dependency");
        NodeList parent = document.getElementsByTagName("parent");

        String artifactId;
        String groupId;
        String version;

        if (parent.getLength() > 0) {
            artifactId = parseParent(parent, "artifactId");
            groupId = parseParent(parent, "groupId");
            version = parseParent(parent, "version");
        } else {
            artifactId = document.getElementsByTagName("artifactId").item(0).getTextContent();
            groupId = document.getElementsByTagName("groupId").item(0).getTextContent();
            version = document.getElementsByTagName("version").item(0).getTextContent();
        }

        Dependency[] dependencies = parseDependencies(deps);
        String dependenciesHash = Utils.calculateHash(Arrays.toString(dependencies));

        String name = groupId.concat(":").concat(artifactId);
        return new PackageItem(name, version, dependencies, dependenciesHash);
    }
}
