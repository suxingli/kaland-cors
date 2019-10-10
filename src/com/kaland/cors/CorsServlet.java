package com.kaland.cors;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kaland.cors.filter.CorsFilter;

/**
 * 跨域Servlet
 * @author 苏行利
 * @date 2019-10-10 16:03:56
 */
@WebServlet(urlPatterns = "/cors", loadOnStartup = 1)
public class CorsServlet extends HttpServlet {
	private static final long serialVersionUID = -2522590307790871960L;
	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
	private static final String PAGE = "/WEB-INF/cors/index.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		request.setAttribute("version", "1.0.3");
		request.setAttribute("allow_methods", CorsFilter.getAllow_methods());
		request.setAttribute("allow_credentials", CorsFilter.getAllow_credentials());
		request.setAttribute("allow_origins", CorsFilter.getAllow_origins());
		request.getRequestDispatcher(PAGE).forward(request, response);
	}

}
