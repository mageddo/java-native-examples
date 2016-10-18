package nativeapi.jna.network;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 10/17/16 3:36 PM
 */
public class JNAUtilsTest {

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);
        int gethostname(byte[] name, int nameLength);
        Pointer fopen(String name, String mode);
        Mtent.ByReference getmntent(Pointer FILE);
    }

    public static void main(String[] args) {

        final byte[] hostname = new byte[30];
        int returnCode = CLibrary.INSTANCE.gethostname(hostname, hostname.length);
        System.out.printf("%s - %d\n", new String(hostname), returnCode);

        final String nfsPath = Class.class.getResource("/mnt/etc_mtab").getPath();
        final Pointer mountFile = CLibrary.INSTANCE.fopen(nfsPath, "r");
        if(mountFile == null){
            System.err.println("File not exists: " + nfsPath);
            System.exit(-1);
        }
//        final Pointer mountFile = CLibrary.INSTANCE.fopen("/proc/mounts", "r");
        Mtent.ByReference mtent;
        while( (mtent = CLibrary.INSTANCE.getmntent(mountFile)) != null ){
            System.out.println(mtent);
        }

    }

    public static class Mtent extends Structure {

        public String mnt_fsname;
        public String mnt_dir;
        public String mnt_type;
        public String mnt_opts;
        public int mnt_freq;
        public int mnt_passno;

        @Override
        protected List getFieldOrder() {
            List<String> fieds = new ArrayList<>();
            for(final Field f: Mtent.class.getDeclaredFields()){
                if(!f.isSynthetic())
                    fieds.add(f.getName());
            }
            return fieds;
        }

        public static class ByReference extends Mtent implements Structure.ByReference {}
        public static class ByValue extends Mtent implements Structure.ByValue {}
    }
}
