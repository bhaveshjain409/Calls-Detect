import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DetectCalls {
    public static void main(String[] args) {
        //file Name that passed through main for computing
        String filePath = args[0];

        //Map for computing the frequency of each seconds later
        Map<Integer, Integer> secondsCount = new HashMap<>();
        // To store uniques seconds
        Set<Integer> seconds = new TreeSet<>();

        // Using Stream instead of other reader or buffer
        try (Stream<String> lines = java.nio.file.Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {

                // Returns values start & end of each line
                String[] timestamps = line.split(":");
                int start = Integer.parseInt(timestamps[0]);
                int end = Integer.parseInt(timestamps[1]);

                while (end > start) {
                    seconds.add(start);
                    secondsCount.compute(start++, (k, currentValue) -> currentValue == null ? 1 : currentValue + 1);
                }
            });
        } catch (Exception e) {
            System.out.println("Failed to read file .. !" + e.getMessage());
            //   e.printStackTrace();
        }
        //Return the map's max values
        int max = secondsCount.values().stream().max(Comparator.naturalOrder()).get();

        // Gives the sets of Map-Keys that has highest value & sort them
        TreeSet<Integer> startEndSecond = seconds.stream().filter(a -> secondsCount.get(a).equals(max)).collect(Collectors.toCollection(TreeSet::new));
        System.out.printf("The peak for this call log is  %d simultaneous calls, that occurred between  %d  and  %d", max, startEndSecond.first(), startEndSecond.last());

    }
}