package sikulix.fixture;

import org.sikuli.script.Region;

public class FitRegion extends Region {
	public Object parse(String s){
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("R\\[(\\d+),(\\d+)\\s(\\d+)x(\\d+)]");
		java.util.regex.Matcher m = p.matcher(s);
		Region r; 
		int x=0,y=0, w=0, h=0;
		if (m.find()) {
		    x = Integer.parseInt(m.group(1));
		    y = Integer.parseInt(m.group(2));
		    w = Integer.parseInt(m.group(3));
		    h = Integer.parseInt(m.group(4));
		}
		r = new Region(x,y,h,w);
		return (Object) r;	}

//	public String toString() {
//	    return String.format("R[%d,%d %dx%d]@%s E:%s, T:%.1f",
//	            x, y, w, h, (getScreen() == null ? "Screen null" : getScreen().toStringShort()),
//	            throwException ? "Y" : "N", autoWaitTimeout);
}
