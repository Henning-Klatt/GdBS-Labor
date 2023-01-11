#include "workers.h"
#include "semaphores.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>


volatile int feld[4];
volatile semaphore mutex_semaphor; // Semaphore
volatile int movements = 25;

void test_setup(void) {
	printf("Test Setup\n");
	readers=0;
  	writers=4;
	srandom(time(NULL));

	for(int i = 0; i < 4; i++){
		feld[i] = 0;
		printf("feld[%i] = 0\n", i);
	}
	feld[0] = 1;

	mutex_semaphor = sem_init(1);
}

void test_end(void) {
 	printf("Test End\n");
}

void reader(long my_id) {
  	printf("Wer hat mich da aufgerufen?\n");
  	exit(1);
}

int move(int my_id){
  	sem_p(mutex_semaphor);
        int von_position = my_id;
        int nach_position = (my_id+1) % 4;

	// Prüfe, ob die 1 an eigener Stelle steht
	
	if(feld[my_id] == 0){ // Wenn nicht dran, geb Semaphore frei und beende
	  	sem_v(mutex_semaphor);
		return 0;
	}

	// Prüfe, ob alle anderen Zellen 0 sind
	for(int i = 0; i < 4; i++){
		if(i != my_id){
			if(feld[i] != 0){
			  	printf("Fehler: Die 1 steht schon in Zelle %i", i);
			  	sem_v(mutex_semaphor);
				return 0;
			}
		}
	}

	// Alle Checks erfolgreich
	feld[nach_position] = feld[von_position];
	feld[von_position] = 0;
	printf("%i>%i\n", von_position, nach_position);
	movements--;

	sem_v(mutex_semaphor);
	return 1;
}

void writer(long long_my_id) {
	usleep(random()%200);
	printf("my_id: %li\n", long_my_id);

	while(movements > 0){
		int result = move(long_my_id);
	}
}
