package org.example.solutions;

import org.example.helper.Util;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 {

    List<Map<String, Integer>> listOfParts;
    Map<String, String> workflows;
    Pattern digits = Pattern.compile("\\d+");

    private class Node {
        String label;
        String comparison;
        Node prev;

        Node(String label) {
            this.label = label;
            this.prev = null;
        }
    }


    public Day19() {
        List<String> input = Util.readAsListOfStrings("19.txt");
        workflows = new HashMap<>();
        listOfParts = new ArrayList<>();
        String line = input.getFirst();
        int i = 1;
        while (!line.isEmpty()) {
            String workflowName = line.split("\\{")[0];
            workflows.put(workflowName, line.split("\\{")[1].replace("}", ""));
            line = input.get(i);
            i++;
        }
        for (String s : input.subList(i, input.size())) {
            Map<String, Integer> map = new HashMap<>();
            Matcher digitsMatcher = digits.matcher(s);
            digitsMatcher.find();
            map.put("x", Integer.parseInt(digitsMatcher.group()));
            digitsMatcher.find();
            map.put("m", Integer.parseInt(digitsMatcher.group()));
            digitsMatcher.find();
            map.put("a", Integer.parseInt(digitsMatcher.group()));
            digitsMatcher.find();
            map.put("s", Integer.parseInt(digitsMatcher.group()));
            listOfParts.add(map);
        }
    }

    private record Range(int low, int high) {
    }

    public String getSolution() {
//        int sum = 0;
//        for (Map<String, Integer> parts : listOfParts) {
//            if (applyWorkflows("in", parts))
//                sum += parts.values().stream().reduce(0, Integer::sum);
//        }
//        return sum;
        BigInteger sum = BigInteger.ZERO;
        getAllPathsToAccept(new Node("in"));
        for (List<Node> path : paths) {
            Map<Character, Range> rangeMap = new HashMap<>();
            rangeMap.put('x', new Range(0, 4000));
            rangeMap.put('m', new Range(0, 4000));
            rangeMap.put('a', new Range(0, 4000));
            rangeMap.put('s', new Range(0, 4000));
            for (Node n : path) {
                String comp = n.comparison;
                if (comp == null)
                    continue;
                for (String comparison : comp.split("&&")) {
                    Range tmp = rangeMap.get(comparison.charAt(0));
                    Matcher digitMatcher = digits.matcher(comparison);
                    digitMatcher.find();
                    int value = Integer.parseInt(digitMatcher.group());
                    if (comparison.contains(">"))
                        tmp = new Range(Math.max(tmp.low + 1, value), tmp.high);
                    else
                        tmp = new Range(tmp.low, Math.min(value - 1, tmp.high));
                    rangeMap.put(comparison.charAt(0), tmp);
                }
            }
            BigInteger partial = BigInteger.ONE;
            for (Range r : rangeMap.values()) {
                partial = partial.multiply(BigInteger.valueOf((r.high-r.low)));
            }
            sum = sum.add(partial);

        }
        return sum.toString();
    }

    Set<List<Node>> paths = new HashSet<>();

    private void getAllPathsToAccept(Node node) {
        if (node.label.equals("R")) {
            return;
        }
        if (node.label.equals("A")) {
            paths.add(followBack(node));
            return;
        }
        String[] options = workflows.get(node.label).split(",");
        options = formatOptions(options);
        for (String option : options) {
            String[] parts = option.split(":");
            Node newNode;
            if (parts.length > 1) {
                newNode = new Node(parts[1]);
                newNode.comparison = parts[0];
            } else
                newNode = new Node(parts[0]);
            newNode.prev = node;
            getAllPathsToAccept(newNode);
        }
    }

    private String[] formatOptions(String[] options) {
        List<String> complements = new ArrayList<>();
        List<String> newOptions = new ArrayList<>();
        for (String option : options) {
            String[] parts = option.split(":");

            if (!complements.isEmpty()) {
                if (parts.length > 1)
                    newOptions.add(String.join("&&", complements) + "&&" + parts[0] + ":" + parts[1]);
                else
                    newOptions.add(String.join("&&", complements) + ":" + parts[0]);
            } else
                newOptions.add(option);

            if (parts.length > 1) {
                if (parts[0].contains("<")) {
                    String var = parts[0].split("<")[0];
                    int val = Integer.parseInt(parts[0].split("<")[1]);
                    complements.add(var + ">" + (val - 1));
                }
                if (parts[0].contains(">")) {
                    String var = parts[0].split(">")[0];
                    int val = Integer.parseInt(parts[0].split(">")[1]);
                    complements.add(var + "<" + (val + 1));
                }
            }
        }
        return newOptions.toArray(new String[0]);
    }

    private List<Node> followBack(Node node) {
        List<Node> list = new ArrayList<>();
        while (node != null) {
            list.add(node);
            node = node.prev;
        }
        Collections.reverse(list);
        return list;
    }

    private boolean applyWorkflows(String workflowId, Map<String, Integer> parts) {
        if (workflowId.equals("R"))
            return false;
        else if (workflowId.equals("A"))
            return true;
        else {
            String[] workflow = workflows.get(workflowId).split(",");
            for (String s : workflow) {
                if (s.contains(">")) {
                    if (greaterThan(s.split(">"), parts))
                        return applyWorkflows(s.split(">")[1].split(":")[1], parts);
                } else if (s.contains("<")) {
                    if (lessThan(s.split("<"), parts))
                        return applyWorkflows(s.split("<")[1].split(":")[1], parts);
                } else
                    return applyWorkflows(s, parts);
            }
        }
        return false;
    }

    private boolean lessThan(String[] strings, Map<String, Integer> parts) {
        return parts.get(strings[0]) < Integer.parseInt(strings[1].split(":")[0]);
    }

    private boolean greaterThan(String[] strings, Map<String, Integer> parts) {
        return parts.get(strings[0]) > Integer.parseInt(strings[1].split(":")[0]);
    }
}
