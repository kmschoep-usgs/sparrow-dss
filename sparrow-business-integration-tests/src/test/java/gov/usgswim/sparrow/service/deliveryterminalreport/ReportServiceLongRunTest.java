package gov.usgswim.sparrow.service.deliveryterminalreport;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import gov.usgswim.sparrow.SparrowServiceTestBaseWithDB;
import gov.usgswim.sparrow.test.SparrowTestBase;

import org.junit.Test;
import static org.junit.Assert.*;

import com.meterware.httpunit.*;
import gov.usgswim.sparrow.service.deliveryterminalreport.ReportRequest;
import static gov.usgswim.sparrow.test.SparrowTestBase.getXPathValue;
import org.junit.Ignore;
import org.w3c.dom.Document;

public class ReportServiceLongRunTest extends SparrowServiceTestBaseWithDB {

	private static final String CONTEXT_SERVICE_URL = "http://localhost:8088/sp_predictcontext";
	private static final String REPORT_SERVICE_URL = "http://localhost:8088/sp_delivery_terminalreport";

	/**
	 * Values containing commas should be escaped
	 * @throws Exception
	 */
	@Test
	public void model50NoAdjustHTMLExportCheckContextTerminalReport() throws Exception {
		String contextRequestText = SparrowTestBase.getXmlAsString(this.getClass(), "context1");

		WebRequest contextWebRequest = new PostMethodWebRequest(CONTEXT_SERVICE_URL);
		contextWebRequest.setParameter("xmlreq", contextRequestText);
		WebResponse contextWebResponse = client.sendRequest(contextWebRequest);
		String actualContextResponse = contextWebResponse.getText();

		assertXpathEvaluatesTo("OK", "//*[local-name()='status']", actualContextResponse);
		int id = getContextIdFromContext(actualContextResponse);

		WebRequest reportWebRequest = new GetMethodWebRequest(REPORT_SERVICE_URL);
		reportWebRequest.setParameter(ReportRequest.ELEMENT_CONTEXT_ID, Integer.toString(id));
		reportWebRequest.setParameter(ReportRequest.ELEMENT_MIME_TYPE, "xhtml_table");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_INCLUDE_ID_SCRIPT, "false");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_INCLUDE_ZERO_TOTAL_ROWS, "true");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_REPORT_TYPE, ReportRequest.ReportType.terminal.toString());
		reportWebRequest.setParameter(ReportRequest.ELEMENT_REPORT_YIELD, "false");

		WebResponse reportWebResponse = client.sendRequest(reportWebRequest);
		String actualReportResponse = reportWebResponse.getText();

		Document xmlDoc = SparrowTestBase.getW3cXmlDocumentFromString(actualReportResponse);
		String rowCountStr = getXPathValue("count(//tbody/tr)", xmlDoc);

		assertEquals("2", rowCountStr);

		String firstTerminalReachName = getXPathValue("//tbody/tr[th[a=9682]]/td[1]", xmlDoc);
		String totalValue = getXPathValue("//tbody/tr[2]/td[.=\"40,735,550\"]", xmlDoc);

		assertEquals("MOBILE R", firstTerminalReachName);
		assertEquals("40,735,550", totalValue);

	}

	@Test
	public void model50NoAdjustXMLExportCheckContextTerminalReport() throws Exception {
		String contextRequestText = SparrowTestBase.getXmlAsString(this.getClass(), "context1");

		WebRequest contextWebRequest = new PostMethodWebRequest(CONTEXT_SERVICE_URL);
		contextWebRequest.setParameter("xmlreq", contextRequestText);
		WebResponse contextWebResponse = client.sendRequest(contextWebRequest);
		String actualContextResponse = contextWebResponse.getText();

		assertXpathEvaluatesTo("OK", "//*[local-name()='status']", actualContextResponse);
		int id = getContextIdFromContext(actualContextResponse);

		WebRequest reportWebRequest = new GetMethodWebRequest(REPORT_SERVICE_URL);
		reportWebRequest.setParameter(ReportRequest.ELEMENT_CONTEXT_ID, Integer.toString(id));
		reportWebRequest.setParameter(ReportRequest.ELEMENT_MIME_TYPE, "xml");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_INCLUDE_ID_SCRIPT, "false");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_INCLUDE_ZERO_TOTAL_ROWS, "true");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_REPORT_TYPE, ReportRequest.ReportType.terminal.toString());
		reportWebRequest.setParameter(ReportRequest.ELEMENT_REPORT_YIELD, "false");

		WebResponse reportWebResponse = client.sendRequest(reportWebRequest);
		String actualReportResponse = reportWebResponse.getText();

//		System.out.println(actualReportResponse);
		Document xmlDoc = SparrowTestBase.getW3cXmlDocumentFromString(actualReportResponse);
		
		String firstReachId = getXPathValue("//*[local-name()='data']/*[local-name()='r'][position()=1]/@id", xmlDoc);
		String numberOfValues = getXPathValue("count(//*[local-name()='data']/*[local-name()='r'][position()=1]/*[local-name()='c'])", xmlDoc);
		String declairedColCount = getXPathValue("//*[local-name()='metadata']/@columnCount", xmlDoc);
		String firstGroupColCount = getXPathValue("//*[local-name()='group'][1]/@count", xmlDoc);
		String secondGroupColCount = getXPathValue("//*[local-name()='group'][2]/@count", xmlDoc);
		String declairedRowCount = getXPathValue("//*[local-name()='metadata']/@rowCount", xmlDoc);

		assertEquals("9682", firstReachId);
		assertEquals("12", numberOfValues);
		assertEquals("12" ,declairedColCount);
		assertEquals("6", firstGroupColCount);
		assertEquals("6", secondGroupColCount);
		assertEquals("2" ,declairedRowCount);

	}

	@Test
	public void model50NoAdjustCSVExportCheckContextTerminalReport() throws Exception {
		String contextRequestText = SparrowTestBase.getXmlAsString(this.getClass(), "context1");

		WebRequest contextWebRequest = new PostMethodWebRequest(CONTEXT_SERVICE_URL);
		contextWebRequest.setParameter("xmlreq", contextRequestText);
		WebResponse contextWebResponse = client.sendRequest(contextWebRequest);
		String actualContextResponse = contextWebResponse.getText();

		assertXpathEvaluatesTo("OK", "//*[local-name()='status']", actualContextResponse);
		int id = getContextIdFromContext(actualContextResponse);

		WebRequest reportWebRequest = new GetMethodWebRequest(REPORT_SERVICE_URL);
		reportWebRequest.setParameter(ReportRequest.ELEMENT_CONTEXT_ID, Integer.toString(id));
		reportWebRequest.setParameter(ReportRequest.ELEMENT_MIME_TYPE, "csv");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_INCLUDE_ID_SCRIPT, "false");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_INCLUDE_ZERO_TOTAL_ROWS, "true");
		reportWebRequest.setParameter(ReportRequest.ELEMENT_REPORT_TYPE, ReportRequest.ReportType.terminal.toString());
		reportWebRequest.setParameter(ReportRequest.ELEMENT_REPORT_YIELD, "false");

		WebResponse reportWebResponse = client.sendRequest(reportWebRequest);
		String actualReportResponse = reportWebResponse.getText();

		//System.out.println(actualReportResponse);

	}



}
