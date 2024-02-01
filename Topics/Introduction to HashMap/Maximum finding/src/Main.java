import java.util.*;

class Main {
    private static void printMaxKey(HashMap<Integer, Integer> map) {
        int max = 0;
        for (Integer n : map.keySet()) {
            if (n > max) {
                max = n;
            }

        }
        System.out.println(map.get(max));
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; ++i) {
            map.put(scanner.nextInt(), scanner.nextInt());
        }

        printMaxKey(map);
    }
}