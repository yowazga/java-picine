public class Hen implements Runnable {
	private Integer count;

	public Hen(Integer number) {this.count = number;}
	
	@Override
	public void run() {
		for (int i = 0; i < this.count; i++)
			System.out.println("Hen");
	}
}
