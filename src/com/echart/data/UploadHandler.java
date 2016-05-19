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

		String result = "";
		String error = "";

		do {
			String savePath = this.getServletContext().getRealPath(GlobalConfig.UPLOAD_DIR);
			File file = new File(savePath);
			if (!file.exists() && !file.isDirectory()) {
				System.out.println(savePath + " not exists. Create it.");
				file.mkdir();
			}

			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				if (!ServletFileUpload.isMultipartContent(request)) {
					error = "Invalid upload content";
					break;
				}

				List<FileItem> list = upload.parseRequest(request);
				System.out.println("IS multi. list size: " + list.size());
				FileItem opFile = null;
				for (FileItem item : list) {
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString("UTF-8");
						System.out.println(name + "=" + value);
					} else {
						opFile = item;
					}
				}
				if (opFile == null) {
					error = "Null upload file";
					break;
				}
				String filename = opFile.getName();
				System.out.println(filename);
				if (filename == null || filename.trim().equals("")) {
					error = "Empty file name!";
					break;
				}

				filename = filename.substring(filename.lastIndexOf("\\") + 1);
				String wholePath = savePath + "/" + filename;
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
			} catch(Exception e) {
				result = "Upload failed!";
				error = "Exception!";
				e.printStackTrace();
			}
		} while(false);

		if (error != "") {
			System.out.println("error: " + error);
		}

		request.setAttribute("result", result);
		request.setAttribute("error", error);
		request.getRequestDispatcher("/message.jsp").forward(request, response);
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
