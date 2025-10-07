import java.util.Scanner;
public class choinka {
	public static void main(String[] args) {
		System.out.println("Podaj wysokość choinki: ");
		Scanner scanner = new Scanner(System.in);
		int hight = scanner.nextInt();
		for(int i=1; i<=hight; i++) {
			for(int k=1; k<=i; k++) {
				System.out.print("*");
			}
			System.out.println();
		}
	}
}
