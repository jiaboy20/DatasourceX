package com.dtstack.dtcenter.loader.client.sql;

import com.dtstack.dtcenter.loader.IDownloader;
import com.dtstack.dtcenter.loader.client.ClientCache;
import com.dtstack.dtcenter.loader.client.IClient;
import com.dtstack.dtcenter.loader.client.IHdfsFile;
import com.dtstack.dtcenter.loader.dto.source.HdfsSourceDTO;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import org.junit.Test;


/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 23:58 2020/2/28
 * @Description：HDFS 测试
 */
public class HdfsTest {
    HdfsSourceDTO source = HdfsSourceDTO.builder()
            .defaultFS("hdfs://ns1")
            .config("{\n" +
                    "    \"dfs.ha.namenodes.ns1\": \"nn1,nn2\",\n" +
                    "    \"dfs.namenode.rpc-address.ns1.nn2\": \"kudu2:9000\",\n" +
                    "    \"dfs.client.failover.proxy.provider.ns1\": \"org.apache.hadoop.hdfs.server.namenode.ha" +
                    ".ConfiguredFailoverProxyProvider\",\n" +
                    "    \"dfs.namenode.rpc-address.ns1.nn1\": \"kudu1:9000\",\n" +
                    "    \"dfs.nameservices\": \"ns1\"\n" +
                    "}")
            .build();

    @Test
    public void testCon() throws Exception {
        IClient client = ClientCache.getClient(DataSourceType.HDFS.getVal());
        Boolean isConnected = client.testCon(source);
        if (Boolean.FALSE.equals(isConnected)) {
            throw new DtLoaderException("连接异常");
        }
    }

    @Test
    public void testFileDownloader() throws Exception {
        IHdfsFile hdfs = ClientCache.getHdfs(DataSourceType.HDFS.getVal());
        IDownloader fileDownloader = hdfs.getFileDownloader(source, "/tmp/textfile");
        while (!fileDownloader.reachedEnd()) {
            System.out.println(fileDownloader.readNext());
            Thread.sleep(1000);
        }
    }

    @Test
    public void getFileStatus() throws Exception {
        IHdfsFile hdfs = ClientCache.getHdfs(DataSourceType.HDFS.getVal());
        System.out.println(hdfs.getStatus(source, "/jars"));
    }
}
