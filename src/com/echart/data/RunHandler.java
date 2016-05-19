package com.echart.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RunHandler extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RunHandler() {
		super();
	}

	/**
	 * Decide executor of the script file.
	 */
	private String getExecutor(String filename) {
		String scriptExecutor = "";
		String exeSuffix = filename.substring(filename.lastIndexOf(".") + 1);
		System.out.println("suffix: " +exeSuffix);
		if (exeSuffix.equals("sh")) {
			scriptExecutor = "sh";
		} else if (exeSuffix.equals("py")) {
			scriptExecutor = "python";
		}

		return scriptExecutor;
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

		String result = "";
		String error = "";

		do {
			String savePath = this.getServletContext().getRealPath(GlobalConfig.UPLOAD_DIR);
			File file = new File(savePath);
			if (!file.exists() && !file.isDirectory()) {
				error = savePath + " not exists!";
				break;
			}

			try {
				String filename = request.getParameter("run_filename");
				System.out.println(filename);
				if (filename == null || filename.trim().equals("")) {
					error = "Empty file name!";
					break;
				}

				filename = filename.substring(filename.lastIndexOf("\\") + 1);
				String wholePath = savePath + "/" + filename;
				String scriptExecutor = getExecutor(filename);

				InputStream shellResultIn = null;
				InputStream shellErrorIn = null;
				Process pro = Runtime.getRuntime().exec(new String[]{scriptExecutor, wholePath});
				pro.waitFor();

				shellErrorIn = pro.getErrorStream();
				BufferedReader errRead = new BufferedReader(new InputStreamReader(shellErrorIn));
				for (String tmpStr = errRead.readLine(); tmpStr != null; tmpStr = errRead.readLine())
				{
					error += tmpStr;
				}

				if (error != null && error != "") {
					break;
				}

				shellResultIn = pro.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(shellResultIn));
				String keysStr = read.readLine();
				String valuesStr = read.readLine();

				// FIXME: useless
				for (String tmpStr = read.readLine(); tmpStr != null; tmpStr = read.readLine())
				{
					result += tmpStr + "\n";
				}

				String[] keys = keysStr.split(" ");
				String[] values = valuesStr.split(" ");
				Map<String, Object> json = new HashMap<String, Object>();
				json.put("keys", keys);
				json.put("values", values);
				String jsonReply = JsonUtil.parseToJson(json, request);
				System.out.println("json reply:" + jsonReply);
				request.setAttribute("json", jsonReply);
			} catch(Exception e) {
				error = "Exception!";
				e.printStackTrace();
			}
		} while(false);

		if (error != "") {
			result = "Run failed!";
			System.out.println("error: " + error);
			request.setAttribute("result", result);
			request.setAttribute("error", error);
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		} else {
			result = "Run sucess!!";
			request.getRequestDispatcher("/show.jsp").forward(request, response);
		}
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
		doGet(request, response);
	}

}
