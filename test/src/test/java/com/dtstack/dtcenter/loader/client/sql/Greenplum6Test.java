package com.dtstack.dtcenter.loader.client.sql;

import com.dtstack.dtcenter.loader.IDownloader;
import com.dtstack.dtcenter.loader.cache.pool.config.PoolConfig;
import com.dtstack.dtcenter.loader.client.BaseTest;
import com.dtstack.dtcenter.loader.client.ClientCache;
import com.dtstack.dtcenter.loader.client.IClient;
import com.dtstack.dtcenter.loader.dto.ColumnMetaDTO;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.Greenplum6SourceDTO;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 03:41 2020/2/29
 * @Description：MySQL 5 测试
 */
public class Greenplum6Test extends BaseTest {

    // 获取数据源 client
    private static final IClient client = ClientCache.getClient(DataSourceType.GREENPLUM6.getVal());

    private static final Greenplum6SourceDTO source = Greenplum6SourceDTO.builder()
            .url("jdbc:pivotal:greenplum://172.16.100.186:5432;DatabaseName=postgres")
            .username("gpadmin")
            .password("gpadmin")
            .schema("public")
            .poolConfig(new PoolConfig())
            .build();

    /**
     * 数据准备
     */
    @BeforeClass
    public static void beforeClass() {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().sql("drop table if exists loader_test").build();
        client.executeSqlWithoutResultSet(source, queryDTO);
        queryDTO = SqlQueryDTO.builder().sql("create table loader_test (id int, name text)").build();
        client.executeSqlWithoutResultSet(source, queryDTO);
        queryDTO = SqlQueryDTO.builder().sql("comment on column loader_test.id is 'id'").build();
        client.executeSqlWithoutResultSet(source, queryDTO);
        queryDTO = SqlQueryDTO.builder().sql("comment on column loader_test.name is '名字'").build();
        client.executeSqlWithoutResultSet(source, queryDTO);
        queryDTO = SqlQueryDTO.builder().sql("comment on table loader_test is '中文_table_comment'").build();
        client.executeSqlWithoutResultSet(source, queryDTO);
        queryDTO = SqlQueryDTO.builder().sql("insert into loader_test values (1, 'nanqi')").build();
        client.executeSqlWithoutResultSet(source, queryDTO);
    }


    /**
     * 获取连接测试
     */
    @Test
    public void getCon() throws Exception{
        Connection connection = client.getCon(source);
        Assert.assertNotNull(connection);
        connection.close();
    }

    /**
     * 测试连通性测试
     */
    @Test
    public void testCon()  {
        Boolean isConnected = client.testCon(source);
        if (Boolean.FALSE.equals(isConnected)) {
            throw new DtLoaderException("connection exception");
        }
    }

    /**
     * 执行查询语句测试
     */
    @Test
    public void executeQuery()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().sql("select count(1) from loader_test").build();
        List<Map<String, Object>> result = client.executeQuery(source, queryDTO);
        Assert.assertTrue(CollectionUtils.isNotEmpty(result));
    }

    @Test(expected = DtLoaderException.class)
    public void isDatabaseExists()  {
        Boolean result = client.isDatabaseExists(source, "public");
        Assert.assertTrue(result);
        Boolean result1 = client.isDatabaseExists(source, null);
    }

    @Test(expected = DtLoaderException.class)
    public void isTableExistsInDatabase()  {
        Boolean result = client.isTableExistsInDatabase(source, "loader_test", "public");
        Assert.assertTrue(result);
        Boolean result1 = client.isTableExistsInDatabase(source, null,null);
    }

    /**
     * 字段别名查询测试
     */
    @Test
    public void executeQueryAlias()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().sql("select id as tAlias from loader_test").build();
        List<Map<String, Object>> result = client.executeQuery(source, queryDTO);
        Assert.assertTrue(CollectionUtils.isNotEmpty(result));
    }

    /**
     * 无结果查询测试
     */
    @Test
    public void executeSqlWithoutResultSet()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().sql("select count(1) from loader_test").build();
        client.executeSqlWithoutResultSet(source, queryDTO);
    }

    /**
     * 获取表
     */
    @Test
    public void getTableList()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().build();
        List<String> tableList = client.getTableList(source, queryDTO);
        Assert.assertTrue(CollectionUtils.isNotEmpty(tableList));
    }

    /**
     * 获取java 标准字段属性
     */
    @Test
    public void getColumnClassInfo()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().tableName("loader_test").build();
        List<String> columnClassInfo = client.getColumnClassInfo(source, queryDTO);
        Assert.assertEquals("java.lang.Integer", columnClassInfo.get(0));
        Assert.assertEquals("java.lang.String", columnClassInfo.get(1));
        Assert.assertTrue(CollectionUtils.isNotEmpty(columnClassInfo));
    }

    /**
     * 获取表字段详细信息
     */
    @Test
    public void getColumnMetaData()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().tableName("loader_test").build();
        List<ColumnMetaDTO> columnMetaData = client.getColumnMetaData(source, queryDTO);
        Assert.assertEquals("INTEGER", columnMetaData.get(0).getType());
        Assert.assertEquals("TEXT", columnMetaData.get(1).getType());
    }

    /**
     * 获取表注释
     */
    @Test
    public void getTableMetaComment()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().schema("public").tableName("loader_test").build();
        String metaComment = client.getTableMetaComment(source, queryDTO);
        Assert.assertEquals("中文_table_comment", metaComment);
    }

    @Test(expected = DtLoaderException.class)
    public void getPartitionColumn() {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().schema("public").tableName("loader_test").build();
        client.getPartitionColumn(source, queryDTO);
    }

    /**
     * 数据预览测试
     */
    @Test
    public void preview() {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().tableName("loader_test").previewNum(1).build();
        List preview = client.getPreview(source, queryDTO);
        Assert.assertTrue(CollectionUtils.isNotEmpty(preview));
    }

    /**
     * 指定sql downloader下载 测试
     */
    @Test
    public void downloader()throws Exception {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().sql("select * from loader_test").build();
        IDownloader downloader = client.getDownloader(source, queryDTO);
        List<String> metaInfo = downloader.getMetaInfo();
        Assert.assertTrue(CollectionUtils.isNotEmpty(metaInfo));
        while (!downloader.reachedEnd()){
            List<List<String>> result = (List<List<String>>)downloader.readNext();
            for (List<String> row : result){
                Assert.assertTrue(CollectionUtils.isNotEmpty(row));
            }
        }
        Assert.assertTrue( StringUtils.isEmpty(downloader.getFileName()));
        Assert.assertTrue(CollectionUtils.isEmpty(downloader.getContainers()));
        downloader.close();
    }

    /**
     * 根据sql 获取对应结果的字段信息
     */
    @Test
    public void getColumnMetaDataWithSql() {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().sql("select * from loader_test ").build();
        List result = client.getColumnMetaDataWithSql(source, queryDTO);
        Assert.assertTrue(CollectionUtils.isNotEmpty(result));
    }

    /**
     * 获取所有的schema
     */
    @Test
    public void getAllDatabases()  {
        SqlQueryDTO queryDTO = SqlQueryDTO.builder().build();
        List databases = client.getAllDatabases(source, queryDTO);
        Assert.assertTrue(CollectionUtils.isNotEmpty(databases));
    }

    /**
     * 获取当前使用的库
     */
    @Test
    public void getCurrentDatabases()  {
        String currentDatabase = client.getCurrentDatabase(source);
        Assert.assertTrue(StringUtils.isNotBlank(currentDatabase));
    }
}
