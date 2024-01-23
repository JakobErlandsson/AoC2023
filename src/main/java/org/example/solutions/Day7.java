package org.example.solutions;

import org.example.helper.Util;

import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class Day7 implements Solution {

    String part;

    private enum Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }
    private class Hand implements Comparable<Hand> {
        List<Integer> cards;
        Integer bid;
        Type type;

        public Hand(List<Integer> cards, Integer bid, Type type) {
            this.cards = cards;
            this.bid = bid;
            this.type = type;
        }

        @Override
        public int compareTo(Hand o) {
            if (!this.type.equals(o.type))
                return this.type.ordinal() - o.type.ordinal();
            for (int i = 0; i < this.cards.size(); i++) {
                if (!this.cards.get(i).equals(o.cards.get(i)))
                    return this.cards.get(i) - o.cards.get(i);
            }
            return 0;
        }
    }

    private Type calculateTypeV2(List<Integer> cards) {
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).equals(1))
                indexList.add(i);
        }
        if (indexList.isEmpty())
            return calculateType(cards);
        else {
            List<Hand> variations = new ArrayList<>();
            for (int i = 2; i <= 14; i++) {
                Integer[] newCards = cards.toArray(new Integer[0]);
                for (Integer index : indexList)
                    newCards[index] = i;
                variations.add(new Hand(Arrays.stream(newCards).toList(), 0, calculateType(Arrays.stream(newCards).toList())));
            }
            Collections.sort(variations);
            return variations.getLast().type;
        }
    }

    private Type calculateType(List<Integer> cards) {
        // Group cards by same card
        Map<Integer, List<Integer>> groupByCard = cards.stream()
                .collect(groupingBy(Integer::intValue));
        List<Integer> lengths = new ArrayList<>();
        // Find sizes of groups of cards
        for (Map.Entry<Integer, List<Integer>> entry : groupByCard.entrySet())
            lengths.add(entry.getValue().size());

        return switch (groupByCard.entrySet().size()) {
            case 1 -> Type.FIVE_OF_A_KIND;
            case 2 -> {
                if (lengths.stream().anyMatch(i -> i.equals(4)))
                    yield Type.FOUR_OF_A_KIND;
                else
                    yield Type.FULL_HOUSE;
            }
            case 3 -> {
                if (lengths.stream().anyMatch(i -> i.equals(3)))
                    yield Type.THREE_OF_A_KIND;
                else
                    yield Type.TWO_PAIR;
            }
            case 4 -> Type.ONE_PAIR;
            default -> Type.HIGH_CARD;
        };
    }

    private List<Integer> convert(String hand) {
        List<Integer> arr = new ArrayList<>(5);
        for (char c : hand.toCharArray()) {
            if (Character.isDigit(c))
                arr.add(Character.getNumericValue(c));
            else {
                arr.add(switch (c) {
                    case 'T': yield 10;
                    case 'J': {
                        if (part.equals("part1"))
                            yield 11;
                        else
                            yield 1;
                    }
                    case 'Q': yield 12;
                    case 'K': yield 13;
                    case 'A': yield 14;
                    default: yield 0;
                });
            }
        }
        return arr;
    }

    List<String> input;
    List<Hand> hands;

    public Day7() {
        input = Util.readAsListOfStrings("7.txt");
    }

    private void buildHands() {
        hands = new ArrayList<>();
        for (String line : input) {
            List<Integer> cards = convert(line.split(" ")[0]);
            Type type = part.equals("part1") ? calculateType(cards) : calculateTypeV2(cards);
            hands.add(new Hand(cards, Integer.parseInt(line.split(" ")[1]), type));
        }
        Collections.sort(hands);
    }

    @Override
    public String part1() {
        part = "part1";
        return getSolution().toString();
    }

    @Override
    public String part2() {
        part = "part2";
        return getSolution().toString();
    }

    private Integer getSolution() {
        buildHands();
        int sum = 0;
        int rank = 1;
        for (Hand hand : hands) {
            sum += hand.bid * rank;
            rank ++;
        }
        return sum;
    }
}
