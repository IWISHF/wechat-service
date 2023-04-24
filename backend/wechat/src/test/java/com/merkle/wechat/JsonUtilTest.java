package com.merkle.wechat;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import weixin.popular.api.CommentAPI;
import weixin.popular.bean.comment.CommentList;
import weixin.popular.bean.comment.CommentListResult;

public class JsonUtilTest {
    @Test
    public void test() throws IOException {
        String jsonString = "{\"k1\":\"v1\",\"k2\":\"v2\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        JsonNode jsonNode1 = actualObj.get("k1");
        Assert.assertEquals(jsonNode1.textValue(), "v1");
    }

    @Test
    public void testGetData() {
        CommentList list = new CommentList();
        list.setMsg_data_id(2247486942L);
        list.setIndex(1);
        CommentListResult result = CommentAPI.list(
                "25_V1EgOvvMaM2Sfirt-Q4ZgnZRxY_KoBNcCVBvxDdfOQ_PmptJdXFCLSbPj6NgO34BpHyj4PrkAQbBfBNBgkoeFZWk_ZaMb7K4jJ5UqfQUudGms66i6-alUXanPPDmYaV6DOeC3Pi-Af42UlJaDDBjAMDQQO",
                list);
        System.out.println(result.getComment());
    }
}
