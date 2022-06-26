package ctwedge.fmtester.experiments;

import java.io.IOException;

import ctwedge.fmtester.DistancesCalculator;

/**
 * Calculate the distance between the test suites of the evolving feature
 * models. In particular, the distances are determined between the following
 * test suites:
 * <ul>
 * <li>old test suite = generated from scratch</li>
 * <li>new test suite = generated from scratch</li>
 * </ul>
 */
public class EvolvingModelDistances_FromScratchTS {

	private static final String EVOLUTION_MODELS_INPUT_FOLDER = "evolutionModels/";
	private static final String FROMSCRATCH_TEST_INPUT_FOLDER = "evolutionModels_TestsCSV/";
	private static final String INCREMENTAl_TEST_INPUT_FOLDER = "evolutionModels_IncrementalTestsCSV/";

	public static void main(String[] args) throws IOException {

		System.out.println("-- DISTANCES BETWEEN TS CALCULATION --");

		// input folders
		String FMName;
		String FMpName;
		String FMSubFolder;
		String FMInputPath;
		String fmPath;
		String fmpPath;

		/* TECHINQUE 1: GENERATION FROM SCRATCH */
		System.out.println();
		System.out.println("-- TECHINQUE 1: GENERATION FROM SCRATCH --");
		System.out.println();
		
		/* EVOLUTION MODEL: AmbientAssistedLiving */
		FMSubFolder = "AmbientAssistedLiving";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "AmbientAssistedLivingv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "AmbientAssistedLivingv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		
		/* EVOLUTION MODEL: AutomotiveMultimedia */
		FMSubFolder = "AutomotiveMultimedia";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "AutomotiveMultimediav1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "AutomotiveMultimediav2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "AutomotiveMultimediav2.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "AutomotiveMultimediav3.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		
		/* EVOLUTION MODEL: Boeing */
		FMSubFolder = "Boeing";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "Boeingv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "Boeingv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "Boeingv2.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "Boeingv3.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		
		/* EVOLUTION MODEL: CarBody */
		FMSubFolder = "CarBody";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "CarBodyv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "CarBodyv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "CarBodyv2.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "CarBodyv3.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "CarBodyv3.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "CarBodyv4.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		
		/* EVOLUTION MODEL: LinuxKernel */
		FMSubFolder = "LinuxKernel";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "LinuxKernelv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "LinuxKernelv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "LinuxKernelv2.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "LinuxKernelv3.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		/* EVOLUTION MODEL: ParkingAssistant */
		FMSubFolder = "ParkingAssistant";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "ParkingAssistantv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "ParkingAssistantv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "ParkingAssistantv2.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "ParkingAssistantv3.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "ParkingAssistantv3.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "ParkingAssistantv4.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		//TS
		FMName = "ParkingAssistantv4.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "ParkingAssistantv5.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		
		/* EVOLUTION MODEL: PPU */
		FMSubFolder = "PPU";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "PPUv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "PPUv2.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv3.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "PPUv3.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv4.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		//TS
		FMName = "PPUv4.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv5.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		//TS
		FMName = "PPUv5.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv6.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		//TS
		FMName = "PPUv6.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv7.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		//TS
		FMName = "PPUv7.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv8.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");

		//TS
		FMName = "PPUv8.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "PPUv9.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		
		/* EVOLUTION MODEL: SmartHotel */
		FMSubFolder = "SmartHotel";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "SmartHotelv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "SmartHotelv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		
		/* EVOLUTION MODEL: SmartWatch */
		FMSubFolder = "SmartWatch";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "SmartWatchv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "SmartWatchv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
		
		
		/* EVOLUTION MODEL: WeatherStation */
		FMSubFolder = "WeatherStation";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		
		System.out.println();
		System.out.println("***** " + FMSubFolder+" *****");
	
		// ------------------------------------------------------------------
		System.out.println("----------------------------------------------");
		
		//TS
		FMName = "WeatherStationv1.xml";
		fmPath = FMInputPath+FMName;
		
		//TS'
		FMpName = "WeatherStationv2.xml";
		fmpPath = FMInputPath+FMpName;
		
		System.out.println("TS: "+ FMName + ", TS': "+FMpName);
		System.out.println("Distance TS-TS': "+DistancesCalculator.percTestSuitesDist_FromModels(fmPath, fmpPath)+"%");		
		// ------------------------------------------------------------------
	}
}
