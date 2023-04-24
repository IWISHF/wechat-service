package com.merkle.wechat.modules.digikey.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.modules.digikey.vo.CMSSearchVo;
import com.merkle.wechat.modules.digikey.vo.DigikeyBodyData;
import com.merkle.wechat.modules.digikey.vo.DigikeyKeywordSearchVo;
import com.merkle.wechat.modules.digikey.vo.DigikeyProductDetailVo;
import com.merkle.wechat.modules.digikey.vo.PartDetailBodyData;

@Component
public class DigikeyAPIServiceImpl {
    @Value("${digikey.oauth.client.id}")
    private String clientId;

    private @Autowired DigikeyOAuthServiceImpl digikeyOAuthServiceImpl;

    public JsonNode keywordSearch(DigikeyKeywordSearchVo vo) throws UnirestException {
        String requestPath = "https://api.digikey.com/services/partsearchwechat/v2/keywordsearch";
        DigikeyBodyData data = new DigikeyBodyData();
        data.setKeywords(vo.getKeyword());
        data.setRecordCount(vo.getPageSize());
        data.setRecordStartPosition((vo.getCurrentPage() - 1) * vo.getPageSize());
        data.setSearchOptions(vo.getSearchOptions());
        data.setSort(vo.getSort());
        data.setFilters(vo.getFilters());
        HttpResponse<JsonNode> resp = Unirest.post(requestPath).header("accept", "application/json")
                .header("content-type", "application/json").header("X-DIGIKEY-Locale-Language", "zh")
                .header("X-DIGIKEY-Locale-Site", "CN").header("x-ibm-client-id", clientId)
                .header("X-DIGIKEY-Locale-Currency", vo.getCurrency())
                .header("authorization", digikeyOAuthServiceImpl.getDigikeyAccessToken()).body(new JSONObject(data))
                .asJson();
        if (resp.getStatus() == 200) {
            return resp.getBody();
        } else {
            throw new ServiceWarn(resp.getBody().toString(), resp.getStatus());
        }
    }

    public JsonNode productDetail(DigikeyProductDetailVo vo) throws UnirestException {
        String requestPath = "https://api.digikey.com/services/partsearchwechat/v2/partdetails";
        PartDetailBodyData data = new PartDetailBodyData();
        data.setIncludeAllAssociateProducts(vo.getIncludeAllAssociateProducts());
        data.setIncludeAllForUseWithProducts(vo.getIncludeAllForUseWithProducts());
        data.setPart(vo.getPart());
        HttpResponse<JsonNode> resp = Unirest.post(requestPath).header("accept", "application/json")
                .header("content-type", "application/json").header("X-DIGIKEY-Locale-Language", "zh")
                .header("X-DIGIKEY-Locale-Site", "CN").header("x-ibm-client-id", clientId)
                .header("X-DIGIKEY-Locale-Currency", vo.getCurrency())
                .header("authorization", digikeyOAuthServiceImpl.getDigikeyAccessToken()).body(new JSONObject(data))
                .asJson();
        if (resp.getStatus() == 200) {
            return resp.getBody();
        } else {
            throw new ServiceWarn(resp.getBody().toString(), resp.getStatus());
        }
    }

    public JsonNode cmsArticleSearch(CMSSearchVo vo) throws Exception {
        String cmsPath = "https://lp-cn1-wechat-production.merklechina.com/wp-json/wp/v2/posts";
        HttpResponse<JsonNode> resp = Unirest.get(cmsPath).queryString("search", vo.getKeywords())
                .queryString("page", vo.getCurrentPage()).queryString("perPage", vo.getPageSize()).asJson();
        if (resp.getStatus() == 200) {
            return resp.getBody();
        } else {
            throw new ServiceWarn(resp.getBody().toString(), resp.getStatus());
        }
    }

}
