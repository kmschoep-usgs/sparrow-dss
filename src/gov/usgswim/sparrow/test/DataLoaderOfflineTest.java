package gov.usgswim.sparrow.test;

import gov.usgswim.sparrow.util.DataLoader;

import java.io.IOException;

import junit.framework.TestCase;

import junit.swingui.TestRunner;

public class DataLoaderOfflineTest extends TestCase {
	public DataLoaderOfflineTest(String sTestName) {
		super(sTestName);
	}

	public static void main(String args[]) {
		String args2[] = {"-noloading", "gov.usgswim.sparrow.test.DataLoaderOfflineTest"};
		TestRunner.main(args2);
	}

	/**
	 * @see DataLoader#getQuery(String,Object[])
	 */
	public void xtestGetQuery() throws IOException {
		String query = DataLoader.getQuery("SelectSystemData");

		String expected =
		"SELECT MODEL_REACH_ID as MODEL_REACH, HYDSEQ FROM MODEL_REACH WHERE SPARROW_MODEL_ID = $ModelId$ ORDER BY HYDSEQ";
		
		this.assertEquals(expected, query);
	}

	/**
	 * @see DataLoader#getQuery(String,long)
	 */
	public void xtestGetQueryWModelID() throws IOException {
		String query = DataLoader.getQuery("SelectSystemData", 999);

		String expected =
		"SELECT MODEL_REACH_ID as MODEL_REACH, HYDSEQ FROM MODEL_REACH WHERE SPARROW_MODEL_ID = 999 ORDER BY HYDSEQ";
		
		this.assertEquals(expected, query);
	}

	/**
	 * @see DataLoader#getQuery(String)
	 */
	public void xtestGetQueryWOtherParams() throws IOException {
		String query = DataLoader.getQuery(
			"SelectReachCoef", new Object[] {"ModelId", 999, "Iteration", 888, "SourceId", 777});
		
		String expected =
		"SELECT coef.VALUE AS Value FROM SOURCE_REACH_COEF coef INNER JOIN MODEL_REACH rch ON coef.MODEL_REACH_ID = rch.MODEL_REACH_ID WHERE rch.SPARROW_MODEL_ID = 999 AND coef.Iteration = 888 AND coef.SOURCE_ID = 777 ORDER BY rch.HYDSEQ";
	
		this.assertEquals(expected, query);
	}
}