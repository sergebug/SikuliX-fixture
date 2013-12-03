package sikulix.fixture;

import java.lang.reflect.Field;

import org.sikuli.basics.Debug;
import org.sikuli.basics.FileManager;
import org.sikuli.basics.Settings;
import org.sikuli.script.App;
import org.sikuli.script.Env;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;

import com.sun.org.apache.xpath.internal.operations.Bool;

import fit.Counts;
//import fit.TypeAdapter;
import fitlibrary.DoFixture;

/**
 * @author szukov
 *
 */
@SuppressWarnings("deprecation")
public class SikuliXDoFixture extends DoFixture {
	private float maxTimeout = 30;
	private Region _lastRegion; 
	private Match _lastMatch;
	private Pattern _lastPattern;
	private String _lastRegionSnapshot; 
	private App _lastApp;
	private Env _lastEnv;
	public Object SystemUnderTest;
	private Field[] keys = Key.class.getDeclaredFields();
	private Field[] modifiers = KeyModifier.class.getDeclaredFields();
	
	/**
	 * SikuliX do fixture 
	 */
	public SikuliXDoFixture() {
		_lastRegion = useScreen(); // Screen is default region
		maxTimeout = Settings.AutoWaitTimeout;
	}
	/** 
	 * Retrieve current context (SystemUnderTests) object
	 */
	@Override
	public Object getSystemUnderTest() {
		return SystemUnderTest;
	}
	/** 
	 * Set current context (SystemUnderTests) object
	 */
	@Override
	public void setSystemUnderTest(Object SystemUnderTests) {
		SystemUnderTest = SystemUnderTests;
	}
	/**
	 * wait for the window with Given title
	 * @param title
	 * @return Region of the retrieved window
	 */
	public Region waitForWindow(String title) {
		int i = 0; 
		while ((isWindowPresent(title)==false) && (i<maxTimeout)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
		App app = new App(title);
		_lastRegion = app.window();	
		setSystemUnderTest(_lastRegion);
		return _lastRegion;
	}
	/** 
	 * Sets context to the last used Region
	 * @return Region
	 */
	public Region useRegion() {
		useRegion(_lastRegion);
		return _lastRegion;
	}
	/**
	 * Sets context to the Region passed as parameter
	 * @param r - Region
	 * @return - Region
	 */
	private Region useRegion(Region r) {
		_lastRegion = r;
		_lastRegionSnapshot = captureRegion(_lastRegion);
		setSystemUnderTest(_lastRegion);
		return _lastRegion;
	}
	/**
	 * Captures region in temporary file and returns file path. File is not being kept after class deleted.
	 * @param r - Region
	 * @return - Region
	 */
	private String captureRegion(Region r){
		Screen scr = r.getScreen();
		String filename = scr.capture(r.getX(),r.getY(),r.getW(),r.getH()).getFile();
		Debug.info(filename);
		return filename;

	}
	/**
	 * Returns true if last used region is still visible. Could be used to identify hidden window.
	 * @param r - Region
	 * @return - Region
	 */
	public boolean isRegionVisible()
	{
			Pattern p = new Pattern(_lastRegionSnapshot).exact();
			Region searchRegion = new Region(_lastRegion);
			boolean result = (searchRegion.exists(p,0)!=null);
			return result;
	}
	/**
	 * Sets context to the Region of application for given title
	 * @param s - window title (substring)
	 * @return Window region
	 */
	public Region useRegion(String s) {
		useApplication(s);
		Region r = useRegion(_lastApp.window());
		return r;
	}
	/**
	 * Sets context to the last Match
	 * @return Match
	 */
	public Match useMatch() {
		Match m = _lastMatch;
		m = useMatch(m);
		return m;
	}
	/**
	 * Sets context to the given Match
	 * @param m - Match object to be used as a new context
	 * @return - Match
	 */
	private Match useMatch(Match m) {
		_lastMatch = m;
		SystemUnderTest = m; 
		return _lastMatch ;
	}
	/**
	 * Set context to the Match using find in the lastRegion for given Pattern
	 * @param p - Pattern 
	 * @return - Match
	 */
	private Match useMatch(Pattern p) {
		Match m;
		try {
			m = _lastRegion.find(p);
			return useMatch(m);
		} catch (FindFailed e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Set context to the Match using find in the lastRegion for Pattern defined by input string
	 * @param s - image file
	 * @return - Match 
	 */
	public Match useMatch(String s) {
		Pattern p = usePattern(s);
		Match m = useMatch(p);
		return m;
	}
	/**
	 * Same as givenMatch(s)
	 * @param s - Image file
	 * @return - Match
	 */
	public Match findMatch(String s) {
		return useMatch(s);
	}
	/**
	 * Set context to the last known Pattern 
	 * @return - Pattern
	 */
	public Pattern usePattern() {
		Pattern p = _lastPattern; 
		return usePattern(p);
	}
	/**
	 * Set context to the Pattern 
	 * @return - Pattern
	 */
	private Pattern usePattern(Pattern p) {
		_lastPattern = p;
		setSystemUnderTest(p);
		return p;
	}
	/**
	 * Set context to the Pattern created from string
	 * @param s - image file
	 * @return - Pattern
	 */
	public Pattern usePattern(String s) {
		Pattern p = new Pattern(s);
		usePattern(p);
		return p;
	}
	/**
	 * Set context to the last used Application
	 * @return App object
	 */
	public App useApplication() {
		useApplication(_lastApp);
		return _lastApp;
	}
	/**
	 * Set context to the given Application
	 * @param a App object
	 * @return App object
	 */
	private App useApplication(App a) {
		_lastApp = a;
		_lastRegion = a.window();
		setSystemUnderTest(_lastApp);
		return _lastApp;
	}
	/**
	 * Set context to the Application with given title
	 * @param s - Application title (substring)
	 * @return App object
	 */
	public App useApplication(String s) {
		App a = new App(s);
		useApplication(a);
		return _lastApp;
	}
	/**
	 * Set Context to the full screen (default Region)
	 * @return Region
	 */
	public Region useScreen(){
		Region r = new Screen();
		useRegion(r);
		return _lastRegion;
	};
	/**
	 * Sets context to the Monitor #
	 * @param scrNumber - number of monitor
	 * @return Region of the given monitor
	 */
	public Region useScreen(int scrNumber){
		try {
			_lastRegion = new Screen(scrNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		useRegion(_lastRegion);
		return _lastRegion;
	};
	/**
	 * Set context to the Env object
	 * Env is useful to call getClipboard()
	 * Note: Env object is marked as depricated. 
	 * @return Env object
	 */
	public Env useEnvironment(){
		_lastEnv = new Env(); 
		setSystemUnderTest(_lastEnv);
		return _lastEnv;
	} 
	/**
	 * Print string to the log - appears in the captured output
	 * @param s String to be recorded to the log
	 */
	public void print(Object s){
		Debug.info("SDF:"+s.toString());
	}
	/**
	 * Pause execution for number of seconds
	 * @param seconds
	 * @throws InterruptedException
	 */
	public void pauseForSeconds(double seconds) throws InterruptedException {
		Thread.sleep((long) (seconds*1000.0));
	};
	private int reverseModifier(String s){
		int result = 0;
		for (Field f : modifiers) {
			String keyName = f.getName();
			if ((f.getType().equals(int.class)) && (s.contains(keyName)==true)) {
					try {
						result+=f.getInt(null);
					} catch (IllegalArgumentException 
							| IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        }
		return result;
	};
	private String reverseKey(String s){
		String result = s;
		for (Field f : keys) {
			String keyName = f.getName();
			if (result.contains(keyName)==true) {
				try {
					result = result.replaceAll(keyName, (String) f.get(null));
				} catch (IllegalArgumentException
						| IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
        }
		return result;
	};
	/**
	 * Press key[s]. Unlike {@link #enter(String)} resolves key names into actual keystrokes i.e. ENTER turns into actual line feed.  
	 * @param s - string to be interpret into key press i.e.: ENTER TAB ESC RIGHT
	 * @return 1 - was possible; 0 - was not possible
	 */
	public int press(String s){
		int result;
		s = reverseKey(s);
		result = typeInRegion(s);
		return result;
	};
	/**
	 * Press key[s] combined with special key (modifiers) such as CTRL+C  
	 * @param modifier - string to be interpret into modifier: ALT CTRL SHIFT
	 * @param s - string to be interpret into key press i.e.: C F4
	 * @return 1 - was possible; 0 - was not possible
	 * examples:
	 * <pre>
	 * |press |CTRL|Plus|C|
	 * |press |ALT|Plus|F4|
	 * |press |SHIFT|Plus|bob|
	 * </pre>
	 */
	public int pressPlus(String modifier, String s){
		int km = reverseModifier(modifier);
		String text = reverseKey(s);
		Debug.info("pressPlus(int "+modifier+", String "+s.toString()+")");
		Region r = _lastRegion;
		return r.type(text, km);
	};
	/**
	 * <pre>
	 * Type text into the current region
	 * 
	 * Unlike {@link #press(String)} does not resolve keys (i.e. string "ENTER" will be interpret as text not as line feed)
	 * Note that "was possible" does not mean "text was entered".
	 * type string into region
	 * |Type |Some Text| in Region|
	 * or
	 * |Type in Region|Some Text|
	 * </pre>  
	 * @param s - string of text to be entered
	 * @return 1 - was possible; 0 - was not possible
	 */
	public int typeInRegion(String s){
		Debug.info("typeInRegion(String "+s.toString()+")");
		return _lastRegion.type(s);
	};
	/**
	 * Same as typeInRegion
	 * @param s - string of text to be entered
	 * @return 1 - was possible; 0 - was not possible
	 */
	public int enter(String s){
		return typeInRegion(s);
	};
	/**
     * Highlight last match
	 * @param i - seconds
	 */
	public void highlightMatch(int i){
		_lastMatch.highlight(i);
	};
	/**
     * Highlight last region
	 * @param i - seconds
	 */
	public void highlightRegion(int i){
		_lastRegion.highlight(i);
	};
	/**
     * Click in the middle of last match
	 */
	public void clickOn(){
		_lastMatch.click();
	};
	/**
     * Click in the middle of found match
	 * @param s - image file
	 */
	public void clickOn(String s){
		Match m = useMatch(s);
		m.click();
	};
	/**
     * double Click in the found match
	 * @param s - image file
	 */
	public void doubleClickOn(String s){
		Match m = useMatch(s);
		m.doubleClick();
	};
	/**
     * right Click in the found match
	 * @param s - image file
	 */
	public void rightClickOn(String s){
		Match m = useMatch(s);
		m.rightClick();
	};
	/**
	 * Sets search region to the whole application window
	 */
	public void inTheApplicationWindow(){
		useRegion(_lastApp.window());
	}
	/**
	 * Sets search region to the top half of application window 
	 */
	public void inTheTop(){
		Region r = _lastApp.window();
		r.setH(r.getH()/2);
		useRegion(r);
	}
	/**
	 * Sets search region to the left half of application window 
	 */
	public void onTheLeft(){
		Region r = _lastApp.window();
		r.setW(r.getW()/2);
		useRegion(r);
	}
	/**
	 * Sets search region to the bottom half of application window 
	 */
	public void inTheBottom(){
		Region r = _lastApp.window();
		r.setH(r.getH()/2);
		r.setY(r.getY()+r.getH());
		useRegion(r);
	}
	/**
	 * Sets search region to the right half of application window 
	 */
	public void onTheRight(){
		Region r = _lastApp.window();
		r.setW(r.getW()/2);
		r.setX(r.getX()+r.getW());
		useRegion(r);
	}
	/**
	 * Sets search region on the right from the found match within current application window
	 * Useful for controls where label is on the left from the entry area.
	 * Height of region is defined by height of pattern 
	 * @param imageFile - image file path or URL
	 */
	public void onTheRightFromMatch(String imageFile){
		Match m = useMatch(imageFile);
		Region w = _lastApp.window();
		int maxWidth = w.getW()-m.getX();
		Region r = m.right(maxWidth);
		useRegion(r);
	};
	/**
	 * Sets search region on the right from the found match within current application window
	 * Useful for fields located on the left from some pattern (i.e. edit-box followed by drop-down)
	 * Height of region is defined by height of pattern 
	 * @param imageFile - image file path or URL
	 */
	public void onTheLeftFromMatch(String imageFile){
		Match m = useMatch(imageFile);
		Region w = _lastApp.window();
		int maxWidth = m.getX()-w.getX();
		Region r = m.left(maxWidth);
		useRegion(r);
	};
	/**
	 * Sets search region below the found match within current application window
	 * Useful for controls where label is above the entry area 
	 * @param s - image file
	 */
	public void belowMatch(String s){
		Match m = useMatch(s);
		Region w = _lastApp.window();
		int maxHight = w.getH()-m.getY();
		Region r = m.below(maxHight);
		useRegion(r);
	};
	/**
	 * Returns true (or false) if given application title present
	 * @param windowTitle (substring)
	 * @return true if exists; false otherwise
	 * Does not change context to the region unlike waitForWindow
	 */
	public Boolean isWindowPresent(String windowTitle){
		App a = new App(windowTitle);
		Region window = a.window();
		if (window==null) {
			return false;
			}
		else {
			return true;
			}
	};
	/**
	 * Return true (or false) if given image exists on the screen
	 * @param imageFile
	 * @return true if exists; false otherwise
	 * Does not change context to the match unlike findMatch
	 */
	public Boolean isMatchPresent(String imageFile){
		try {
			Pattern p = new Pattern(imageFile);
			_lastRegion.find(p);
			return true;
		}
		catch (FindFailed e) { 
			return false;
		}
	};
	/**
	 * Set current context to the region limited between two sample images. Search performed in the current region. 
	 * @param imageFrom - image of the top left corner
	 * @param imageTo - image of the bottom right corner
	 * @return New region between imageFrom and imageTo 
	 * @throws FindFailed
	 */
	public Region useRegionBetweenAnd(String imageFrom, String imageTo) throws FindFailed{
		Region r ;
		Pattern p;
		r = new Region(_lastRegion);
		p = new Pattern(imageFrom);
		Match mFrom = _lastRegion.find(p);
		p = new Pattern(imageTo);
		Match mTo = _lastRegion.find(p);
		r.setX(mTo.getX()-mFrom.getX()-mFrom.getW());
		r.setY(mTo.getY()-mFrom.getY()-mFrom.getH());
		r.setW(mTo.getX()-r.getW());
		r.setH(mTo.getY()-r.getH());
		return useRegion(r);
	}
	/**
	 * <pre>  
	 * Will not execute any action below "onError" method to the end of table unless test failed.
	 * Common usage - add to tear down page to restart an application in case of error.
	 * Failed test will remain failed but further tests may have chance to use recovered environment. 
	 * </pre>  
	 */
	public void onError()
	{
		int errorCount = 0;
		Counts cnt = super.getRuntimeContext().getTestResults().getCounts();
		errorCount += cnt.wrong;
		errorCount += cnt.exceptions;
		// if there is no error - abandon test
		if (errorCount==0) {
			abandon();
		};
		
	};
	/**
	 * Captures screenshot into a temporary file, logs and returns filename. 
	 * @return PNG filename in temporary folder.
	 */
	public String getScreenshot()
	{
		String log, from;
		try {
		    from = captureRegion(_lastRegion.getScreen());  // current file. will be deleted by sikuli engine upon completion
		    log = from.replace("sikuli","screenshot");
		    FileManager.xcopy(from, log, null);
			Debug.info(log);
			return log;
		} catch (Exception e) {
			e.printStackTrace();
			log = "Failed to capture screenshot.";
		}
		return log;
	}
}
