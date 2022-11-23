import static cTools.KernelWrapper.*;

import java.util.Arrays;
import java.io.File;


class Shell {

    // Execute single command without && or > < -
    public static int execute(String command){
        String[] argumente = command.split(" ");

        File f = new File(argumente[0]);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("Datei (" + argumente[0] + ") nicht vorhanden");
            exit(0);
        }

        if (!f.canExecute()) {
            System.out.println("Datei nicht ausführbar");
            exit(0);
        }

        int result = execv(argumente[0], argumente);
        return result;
    }

    public static void main(String[] args) {

        while (true) {
            System.out.print("> ");
            String input = System.console().readLine();
            String[] commands = input.split(" && ");

            for (int i = 0; i < commands.length; i++) {

                String command = commands[i];

                if (command.equals("exit")) {
                    System.out.println("Bye! :)");
                    exit(0);
                }

                int pids = fork();

                // Error
                if (pids < 0) {
                }

                // Child PID
                if (pids == 0) {

                    System.out.println("Child: " + pids);

                    // /bin/cat abc > xyz    # wirkt wie "cp abc xyz"
                    if(command.contains(">")){
                        String[] splitArray = command.split(" > ");

                        // STDout close:
                        int stdfd = close(1);

                        // int open(String path, int flags)
                        int fd = open(splitArray[splitArray.length-1], O_WRONLY | O_CREAT);
                        int result = execute(splitArray[0]);

                        // int write(int fd, byte[] buf, int count)
                        int closeresult = close(fd);

                    }
                    // /bin/cat < abc    # Inhalt der Datei abc wird auf dem Terminal ausgegeben
                    if(command.contains("<")){
                        String[] splitArray = command.split(" < ");

                        // close STDin with fd = 0
                        int stdfd = close(0);
                        File f = new File(splitArray[splitArray.length-1]);
                        if (!f.exists() || f.isDirectory()) {
                            System.out.println("Datei (" + splitArray[splitArray.length-1] + ") nicht vorhanden");
                            exit(0);
                        }
                        int fd = open(splitArray[splitArray.length-1], O_RDONLY);
                        int result = execute(splitArray[0]);
                        int closeresult = close(fd);

                    }
                    if(command.contains("-")){
                        String[] splitArray = command.split(" - ");

                    }

                    int result = execute(command);
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

