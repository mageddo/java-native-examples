package nativeapi.jna.stat;

import java.util.Date;

/**
 * @see https://github.com/bminor/glibc/blob/master/io/sys/stat.h
 *
 * ./sysdeps/mach/hurd/bits/stat.h:#define	__S_IFSOCK	0140000	// Socket.
 * ./sysdeps/unix/sysv/linux/bits/stat.h:#define	__S_IFMT	0170000	// These bits determine file type
 *
 * ./io/sys/stat.h:#define	__S_ISTYPE(mode, mask)	(((mode) & __S_IFMT) == (mask))
 * ./io/sys/stat.h:# define S_ISSOCK(mode) __S_ISTYPE((mode), __S_IFSOCK)
 */
public class Main {
  public static void main(String[] args) {
    {
      final var stat = new Stat.ByReference();
      final var res = Stats.INSTANCE.stat("/var/run/docker.sock", stat);
      System.out.println(res);
      System.out.printf("st_mode=%s%n", parse(stat));
      System.out.println(stat.st_mtim.tv_sec);
    }
    {
      final var stat = new Stat.ByReference();
      final var res = Stats.INSTANCE.stat("/home/typer/kill-quarkus-dev.sh", stat);
      System.out.println(res);
      System.out.printf("st_mode=%s%n", parse(stat));
      System.out.println(new Date(stat.st_mtim.tv_sec * 1000));
//      System.out.println(stat);
    }
  }
  static int __S_IFSOCK =	0140000;
  static int __S_IFMT	= 0170000;

  static boolean __S_ISTYPE(int mode, int mask)	{
    return ((mode) & __S_IFMT) == (mask);
  }

  static boolean S_ISSOCK(int mode) {
    return __S_ISTYPE((mode), __S_IFSOCK);
  }

  static String parse(Stat.ByReference stat) {
    return String.format("%s | %s", S_ISSOCK(stat.st_mode.intValue()), stat.st_mode);
  }
}
