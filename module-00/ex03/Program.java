import java.util.Scanner;

public class Program {

	private static Scanner scanner = new Scanner(System.in);
	private static int weeks = 0;

	private static int getMinGrad()
	{
		int minGrage = 9;
		for (int i = 0; i < 5; i++)
		{
			if (scanner.hasNextInt())
			{
				int grade = scanner.nextInt();
				if (grade < 1 || grade > 9)
				{
					System.err.println("IllegalArgument");
					scanner.close();
					System.exit(-1);
				}
				minGrage = grade < minGrage ? grade : minGrage;
			}
			else
			{
				System.err.println("IllegalArgument");
				scanner.close();
				System.exit(-1);
			}
		}
		scanner.nextLine();
		return minGrage;
	}

	private static long getWeekGrades()
	{
		long grades = 0;
		String input = scanner.nextLine();

		while (weeks < 18 && !input.equals("42"))
		{
			if (input.equals("Week " + (weeks + 1)))
			{
				grades = grades * 10 + getMinGrad();
				weeks++;
				input = scanner.nextLine();
			}
			else
			{
				System.err.println("IllegalArgument");
				scanner.close();
				System.exit(-1);
			}
		}
		scanner.close();
		return grades;
	}

    private static void printGraph(long grades)
	{
        long divisor = 1;

        for (int i = 1; i < weeks; i++) {
            divisor *= 10;
			System.out.println("divisor is: " + divisor);

        }

        for (int i = 0; i < weeks; i++) {
            int minGrade = (int) (grades / divisor) % 10;
            System.out.print("Week " + (i + 1) + " ");
            for (int j = 0; j < minGrade; j++) {
                System.out.print("=");
            }
            System.out.println(">");
            divisor /= 10;
        }
    }

	public static void main(String[] args)
	{
		long grades = getWeekGrades();
		printGraph(grades);
	}
}