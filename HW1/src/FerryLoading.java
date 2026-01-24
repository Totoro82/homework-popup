import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class FerryLoading {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numCases = scanner.nextInt();
        TestCase[] testCases = new TestCase[numCases];

        for (int i = 0; i < numCases; i++) {
            //initialize test cases
            testCases[i] = new TestCase();
            testCases[i].n = scanner.nextInt();
            testCases[i].t = scanner.nextInt();
            testCases[i].m = scanner.nextInt();

            testCases[i].cars = new Car[testCases[i].m];
            //init each car for each test case
            for (int j = 0; j < testCases[i].m; j++) {
                testCases[i].cars[j] = new Car();
                testCases[i].cars[j].arrivalTime = scanner.nextInt();
                testCases[i].cars[j].bank = scanner.next();
            }
        }
        scanner.close();

        for (int i = 0; i < numCases; i++) {
            solveTestCase(testCases[i]);
        }
    }

    private static void solveTestCase(TestCase testCase) {
        String ferryPosition = "left";
        Queue<Integer> waitingLeft = new LinkedList<>();
        Queue<Integer> waitingRight = new LinkedList<>();
        Queue<Integer> onFerry = new LinkedList<>();
        int[] results = new int[testCase.m];
        int nextCar = 0;
        int time = 0;

        while(nextCar < testCase.m || !waitingLeft.isEmpty() || !waitingRight.isEmpty()) {
            // add all arrived cars to the corresponding queues
            while(nextCar < testCase.m && testCase.cars[nextCar].arrivalTime <= time) {
                if(testCase.cars[nextCar].bank.equals("left")) {
                    waitingLeft.add(nextCar);//add the index
                } else {
                    waitingRight.add(nextCar);
                }
                nextCar++;
            }
            //load ferry
            Queue<Integer> currentQueue = ferryPosition.equals("left") ? waitingLeft : waitingRight;
            int loadedCars = 0;
            while(loadedCars < testCase.n && !currentQueue.isEmpty()) {
                onFerry.add(currentQueue.poll());
                loadedCars++;
            }

            //what to do?
            if(!onFerry.isEmpty() || !waitingLeft.isEmpty() || !waitingRight.isEmpty()) {
                ferryPosition = ferryPosition.equals("left") ? "right" : "left";
                time += testCase.t;
            } else if (nextCar < testCase.m) {
                time = testCase.cars[nextCar].arrivalTime;
            }

            //unload ferry
            while(!onFerry.isEmpty()) {
                int carIndex = onFerry.poll();
                results[carIndex] = time;
            }
        }

        for (int result : results) {
            System.out.println(result);
        }
    }
}

class TestCase {
    int n;
    int t;
    int m;
    Car[] cars;
}

class Car {
    int arrivalTime;
    String bank;
}