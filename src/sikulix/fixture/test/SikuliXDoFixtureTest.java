package sikulix.fixture.test;

//import java.awt.image.BufferedImage;
//import java.net.URL;

//import javax.imageio.ImageIO;

import sikulix.fixture.SikuliXDoFixture;
import junit.framework.TestCase;

import org.sikuli.script.App;
import org.sikuli.script.Env;
//import org.sikuli.script.Key;
//import org.sikuli.script.KeyModifier;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
//import org.sikuli.script.Screen;

@SuppressWarnings("deprecation")

public class SikuliXDoFixtureTest extends TestCase {
	private SikuliXDoFixture sdf = new SikuliXDoFixture();
	
    public void testPauseForSecond() {
		try {
			sdf.pauseForSeconds(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void testIsExplorerProcessIdle() throws Exception {
    	assertNotNull(sdf.isProcessIdle("Explorer"));
    }
    public void testPauseForFractionOfSecond() {
		try {
			sdf.pauseForSeconds(0.5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

    public void testInstantiateSikuliDoFixture() {
		String cls = sdf.getClass().toString();
		assertEquals(cls, "class sikulix.fixture.SikuliXDoFixture");
    }
    
    public void testNonExistingApp() {
    	Boolean isExists = sdf.isWindowPresent("non existing app");
		assertFalse(isExists);
    }

    public void testScreenIsDefaultRegion() {
		assertEquals(sdf.getSystemUnderTest().toString(),sdf.useScreen().toString());
    }

    public void testGivenEnvironment(){
    	Env env = sdf.useEnvironment();
    	assertEquals(sdf.getSystemUnderTest().toString(), env.toString());
    }
    
    public void testGivenApplication(){
    	App a = sdf.useApplication("explorer");
    	assertEquals(sdf.getSystemUnderTest().toString(), a.toString());
    }

    public void testRunCalculator() throws Throwable {
    	Pattern p;
    	Match m;
    	App calc = sdf.useApplication("Calculator");
    	App.open("c:\\windows\\system32\\calc.exe");
    	sdf.waitForWindow("Calculator");
		assertTrue(sdf.isWindowPresent("Calculator"));
		Region r = sdf.useRegion("Calculator");
		assertEquals(sdf.getSystemUnderTest().toString(), r.toString());
		r.highlight(1);
		p = sdf.usePattern("/img/calc-WIN7.png");
		assertEquals(sdf.getSystemUnderTest().toString(), p.toString());
		//assertTrue(sdf.isMatchPresent("http://localhost:8888/files/img/calc.png"));
		assertFalse(sdf.isMatchPresent("https://www.google.com/images/srpr/logo4w.png"));
		m = sdf.useMatch("/img/calc-WIN7.png");
		assertEquals(sdf.getSystemUnderTest().toString(), r.getLastMatch().toString());
		assertTrue(sdf.getScreenshot().contains(".png"));
		sdf.usePattern("/img/1-WIN7.png");
		m = sdf.useMatch("/img/1-WIN7.png");
		m = sdf.useMatch();
		assertEquals(sdf.getSystemUnderTest().toString(), m.toString());
		m.click();
		m = sdf.useMatch("/img/plus-WIN7.png");
		sdf.clickOn();
		r = sdf.useRegion("Calculator");
		assertTrue(sdf.isRegionVisible());
		m = sdf.useMatch("/img/1.png");
		m.click();
		sdf.press("ENTER");
		sdf.pressPlus("CTRL","C");
		String result = Env.getClipboard();
		assertEquals("2",result);
	 	calc.close();
		assertFalse(sdf.isRegionVisible());
    }
}
