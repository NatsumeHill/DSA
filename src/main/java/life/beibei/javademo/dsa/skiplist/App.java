package life.beibei.javademo.dsa.skiplist;

public class App {
    public static void main(String[] args) {
        SkipListInterface<Integer, String> skipList = new SkipList<>();
        skipList.put(5, "hello");
        skipList.put(2, "wow!");
        skipList.put(6, "kiki");
        skipList.put(20, "dodo");
        skipList.put(10, "beibei");
        skipList.put(11, "zhangzhang");
        skipList.put(21, "jiojio");
        skipList.remove(2);
        System.out.println(skipList);
    }
}
