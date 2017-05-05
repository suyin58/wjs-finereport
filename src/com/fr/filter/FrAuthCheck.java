package com.fr.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class FrAuthCheck
 */
public class FrAuthCheck implements Filter {

    /**
     * Default constructor. 
     */
    public FrAuthCheck() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		System.out.println(request.getContextPath());
		System.out.println(request.getRequestURI());
		// 监控必须是查询功能 --  参数必须含有 reportlet=(**).cpt
		String reportlet = request.getParameter("reportlet");
		res.setContentType("application/json;charset=UTF-8");
		String op = request.getParameter("op");
		String resource =  request.getParameter("resource");
		if(!"emb".equals(op) && !(resource == null || resource.length() ==0)){

			if(null == reportlet || reportlet.length() == 0 || !reportlet.contains("cpt")){
				res.getWriter().write("参数必须含有 reportlet=(**).cpt");
				return;
			}
		}
		
		// 调用域名权限校验
		if(!MisCookieUtil.loginCheck(MisCookieUtil.getMisSessionId(request))){
			res.getWriter().write("登录校验失败，请先登录");
			return;
		}
		
		
		chain.doFilter(req, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
