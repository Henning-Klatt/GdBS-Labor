import static cTools.KernelWrapper.*;

import java.util.Arrays;


class Shell {
        public static void main(String[] args) {

                System.out.print("> ");
                String input = System.console().readLine();
				
          		String[] array = input.split(" ");
                System.out.println(Arrays.toString(array));


		int pids = fork();

		// Error
		if(pids < 0 ){
		}

		// Child PID
		if(pids == 0){
		  // int execv(String path, String[] argv)
		  //int result = execv(String pathh, String[] argv)
		  System.out.println("Child: " + pids);
		  exit(0);
		}

		// Parent PID
		if(pids > 0){
		  System.out.println("Parent: " + pids);
		  
		  // int waitpid(int pid, int[] status, int options)
		  int[] status = new int[10];
		  int options = 0;
		  int wait = waitpid(pids, status, options);
		}

        }
}
