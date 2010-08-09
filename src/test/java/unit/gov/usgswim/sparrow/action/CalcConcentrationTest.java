package gov.usgswim.sparrow.action;

import static org.junit.Assert.*;
import org.junit.Before;
import gov.usgswim.datatable.DataTable;
import gov.usgswim.datatable.DataTableWritable;
import gov.usgswim.sparrow.SparrowUnits;
import gov.usgswim.sparrow.action.DeliveryReach;
import gov.usgswim.sparrow.parser.DataColumn;
import gov.usgswim.sparrow.util.DLUtils;
import gov.usgswim.sparrow.util.TabDelimFileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;

import org.junit.Test;

/**
 * @author eeverman
 *
 */
public class CalcConcentrationTest {

	static final int EXAMPLE_CONTEXT_ID = 50;
	static final long EXAMPLE_MODEL_ID = 99;
	
	final static double CONV_FACTOR = .0011198d;
	
	final static double OK_ERR = .00000000001d;
	
	
	DataTableWritable baseTable;
	
	@Before
	public void doInit() throws Exception {
		InputStream fileStream =
			getClass().getResourceAsStream("/gov/usgswim/sparrow/tab_delimit_sample_data.txt");

		baseTable = TabDelimFileUtil.readAsDouble(fileStream,
				true, DLUtils.DO_NOT_INDEX);
		
		//build row IDs matching the row index
		for (int i=0; i< baseTable.getRowCount(); i++) {
			baseTable.setRowId(i, i);
		}
		

	}
	
	@Test
	public void testTheTestSetup() {
		assertEquals(5, baseTable.getColumnCount());
		assertEquals(10, baseTable.getRowCount());
		assertEquals(0d, baseTable.getDouble(0, 0), OK_ERR);
		assertEquals(94d, baseTable.getDouble(9, 4), OK_ERR);
		assertTrue(baseTable.hasRowIds());
	}
	

	@Test
	public void testWithNoZeroDivisors() throws Exception {

		int baseColIndex = 0;
		int flowColIndex = 1;
		
		baseTable.getColumns()[baseColIndex].setUnits(SparrowUnits.KG_PER_YEAR.getUserName());
		baseTable.getColumns()[flowColIndex].setUnits(SparrowUnits.CFS.getUserName());
		
		DataColumn baseCol = new DataColumn(baseTable, baseColIndex, EXAMPLE_CONTEXT_ID);
		DataColumn flowCol = new DataColumn(baseTable, flowColIndex, null, EXAMPLE_MODEL_ID);
		
		CalcConcentration calc = new CalcConcentration();
		calc.setBaseData(baseCol);
		calc.setStreamFlowData(flowCol);
		
		DataColumn result = calc.run();
		
		
		assertEquals((0d / 1d) * CONV_FACTOR, result.getDouble(0), OK_ERR);
		assertEquals((10d / 11d) * CONV_FACTOR, result.getDouble(1), OK_ERR);
		assertEquals((90d / 91d) * CONV_FACTOR, result.getDouble(9), OK_ERR);
		
	}
	
	@Test
	public void testWithZeroDivisor() throws Exception {

		int baseColIndex = 1;
		int flowColIndex = 0;
		
		baseTable.getColumns()[baseColIndex].setUnits(SparrowUnits.KG_PER_YEAR.getUserName());
		baseTable.getColumns()[flowColIndex].setUnits(SparrowUnits.CFS.getUserName());
		
		
		DataColumn baseCol = new DataColumn(baseTable, baseColIndex, EXAMPLE_CONTEXT_ID);
		DataColumn flowCol = new DataColumn(baseTable, flowColIndex, null, EXAMPLE_MODEL_ID);
		
		CalcConcentration calc = new CalcConcentration();
		calc.setBaseData(baseCol);
		calc.setStreamFlowData(flowCol);
		
		DataColumn result = calc.run();
		
		
		assertEquals(Double.POSITIVE_INFINITY, result.getDouble(0), OK_ERR);
		assertEquals((11d / 10d) * CONV_FACTOR, result.getDouble(1), OK_ERR);
		assertEquals((91d / 90d) * CONV_FACTOR, result.getDouble(9), OK_ERR);
		
	}
	
	@Test
	public void testWithNotANumber() throws Exception {

		int baseColIndex = 1;
		int flowColIndex = 0;
		
		baseTable.getColumns()[baseColIndex].setUnits(SparrowUnits.KG_PER_YEAR.getUserName());
		baseTable.getColumns()[flowColIndex].setUnits(SparrowUnits.CFS.getUserName());
		
		baseTable.setValue(Double.NaN, 0, baseColIndex);
		DataColumn baseCol = new DataColumn(baseTable, baseColIndex, EXAMPLE_CONTEXT_ID);
		DataColumn flowCol = new DataColumn(baseTable, flowColIndex, null, EXAMPLE_MODEL_ID);
		
		CalcConcentration calc = new CalcConcentration();
		calc.setBaseData(baseCol);
		calc.setStreamFlowData(flowCol);
		
		DataColumn result = calc.run();
		
		
		assertEquals(Double.NaN, result.getDouble(0), OK_ERR);
		assertEquals((11d / 10d) * CONV_FACTOR, result.getDouble(1), OK_ERR);
		assertEquals((91d / 90d) * CONV_FACTOR, result.getDouble(9), OK_ERR);
		
	}
	
	@Test
	public void testWithWrongLoadUnit() throws Exception {

		int baseColIndex = 1;
		int flowColIndex = 0;
		
		baseTable.getColumns()[baseColIndex].setUnits(SparrowUnits.SQR_KM.getUserName());	//wrong
		baseTable.getColumns()[flowColIndex].setUnits(SparrowUnits.CFS.getUserName());
		
		DataColumn baseCol = new DataColumn(baseTable, baseColIndex, EXAMPLE_CONTEXT_ID);
		DataColumn flowCol = new DataColumn(baseTable, flowColIndex, null, EXAMPLE_MODEL_ID);
		
		CalcConcentration calc = new CalcConcentration();
		calc.setBaseData(baseCol);
		calc.setStreamFlowData(flowCol);
		
		DataColumn result = calc.run();
		assertNull(result);
	}
	
	@Test
	public void testWithWrongFlowUnit() throws Exception {

		int baseColIndex = 1;
		int flowColIndex = 0;
		
		baseTable.getColumns()[baseColIndex].setUnits(SparrowUnits.KG_PER_YEAR.getUserName());	//wrong
		baseTable.getColumns()[flowColIndex].setUnits(SparrowUnits.MG_PER_L.getUserName());
		
		DataColumn baseCol = new DataColumn(baseTable, baseColIndex, EXAMPLE_CONTEXT_ID);
		DataColumn flowCol = new DataColumn(baseTable, flowColIndex, null, EXAMPLE_MODEL_ID);
		
		CalcConcentration calc = new CalcConcentration();
		calc.setBaseData(baseCol);
		calc.setStreamFlowData(flowCol);
		
		DataColumn result = calc.run();
		assertNull(result);
	}
	

}