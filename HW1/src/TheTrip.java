import java.util.Scanner;

public class TheTrip {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            Trip trip = new Trip();
            trip.students = scanner.nextInt();
            if (trip.students == 0) return;
            trip.spenditures = new int[trip.students];
            for (int i = 0; i < trip.students; i++) {
                    trip.spenditures[i] = Math.round(scanner.nextFloat() * 100);
            }
            resolveExpenditures(trip);
        }
    }

    private static void resolveExpenditures(Trip trip) {
        int totalCents = 0; // sum of all expenses in cents
        for (int i = 0; i < trip.students; i++) {
            totalCents += trip.spenditures[i];
        }
        int low = totalCents / trip.students; // base amount everyone should pay
        int extraCentSlots = totalCents % trip.students; // how many ppl need to pay an extra cent

        int spentAboveLow = 0; // total cents above low from ppl who spent more
        int pplAboveLow = 0; // how many ppl spent more than low
        for (int i = 0; i < trip.students; i++) {
            if (trip.spenditures[i] > low) {
                spentAboveLow += trip.spenditures[i] - low;
                pplAboveLow++;
            }
        }
        int result = spentAboveLow - Math.min(extraCentSlots, pplAboveLow);// how many ppl want a slot in extracent and how many benefit from it
        System.out.printf("$%.2f%n", result / 100.0);// print min amount to exchange
    }
}

class Trip {
    int students;
    int[] spenditures;
}