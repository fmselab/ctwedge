package ctwedge.web.status;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/status/")
public class Status extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String DIR_TODO = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Todo";
	private File dir;
	private String result;

	public Status() {
		super();
		dir = new File(DIR_TODO);
		result = "Executing...";
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		File[] files = dir.listFiles();
	
		String name = request.getParameter("name");
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().compareToIgnoreCase(name) != 0) {
				result = "Done!";
			}
		}
		
		response.getWriter().append(result);
	}
}
