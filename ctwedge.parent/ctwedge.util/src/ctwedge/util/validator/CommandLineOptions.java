package ctwedge.util.validator;

import org.kohsuke.args4j.Option;

public class CommandLineOptions {
    
    @Option(name = "--m", usage = "Path to model file", required = true)
    private String modelPath;

    @Option(name = "--ts", usage = "Path to test suite CSV file", required = true)
    private String testSuitePath;

    @Option(name = "--t", usage = "Coverage strength (required for completeness/minimality)")
    private Integer strength;

    @Option(name = "--val", usage = "Enable validity check")
    private boolean validity;

    @Option(name = "--com", usage = "Enable completeness check")
    private boolean completeness;

    @Option(name = "--min", usage = "Enable minimality check")
    private boolean minimality;

    // Getters
    public String getModelPath() { return modelPath; }
    public String getTestSuitePath() { return testSuitePath; }
    public Integer getStrength() { return strength; }
    public boolean isValidity() { return validity; }
    public boolean isCompleteness() { return completeness; }
    public boolean isMinimality() { return minimality; }
}
