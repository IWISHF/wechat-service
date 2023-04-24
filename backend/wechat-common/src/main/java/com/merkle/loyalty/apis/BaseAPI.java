package com.merkle.loyalty.apis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import com.merkle.loyalty.PrismEnv;
import com.merkle.loyalty.response.PrismResponse;
import com.merkle.loyalty.util.SignatureUtil;

/**
 * 
 * @author tyao
 *
 */
public abstract class BaseAPI {

    public static PrismResponse send(PrismEnv env, String path, Map<String, String> params) throws Exception {
        params.put("uuid", env.getUuid());
        String sigUrl = buildUrlForSig(path, env.getSecret() + "/", params);
        params.put("sig", SignatureUtil.getSignature(sigUrl));
        // generateSig(env, params);
        return sendRequest(env, path, params);
    }

    // private static void generateSig(PrismEnv env, Map<String, String> params)
    // {
    // params.put("sig", SignatureUtil.getSignature(env.getSecret(), params));
    // }

    private static PrismResponse sendRequest(PrismEnv env, String path, Map<String, String> params) throws Exception {
        String requestUrl = buildUrl(env.getBaseUrl(), path, params);
        Content content = Request.Get(requestUrl).execute().returnContent();
        System.out.println(requestUrl);
        return new PrismResponse(content.asString());
    }

    private static String buildUrlForSig(String path, String secret, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(secret);
        builder.append(path);
        builder.append("?");
        // params.forEach((k, v) -> {
        // builder.append(k);
        // builder.append("=");
        // try {
        // builder.append(URLEncoder.encode(new String(v.getBytes(), "UTF-8"),
        // "UTF-8"));
        // } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
        // }
        // builder.append("&");
        // });

        Set<String> keySet = params.keySet();
        keySet.forEach((k) -> {
            String v = params.get(k);
            if (v != null) {
                builder.append(k);
                builder.append("=");
                try {
                    builder.append(URLEncoder.encode(new String(v.getBytes(), "UTF-8"), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                builder.append("&");
            }
        });
        if (builder.toString().endsWith("&")) {
            builder.deleteCharAt(builder.lastIndexOf("&"));
        }
        return builder.toString();
    }

    private static String buildUrl(String baseUrl, String path, Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(path);
        builder.append("?");
        Set<String> keySet = params.keySet();
        keySet.forEach((k) -> {
            String v = params.get(k);
            if (v != null) {
                builder.append(k);
                builder.append("=");
                try {
                    builder.append(URLEncoder.encode(new String(v.getBytes(), "UTF-8"), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                builder.append("&");
            }
        });
        if (builder.toString().endsWith("&")) {
            builder.deleteCharAt(builder.lastIndexOf("&"));
        }
        return builder.toString();
    }

}
