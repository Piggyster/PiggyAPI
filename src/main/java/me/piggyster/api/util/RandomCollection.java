package me.piggyster.api.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class RandomCollection<E> {

    private final TreeMap<Double, List<E>> weights;
    private TreeMap<Double, List<E>> map;
    private SecureRandom random;
    private double total;

    public RandomCollection() {
        this(new SecureRandom());
    }

    public RandomCollection(final SecureRandom random) {
        this.map = new TreeMap<>();
        this.weights = new TreeMap<>();
        this.total = 0.0;
        this.random = random;
    }

    public void addAll(Map<E, Double> weights) {
        weights.forEach((result, weight) -> add(weight, result));
    }

    public void add(final double weight, final E result) {
        if (weight <= 0.0) {
            return;
        }

        total += weight;

        if (map.containsKey(total)) {
            map.get(total).add(result);
        } else {
            List<E> list = new ArrayList<>();
            list.add(result);
            map.put(total, list);
        }

        if (weights.containsKey(weight)) {
            weights.get(weight).add(result);
        } else {
            List<E> list = new ArrayList<>();
            list.add(result);
            weights.put(weight, list);
        }
    }

    public E next() {
        double value = random.nextDouble() * total;
        List<E> possible = map.ceilingEntry(value).getValue();

        if (possible.size() == 1)
            return possible.get(0);
        else
            return possible.get(ThreadLocalRandom.current().nextInt(possible.size()));
    }

    public void destroy() {
        random = null;
        map.clear();
        map = null;
        total = 0.0;
    }

    public double getWeight(final E result) {
        return weights.entrySet().stream().filter(e -> e.getValue().contains(result)).findAny().map(Map.Entry::getKey).orElse(100.0);
    }

    public double allWeights() {
        return total;
    }

    public Stream<E> stream() {
        return map.values().stream().flatMap(List::stream);
    }

    public static <V> V randomItem(List<V> list) {
        int randomIndex = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(randomIndex);
    }

    public static <V> List<V> randomItems(List<V> list, int amount) {
        List<V> oldItems = new ArrayList<>(list);
        List<V> newItems = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            V item = randomItem(oldItems);
            newItems.add(item);
            oldItems.remove(item);
        }
        return newItems;
    }
}
