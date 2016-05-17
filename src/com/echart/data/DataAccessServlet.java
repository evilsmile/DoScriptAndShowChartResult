package com.echart.data;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataAccessServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DataAccessServlet() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String[] categories = {"Ð¬", "³ÄÉÀ", "ÍâÌ×", "³¤¿ã"};
		Integer[] values = {80, 50, 75, 100};
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("categories", categories);
		json.put("values", values);
		JsonUtil.writeJson(json, request, response);
	}

}
