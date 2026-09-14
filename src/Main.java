import misiones.Mision2;
import misiones.Mision4;
import parser.InputParser;

public class Main {

    public static void main(String[] args) {

        try {

            InputParser parser = new InputParser(System.in);
            Mision2 mision2 = new Mision2();
            System.out.println("---------Mission 2---------");

            System.out.println("Enter the number of test cases:");

            int numberOfCases = parser.nextInt();

            for (int i = 1; i <= numberOfCases; i++) {

                System.out.println("Enter data for case #" + i + ":");

                String result = mision2.resolverCaso(parser, i);

                System.out.println(result);
            }

        } catch (Exception e) {

            System.err.println("Error al procesar la entrada: " + e.getMessage());
        }

        try {

            InputParser parser = new InputParser(System.in);
            Mision4 mision4 = new Mision4();
            System.out.println("---------Mission 4---------");
            System.out.println("Enter the number of test cases:");

            int numberOfCases = parser.nextInt();

            for (int i = 1; i <= numberOfCases; i++) {

                System.out.println("Enter data for case #" + i + ":");

                String result = mision4.resolverCaso(parser, i);

                System.out.println(result);
            }

        } catch (Exception e) {

            System.err.println(
                    "Error al procesar la entrada: " + e.getMessage()
            );
        }
    }
}