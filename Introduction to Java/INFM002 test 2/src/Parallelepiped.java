import java.util.Comparator;
import java.util.Random;


public class Parallelepiped {
	private int weight;
	private int height;
	private int lenght;
	private int width;
	
	private double pressure;
	
	private static Random rand = new Random();
	
	
	public Parallelepiped(int weight, int h, int l, int w){
		this.weight = weight;
		height = h;
		lenght = l;
		width = w;
		pressure = calcPressure();
	}
	
	public double calcPressure(){		
		return (double)weight / (lenght*width);
	}
	
	public String toString() {
		return String.format("| Weight: %3d | Height: %3d | Lenght: %3d | Width: %3d | Pressure: %3.3f |", weight,  height, lenght, width, pressure);
	}
	
	public static Parallelepiped[] generateParallelepipeds(int count, int from, int to, int weightFrom, int weightTo){
		Parallelepiped[] obj = new Parallelepiped[count];		
		for (int i = 0; i < count; i++){			
			obj[i] = new Parallelepiped((rand.nextInt(weightTo-weightFrom)+weightFrom),
					(rand.nextInt(to-from)+from),
					(rand.nextInt(to-from)+from),
					(rand.nextInt(to-from)+from));
		}		
		return obj;
	}
	
	public static void printParallelepipedArray(Parallelepiped[] obj){
		for(int i = 0; i < obj.length; i++){
			System.out.println(obj[i]);;
		}
	}
		
	public static Comparator<Parallelepiped> PressureComparator = new Comparator<Parallelepiped>() {
		public int compare(Parallelepiped obj1, Parallelepiped obj2) {
			int pressure1 = (int)(obj1.pressure * 1000);
			int pressure2 = (int)(obj2.pressure * 1000);
			// ascending order
			return pressure1 - pressure2;
			// descending order
			// return pressure2 - pressure1;
		}
	};
	
	public static Comparator<Parallelepiped> WeightComparator = new Comparator<Parallelepiped>() {
		public int compare(Parallelepiped obj1, Parallelepiped obj2) {
			// ascending order
			return obj1.weight - obj2.weight;					
			// descending order
			//return obj2.weight - obj1.weight;					
		}
	};

}
