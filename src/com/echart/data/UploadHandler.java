package com.echart.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadHandler extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadHandler() {
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
			String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
			File file = new File(savePath);
			if (!file.exists() && !file.isDirectory()) {
				System.out.println(savePath + " not exists. Create it.");
				file.mkdir();
			}
			String message = "";
			String result = "";
			String error = "";
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				if (!ServletFileUpload.isMultipartContent(request)) {
					return;
				}

				List<FileItem> list = upload.parseRequest(request);
				for (FileItem item : list) {
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString("UTF-8");
						System.out.println(name + "=" + value);
					} else {
						String filename = item.getName();
						System.out.println(filename);
						if (filename == null || filename.trim().equals("")) {
							continue;
						}

						filename = filename.substring(filename.lastIndexOf("\\") + 1);
						InputStream in = item.getInputStream();
						FileOutputStream out = new FileOutputStream(savePath + "/" + filename);
						byte buffer[] = new byte[1024];
						int len = 0;
						while ((len = in.read(buffer)) > 0) {
							out.write(buffer, 0, len);
						}

						in.close();
						out.close();
						item.delete();
						message = "Upload success.<br/>";

						try {
							InputStream shellResultIn = null;
							InputStream shellErrorIn = null;
							Process pro = Runtime.getRuntime().exec(new String[]{"sh", savePath + "/" + filename});
							pro.waitFor();
							shellResultIn = pro.getInputStream();
							BufferedReader read = new BufferedReader(new InputStreamReader(shellResultIn));
							for (String tmpStr = read.readLine(); tmpStr != null; tmpStr = read.readLine())
							{
								result += tmpStr + "\n";
							}

							shellErrorIn = pro.getErrorStream();
							read = new BufferedReader(new InputStreamReader(shellErrorIn));
							error = read.readLine();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch(Exception e) {
				message = "Upload failed!";
				e.printStackTrace();
			}
			request.setAttribute("message", message);
			request.setAttribute("result", result);
			request.setAttribute("error", error);

			System.out.println(result);
			String[] categories = {"show", "chensan", "waitao", "changku"};
			String[] values = {"80", "50", "75", "120"};
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("categories", categories);
			json.put("values", values);
			String jsonReply = JsonUtil.parseToJson(json, request);
			System.out.println("json reply:" + jsonReply);
			request.setAttribute("json", jsonReply);

			if (error != null && error != "") {
				request.getRequestDispatcher("/message.jsp").forward(request, response);
			} else {
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
