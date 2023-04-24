package com.merkle.wechat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateMessageTest {
    public static void main(String[] args) {
        String s = "{{first.DATA}}\n签到人：{{keyword1.DATA}}\n签到时间：{{keyword2.DATA}}\n签到状态：{{keyword3.DATA}}\n{{remark.DATA}}";
        Pattern p = Pattern.compile("(?<=\\{)\\w+");
        Matcher m = p.matcher(s);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
