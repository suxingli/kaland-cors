package com.kaland.cors.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 跨域过滤器
 * @author 苏行利
 * @date 2019-10-10 16:02:12
 */
@WebFilter(urlPatterns = "/*")
public class CorsFilter implements Filter {
	private static final String OPTIONS = "OPTIONS";
	private static final String ORIGIN = "origin";
	private static final String ALLOW_METHODS = "allow-methods";
	private static final String ALLOW_CREDENTIALS = "allow-credentials";
	private static final String ALLOW_ORIGINS = "allow-origins";
	private static final String ALLOW_ORIGIN = "allow-origin";
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
	private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	private static String allow_methods; // 允许跨域请求的方法
	private static String allow_credentials; // 是否允许发送Cookie
	private static List<String> allow_origins = new ArrayList<String>(0); // 允许跨域请求的域列表

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			Document doc = new SAXReader().read(CorsFilter.class.getResourceAsStream("/cors-cfg.xml"));
			Element root = doc.getRootElement();
			allow_methods = root.elementTextTrim(ALLOW_METHODS);
			allow_credentials = root.elementTextTrim(ALLOW_CREDENTIALS);
			Element _allow_origins = root.element(ALLOW_ORIGINS);
			if (_allow_origins == null) {
				return;
			}
			for (Element element : _allow_origins.elements(ALLOW_ORIGIN)) {
				allow_origins.add(element.getTextTrim());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest _request = (HttpServletRequest) request;
		String origin = _request.getHeader(ORIGIN);
		if (origin == null) { // 同域请求
			chain.doFilter(request, response);
			return;
		}
		String _origin = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : (":" + request.getServerPort())); // 本地域
		if (origin.equals(_origin)) { // 同域请求
			chain.doFilter(request, response);
			return;
		}
		HttpServletResponse _response = (HttpServletResponse) response;
		if (allow_origins.contains(origin) || allow_origins.contains("*")) { // 请求的域存在白名单中
			_response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, _request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS));
			_response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
			if (allow_methods != null && !"".equals(allow_methods)) {
				_response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, allow_methods);
			}
			if (allow_credentials != null && !"".equals(allow_credentials)) {
				_response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, allow_credentials);
			}
			if (OPTIONS.equals(_request.getMethod())) { // 预检请求
				return;
			}
			chain.doFilter(request, _response);
			return;
		}
		_response.setStatus(403); // 设置状态码为禁止访问
		_response.setContentType(CONTENT_TYPE); // 设置响应内容类型
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"").append("code").append("\":").append(403).append(",");
		buffer.append("\"").append("msg").append("\":\"").append("不支持跨域请求").append("(").append(origin).append(" -> ").append(_origin).append(")").append("\"");
		buffer.append("}");
		PrintWriter writer = _response.getWriter();
		writer.write(buffer.toString());
		writer.close();
		return;
	}

	@Override
	public void destroy() {

	}

	/**
	 * 获取允许跨域请求的方法
	 * @author 苏行利
	 * @return 允许跨域请求的方法
	 * @date 2019-10-10 16:02:32
	 */
	public static String getAllow_methods() {
		return allow_methods;
	}

	/**
	 * 是否允许发送Cookie
	 * @author 苏行利
	 * @return 是否允许发送Cookie
	 * @date 2019-10-10 16:02:43
	 */
	public static String getAllow_credentials() {
		return allow_credentials;
	}

	/**
	 * 获取允许跨域请求的域列表
	 * @author 苏行利
	 * @return 允许跨域请求的域列表
	 * @date 2019-10-10 16:02:53
	 */
	public static List<String> getAllow_origins() {
		return allow_origins;
	}
}
