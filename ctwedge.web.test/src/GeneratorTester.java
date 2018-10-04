import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class GeneratorTester {

	@Test
	public void testWorking() throws IOException {
		String model = "Model prova Parameters: a: Boolean; b: Boolean; c: Boolean; Constraints: # a -> b#";
		model = model.replaceAll(" ","%20");
		URL oracle = new URL("http://localhost:8080/ctwedge.generator?model=" + model);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
		}
		in.close();
	}
	
	public static void testServer(String model, int strength, String generator, boolean ignoreConstraints) throws Exception {
		//model = model.replaceAll(" ","%20");
		HttpClient client = HttpClientBuilder.create().build();
        
		URIBuilder builder = new URIBuilder("http://localhost:8080/ctwedge.generator");
		builder.setParameter("model", model).setParameter("strength", ""+strength).setParameter("generator", generator).setParameter("ignConstr", ""+ignoreConstraints);
		
		HttpGet post = new HttpGet(builder.build());

        try {
            HttpResponse response = client.execute(post);

            // Print out the response message
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		/*URL oracle = new URL("http://localhost:8080/ctwedge.generator?model=" + model + "&strength=" + strength + "&generator=" + generator + "&ignoreConstraints=" + ignoreConstraints);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
		}
		in.close();*/
	}
	
	public static String buildModel(String parameters, String constraints) {
		return "Model prova Parameters: " + parameters + " Constraints: " + constraints;
	}
	
	@Test
	public void test() throws Exception {
		testServer(buildModel("a: Boolean; b: Boolean; c: Boolean;", "# a -> b #"), 2, "acts", false);
		//testServer(buildModel("a: {ON, OFF}; b: {100, 2MP, MP};", "# b==100 #"), 2, "acts", false);
		//testServer(buildModel(" a: {ON, OFF}; b: {100, 2MP, MP}; d: [1..200];", "# d==100+2 #"), 2, "acts", false);
	}
	
	@Test
	public void testCASA() throws Exception {
		testServer(buildModel("a: Boolean; b: Boolean; c: Boolean;", "# a==true #"), 2, "casa", false);
		//testServer(buildModel("a: {ON, OFF}; b: {100, 2MP, MP};", "# b==100 #"), 2, "acts", false);
		//testServer(buildModel(" a: {ON, OFF}; b: {100, 2MP, MP}; d: [1..200];", "# d==100+2 #"), 2, "acts", false);
	}
	
	@Test
	public void testCASAWrong() throws Exception {
		//testServer(buildModel("a: Boolean; b: Boolean; c: Boolean;", "# a==true #"), 2, "casa", false);
		//testServer(buildModel("a: {ON, OFF}; b: {100, 2MP, MP};", "# b==100 #"), 2, "acts", false);
		//testServer(buildModel(" a: {ON, OFF}; b: {100, 2MP, MP}; d: [1..200];", "# d==100+2 #"), 2, "acts", false);
		testServer(buildModel("a: Boolean; b: Boolean; c: Boolean; d: [1..10];", "# d>9 #"), 2, "casa", false);
	}
	
	@Test
	public void testACTS2() throws Exception {
		URL oracle = new URL("http://localhost:8080/ctwedge.generator/?model=Model%20Phone%0A%20Parameters%3A%0A%20%20%20emailViewer%20%3A%20Boolean%0A%20%20%20textLines%3A%20%20%5B%2025%20..%2030%20%5D%0A%20%20%20display%20%3A%20%7B16MC%2C%208MC%2C%20BW%7D%0A%0A%20Constraints%3A%0A%20%20%20%23%20emailViewer%20%3D%3E%20textLines%20%3E%2028%20%23%0A&strength=3&generator=ACTS&ignConstr=true");
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
		}
		in.close();
	}
}
