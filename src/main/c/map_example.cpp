/*
compiling

	g++ -fPIC -shared -o src/main/resources/lib/libmap.dll src/main/c/map.cpp

### Map
* +18 primeiro item da map
* +20 ultimo item da map
* +28 tamanho da map

### MapItem
- se nao tiver +18 entao o proximo eh o +08
- caso tenha o +18 entao o proximo esta nesse endereco
- o inicio da mapitem eh [endereco de uma chave da map] -20


20 - key
24 - value
38 - next
*/

#include <string.h>
#include <iostream>
#include <map>
#include <utility>

using namespace std;

extern "C" map<int, string>  findUserById();
extern "C" map<int, int>* getPlayers();
extern "C" int playerAction(int action);

void printPlayers(map<int, int>* players);

map<int, string> findUserById(){
	map<int, string> user;
	user[1] = "Bob";
	user[2] = "Bob";
	user[3] = "Ana";
	return user;
}


map<int, int> players;

map<int, int>* getPlayers(){
	printf("players=%p\n", &players);
	return &players;
}

int playerAction(int action){
	int ret = -1;
	if(action < 0){
		players.clear();
		
	} else if(players.find(action) == players.end()){
		players[action] = 100;
		ret = 1; // player created
	}else{
		int* p = &players[action];
		*p = *p-1;
		ret = 2; // player hited
	}
	printPlayers(&players);
	return ret;
}


int main(){
	int action = -1;
	printf("1 = Reduce Player x Health if doesn't exists create a new player with 100  health\n");
	for(;;){
		scanf("%d", &action);
		playerAction(action);
	}

}


void printPlayers(map<int, int>* players) {
	map<int, int>::iterator it;
	for ( it = (*players).begin(); it != (*players).end(); it++ ) { 
		printf("players=%p, it=%x, %03d=%03d, %x=%x\n", players, it, it->first, it->second, &it->first, &it->second);
	}
	printf("\n");
}
