package life.beibei.javademo.dsa.queue;

import org.junit.Test;

import java.util.*;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.*;

public class PriorityQueueTest {
    @Test
    public void queueTest(){
        Queue<Integer> queue = new PriorityQueue<>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            int seed = random.nextInt(100);
            System.out.print(seed);
            System.out.print("\t");
            queue.add(seed);
        }
        System.out.println();
        while (!queue.isEmpty()){
            System.out.println(queue.poll());
        }

        Map<String, String> map = new LinkedHashMap<>();
        map.put("KiKi", "Cute");
        map.put("BeiBei", "Cute");
        map.put("ZZ", "Cute");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

}