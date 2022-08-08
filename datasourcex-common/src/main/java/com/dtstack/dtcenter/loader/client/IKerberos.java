/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.dtcenter.loader.client;

import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import com.dtstack.rpc.annotation.FileDownload;
import com.dtstack.rpc.annotation.FileUpload;
import com.dtstack.rpc.annotation.RpcNodeSign;
import com.dtstack.rpc.annotation.RpcService;
import com.dtstack.rpc.enums.RpcRemoteType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * kerberos 操作类
 *
 * @author ：nanqi
 * date：Created in 下午8:19 2022/2/23
 * company: www.dtstack.com
 */
@RpcService(rpcRemoteType = RpcRemoteType.DATASOURCEX_CLIENT)
public interface IKerberos {

    /**
     * 从 ZIP 包中解压出 Kerberos 配置信息，返回只存储相对路径，在校验之前再做转化
     *
     * @param sourceDTO         数据源信息
     * @param zipLocation       压缩包文件路径
     * @param localKerberosPath 本地解压路径
     * @return 处理后的 kerberos 配置信息
     * @throws IOException 文件 io 异常
     */
    Map<String, Object> parseKerberosFromUpload(@RpcNodeSign("tenantId") ISourceDTO sourceDTO, @FileUpload String zipLocation, @FileDownload String localKerberosPath) throws IOException;

    /**
     * 从 JDBC URL 中获取 principal
     *
     * @param sourceDTO 数据源信息
     * @param url       jdbc url
     * @return principal
     */
    String getPrincipals(@RpcNodeSign("tenantId") ISourceDTO sourceDTO, String url);

    /**
     * 从 Kerberos 配置文件中获取 Principal
     *
     * @param sourceDTO      数据源信息
     * @param kerberosConfig kerberos 配置
     * @return 所有的 principal 账号
     */
    List<String> getPrincipals(@RpcNodeSign("tenantId") ISourceDTO sourceDTO, Map<String, Object> kerberosConfig);

    /**
     * 从本地文件夹路径解析出 kerberos 配置, 返回的是 server 的绝对路径, 直接将返回值放入其他方法的 kerberosConfig 即可
     *
     * @param kerberosDir 本地 kerberos 文件夹绝对路径
     * @return kerberos 配置
     */
    Map<String, Object> parseKerberosFromLocalDir(@RpcNodeSign("tenantId") ISourceDTO sourceDTO, @FileUpload String kerberosDir);

    /**
     * 校验 kerberos 认证是否通过
     *
     * @param kerberosConfig principalFile、java.security.krb5.conf 路径需要是本地绝对路径
     * @return 认证是否通过
     */
    Boolean authTest(@RpcNodeSign("tenantId") ISourceDTO sourceDTO, Map<String, Object> kerberosConfig);
}
