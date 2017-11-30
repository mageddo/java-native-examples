package nativeapi.jna.linux.password;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

/**
 * https://linux.die.net/man/3/getpwnam
 * http://man7.org/linux/man-pages/man3/getspnam.3.html
 * http://man7.org/linux/man-pages/man3/crypt.3.html
 * https://www.gnu.org/software/coreutils/coreutils.html
 * https://github.com/coreutils/coreutils/blob/v8.4/src/su.c
 */
public class Main {
	public static void main(String[] args) {
		final Scanner scanner = new Scanner(System.in);
		System.out.println("type the user");
		final String user = scanner.nextLine();

		System.out.println("type password");
		final String password = scanner.nextLine();

		System.out.println("RESULT\n===========================================");
		final SPassword passwd = CLibrary.INSTANCE.getspnam(user);
		if(passwd == null){
			// http://man7.org/linux/man-pages/man3/errno.3.html
			// it can be
			// EACCES(13) The caller does not have permission to access the shadow password file.
			// ERANGE(34) Supplied buffer is too small.
			throw new RuntimeException(String.valueOf(Native.getLastError()));
		}

		final String encrypted = Crypt.INSTANCE.crypt(password, passwd.sp_pwdp);
		System.out.printf("matches=%b%n", encrypted.equals(passwd.sp_pwdp));
	}

	interface Crypt extends Library {
		/**
		 * https://stackoverflow.com/questions/5989444/undefined-reference-to-crypt
		 */
		Crypt INSTANCE = Native.loadLibrary("crypt", Crypt.class);

//		char *crypt(const char *key, const char *salt);
			String crypt(String key, String salt);
	}

	interface CLibrary extends Library {
		CLibrary INSTANCE = Native.loadLibrary("c", CLibrary.class);

//		struct passwd *getpwnam(const char *name);
		Password getpwnam(String username);

//		struct spwd *getspnam(const char *name);
		SPassword getspnam(String username);
	}

	public static class SPassword extends Structure {
		public String sp_namp;     /* Login name */
		public String sp_pwdp;     /* Encrypted password */
		public long  sp_lstchg;   /* Date of last change
                                     (measured in days since
                                     1970-01-01 00:00:00 +0000 (UTC)) */
		public long  sp_min;      /* Min # of days between changes */
		public long  sp_max;      /* Max # of days between changes */
		public long  sp_warn;     /* # of days before password expires
                                     to warn user to change it */
		public long  sp_inact;    /* # of days after password expires
                                     until account is disabled */
		public long  sp_expire;   /* Date when account expires
                                     (measured in days since
                                     1970-01-01 00:00:00 +0000 (UTC)) */
		public long sp_flag;  /* Reserved */

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("sp_namp", "sp_pwdp", "sp_lstchg", "sp_min", "sp_max", "sp_warn", "sp_inact", "sp_expire", "sp_flag");
		}
	}
	public static class Password extends Structure {

		public String pw_name;
		public String pw_passwd;

		public int pw_uid;
		public int pw_gid;

		public String pw_gecos;
		public String pw_dir;
		public String pw_shell;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(
				"pw_name", "pw_passwd",
				"pw_uid", "pw_gid",
				"pw_gecos","pw_dir", "pw_shell"
			);
		}
	}

//	struct passwd {
//    char   *pw_name;       /* username */
//    char   *pw_passwd;     /* user password */
//		uid_t   pw_uid;        /* user ID */
//		gid_t   pw_gid;        /* group ID */
//    char   *pw_gecos;      /* user information */
//    char   *pw_dir;        /* home directory */
//    char   *pw_shell;      /* shell program */
//	};

//	struct spwd {
//               char *sp_namp;     /* Login name */
//               char *sp_pwdp;     /* Encrypted password */
//		long  sp_lstchg;   /* Date of last change
//                                     (measured in days since
//                                     1970-01-01 00:00:00 +0000 (UTC)) */
//		long  sp_min;      /* Min # of days between changes */
//		long  sp_max;      /* Max # of days between changes */
//		long  sp_warn;     /* # of days before password expires
//                                     to warn user to change it */
//		long  sp_inact;    /* # of days after password expires
//                                     until account is disabled */
//		long  sp_expire;   /* Date when account expires
//                                     (measured in days since
//                                     1970-01-01 00:00:00 +0000 (UTC)) */
//		unsigned long sp_flag;  /* Reserved */
//	};
}
