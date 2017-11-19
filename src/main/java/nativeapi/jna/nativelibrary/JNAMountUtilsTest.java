package nativeapi.jna.nativelibrary;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Calling some standard c functions,
 * passing and getting complex structs by reference, working with files and more
 */
public class JNAMountUtilsTest {

	public interface CLibrary extends Library {
		CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);

		int gethostname(byte[] name, int nameLength);

		Pointer fopen(String name, String mode);

		MountContent.ByReference getmntent(Pointer FILE);
	}

	public static void main(String[] args) {

		final byte[] hostname = new byte[30];
		int returnCode = CLibrary.INSTANCE.gethostname(hostname, hostname.length);
		System.out.printf("%s - %d\n", new String(hostname), returnCode);

		final String nfsPath = Class.class.getResource("/mnt/etc_mtab").getPath();
		final Pointer mountFile = CLibrary.INSTANCE.fopen(nfsPath, "r");
		if (mountFile == null) {
			System.err.println("File not exists: " + nfsPath);
			System.exit(-1);
		}
		MountContent.ByReference mtent;
		while ((mtent = CLibrary.INSTANCE.getmntent(mountFile)) != null) {
			System.out.println(mtent);
		}

	}

	public static class MountContent extends Structure {

		public String mnt_fsname;
		public String mnt_dir;
		public String mnt_type;
		public String mnt_opts;
		public int mnt_freq;
		public int mnt_passno;

		@Override
		protected List getFieldOrder() {
			List<String> fieds = new ArrayList<>();
			for (final Field f : MountContent.class.getDeclaredFields()) {
				if (!f.isSynthetic())
					fieds.add(f.getName());
			}
			return fieds;
		}

		public static class ByReference extends MountContent implements Structure.ByReference {
		}

		public static class ByValue extends MountContent implements Structure.ByValue {
		}
	}
}
