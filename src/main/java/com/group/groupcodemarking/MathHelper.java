package com.group.groupcodemarking;

import java.util.Collections;
import java.util.List;

public class MathHelper {
    public static List<Integer> normaliseList(List<Integer> numbers, int minAllowed, int maxAllowed) {
        int max = Collections.max(numbers);
        int min = Collections.min(numbers);
        numbers.forEach(e -> {
            System.out.println(e);
            System.out.println((maxAllowed - minAllowed) * (e - min) / (max - min) + minAllowed);
            System.out.println("-----");
        });
        return null;
    }
}
