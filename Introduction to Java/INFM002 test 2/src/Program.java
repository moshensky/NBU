import java.util.Arrays;


public class Program {
	
	public static void main(String args[]){
		Parallelepiped[] objects = Parallelepiped.generateParallelepipeds(10, 5, 20, 50, 56);
		
		System.out.println();
		System.out.println("Parallelepipeds:");
		printSlashes(74);
		Parallelepiped.printParallelepipedArray(objects);
		printSlashes(74);
		
		System.out.println();
		System.out.println("Sorted by weight:");
		printSlashes(74);
		Arrays.sort(objects, Parallelepiped.WeightComparator);	
		Parallelepiped.printParallelepipedArray(objects);
		printSlashes(74);
		
		System.out.println();
		System.out.println("Sorted by pressure:");
		printSlashes(74);
		Arrays.sort(objects, Parallelepiped.PressureComparator);
		Parallelepiped.printParallelepipedArray(objects);
		printSlashes(74);
		
		System.out.println();
		System.out.println("Sorted by weight and pressure:");
		printSlashes(74);
		
		int from = 0;
		boolean equals = false;
		Arrays.sort(objects, Parallelepiped.WeightComparator);
		
		for(int i = 0; i < objects.length - 1; i++){
			if (objects[i] == objects[i+1]){
				from = i;
				equals = true;
			} else if (equals && (objects[i] != objects[i+1]))
			{
				equals = false;
				Arrays.sort(objects, from, i-1, Parallelepiped.PressureComparator);
			}
		}
		
		Parallelepiped.printParallelepipedArray(objects);
		printSlashes(74);
	}
	
	private static void printSlashes(int num){
		for(int i = 0; i < num; i++)
			System.out.print("-");
		System.out.println();
	}
	
}
