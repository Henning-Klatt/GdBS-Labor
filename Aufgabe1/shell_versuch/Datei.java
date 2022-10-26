import static cTools.KernelWrapper.*;

class hello {
  public static void main(String[] args) {
 	int fh = open("dateiname.txt", O_RDONLY);
	uint8_t one_byte;
	while (read(fh, &one_byte, 1)==1) {
		printf("%c", one_byte);
	}
	close(fh);
  }
}

