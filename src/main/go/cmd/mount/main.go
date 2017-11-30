// https://github.com/mageddo/go-static-linking
package main

// #include <mntent.h>
// #include <stdio.h>
import "C"

import "fmt"

func main() {
	f := C.fopen(C.CString("/etc/mtab"), C.CString("r"))
	mpoint := C.getmntent(f)
	fmt.Printf("fsname=%s, mntdir=%s, mnt_type=%s\n", string(*mpoint.mnt_fsname), string(*mpoint.mnt_dir), string(*mpoint.mnt_type))
}
