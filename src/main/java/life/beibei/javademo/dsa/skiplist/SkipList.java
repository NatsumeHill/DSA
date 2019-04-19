package life.beibei.javademo.dsa.skiplist;

import org.apache.commons.lang3.RandomUtils;

/**
 * 跳跃表简单实现
 *
 * @author fangkui
 */
public class SkipList<K extends Comparable<? super K>, V> implements SkipListInterface<K, V> {

    private static final int MAX_LEVEL = 16;

    private int level;
    private int nodeCount;
    /**
     * header 只使用forward列表进行查找,没有key,value
     */
    private Node<K, V> header;

    public SkipList() {
        header = new Node<>(null, null);
        header.forward = new Node[MAX_LEVEL];
    }

    /**
     * 跳表节点定义
     *
     * @param <K>
     * @param <V>
     */
    static class Node<K extends Comparable<? super K>, V> {
        final K key;
        V value;
        Node[] forward;
        int nodeLevel;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("node-->key:").append(key).append(",");
            stringBuilder.append("node-->value:").append(value).append("\n");
            for (int i = 0; i <= nodeLevel; ++i) {
                if (forward[i] != null) {
                    stringBuilder.append("forward[").append(i).append("],key:").append(forward[i].key)
                            .append(",value:").append(forward[i].value).append("\n");
                }
            }
            return stringBuilder.toString();
        }
    }

    /**
     * 添加元素
     *
     * @param key   元素键
     * @param value 元素值
     * @return V 之前的value,如果不存在,返回null
     */

    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        // 保存当插入新节点时,需要更新的forward
        Node[] toUpdate = new Node[MAX_LEVEL];
        Node<K, V> node = this.search(key, toUpdate);
        // 获取旧的value
        V oldValue = null;
        if (node != null && node.getKey().equals(key)) {
            oldValue = node.getValue();
        }
        // 随机生成新节点的level
        int nodeLevel = RandomUtils.nextInt(0, MAX_LEVEL);
        // 如果随机level大于当前SkipList的level
        if (nodeLevel > this.level) {
            // 层数增加
            nodeLevel = ++this.level;
            // header也需要更新
            toUpdate[nodeLevel] = this.header;
        }
        // 创建新的节点
        Node<K, V> newNode = createNode(nodeLevel, key, value);
        // 调整forward指针
        for (int i = nodeLevel; i >= 0; --i) {
            node = toUpdate[i];
            newNode.forward[i] = node.forward[i];
            // 只更新前置节点被检索到的forward
            node.forward[i] = newNode;
        }
        ++this.nodeCount;
        return oldValue;
    }

    /**
     * @param level 节点层数
     * @param key   节点排序key
     * @param value 节点值
     * @return 节点实例
     */
    private Node<K, V> createNode(int level, K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        node.forward = new Node[level + 1];
        node.nodeLevel = level;
        return node;
    }

    /**
     * @param level 节点层数
     * @return 节点实例
     */
    private Node<K, V> createNode(int level) {
        Node<K, V> node = new Node<>(null, null);
        node.forward = new Node[level + 1];
        node.nodeLevel = level;
        return node;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(K key) {
        Node<K, V> node = this.header;
        for (int i = this.level; i >= 0; --i) {
            while (node.forward[i] != null && node.forward[i].getKey().compareTo(key) < 0) {
                node = node.forward[i];
            }
        }
        node = node.forward[0];
        if (node != null && node.getKey().equals(key)) {
            return node.getValue();
        }
        return null;
    }

    @Override
    public V remove(K key) {
        // 保存当插入新节点时,需要更新的forward
        Node[] toUpdate = new Node[MAX_LEVEL];
        Node<K, V> node = search(key, toUpdate);
        // 没有找到
        if (node == null || !node.getKey().equals(key)) {
            return null;
        }
        // 更新forward
        for (int i = 0; i <=this.level; ++i) {
            if (toUpdate[i].forward[i] != node) {
                break;
            }
            toUpdate[i].forward[i] = node.forward[i];
        }
        // 头部节点forward指向为null,说明层数已经减少
        while (this.level > 0 && header.forward[level] == null) {
            --level;
        }
        return node.getValue();
    }

    /**
     * 根据key检索节点
     *
     * @param key    节点键值
     * @param update 检索过程中遇到的节点
     * @return 检索结果
     */
    @SuppressWarnings("unchecked")
    private Node<K, V> search(K key, Node[] update) {
        Node<K, V> node = this.header;
        // 从最上面一层,逐层往下搜索
        for (int i = this.level; i >= 0; --i) {
            while (node.forward[i] != null && node.forward[i].getKey().compareTo(key) < 0) {
                node = node.forward[i];
            }
            // 每一层被搜索到的最后一个节点都需要更新,
            // 由于后面更新时,是按照level从上倒下进行更新forward
            // 所以,只有被检索到的forward才会更新
            update[i] = node;
        }
        node = node.forward[0];
        return node;
    }

    @Override
    public int getCount() {
        return this.nodeCount;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Node<K, V> tmp = this.header;
        while (tmp.forward[0] != null) {
            tmp = tmp.forward[0];
            stringBuilder.append(tmp.toString());
            stringBuilder.append("-----------------------------\n");
        }
        if (stringBuilder.lastIndexOf("\n") >= 0) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("\n"));
        }
        return stringBuilder.toString();
    }
}
