import static cTools.KernelWrapper.*;

import java.io.File;


class Head{
    public static void main(String[] args) {

        int buffersize = 256;
        int maxlines = -1;

        for (int i = 0; i < args.length; i++) {

            // Wenn Datei als Argument
            if(args[i].charAt(0) != '-'){
                readFile(args[i], buffersize, maxlines);
            }
            else {
                if (args[i].equals("-c")) {
                    buffersize = Integer.parseInt(args[i + 1]);
                    i++;
                }
                if (args[i].equals("-n")) {
                    maxlines = Integer.parseInt(args[i + 1]);
                    i++;
                }
                if (args[i].equals("--help")) {
                    System.out.println("head [OPTION]... [FILE]...");
                    exit(0);
                }
                if (args[i].equals("-")) {
                    readFileDescriptor(STDIN_FILENO, buffersize, maxlines);
                }
            }
        }
        exit(0);
    }

    public static void readFileDescriptor(int fd, int buffersize, int maxlines){

        int linecounter = 0;

        byte[] buf = new byte[buffersize];
        byte[] outputbuf = new byte[buffersize];

        int result = read(fd, buf, buffersize);

        for(int i = 0; i < buffersize && ((linecounter < maxlines) || maxlines == -1); i++){
            if (buf[i] == '\n') {
                linecounter++;
            }
            outputbuf[i] = buf[i];
        }
        write(STDOUT_FILENO, outputbuf, buffersize);
    }

    public static void readFile(String filename, int buffersize, int maxlines){
        File f = new File(filename);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("Datei (" + filename + ") nicht vorhanden");
            exit(0);
        }

        int fd = open(filename, O_RDONLY);
        readFileDescriptor(fd, buffersize, maxlines);
        int result = close(fd);
    }
}