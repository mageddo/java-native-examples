package nativeapi.jna.customlibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class HelloWorld {
	public interface CTest extends Library {
		void helloFromC();
	}

	static public void main(String argv[]) {
		CTest ctest = (CTest) Native.loadLibrary(CustomLibraryJNATest.getLibPath() + "/libctest.so", CTest.class);
		ctest.helloFromC();
	}
}
