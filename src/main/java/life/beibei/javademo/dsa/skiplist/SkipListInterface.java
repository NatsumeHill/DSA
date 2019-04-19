package life.beibei.javademo.dsa.skiplist;

/**
 * 跳跃表接口
 *
 * @author fangkui
 */
public interface SkipListInterface<K extends Comparable<? super K>, V> {
    /**
     * 添加元素到表
     *
     * @param key   元素键
     * @param value 元素值
     * @return 之前的value, 如果不存在, 返回null
     */
    V put(K key, V value);

    /**
     * 根据键值查找元素
     *
     * @param key 键值
     * @return 存在返回value, 不存在返回null
     */
    V get(K key);

    /**
     * 根据键值删除元素
     *
     * @param key 键值
     * @return 存在且删除成功返回元素value, 不存在或者删除失败返回null
     */
    V remove(K key);

    /**
     * 返回元素个数
     *
     * @return 元素个数
     */
    int getCount();

    /**
     * 返回最大层数
     *
     * @return 最大层数
     */
    int getLevel();
}
