package ctwedge.generator.importer;

public class CitLabImporter {
	
	/*@Deprecated
	private static boolean isNotIDChar(char c) {
		return c==' ' || c=='>' || c=='<' || c=='=' || c=='+' || c=='-' || c=='*' || c=='/' || c=='%';
	}
	
	@Deprecated
	public static String replaceIDBeforePoint(String constraint) {
		String res = constraint;
		for (int i=res.length()-1; i>=0; i--) {
			if (constraint.charAt(i)=='.') {
				int j=i-1;
				while (j>0 && !isNotIDChar(constraint.charAt(j))) j--;
				res=res.substring(0, j+1)+res.substring(i+1);
				i=j+1;
			}
		}
		return res;
	}*/
	
	/** a simple importer (do not work for all models as it does not rely on the Xtext parser) */
	public static String importFromCitlab(String citlabModel) {
		StringBuilder sb = new StringBuilder();
		for (String line : citlabModel.split("\n")) {
			if (line.contains("Boolean")) {
				sb.append(line.split("Boolean ")[1].split(";")[0].trim() + ": Boolean;");
			} else if (line.contains("Enumerative")) {
				sb.append(line.split("Enumerative ")[1].split("\\{")[0].trim()+": {"+line.split("\\{")[1]);
			} else if (line.contains("Numbers")) {
				sb.append(line.split("Numbers ")[1].split("\\{")[0].trim()+": {"+line.split("\\{")[1]);
			} else if (line.contains("#")) {
				sb.append(line.replaceAll("(\\w+)\\.", "").replace("==true", "").replace("!=false", ""));
			} else if (!line.endsWith("end")) {
				sb.append(line);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
