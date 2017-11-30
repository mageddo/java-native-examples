// https://github.com/mageddo/go-static-linking
package main

// #cgo CFLAGS: -I${SRCDIR}/../../../resources/lib/include
// #cgo LDFLAGS: ${SRCDIR}/../../../resources/lib/libcounter.so
// #include <counter.h>
import "C"

func main() {
	println(C.incrementAndGet())
	println(C.incrementAndGet())
}
