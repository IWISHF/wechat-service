package com.school.project.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.school.project.framework.exceptions.SwpException;
import com.school.project.framework.exceptions.XmlToObjectFailException;
import com.thoughtworks.xstream.XStream;

public class XmlUtil {

	/**
	 * switch xml string to map
	 * @author Danny.Wang
	 * @param rquest
	 * @return
	 * @throws SwpException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws SwpException{
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = null;
		try {
			ins = request.getInputStream();
			Document doc = reader.read(ins);
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> list = root.elements();
			
			for (Element element : list) {
				map.put(element.getName(), element.getText());
			}
			
			return map;
		} catch (Exception e) {
			throw new XmlToObjectFailException("paser xml to object exception: " + e.getCause()); 
		} finally {
			try {
				ins.close();
			} catch (IOException e) {
				throw new XmlToObjectFailException("paser xml to object exception: " + e.getCause()); 
			}
		}
	}
	
	/**
	 * switch java object to xml string<p>
	 * use {@link com.thoughtworks.xstream.XStream} XStream method .alias 
	 * @author Danny.Wang
	 * @param obj
	 * @return
	 */
	public static String objectToXml(Object obj) {
		XStream stream = new XStream();
		stream.processAnnotations(obj.getClass());
		String xml = stream.toXML(obj);
		return xml;
	}
	
	/*public static <T> T xmlToObject (Class<? extends T> c, String xml) throws InstantiationException, IllegalAccessException, IOException {
		XStream stream = new XStream();
		TextMessage obj = (TextMessage) stream.fromXML(
				"<com.school.project.framework.data.xml.TextMessage>"
				+ "<ToUserName><![CDATA[toUser]]></ToUserName> "
				+ "<FromUserName>o80l0uJAH7SFHkKAhmHbPz3waUrM</FromUserName>"
				+ "<MsgType>text</MsgType>"
				+ "<Content>哦哦</Content>"
				+ "<MsgId>6221356451888457278</MsgId>"
				+ "</com.school.project.framework.data.xml.TextMessage>");
		System.out.println(obj);
		return null;
	}*/
	
	/**
	 * switch xml string to object
	 * @author Danny.Wang
	 * @param c
	 * @param xml
	 * @return
	 */
	/*public static <T> T xmlToObject(Class<? extends T> c, String xml) {
		Serializer serializer = new Persister();
		try {
			T obj = serializer.read(c, xml);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String objectToXmlString(Object object, Class<?> c) {
		StringWriter xml = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			marshaller.marshal(object, xml);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xml.toString();
	}*/
}
