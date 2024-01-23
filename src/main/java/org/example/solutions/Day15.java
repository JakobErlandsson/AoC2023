package org.example.solutions;

import org.example.helper.Util;

import java.util.*;

public class Day15 implements Solution {

    List<String> input;
    private static final int MULTIPLE = 17;
    private static final int MODULO = 256;

    Map<Integer, List<Lens>> boxes;

    private record Lens(String label, Integer focalLength){}

    public Day15() {
        String s = Util.readAsString("15.txt");
        input = Arrays.stream(s.split(",")).toList();
        populateBoxes();
    }

    private Integer hash(String s) {
        int sum = 0;
        for (Character c : s.toCharArray()) {
            sum += c;
            sum *= MULTIPLE;
            sum %= MODULO;
        }
        return sum;
    }

    @Override
    public String part1() {
        return input.stream().map(this::hash).reduce(0, Integer::sum).toString();
    }

    private void addTo(Integer box, Lens lens) {
        List<Lens> lenses;
        if (boxes.containsKey(box))
            lenses = boxes.get(box);
        else
            lenses = new ArrayList<>();
        for (Lens l : lenses) {
            if (l.label.equals(lens.label)) {
                lenses.set(lenses.indexOf(l), lens);
                boxes.put(box, lenses);
                return;
            }
        }
        lenses.add(lens);
        boxes.put(box, lenses);
    }

    private void removeFrom(Integer box, String lens) {
        List<Lens> lenses = boxes.get(box);
        if (lenses != null) {
            for (Lens l : lenses) {
                if (l.label.equals(lens)) {
                    lenses.remove(l);
                    return;
                }

            }
        }
    }

    public String part2() {
        int sum = 0;
        for (Map.Entry<Integer, List<Lens>> entry : boxes.entrySet()) {
            int focalPower = 0;
            for (int i = 0; i < entry.getValue().size(); i++) {
                focalPower += (entry.getKey()+1) * (i + 1) * entry.getValue().get(i).focalLength;
            }
            sum += focalPower;
        }
        return sum + "";
    }

    private void populateBoxes() {
        boxes = new HashMap<>();
        for (String s : input) {
            String[] split = s.split("[-=]");
            String label = split[0];
            int boxNumber = hash(label);
            if (split.length > 1) {
                int focalLength = Integer.parseInt(split[1]);
                addTo(boxNumber, new Lens(label, focalLength));
            } else
                removeFrom(boxNumber, label);
        }
    }
}
