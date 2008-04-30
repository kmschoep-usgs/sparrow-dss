package gov.usgswim.sparrow.test.parsers;

import gov.usgswim.sparrow.parser.PredictionContext;
import gov.usgswim.sparrow.parser.TerminalReaches;

import java.io.StringReader;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

public class PredictionContextTest extends TestCase {
	protected XMLInputFactory inFact = XMLInputFactory.newInstance();

	public void testParseMainUseCase() throws XMLStreamException {
		String testRequest = "<prediction-context "
		+ "  xmlns=\"http://www.usgs.gov/sparrow/prediction-schema/v0_2\" "
		+ "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		+ "	model-id=\"22\">"
		+ "	<adjustment-groups conflicts=\"accumulate\">"
		+ "		<reach-group enabled=\"true\" name=\"Northern Indiana Plants\">"
		+ "			<desc>Plants in Northern Indiana that are part of the 'Keep Gary Clean' Project</desc>"
		+ "			<notes>"
		+ "				I initially selected HUC 01746286 and 01746289,"
		+ "				but it looks like there are some others plants that need to be included."
		+ "				As a start, we are proposing a 10% reduction across the board,"
		+ "				but we will tailor this later based on plant type."
		+ "			</notes>"
		+ "			<adjustment src=\"5\" coef=\".9\"/>"
		+ "			<adjustment src=\"4\" coef=\".75\"/>"
		+ "			<logical-set>"
		+ "				<criteria attrib=\"huc8\">01746286</criteria>"
		+ "			</logical-set>"
		+ "			<logical-set>"
		+ "				<criteria attrib=\"huc8\">01746289</criteria>"
		+ "			</logical-set>"
		+ "		</reach-group>"
		+ "		<reach-group enabled=\"false\" name=\"Southern Indiana Fields\">"
		+ "			<desc>Fields in Southern Indiana</desc>"
		+ "			<notes>"
		+ "				The Farmer's Alminac says corn planting will be up 20% this year,"
		+ "				which will roughly result in a 5% increase in the aggrecultural source."
		+ "				This is an estimate so I'm leaving it out of the runs created	for the EPA."
		+ "			</notes>"
		+ "			<adjustment src=\"1\" coef=\"1.05\"/>"
		+ "			<logical-set>"
		+ "				<criteria attrib=\"reach\" relation=\"upstream\">8346289</criteria>"
		+ "			</logical-set>"
		+ "			<logical-set>"
		+ "				<criteria attrib=\"reach\" relation=\"upstream\">9374562</criteria>"
		+ "			</logical-set>"
		+ "		</reach-group>"
		+ "		<reach-group enabled=\"true\" name=\"Illinois\">"
		+ "			<desc>The entire state of Illinois</desc>"
		+ "			<notes>The Urban source for Illinois is predicted is to increase 20%.</notes>"
		+ "			<adjustment src=\"2\" coef=\"1.2\"/>"
		+ "			<logical-set>"
		+ "				<criteria attrib=\"state-code\">il</criteria>"
		+ "			</logical-set>"
		+ "		</reach-group>"
		+ "		<reach-group enabled=\"true\" name=\"Illinois\">"
		+ "			<desc>Wisconsin River Plants</desc>"
		+ "			<notes>"
		+ "				We know of 3 plants on the Wisconsin River which have announced improved"
		+ "				BPM implementations."
		+ "			</notes>"
		+ "			<adjustment src=\"2\" coef=\".75\"/>"
		+ "			<reach id=\"483947453\">"
		+ "				<adjustment src=\"2\" coef=\".9\"/>"
		+ "			</reach>"
		+ "			<reach id=\"947839474\">"
		+ "				<adjustment src=\"2\" abs=\"91344\"/>"
		+ "			</reach>"
		+ "		</reach-group>"
		+ "	</adjustment-groups>"
		+ "	<analysis>"
		+ "		<select>"
		+ "			<data-series source=\"1\" per=\"area\">incremental</data-series>"
		+ "			<agg-function per=\"area\">avg</agg-function>"
		+ "			<analytic-function partition=\"HUC6\">rank-desc</analytic-function>"
		+ "			<nominal-comparison type=\"percent | absolute\"/>"
		+ "		</select>"
		+ "		<limit-to>contributors | terminals | area-of-interest</limit-to>"
		+ "		<group-by>HUC8</group-by>"
		+ "	</analysis>"
		+ "	<terminal-reaches>"
		+ "		<reach>2345642</reach>"
		+ "		<reach>3425688</reach>"
		+ "		<reach>5235424</reach>"
		+ "		<logical-set/>"
		+ "	</terminal-reaches>"
		+ "	<area-of-interest>"
		+ "		<logical-set/>	"
		+ "	</area-of-interest>"
		+ "</prediction-context>";
		XMLStreamReader reader = inFact.createXMLStreamReader(new StringReader(testRequest));
		PredictionContext pCon = new PredictionContext();
		reader.next();
		pCon.parse(reader);

		assertEquals("22", pCon.getModelID());
		assertEquals("HUC8", pCon.getAnalysis().getGroupBy());
		assertEquals(3, pCon.getTerminalReaches().getReachIDs().size());
		
		// should have stopped at the end tag
		assertTrue(reader.getEventType() == XMLStreamConstants.END_ELEMENT);
		assertEquals(PredictionContext.MAIN_ELEMENT_NAME, reader.getLocalName());
	}
}