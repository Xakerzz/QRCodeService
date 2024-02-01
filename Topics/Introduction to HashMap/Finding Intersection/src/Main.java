import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

class Main {
    private static void printCommon(int[] firstArray, int[] secondArray) {
        ArrayList<Integer> arrayNumbs = new ArrayList<>();
        for (int i = 0; i < firstArray.length; i++) {
            int temp1 = firstArray[i];
            int j = 0;
            for (; j < secondArray.length; j++) {
                int temp2 = secondArray[j];
                if (temp1 == temp2) {
                    if (!arrayNumbs.contains(temp1)) {
                        arrayNumbs.add(temp1);
                    }
                }

            }

        }
        Collections.sort(arrayNumbs);
        for (Integer n : arrayNumbs) {
            System.out.print(n + " ");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] firstArray = new int[n];
        int[] secondArray = new int[n];
        for (int i = 0; i < n; ++i) {
            firstArray[i] = scanner.nextInt();
        }
        for (int i = 0; i < n; ++i) {
            secondArray[i] = scanner.nextInt();
        }

        printCommon(firstArray, secondArray);
    }
}