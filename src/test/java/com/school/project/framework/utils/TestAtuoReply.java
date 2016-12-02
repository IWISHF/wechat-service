package com.school.project.framework.utils;

import org.junit.Test;

public class TestAtuoReply {

	@Test
	public void test() {
		String xml = AutoReplyMessageUtil.autoReplyMpNews("tousername", "fromUsername");
		System.out.println(xml);
	}

}

