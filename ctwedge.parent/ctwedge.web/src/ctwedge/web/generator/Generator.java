package ctwedge.web.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.emf.common.util.EList;

import com.google.common.base.Throwables;
import com.google.gson.JsonObject;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.casa.CASAConstraintException;
import ctwedge.generator.casa.CASATranslator;
import ctwedge.generator.util.ParameterSize;
import ctwedge.generator.util.Utility;
import ctwedge.util.ext.ICTWedgeTestGenerator;
import ctwedge.web.generator.ipapi.Ipapi;



/**
 * Servlet implementation class Generator It is the REST Service module of the
 * Generator
 */
@WebServlet("/generator/")
public class Generator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String DIR_TODO = "/var/www/foselab_html/ctwedge/todo";
	public static ServletContext context;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Generator() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		context = request.getServletContext();
		try {
			// response.getWriter().append("Served at: ").append(request.getContextPath());
			String model = request.getParameter("model");
			String generator = request.getParameter("generator");
			String strength = request.getParameter("strength");
			String ignoreConstraints = request.getParameter("ignConstr");
			String modelName = "";
			Ipapi.saveData("/var/www/foselab_html/ctwedge/", generator + " " + strength + " " + model, getIP(request));
			try {
				modelName = Utility.loadModel(model).getName();
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().append(Throwables.getStackTraceAsString(e));
			}
			if (modelName==null || modelName.isEmpty()) {
				response.getWriter().append("Syntax Error: missing model name");
			}
			int t = 2;
			try {
				t = Integer.parseInt(strength);
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().append(Throwables.getStackTraceAsString(e));
			}
			boolean ignoreC = false;
			try {
				ignoreC = Boolean.parseBoolean(ignoreConstraints);
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().append(Throwables.getStackTraceAsString(e));
			}
			String res = "Input parameters:\n" + model + "\n" + generator + "\n" + t + "\n" + ignoreC + "\n";
			String ts ="";
			JsonObject obj = new JsonObject();
			
			if (isSmall(model)) {
				try {
					// find the right generator
					ICTWedgeTestGenerator gen;
					if (generator.equalsIgnoreCase("casa"))
						gen = new CASATranslator();
					else
						gen = new ACTSTranslator();
					
					ts = Utility.getTestSuite(model, gen, t, ignoreC, context.getRealPath("/")).toString();
					if (ts == null || ts.isEmpty()) {
						response.getWriter().append("Empty test suite. There may be a syntax error in the input model.");
					}
					obj.addProperty("isSmall", true);
					obj.addProperty("result", ts);
				} catch (CASAConstraintException e) {
					response.getWriter().append(
							"Exception: arithmetic and relational expressions in constraints are not supported in CASA");
				} catch (Exception e) {
					e.printStackTrace();
					response.getWriter().append(Throwables.getStackTraceAsString(e));
				}
			} else {
				// response.getWriter().append(res);
				String timestamp = new SimpleDateFormat("yyyyMMddkkmmssSSS")
						.format(new Timestamp(System.currentTimeMillis()));
				PrintWriter todo = new PrintWriter(
						DIR_TODO + "/" + timestamp + "_" + generator + "_" + strength + "_" + ignoreConstraints + "_" + modelName +".ctw");
				todo.write(model);
				todo.close();
				ts = timestamp + ".csv";
				if (ts == null || ts.isEmpty()) {
					response.getWriter().append("Empty test suite. There may be a syntax error in the input model.");
				}				
				obj.addProperty("isSmall", false);
				obj.addProperty("result", ts);
			}
			
			res = obj.toString();
			response.getWriter().append(res);
		} catch (Exception e) {
			response.getWriter().append("An error has happened: " + e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public boolean isSmall(String model) {
		int size = 1;
		CitModel citModel = Utility.loadModel(model);
		EList<Parameter> parameters = citModel.getParameters(); // get parameter list
		for (Parameter p : parameters) {
			size = size * ParameterSize.eInstance.doSwitch(p);
		}
		
		if (size > 100 )
			return false;
		else 
			return true;
	}

}


