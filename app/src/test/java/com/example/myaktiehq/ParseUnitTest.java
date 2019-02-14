package com.example.myaktiehq;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ParseUnitTest {
    private static final String TAG = ParseUnitTest.class.getName() + "\n\b-->";

    @Test
    public void test() {
        try {
            File fXmlFile = new File("F:/home/temp/data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("row");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                NodeList childNodes = nNode.getChildNodes();
                Node node = null;
                for (int i = 0; i < childNodes.getLength(); i++) {
                    node = childNodes.item(i).getNextSibling();
                    Element eElement = (Element) node;
                    System.out.println("\nCurrent Element :" + eElement.getAttribute("id"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}