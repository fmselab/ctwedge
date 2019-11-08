package ctwedge.web.generator.ipapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Ipapi {

	private static final String NEW_LINE = System.getProperty("line.separator");

	/** write info
	 * 
	 * @param path
	 * @param regexp
	 * @param IPNumber
	 */
	public static void saveData(String path, String regexp, String IPNumber) {
		try {
			String info = regexp + "\t " + new Date() + " " + IPNumber + "\t" + getLocation(IPNumber) + NEW_LINE;
			//System.err.println(path + " +*" +info);
			Files.write(Paths.get(path + "locations.txt"), info.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getLocation(String IPNumber) throws IOException {
		URL ipapi = new URL("https://ipapi.co/" + IPNumber + "/json/");

		URLConnection c = ipapi.openConnection();
		c.setRequestProperty("User-Agent", "java-ipapi-client");
		BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
		String line;
		String location = "";
		while ((line = reader.readLine()) != null) {
			if (line.matches(".*\"(city|region|country_name)\".*"))
				location += line;

		}
		;
		reader.close();
		return location;
	}

}