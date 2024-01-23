package org.example.solutions;

import org.example.helper.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 implements Solution{
    List<Integer> times;
    List<Integer> records;
    Pattern digitsPattern = Pattern.compile("\\d+");

    public Day6() {
        List<String> input = Util.readAsListOfStrings("6.txt");
        times = new ArrayList<>();
        records = new ArrayList<>();
        Matcher matcherTime = digitsPattern.matcher(input.getFirst());
        Matcher matcherDistance = digitsPattern.matcher(input.getLast());
        while (matcherTime.find())
            times.add(Integer.parseInt(matcherTime.group()));
        while (matcherDistance.find())
            records.add(Integer.parseInt(matcherDistance.group()));
    }

    @Override
    public String part1() {
        Integer prod = 1;
        for (int j = 0; j < times.size(); j++) {
            int sum = 0;
            int time = times.get(j);
            for (int i = 0; i <= time; i++) {
                int timeRemaining = time - i;
                if (i * timeRemaining > records.get(j))
                    sum += 1;

            }
            prod *= sum;
        }
        return prod.toString();
    }

    @Override
    public String part2() {
        StringBuilder sb = new StringBuilder();
        for (Integer time : times)
            sb.append(time);
        long newTimeLimit = Long.parseLong(sb.toString());
        sb = new StringBuilder();
        for (Integer dist : records)
            sb.append(dist);
        long newRecord = Long.parseLong(sb.toString());
        long start = 0L;
        long end = newTimeLimit;
        for (long l = 0L; l <= newTimeLimit; l++) {
            long timeRemaining = newTimeLimit - l;
            if (timeRemaining * l > newRecord) {
                start = l;
                break;
            }
        }
        for (long l = newTimeLimit; l >= 0; l--) {
            long timeRemaining = newTimeLimit - l;
            if (timeRemaining * l > newRecord) {
                end = l;
                break;
            }
        }
        return end - start + 1 + "";
    }
}
