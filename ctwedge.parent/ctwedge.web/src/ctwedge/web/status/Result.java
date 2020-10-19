package ctwedge.web.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/results/")
public class Result extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String DIR_RES = "/var/www/foselab_html/ctwedge/results";
	// private final static String DIR_=
	// "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Todo";
	// private File dir;
	// private String result;

	public Result() {
		super();
		// dir = new File(DIR_TODO);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String result;
		String name = request.getParameter("name");

		File file = new File(DIR_RES + "/" + name);

		if (file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				String disposition = "attachment; fileName=" + name;
				response.setContentType("text/csv");
				response.setHeader("Content-Disposition", disposition);
				response.setHeader("content-Length", String.valueOf(stream(inputStream, response.getOutputStream())));

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			result = "<html><body><p> The file is being processed and it's not ready yet. Please try opening this link again soon.</p></body></html>";
			response.getWriter().append(result);
		}

	}
	
	private long stream(InputStream input, OutputStream output) throws IOException {

	    try (ReadableByteChannel inputChannel = Channels.newChannel(input); WritableByteChannel outputChannel = Channels.newChannel(output)) {
	        ByteBuffer buffer = ByteBuffer.allocate(10240);
	        long size = 0;

	        while (inputChannel.read(buffer) != -1) {
	            buffer.flip();
	            size += outputChannel.write(buffer);
	            buffer.clear();
	        }
	        return size;
	    }
	}
}
