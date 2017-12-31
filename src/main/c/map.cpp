/*
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

void printPlayers(map<int, int>* players);

map<int, string> findUserById(){
	map<int, string> user;
	user[1] = "Bob";
	user[2] = "Bob";
	user[3] = "Ana";
	return user;
}

map<int, int>* getPlayers(){
	map<int, int> players;
	players[1] = 100;
	players[2] = 99;
	players[3] = 98;
	printf("players=%p, first=%p\n", &players, &players[1]);
	return &players;
}

/*
int main(){
	map<int, int> players;
	int action = -1;
	
	for(;;){
		
		printf("1 = Reduce Player x Health if doesn't exists create a new player with 100  health\n");
		scanf("%d", &action);
		if(action < 0){
			players.clear();
		} else if(players.find(action) == players.end()){
			players[action] = 100;
		}else{
			int* p = &players[action];
			*p = *p-1;
		}
		printPlayers(&players);
		map<int, int>::iterator it;
		
	}

}

*/
void printPlayers(map<int, int>* players) {
	map<int, int>::iterator it;
	for ( it = (*players).begin(); it != (*players).end(); it++ ) { 
		printf("players=%p, %03d=%03d, %p=%p\n", players, it->first, it->second, &it->first, &it->second);
	}
}
