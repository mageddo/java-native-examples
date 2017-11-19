// https://github.com/vladimirvivien/go-cshared-examples/blob/master/awesome.go
package main

import "C"

// You must add the comment bellow to export the function
//export Add
func Add(a, b int) int {
	return a + b
}

func main(){}
