package org.example.solutions;

import org.example.helper.Util;

import java.math.BigInteger;
import java.util.*;

public class Day20 implements Solution {

    private final List<String> input;

    private Map<Module, List<String>> modules;
    private Map<String, Module> moduleNames;

    private enum Type {
        CONJUNCTION,
        FLIP_FLOP,
        NO_TYPE
    }

    private enum PulseType {
        LOW,
        HIGH
    }

    private record Pulse(Module caller, PulseType pulseType, Module module) {
    }

    private record ModulePair(String caller, PulseType type, String receiver) {
    }

    private record Score(long low, long high) {}

    private final Map<String, Long> firstHigh = new HashMap<>();

    private class Module {
        String name;
        Type type;
        Boolean on;
        Map<Module, PulseType> callers;
        PulseType incoming;

        public Module(String name, Type type) {
            this.name = name;
            this.type = type;
            if (this.type == Type.FLIP_FLOP)
                on = false;
            if (this.type == Type.CONJUNCTION)
                callers = new HashMap<>();
        }
    }

    public Day20() {
        input = Util.readAsListOfStrings("20.txt");
        firstHigh.put("rf", 0L);
        firstHigh.put("vq", 0L);
        firstHigh.put("sn", 0L);
        firstHigh.put("sr",0L);
    }

    private void buildMap() {
        modules = new HashMap<>();
        moduleNames = new HashMap<>();
        for (String line : input) {
            String[] tmp = line.split(" -> ");
            Module module = switch (tmp[0].charAt(0)) {
                case '&' -> new Module(tmp[0].substring(1), Type.CONJUNCTION);
                case '%' -> new Module(tmp[0].substring(1), Type.FLIP_FLOP);
                default -> new Module(tmp[0], Type.NO_TYPE);
            };
            modules.put(module, Arrays.stream(tmp[1].split(", ")).toList());
            moduleNames.put(module.name, module);
        }
        Module broadCaster = moduleNames.get("broadcaster");
        broadCaster.incoming = PulseType.LOW;
        moduleNames.put("broadcaster", broadCaster);
        configureConjunctionModules();
    }

    private Score sendPulse(String toCheck) {
        long low = 0;
        long high = 0;
        Queue<ModulePair> queue = new ArrayDeque<>();
        queue.add(new ModulePair(null, PulseType.LOW, "broadcaster"));
        while (!queue.isEmpty()) {
            ModulePair m = queue.poll();
            Module receiver = moduleNames.get(m.receiver);
            if (m.type == PulseType.HIGH)
                high++;
            else
                low++;
            List<String> toSendTo = modules.get(receiver);
            PulseType pt;
            if (receiver.type == Type.NO_TYPE) {
                pt = receiver.incoming;
            } else if (receiver.type == Type.FLIP_FLOP) {
                if (m.type == PulseType.LOW) {
                    if (receiver.on) {
                        receiver.on = false;
                        pt = PulseType.LOW;
                    } else {
                        receiver.on = true;
                        pt = PulseType.HIGH;
                    }
                } else {
                    toSendTo = Collections.emptyList();
                    pt = null; // Doesn't matter since no pulse will be sent
                }

            } else { // Conjunction type
                receiver.callers.put(moduleNames.get(m.caller), receiver.incoming);
                if (receiver.callers.values().stream().allMatch(p -> p == PulseType.HIGH))
                    pt = PulseType.LOW;
                else
                    pt = PulseType.HIGH;
            }

            if (receiver.name.equals(toCheck) && pt == PulseType.HIGH)
                return new Score(-1, -1);


            modules.put(receiver, modules.get(receiver));
            moduleNames.put(receiver.name, receiver);

            for (String name : toSendTo) {
                if (moduleNames.containsKey(name)) {
                    Module mod = moduleNames.get(name);
                    mod.incoming = pt;
                    queue.add(new ModulePair(m.receiver, pt, mod.name));
                }
                else {
                    if (pt == PulseType.HIGH)
                        high++;
                    else
                        low++;
                }

            }
        }
        return new Score(low, high);
    }

    private void configureConjunctionModules() {
        for (Map.Entry<Module, List<String>> entry : modules.entrySet()) {
            for (String name : entry.getValue()) {
                if (moduleNames.containsKey(name) && moduleNames.get(name).type == Type.CONJUNCTION) {
                    moduleNames.get(name).callers.put(entry.getKey(), PulseType.LOW);
                }
            }
        }
    }


    @Override
    public String part1() {
        buildMap();
        long low = 0;
        long high = 0;
        for (int i = 0; i < 1000; i++) {
            Score score = sendPulse("");
            low += score.low;
            high += score.high;
        }
        BigInteger result = BigInteger.valueOf(low).multiply(BigInteger.valueOf(high));
        return result.toString();
    }

    @Override
    public String part2() {
        buildMap();
        for (String con : List.of("rf", "vq", "sn", "sr")) {
            Long presses = 0L;
            while (true) {
                presses++;
                Score score = sendPulse(con);
                if (score.low == -1) {
                    System.out.println(presses);
                    break;
                }
            }
        }
        return "";

    }

}
