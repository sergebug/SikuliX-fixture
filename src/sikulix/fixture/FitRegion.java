package sikulix.fixture;

import org.sikuli.script.Region;

public class FitRegion extends Region {
	public Object parse(String s){
		//if (!(s.startsWith("R["))) {throw new exception()};
		int x=0;
		int y=0;
		int w=0;
		int h = 0;
		Region r = new Region(x,y,w,h);
		return (Object) r;
	}

//	public String toString() {
//	    return String.format("R[%d,%d %dx%d]@%s E:%s, T:%.1f",
//	            x, y, w, h, (getScreen() == null ? "Screen null" : getScreen().toStringShort()),
//	            throwException ? "Y" : "N", autoWaitTimeout);
}
