package ctwedge.generator.importer;

/** TODO */
public class ACTSImporter {
	
	public static final ACTSImporter INSTANCE = new ACTSImporter();
	
	/** a simple importer (do not work for all models as it does not rely on the Xtext parser) */
	public String importFromACTS(String actsModel) {
		StringBuilder sb = new StringBuilder();
		for (String line : actsModel.split("\n")) {
			line = line.trim();
			if (line.contains("#")) line = line.split("#")[0].trim();
			if (line.contains("[")) {
				sb.append(line.replace("[","{").replace("]","}"));
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
