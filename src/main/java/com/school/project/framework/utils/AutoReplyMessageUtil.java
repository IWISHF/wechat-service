package com.school.project.framework.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.school.project.framework.data.xml.Item;
import com.school.project.framework.data.xml.MpNewsMsg;
import com.school.project.framework.data.xml.TextMsg;

public class AutoReplyMessageUtil {
	
	public static String autoReplyMpNews(String toUserName, String fromUserName) {
		Item ite1 = new Item("test", "test", "http://picture.tunnel.qydev.com/wx/pic/Koala.jpg", "http://iwish.tunnel.qydev.com/wx/test");
		Item ite2 = new Item("test", "test", "http://picture.tunnel.qydev.com/wx/pic/Koala.jpg", "http://iwish.tunnel.qydev.com/wx/test");
		List<Item> articles = new ArrayList<Item>();
		articles.add(ite1);
		articles.add(ite2);
		MpNewsMsg mpnews = new MpNewsMsg();
		mpnews.setArticleCount(2);
		mpnews.setArticles(articles);
		mpnews.setCreateTime(new Date().getTime());
		mpnews.setFromUserName(toUserName);
		mpnews.setToUserName(fromUserName);
		mpnews.setMsgType("news");
		return XmlUtil.objectToXml(mpnews);
	}

	public static String autoReplyTextMessage(String toUserName, String fromUserName, String content) {
		StringBuffer sb = new StringBuffer();
		appendString(sb,"亲爱的你没有按照规则来哦，你发送的消息是：" + content);
		TextMsg msg = new TextMsg();
		setValues(toUserName, fromUserName, sb, msg);
		return XmlUtil.objectToXml(msg);
	}
	
	public static String autoReplySubscribeMsg(String toUserName, String fromUserName) {
		StringBuffer sb = new StringBuffer();
		appendString(sb,"欢迎您的关注，请按照菜单提示进行操作：\n", "1、公众号介绍", "2、调戏客服", "3、词组翻译\n", "回复？调出此菜单。");
		TextMsg msg = new TextMsg();
		setValues(toUserName, fromUserName, sb, msg);
		return XmlUtil.objectToXml(msg);
	}
	
	public static String autoReplyFun1(String toUserName, String fromUserName) {
		StringBuffer sb = new StringBuffer();
		appendString(sb,"公众号介绍：", "该公众号主要面向编程爱好者", "正在开发中，精彩内容马上上线哦", "最后，谢谢您的关注！！");
		TextMsg msg = new TextMsg();
		setValues(toUserName, fromUserName, sb, msg);
		return XmlUtil.objectToXml(msg);
	}
	
	public static String autoReplyFun2(String toUserName, String fromUserName) {
		StringBuffer sb = new StringBuffer();
		appendString(sb,"小编很忙的哦，请勿打扰");
		TextMsg msg = new TextMsg();
		setValues(toUserName, fromUserName, sb, msg);
		return XmlUtil.objectToXml(msg);
	}
	
	public static String autoReplyFun3(String toUserName, String fromUserName) {
		StringBuffer sb = new StringBuffer();
		appendString(sb,"马不停蹄的编码，还是满足不了你吗，满足不了吗，满足。。。");
		TextMsg msg = new TextMsg();
		setValues(toUserName, fromUserName, sb, msg);
		return XmlUtil.objectToXml(msg);
	}

	private static void setValues(String toUserName, String fromUserName, StringBuffer sb, TextMsg msg) {
		msg.setContent(sb.toString());
		msg.setCreateTime(new Date().getTime());
		msg.setFromUserName(toUserName);
		msg.setToUserName(fromUserName);
		msg.setMsgType("text");
	}
	
	private static void appendString(StringBuffer sb,String...args) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (i == args.length - 1) {
					sb.append(args[i]);
					break;
				}
				sb.append(args[i] + "\n");
			}
		}
	}
	
}
