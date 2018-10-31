// this feature model is made available from the University of
// Texas at Austin by the Product Line Research Architecture
// research group at http://www.cs.utexas.edu/users/schwartz/




BerkeleyDb : [FLogging] FPersistency [FStatistics] [featureMemoryBudget] FConcurrency* FDbOperation* FBtree BASE :: BerkeleyDB;

FConcurrency	: featureLatch
		| featureFSync
		| featureTransaction
		| dummyFeatureLocking
		| featureCheckLeaks;

FLogging 	: [featureLoggingFile] [featureLoggingConsole] [featureLoggingDbLog] 
		  [featureLoggingFinest] [featureLoggingFiner] [featureLoggingFine] 
		  [featureLoggingInfo] [featureLoggingConfig] [featureLoggingSevere] 
		  featureLoggingBase :: Logging;

FPersistency 	: FPersistencyFeatures* FIOFeature :: Persistency;

FIOFeature 	: [featureDirectNIO] FNIOType :: NIO
		| [featureSynchronizedIO] featureIO :: IO;

FNIOType 	: featureNIO :: NotChunked 
		| featureChunkedNIO :: Chunked;

FStatistics : FStatisticsFeatures + featureStatisticsBase :: Statistics;

FPersistencyFeatures 
		: featureChecksum
		| featureFileHandleCache
		| featureHandleFullDiscError
		| featureEnvironmentLock
		| [featureCustomizableCheckpointerTime] [featureCustomizableCheckpointerBytes] 
		  [featureCheckpointerDaemon] :: Checkpointer
		| [featureLookAheadCache] [featureCleanerDaemon] :: Cleaner;

FStatisticsFeatures 
		: [featureStatisticsEnvLog] [featureStatisticsEnvINCompressor] 
		  [featureStatisticsEnvFSync]  [featureStatisticsEnvEvictor] [featureStatisticsEnvCleaner] 
		  [featureStatisticsEnvCheckpointer] [featureStatisticsEnvCaching] featureStatisticsEnvBase :: EnvStats 
		| featureStatisticsDatabase 
		| featureStatisticsLock
	       	| featureStatisticsPreload
		| featureStatisticsSequence 
		| featureStatisticsTransaction;

FBtree 		: [featureVerifier] [featureTreeVisitor] [featureINCompressor] [FEvictor] :: BTree;

FEvictor	: [featureCriticalEviction] [featureEvictorDaemon] featureEvictor :: Evictor;

FDbOperation 	: featureDeleteDb 
		| featureTruncateDb;


%% //Semantic Dependencies
featureEvictor or featureEvictorDaemon or featureLookAheadCache or
		featureStatisticsEnvCaching implies featureMemoryBudget;
featureCheckLeaks implies featureStatisticsLock;
featureCriticalEviction implies featureINCompressor;
featureCustomizableCheckpointerBytes implies featureCustomizableCheckpointerTime;
featureDeleteDb implies dummyFeatureLocking and featureEvictor and featureINCompressor and featureMemoryBudget;
featureLatch implies dummyFeatureLocking and featureCheckLeaks and featureDeleteDb and featureEvictor and 
		featureFileHandleCache and featureFSync and featureINCompressor and featureMemoryBudget and
		featureStatisticsLock and featureTreeVisitor and featureTruncateDb and featureVerifier;
featureLoggingSevere implies featureEnvironmentLock;
featureLoggingFine implies dummyFeatureLocking and featureEvictor and featureINCompressor;
featureLoggingInfo implies featureChecksum and featureMemoryBudget;
featureLoggingBase or featureLoggingFinest implies featureTransaction;
featureMemoryBudget implies featureEvictor and featureLatch;
featureStatisticsLock or featureStatisticsTransaction implies dummyFeatureLocking;
featureStatisticsEnvEvictor implies featureEvictor;
featureStatisticsEnvFSync implies featureFSync;
featureStatisticsEnvINCompressor implies featureINCompressor;
featureStatisticsTransaction implies featureTransaction;
featureStatisticsDatabase implies featureTreeVisitor;
featureTransaction implies dummyFeatureLocking and featureDeleteDb and featureTruncateDb;
featureTruncateDb implies featureDeleteDb;
featureVerifier implies featureINCompressor and featureTreeVisitor;

