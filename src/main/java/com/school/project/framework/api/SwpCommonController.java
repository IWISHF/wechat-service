package com.school.project.framework.api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;
import com.school.project.framework.utils.AutoReplyMessageUtil;
import com.school.project.framework.utils.XmlUtil;

@RestController
@RequestMapping("/wx")
public class SwpCommonController extends SwpAbstractController {
	
	@RequestMapping("/test")
	public ModelAndView test() {
		System.out.println("this is test case");
		ModelAndView mv = new ModelAndView("home");
//		ModelAndView mv = new ModelAndView();
//		RedirectView view = new RedirectView("http://www.baidu.com");
//		mv.setView(view);
		return mv;
	}
	
	/**
	 * This method is to open weixin developer model<p>
	 * Manily use SHA1 encryption and decrypt
	 * @author Danny.Wang
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 * @throws AesException
	 * @throws IOException
	 */
	@RequestMapping(value = "/wxConnect", method = RequestMethod.GET)
	public String DealWithWxConnect(String signature, String timestamp, String nonce, String echostr) throws AesException, IOException {
		String sha = SHA1.getPublicWxSHA1(ACCESS_TOKEN, timestamp, nonce);
		if (signature.equals(sha)) {
			System.out.println("this is wx connect test");
			return echostr;
		} else {
			throw new AesException(AesException.ValidateSignatureError);
		}
	}
	
	@RequestMapping(value = "/wxConnect", method = RequestMethod.POST)
	public Object DealWithWxMessage(HttpServletRequest request) throws Exception {
		Map<String, String> map = XmlUtil.xmlToMap(request);
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String msgType = map.get("MsgType");
		String content = map.get("Content");
		
		String message = null;
		
		if (TEXT_TYPE.equals(msgType)) {
			if ("1".equals(content)) {
				message = AutoReplyMessageUtil.autoReplyMpNews(toUserName, fromUserName);
//				message = AutoReplyMessageUtil.autoReplyFun1(toUserName, fromUserName);
				//getAccessToken();
			} else if ("2".equals(content)) {
				message = AutoReplyMessageUtil.autoReplyFun2(toUserName, fromUserName);
			}else if ("3".equals(content)) {
				message = AutoReplyMessageUtil.autoReplyFun3(toUserName, fromUserName);
			}else if ("？".equals(content) || "?".equals(content)) {
				message = AutoReplyMessageUtil.autoReplySubscribeMsg(toUserName, fromUserName);
			} else {
				message = AutoReplyMessageUtil.autoReplyTextMessage(toUserName, fromUserName, content);
			}
		} else if (EVENT_TYPE.equals(msgType)) {
			String event = map.get("Event");
			if (SUBSCRIBE_TYPE.equals(event)) {
				message = AutoReplyMessageUtil.autoReplySubscribeMsg(toUserName, fromUserName);
			}
		}
		
		return message;
	}
}
