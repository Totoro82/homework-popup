import java.util.Arrays;
import java.util.Scanner;

public class Ljutnja {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long M = scanner.nextLong();
        int N = scanner.nextInt();
        long[] needs = new long[N];
        long totalNeeds = 0;

        //fill out the needs and calculate totalNeed
        for (int i = 0; i < N; i++) {
            needs[i] = scanner.nextLong();
            totalNeeds += needs[i];
        }
        scanner.close();

        Arrays.sort(needs);

        //how much suffering remains
        long remaining = totalNeeds - M;
        long result = 0;

        //we look for the optimum level of suffering for every children
        for (int i = 0; i < N; i++) {
            long childrenLeft = N - i;
            //how much suffering per children if we distribute btw the children left
            long suffering = remaining/childrenLeft;

            //if the lowest asking child can suffer that much
            if(suffering <= needs[i]) {
                //how many children suffer one more from the level
                long extra = remaining%childrenLeft;
                //extra children suffer +1
                result += extra * (suffering+1) * (suffering+1);
                //the rest suffer normal
                result += (childrenLeft-extra) * suffering * suffering;

                System.out.println(result);
                return;
            }
            //here, this kid has to suffer all its needs because the average suffering is larger than its needs
            result += needs[i] * needs[i];
            remaining -= needs[i];
        }


    }
}
