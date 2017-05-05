package com.fr.filter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MisCookieUtil {
	
	/*MIS 系统URL路径，用于权限校验*/
	public static final String MIS_PORTAL_CONTEXT_PATH = "mis.portal.context.path";
	
	/*后台管理系统主域名*/
	public static final String MIS_PROTAL_BASE_DOMAIN = "mis.portal.base.domain";
	
	/*后台管理sessionID*/
	public static final String MIS_SSO_SESSION_ID = "wjs_mis_sso_session_id";
	
	/*后台登录校验URI*/
	public static final String MIS_SSO_LOGIN_CHECK_URI = "/logon/misLoginCheck.json";
	
	/*后台权限校验URI*/
	public static final String MIS_SSO_OPERATE_CHECK_URI = "/logon/operateCheck.json";

	static Properties prop = new Properties(); 
	static {
		try {
			//读取属性文件a.properties
			InputStream in = new BufferedInputStream(new FileInputStream("config.properties"));
			prop.load(in); ///加载属性列表
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	

	public static String getMisSessionId(HttpServletRequest request) {


		if (request.getCookies() == null){
			return null;
		}
		for (Cookie cookie : request.getCookies()) {
			if (MIS_SSO_SESSION_ID.equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}
	
	public static String setMisSessionId(HttpServletResponse response){

		String sessionId = UUID.randomUUID().toString();
		Cookie loginCookie = new Cookie(MIS_SSO_SESSION_ID, sessionId);  
		String domain = prop.getProperty(MIS_PROTAL_BASE_DOMAIN);
        loginCookie.setDomain((domain == null || domain.length() == 0)? ".wjs.com" : domain);  
        loginCookie.setPath("/");  
        response.addCookie(loginCookie);  
		return sessionId;
	}
	
	
	/**
	 * 校验登录是否成功，登录成功返回OperatorVo对象
	 * @return
	 * @author Silver 
	 * @date 2017年5月2日 下午2:12:53
	 */
	public static Boolean loginCheck(String sessionId){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", sessionId);
		String contextPath = prop.getProperty(MIS_PORTAL_CONTEXT_PATH);
		String url = ((contextPath == null || contextPath.length() == 0) ? "http://mis.wjs.com" : contextPath) + MIS_SSO_LOGIN_CHECK_URI;
		String json = HttpClientUtils.postWithoutException(url, params);
		System.out.println("post - url:" + url + ", result:" + json);
		if(json == null || json.length() == 0){
			return false;
		} 		
		return true;
	}
	
	

}

