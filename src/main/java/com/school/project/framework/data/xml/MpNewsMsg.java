package com.school.project.framework.data.xml;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("xml")
public class MpNewsMsg extends BaseMsg implements Serializable{

	private int ArticleCount;
	
	private List<Item> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Item> getArticles() {
		return Articles;
	}

	public void setArticles(List<Item> articles) {
		Articles = articles;
	}

	
}
