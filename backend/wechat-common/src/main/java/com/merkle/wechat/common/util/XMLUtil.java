package com.merkle.wechat.common.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLUtil {

    protected static Logger logger = LoggerFactory.getLogger("XMLUtil");

    private static DocumentBuilderFactory docFactory;

    private static DocumentBuilderFactory getInstance() {
        if (null == docFactory) {
            docFactory = DocumentBuilderFactory.newInstance();
        }
        return docFactory;
    }

    public static Document getDocument(String xmlStr) {
        docFactory = getInstance();
        Document document = null;
        try {
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(cleanInvalidXmlChars(xmlStr).getBytes()));
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            logger.error("XML parse error!", e);
        }
        return document;
    }

    /**
     * Eliminate incompatible character in the XML string.
     * 
     * <p>
     * XML processors accept any character in the range specified for Char.
     * <code>Character Range: #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]</code>
     * any Unicode character, excluding the surrogate blocks, FFFE, and FFFF.
     * </p>
     * <a href="http://www.w3.org/TR/xml/#charsets">W3C: XML/#Characters</a>
     *
     * eg: cleanInvalidXmlChars("\uFFFF🙆") -> "🙆"
     *
     * @param xmlStr
     * @return
     */
    private static String cleanInvalidXmlChars(String xmlStr) {
        if (StringUtils.isNotEmpty(xmlStr)) {
            int[] codePoints = xmlStr.codePoints().filter(XMLUtil::isValidXmlCharacter).toArray();
            xmlStr = new String(codePoints, 0, codePoints.length);
        }
        return xmlStr;
    }

    /***
     * Is char in XML character range.
     *
     * XML Character Range: #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] |
     * [#x10000-#x10FFFF]
     *
     * eg: isValidXmlCharacter("\uFFFF") -> false; isValidXmlCharacter("🙆") ->
     * true
     *
     * @param codePoint
     * @return
     */
    private static boolean isValidXmlCharacter(int codePoint) {
        // XML Character Range: #x9 | #xA | #xD | [#x20-#xD7FF] |
        // [#xE000-#xFFFD] | [#x10000-#x10FFFF]
        return (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || (codePoint >= 0x20 && codePoint <= 0xD7FF)//
                || (codePoint >= 0xE000 && codePoint <= 0xFFFD)//
                || (codePoint >= 0x10000 && codePoint <= 0x10FFFF);
    }

    public static String getNodeContent(Document document, String nodeName) {
        NodeList nl = document.getElementsByTagName(nodeName);
        if (0 == nl.getLength()) {
            return "";
        } else {
            return nl.item(0).getTextContent();
        }
    }

    public static String getNodeContent(Document document, String nodeName, int item) {
        NodeList nl = document.getElementsByTagName(nodeName);
        if (0 == nl.getLength() || item > nl.getLength() - 1) {
            return "";
        } else {
            return nl.item(item).getTextContent();
        }
    }

    public static String getNodeXml(Document document, String nodeName, int item) {
        StreamResult result = new StreamResult(new StringWriter());
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(document.getElementsByTagName(nodeName).item(item)), result);
        } catch (Exception e) {
            logger.error("Get XML node value error!", e);
        }
        return result.getWriter().toString();
    }

}
