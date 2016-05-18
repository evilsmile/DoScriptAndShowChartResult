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
		String result = "";
		String error = "";

		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			if (!ServletFileUpload.isMultipartContent(request)) {
				System.out.println("not multi");
				return;
			}

			List<FileItem> list = upload.parseRequest(request);
			System.out.println("IS multi. list size: " + list.size());
			FileItem opFile = null;
			String operation = "";
			for (FileItem item : list) {
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString("UTF-8");
					System.out.println(name + "=" + value);
					if (name.equals("upload")) {
						operation = "upload";
					} else if (name.equals("run")) {
						operation = "run";
					}
				} else {
					opFile = item;
				}
			}
			if (opFile == null) {
				error = "No upload file";
				System.out.println(error);
				return;
			}
			String filename = opFile.getName();
			System.out.println(filename);
			if (filename == null || filename.trim().equals("")) {
				error = "Empty file name!";
				System.out.println(error);
				return;
			}

			filename = filename.substring(filename.lastIndexOf("\\") + 1);
			String wholePath = savePath + "/" + filename;
			//true
			if (operation.equals("upload")) {
				InputStream in = opFile.getInputStream();
				FileOutputStream out = new FileOutputStream(wholePath);
				byte buffer[] = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}

				in.close();
				out.close();
				opFile.delete();
				result = "Upload success.";
				request.setAttribute("result", result);
				request.setAttribute("error", error);
				request.getRequestDispatcher("/message.jsp").forward(request, response);

			} else if (operation.equals("run")) {
				try {
					String scriptExecutor = "";

					String exeSuffix = filename.substring(filename.lastIndexOf(".") + 1);
					System.out.println("suffix: " +exeSuffix);
					if (exeSuffix.equals("sh")) {
						scriptExecutor = "sh";
					} else if (exeSuffix.equals("py")) {
						scriptExecutor = "python";
					}
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
						System.out.println(error);
						result = "Run failed!";
						request.setAttribute("result", result);
						request.setAttribute("error", error);
						request.getRequestDispatcher("/message.jsp").forward(request, response);
						return;
					}

					shellResultIn = pro.getInputStream();
					BufferedReader read = new BufferedReader(new InputStreamReader(shellResultIn));
					String categoriesStr = read.readLine();
					String dataStr = read.readLine();

					// FIXME: useless
					for (String tmpStr = read.readLine(); tmpStr != null; tmpStr = read.readLine())
					{
						result += tmpStr + "\n";
					}

					String[] categories = categoriesStr.split(" ");
					String[] values = dataStr.split(" ");
					Map<String, Object> json = new HashMap<String, Object>();
					json.put("categories", categories);
					json.put("values", values);
					String jsonReply = JsonUtil.parseToJson(json, request);
					System.out.println("json reply:" + jsonReply);
					request.setAttribute("json", jsonReply);

					request.getRequestDispatcher("/show.jsp").forward(request, response);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(result);
		} catch(Exception e) {
			result = "Upload failed!";
			error = "Exception!";
			request.setAttribute("result", result);
			request.setAttribute("error", error);
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			e.printStackTrace();
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
