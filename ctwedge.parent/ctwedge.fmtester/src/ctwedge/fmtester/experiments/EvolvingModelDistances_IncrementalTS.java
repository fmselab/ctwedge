package ctwedge.fmtester.experiments;

import java.io.IOException;

import ctwedge.fmtester.DistancesCalculator;

/**
 * Calculate the distance between the test suites of the evolving feature
 * models. In particular, the distances are determined between the following
 * test suites:
 * <ul>
 * <li>old test suite = generated from scratch</li>
 * <li>new test suite = generated in an incremental way</li>
 * </ul>
 */
public class EvolvingModelDistances_IncrementalTS {

	private static final String EVOLUTION_MODELS_INPUT_FOLDER = "evolutionModels/";
	private static final String FROMSCRATCH_TEST_INPUT_FOLDER = "evolutionModels_TestsCSV/";
	private static final String INCREMENTAl_TEST_INPUT_FOLDER = "evolutionModels_IncrementalTestsCSV/";

	public static void main(String[] args) throws IOException {

		System.out.println("-- DISTANCES BETWEEN TS CALCULATION --");

		// input names
		String FMName = "";
		String FMpName = "";
		String FMSubFolder = "";
		String FMInputPath = "";
		String fmPath = "";
		String fmpPath = "";
		String TSName = "";
		String TSpName = "";
		String TSPath = "";
		String TSpPath = "";

		/* TECHINQUE 2: INCREMENTAL GENERATION */
		System.out.println();
		System.out.println("-- TECHNIQUE 2: INCREMENTAL GENERATION --");
		System.out.println();

		/* EVOLUTION MODEL: AmbientAssistedLiving */
		// ********************************************************************
		FMSubFolder = "AmbientAssistedLiving";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "AmbientAssistedLivingv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_AmbientAssistedLivingv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "AmbientAssistedLivingv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_AmbientAssistedLivingv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

//			System.out.println("TSPath: "+TSPath);
//			System.out.println("fmPath: "+ fmPath);
//			System.out.println("TSpPath: "+TSpPath);
//			System.out.println("fmpPath: "+fmpPath);
		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		// ------------------------------------------------------------------

		System.out.println("----------------------------------------------");
		// ********************************************************************

		/* EVOLUTION MODEL: AutomotiveMultimedia */
		// ********************************************************************
		FMSubFolder = "AutomotiveMultimedia";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "AutomotiveMultimediav1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_AutomotiveMultimediav1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "AutomotiveMultimediav2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_AutomotiveMultimediav2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v2_old-v3_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "AutomotiveMultimediav2.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_AutomotiveMultimediav2.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "AutomotiveMultimediav3.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_AutomotiveMultimediav3.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		// ------------------------------------------------------------------

		System.out.println("----------------------------------------------");
		// ********************************************************************

		/* EVOLUTION MODEL: Boeing */
		// ********************************************************************
		FMSubFolder = "Boeing";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "Boeingv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_Boeingv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "Boeingv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_Boeingv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v2_old-v3_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "Boeingv2.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_Boeingv2.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "Boeingv3.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_Boeingv3.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		// ------------------------------------------------------------------

		System.out.println("----------------------------------------------");
		// ********************************************************************

		/* EVOLUTION MODEL: CarBody */
		// ********************************************************************
		FMSubFolder = "CarBody";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "CarBodyv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_CarBodyv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "CarBodyv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_CarBodyv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v2_old-v3_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "CarBodyv2.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_CarBodyv2.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "CarBodyv3.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_CarBodyv3.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		// ------------------------------------------------------------------

		// v3_old-v4_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "CarBodyv3.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_CarBodyv3.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "CarBodyv4.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_CarBodyv4.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		// ------------------------------------------------------------------

		System.out.println("----------------------------------------------");
		// ********************************************************************

		/* EVOLUTION MODEL: LinuxKernel */
		// ********************************************************************
		FMSubFolder = "LinuxKernel";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "LinuxKernelv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_LinuxKernelv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "LinuxKernelv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_LinuxKernelv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v2_old-v3_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "LinuxKernelv2.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_LinuxKernelv2.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "LinuxKernelv3.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_LinuxKernelv3.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ********************************************************************

		/* EVOLUTION MODEL: ParkingAssistant */
		// ********************************************************************
		FMSubFolder = "ParkingAssistant";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "ParkingAssistantv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_ParkingAssistantv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "ParkingAssistantv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_ParkingAssistantv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v2_old-v3_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "ParkingAssistantv2.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_ParkingAssistantv2.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "ParkingAssistantv3.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_ParkingAssistantv3.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v3_old-v4_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "ParkingAssistantv3.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_ParkingAssistantv3.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "ParkingAssistantv4.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_ParkingAssistantv4.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v4_old-v5_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "ParkingAssistantv4.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_ParkingAssistantv4.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "ParkingAssistantv5.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_ParkingAssistantv5.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		// ------------------------------------------------------------------

		System.out.println("----------------------------------------------");
		// ********************************************************************

		/* EVOLUTION MODEL: PPU */
		// ********************************************************************
		FMSubFolder = "PPU";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "PPUv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v2_old-v3_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "PPUv2.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv2.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv3.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv3.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v3_old-v4_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "PPUv3.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv3.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv4.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv4.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v4_old-v5_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "PPUv4.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv4.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv5.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv5.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		// ------------------------------------------------------------------

		// v5_old-v6_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "PPUv5.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv5.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv6.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv6.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v6_old-v7_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "PPUv6.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv6.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv7.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv7.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v7_old-v8_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "PPUv7.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv7.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv8.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv8.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		// v8_old-v9_incremental
		// ------------------------------------------------------------------
		// First Model
		// CTWedge model
		FMName = "PPUv8.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_PPUv8.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "PPUv9.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_PPUv9.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");
		// ------------------------------------------------------------------

		System.out.println("----------------------------------------------");
		// ********************************************************************

		/* EVOLUTION MODEL: SmartHotel */
		// ********************************************************************
		FMSubFolder = "SmartHotel";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "SmartHotelv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_SmartHotelv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "SmartHotelv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_SmartHotelv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		/* EVOLUTION MODEL: Smartwatch */
		// ********************************************************************
		FMSubFolder = "Smartwatch";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "Smartwatchv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_Smartwatchv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "Smartwatchv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_Smartwatchv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

		/* EVOLUTION MODEL: WeatherStation */
		// ********************************************************************
		FMSubFolder = "WeatherStation";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		System.out.println();
		System.out.println("***** " + FMSubFolder + " *****");

		// v1_old-v2_incremental
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		// First Model
		// CTWedge model
		FMName = "WeatherStationv1.xml";
		fmPath = FMInputPath + FMName;
		// TS
		TSName = "CSVTest_WeatherStationv1.csv";
		TSPath = FROMSCRATCH_TEST_INPUT_FOLDER + TSName;

		// Second Model
		// CTWedge model
		FMpName = "WeatherStationv2.xml";
		fmpPath = FMInputPath + FMpName;
		// TS
		TSpName = "CSVIncrementalTest_WeatherStationv2.csv";
		TSpPath = INCREMENTAl_TEST_INPUT_FOLDER + TSpName;

		System.out.println("Old TS: " + FMName);
		System.out.println("Incremental evolved TS': " + FMpName);
		System.out.println("Distance TS-TS': "
				+ DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, TSPath, ",", fmpPath, TSpPath, ";")
				+ "%");

		System.out.println("----------------------------------------------");
		// ------------------------------------------------------------------

	}

}
