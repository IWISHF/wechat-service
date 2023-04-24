package com.qq.weixin.mp.aes;

import org.apache.commons.codec.binary.Base64;

import net.sf.json.JSONObject;

/**
 * 封装对外访问方法
 * 
 * @author liuyazhuang
 *
 */
public class WXMiniDataCrypt {

    private static final String WATERMARK = "watermark";
    private static final String APPID = "appid";

    /**
     * 解密数据
     * 
     * @return
     * @throws Exception
     */
    public static String decrypt(String appId, String encryptedData, String sessionKey, String iv) {
        String result = "";
        try {
            AES aes = new AES();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey),
                    Base64.decodeBase64(iv));
            if (null != resultByte && resultByte.length > 0) {
                result = new String(PKCS7Encoder.decode(resultByte));
                JSONObject jsonObject = JSONObject.fromObject(result);
                String decryptAppid = jsonObject.getJSONObject(WATERMARK).getString(APPID);
                if (!appId.equals(decryptAppid)) {
                    result = "";
                }
            }
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String appId = "wxb7bfcfa4511c8782";
        String encryptedData = "NtG2nNqb6TnTbw7LbikrpMJxKAwHrCWHz8YX2G6gJbxi842Mm2+LEaVuh5zyLSNLOgVOnNPtdtFG9Whu/cmbzDOgUFe+9ebsArPhRqrQ5S/hbL6mKJVXJmaYLDfTlucLqy4GovPeiYIhJNZpLstyQoPgw/cdIVjZOA/4Uw9D1GMCkeXTgfW0ZQ6XgCHvU4O03vArfgMYk/jAu+dXpBVjig==";
        String sessionKey = "UvFdULBSYNRhtuKUk+99jw==";
        String iv = "LsMeJ7zZb5jpbKD1JZnkzg==";
        System.out.println(decrypt(appId, encryptedData, sessionKey, iv));
    }
}