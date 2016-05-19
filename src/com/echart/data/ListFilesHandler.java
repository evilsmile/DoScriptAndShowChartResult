package com.echart.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListFilesHandler extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ListFilesHandler() {
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
		String uploadFilePath = this.getServletContext().getRealPath(GlobalConfig.UPLOAD_DIR);
//		Map<String, String> fileNameMap = new HashMap<String, String>();
		List<String> fileList = new ArrayList<String>();
		listFile(new File(uploadFilePath), fileList);
		System.out.println("files: " + fileList);
		request.setAttribute("files", fileList);
		request.getRequestDispatcher("/listfile.jsp").forward(request, response);
	}

	private void listFile(File file, List<String> fileList) {
		if (!file.isFile()) {
			File files[] = file.listFiles();
			for (File f : files) {
				listFile(f, fileList);
			}
		} else {
			String realName = file.getName().substring(file.getName().indexOf("_") + 1);
			System.out.println(file.getName() + "---" + realName);
			fileList.add(realName);
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
