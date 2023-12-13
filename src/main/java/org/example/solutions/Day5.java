package org.example.solutions;

import org.example.helper.Util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {

    private record Range(Long start, Long end) {
    }

    private class SpecialMap {
        Map<Integer, Range> sourceRanges;
        Map<Integer, Range> destRanges;

        SpecialMap() {
            sourceRanges = new HashMap<>();
            destRanges = new HashMap<>();
        }

        Long getFromSource(Long source) {
            Integer id = isWithinSource(source);
            if (id != null) {
                Long diff = source - sourceRanges.get(id).start;
                return destRanges.get(id).start + diff;
            } else
                return source;

        }

        Long getFromDest(Long dest) {
            Integer id = isWithinDest(dest);
            if (id != null) {
                Long diff = dest - destRanges.get(id).start;
                return sourceRanges.get(id).start + diff;
            } else
                return dest;
        }

        private Integer isWithinSource(Long source) {
            for (Map.Entry<Integer, Range> entry : sourceRanges.entrySet()) {
                if (source >= entry.getValue().start && source <= entry.getValue().end) {
                    return entry.getKey();
                }
            }
            return null;
        }

        private Integer isWithinDest(Long dest) {
            for (Map.Entry<Integer, Range> entry : destRanges.entrySet()) {
                if (dest >= entry.getValue().start && dest <= entry.getValue().end) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }

    AtomicInteger counter;

    List<String> input;

    List<Long> seeds;
    List<Range> seedsV2;
    SpecialMap seedToSoil = new SpecialMap();
    SpecialMap soilToFertilizer = new SpecialMap();
    SpecialMap fertilizerToWater = new SpecialMap();
    SpecialMap waterToLight = new SpecialMap();
    SpecialMap lightToTemperature = new SpecialMap();
    SpecialMap temperatureToHumidity = new SpecialMap();
    SpecialMap humidityToLocation = new SpecialMap();

    Map<String, SpecialMap> descriptionToMapMap = Map.of(
            "seed-to-soil map:", seedToSoil,
            "soil-to-fertilizer map:", soilToFertilizer,
            "fertilizer-to-water map:", fertilizerToWater,
            "water-to-light map:", waterToLight,
            "light-to-temperature map:", lightToTemperature,
            "temperature-to-humidity map:", temperatureToHumidity,
            "humidity-to-location map:", humidityToLocation
    );

    Pattern digitsPattern = Pattern.compile("\\d+");

    public Day5() throws IOException {
        input = Util.readAsListOfStrings("5.txt");
        seeds = new ArrayList<>();
        seedsV2 = new ArrayList<>();
        Matcher digitsMatcher = digitsPattern.matcher(input.get(0));
        while (digitsMatcher.find())
            seeds.add(Long.parseLong(digitsMatcher.group()));
        for (int i = 0; i < seeds.size() - 1; i += 2) {
            seedsV2.add(new Range(seeds.get(i), seeds.get(i) + seeds.get(i + 1) - 1));
        }
        counter = new AtomicInteger();
        buildMaps();
    }

    private void buildMaps() { // Building them backwards
        SpecialMap currentMap = null;
        for (String line : input.subList(2, input.size())) {
            if (descriptionToMapMap.containsKey(line)) {
                currentMap = descriptionToMapMap.get(line);
            } else if (!line.isEmpty()) {
                Matcher digitsMatcher = digitsPattern.matcher(line);
                List<Long> numbers = new ArrayList<>();
                while (digitsMatcher.find())
                    numbers.add(Long.parseLong(digitsMatcher.group()));
                Long destEnd = numbers.get(0) + numbers.get(2) - 1;
                Long sourceEnd = numbers.get(1) + numbers.get(2) - 1;
                Integer id = counter.getAndIncrement();
                currentMap.destRanges.put(id, new Range(numbers.get(0), destEnd));
                currentMap.sourceRanges.put(id, new Range(numbers.get(1), sourceEnd));

            }
        }
    }

    public Long getSolution(String part) {
        long l = 0L;
        if (part.equals("part2")) {
            while (true) {
                Long humidity = humidityToLocation.getFromDest(l);
                Long temperature = temperatureToHumidity.getFromDest(humidity);
                Long light = lightToTemperature.getFromDest(temperature);
                Long water = waterToLight.getFromDest(light);
                Long fertilizer = fertilizerToWater.getFromDest(water);
                Long soil = soilToFertilizer.getFromDest(fertilizer);
                Long seed = seedToSoil.getFromDest(soil);
                if (contains(seed))
                    return l;
                l++;
            }
        }
        else {
            List<Long> locations = getSeedLocations();
            long smallest = locations.getFirst();
            for (Long loc : locations) {
                smallest = Math.min(smallest, loc);
            }
            return smallest;
        }
    }

    private boolean contains(Long seed) {
        for (Range r : seedsV2) {
            if (seed >= r.start && seed <= r.end)
                return true;
        }
        return false;
    }

    private List<Long> getSeedLocations() {
        List<Long> locations = new ArrayList<>();
        for (Long seed : seeds) {
            Long soil = seedToSoil.getFromSource(seed);
            Long fertilizer = soilToFertilizer.getFromSource(soil);
            Long water = fertilizerToWater.getFromSource(fertilizer);
            Long light = waterToLight.getFromSource(water);
            Long temperature = lightToTemperature.getFromSource(light);
            Long humidity = temperatureToHumidity.getFromSource(temperature);
            Long location = humidityToLocation.getFromSource(humidity);
            locations.add(location);
        }
        return locations;
    }


}
