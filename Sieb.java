public class Sieb{

	public static void main(String[] args){
	
		boolean[] numbers = new boolean[100];
		for(int i = 0; i < numbers.length; i++){
			numbers[i] = true;
			// System.out.println(numbers[i]);
		}
		for(int i = 2; i < numbers.length; i++){
			if(numbers[i]){
				System.out.println(i);
			}
			int k = 2;
			while(k * i <= 99){
				numbers[i*k] = false;
				k++;
			}

		}

	
	}

}
