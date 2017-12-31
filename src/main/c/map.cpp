#include <string.h>
#include <iostream>
#include <map>
#include <utility>

using namespace std;

extern "C" map<int, string>  findUserById();


void printPlayers(map<int, int> players);

map<int, string> findUserById(){
	map<int, string> user;
	user[1] = "Bob";
	user[2] = "Bob";
	user[3] = "Ana";
	return user;
}

int main(){
	map<int, int> players;
	int action = -1;
	
	for(;;){
		
		printf("1 = Reduce Player x Health if doesn't exists create a new player with 100  health\n");
		scanf("%d", &action);
		
		if(players.find(action) == players.end()){
			players[action] = 100;
		}else{
			int* p = &players[action];
			*p = *p-1;
		}
		printPlayers(players);
		
	}
	
}


void printPlayers(map<int, int> players) {
	
	map<int, int>::iterator it;
	for ( it = players.begin(); it != players.end(); it++ ) {
	    std::cout << it->first  // string (key)
	              << '='
	              << it->second   // string's value 
	              << std::endl ;
	}
	
/*	for(map<int, pair<int,int> >::const_iterator it = players.begin(); it != players.end(); ++it) {
	    std::cout << it->first << " " << it->second.first << " " << it->second.second << "\n";
	}
*/
}
