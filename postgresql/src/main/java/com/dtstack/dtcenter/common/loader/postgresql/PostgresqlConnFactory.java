package com.dtstack.dtcenter.common.loader.postgresql;

import com.dtstack.dtcenter.common.loader.common.ConnFactory;
import com.dtstack.dtcenter.loader.source.DataBaseType;

/**
 * @company: www.dtstack.com
 * @Author ：Nanqi
 * @Date ：Created in 15:53 2020/1/7
 * @Description：
 */
public class PostgresqlConnFactory extends ConnFactory {
    public PostgresqlConnFactory() {
        this.driverName = DataBaseType.PostgreSQL.getDriverClassName();
    }
}
