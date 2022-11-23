import static cTools.KernelWrapper.*;

import java.io.File;


class Head{
    public static void main(String[] args) {

        int buffersize = 256;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-c")) {
                buffersize = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("--help")) {
                System.out.println("head [OPTION]... [FILE]...");
                exit(0);
            }
        }
        byte[] buf = new byte[buffersize];

        for (int i = 0; i < args.length; i++) {

            if(args[i].equals("-")){
                // Get stuff from stdin

                // int read(int fd, byte[] buf, int count)
                int result = read(STDIN_FILENO, buf, buffersize);
                //System.out.print(new String(buf, 0));
                write(STDOUT_FILENO, buf, buffersize);
            }

            File f = new File(args[i]);
            if (!f.exists() || f.isDirectory()) {
                System.out.println("Datei (" + args[i] + ") nicht vorhanden");
                exit(0);
            }

        }

    }
}