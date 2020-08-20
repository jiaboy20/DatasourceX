package com.dtstack.dtcenter.loader.cache.connection;

import com.dtstack.dtcenter.common.thread.RdosThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 13:49 2020/3/4
 * @Description：缓存超时处理中心
 */
@Slf4j
public class HashCacheConnectionKey {
    private static final Map<String, DataSourceConnection> SESSION_CONN_MAP = new ConcurrentHashMap<>();

    private static final ScheduledExecutorService SCHEDULED_THREADPOOL_EXECUTOR = new ScheduledThreadPoolExecutor(1,
            new RdosThreadFactory("hashCacheConnectionKey"));

    static {
        SCHEDULED_THREADPOOL_EXECUTOR.scheduleAtFixedRate(new HashCacheConnectionKey.CacheTimerTask(), 0, 10,
                TimeUnit.SECONDS);
    }

    public static List<String> getSimilarSessionKey(String similarKey) {
        return SESSION_CONN_MAP.keySet().stream().filter(key -> key.startsWith(similarKey)).collect(Collectors.toList());
    }

    static class CacheTimerTask implements Runnable {
        @Override
        public void run() {
            Iterator<String> iterator = SESSION_CONN_MAP.keySet().iterator();
            while (iterator.hasNext()) {
                clearKey(iterator.next());
            }
        }
    }

    /**
     * 新增缓存
     *
     * @param sessionKey
     * @param cacheNode
     */
    public static void addKey(String sessionKey, DataSourceConnection cacheNode) {
        SESSION_CONN_MAP.put(sessionKey, cacheNode);
    }

    /**
     * 判断是否存在
     *
     * @param sessionKey
     * @return
     */
    public static Boolean isContainSessionKey(String sessionKey) {
        return SESSION_CONN_MAP.containsKey(sessionKey);
    }

    /**
     * 获取缓存连接信息
     *
     * @param sessionKey
     * @return
     */
    @Nullable
    public static DataSourceConnection getSourceConnection(String sessionKey) {
        return SESSION_CONN_MAP.get(sessionKey);
    }

    public static void clearKey(String sessionKey) {
        clearKey(sessionKey, null, true);
    }

    /**
     * @param sessionKey
     * @param sourceType
     * @param isCheck    是否需要校验，false 则不校验，直接删除整个节点
     */
    public static void clearKey(String sessionKey, Integer sourceType, Boolean isCheck) {
        log.info("关闭连接 sessionKey: {} sourceType: {} isCheck: {}", sessionKey, sourceType, isCheck);
        DataSourceConnection dataNode = SESSION_CONN_MAP.get(sessionKey);
        if (dataNode == null) {
            SESSION_CONN_MAP.remove(sessionKey);
            return;
        }

        // 判断数据源时间是否超时，超时直接全部处理
        if (System.currentTimeMillis() > dataNode.getTimeoutStamp()) {
            dataNode.close();
            SESSION_CONN_MAP.remove(sessionKey);
        }

        // 如果数据源不存在
        // 如果 isCheck 为 false 则说明删除整个节点
        if (null == sourceType && !isCheck) {
            dataNode.close();
            SESSION_CONN_MAP.remove(sessionKey);
            return;
        }

        // 如果不需要判断且数据源存在，这通过节点关闭数据，如果需要判断已经在第一步判断掉了
        if (!isCheck) {
            dataNode.close(sourceType);
        }
    }
}
