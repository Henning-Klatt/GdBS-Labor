import static cTools.KernelWrapper.*;

import java.util.Arrays;
import java.io.File;


class Shell {
    public static void main(String[] args) {

        while (true) {
            System.out.print("> ");
            String input = System.console().readLine();

            String[] commands = input.split(" && ");

            for (int i = 0; i < commands.length; i++) {

                String[] command = commands[i].split(" ");
                System.out.println(Arrays.toString(command));

                if (command[0].equals("exit")) {
                    System.out.println("Bye! :)");
                    exit(0);
                }

                File f = new File(command[0]);
                if (!f.exists() || f.isDirectory()) {
                    System.out.println("Datei nicht vorhanden");
                    break;
                }

                if (!f.canExecute()) {
                    System.out.println("Datei nicht ausführbar");
                    break;
                }


                int pids = fork();

                // Error
                if (pids < 0) {
                }

                // Child PID
                if (pids == 0) {

                    System.out.println("Child: " + pids);
                    int result = execv(command[0], command);
                    System.out.println("Result: " + result);
                    exit(0);
                }

                // Parent PID
                if (pids > 0) {
                    System.out.println("Parent: " + pids);

                    // int waitpid(int pid, int[] status, int options)
                    int[] status = new int[10];
                    int options = 0;
                    int wait = waitpid(pids, status, options);
                
                  	System.out.println("Exit Status Child: " + status[0]);

                    // Success
                    if (status[0] == 0) {
						
                    }
                    // Failure
                    else {
						break;
                    }

                    
                }

            }
        }
    }

}

