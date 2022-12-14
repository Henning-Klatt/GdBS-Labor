#include "workers.h"
#include "semaphores.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>

// AUFGABENSTELLUNG:
// Bringen Sie diese Programm mittels Semaphoren in Ordnung.
// https://de.wikipedia.org/wiki/Philosophenproblem

//-----------------------------------------------------------------------------
// alle globalen variablen fuer die beiden worker hier definieren,
// alle unbedingt mit "volatile" !!!
//-----------------------------------------------------------------------------

volatile int staebchen[5]={1,1,1,1,1};
volatile int have_one[5]={0,0,0,0,0}; // nur zur deadlock erkennung

volatile semaphore mutex_semaphor[5]; // 5 Semaphoren



//mutex_semaphor = sem_init(1);


//-----------------------------------------------------------------------------
// bevor der test beginnt wird test_setup() einmal aufgerufen
//-----------------------------------------------------------------------------

void test_setup(void) {
  printf("Test Setup\n");
  readers=0;
  writers=5;
  srandom(time(NULL));
  
  for(int i = 0; i < 5; i++){
	mutex_semaphor[i] = sem_init(1);
	printf("Semaphore %i initialisiert!\n", i);
}
}

//-----------------------------------------------------------------------------
// wenn beider worker fertig sind wird test_end() noch aufgerufen
//-----------------------------------------------------------------------------

void test_end(void) {
  printf("Test End\n");
}

//-----------------------------------------------------------------------------
// die beiden worker laufen parallel:
//-----------------------------------------------------------------------------

void reader(long my_id) {
  printf("Wer hat mich da aufgerufen?\n");
  exit(1);
}


int staebchen_nehmen(int my_id, int pos) {
  sem_p(mutex_semaphor[pos]); // Entsprechende Semaphore vom Stäbchen blockieren
  int n=staebchen[pos];
  if (n==1) { // Wenn Gabel an pos da ist
    printf("my_id: %i nimmt Gabel an pos: %i\n", my_id, pos);
    staebchen[pos] = 0; // ergibt 0, gibt aber chance zur fehlererkennung
    sem_v(mutex_semaphor[pos]);
    return 1;
  } else if (n==0) { // Wenn keine Gabel an pos da ist
    sem_v(mutex_semaphor[pos]);
    return 0;
  } else {
    printf("Fehler: staebchen[%i]=%i\n", pos, n);
    exit(1);
  }
}

void staebchen_weglegen(int my_id, int pos) {
  //sem_p(mutex_semaphor);
  printf("my_id: %i legt pos: %i weg\n", my_id, pos);
  if (staebchen[pos]!=0) {
    printf("Fehler: staebchen[%i]=%i statt 0\n", pos, staebchen[pos]);
    exit(1);
  }
  staebchen[pos] = 1; // ergibt 1, gibt aber chance zur fehlererkennung
  //sem_v(mutex_semaphor);
}


void writer(long long_my_id) {
  int my_id=long_my_id;
  int nxt=(my_id+1)%5;

  int i=100; // will so oft was essen
  int links=0;  // welche staebchen habe ich gerade
  int rechts=0;
  while (i>0) {
    //sem_p(mutex_semaphor);
    // probiere zufaellig staebchen zu nehmen, die noch nicht da sind
    if (!links && random()%10==7) {
      //sem_p(mutex_semaphor);
      links=staebchen_nehmen(my_id, my_id);
      //sem_v(mutex_semaphor);
    }
    if (!rechts && random()%10==7) {
      //sem_p(mutex_semaphor);
      rechts=staebchen_nehmen(my_id, nxt);
      //sem_v(mutex_semaphor);
    }

    // wenn beide da sind: essen!
    if (links && rechts) {
      //sem_p(mutex_semaphor);
      printf("my_id: %i futtert jetzt\n", my_id);
      usleep(random()%200);
      i--;
      // staebchen wieder hinlegen
      staebchen_weglegen(my_id, my_id); links=0;
      staebchen_weglegen(my_id, nxt);   rechts=0;
      //sem_v(mutex_semaphor);
    }

    // fuer die deadlock-erkennung melden, wenn ein staebchen von mir
    // belegt ist (beide koennen hier nicht belegt sein, dann wurde gegessen)
    have_one[my_id] = links || rechts;
    if (have_one[0]+have_one[1]+have_one[2]+have_one[3]+have_one[4]==5) {
      printf("Deadlock!\n");
      //exit(1);
      printf("my_id: %i gibt freiwillig ab\n", my_id);
      if (links) {
        staebchen_weglegen(my_id, my_id); links=0;
      }
      if (rechts) {
        staebchen_weglegen(my_id, nxt);   rechts=0;
      }
      have_one[my_id] = 0;
    }
    

    // denken
    //sem_p(mutex_semaphor);
    usleep(random()%200);
    //sem_v(mutex_semaphor);
  }
}
