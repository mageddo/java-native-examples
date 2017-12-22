#include <string.h>
#include <iostream>
#include <map>
#include <utility>

using namespace std;

extern "C" map<int, string>  findUserById();

map<int, string> findUserById(){
	map<int, string> user;
	user[1] = "Bob";
	user[2] = "Bob";
	user[3] = "Ana";
	return user;
}
