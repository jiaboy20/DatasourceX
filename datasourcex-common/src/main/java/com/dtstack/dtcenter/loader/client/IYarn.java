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
import com.dtstack.dtcenter.loader.dto.yarn.YarnApplicationInfoDTO;
import com.dtstack.dtcenter.loader.dto.yarn.YarnApplicationStatus;
import com.dtstack.dtcenter.loader.dto.yarn.YarnResourceDTO;
import com.dtstack.dtcenter.loader.dto.yarn.YarnResourceDescriptionDTO;
import com.dtstack.rpc.annotation.RpcNodeSign;
import com.dtstack.rpc.annotation.RpcService;
import com.dtstack.rpc.enums.RpcRemoteType;

import java.util.List;

/**
 * yarn client interface
 *
 * @author ：wangchuan
 * date：Created in 下午1:50 2022/3/15
 * company: www.dtstack.com
 */
@RpcService(rpcRemoteType = RpcRemoteType.DATASOURCEX_CLIENT)
public interface IYarn {

    /**
     * 获取 yarn 上的任务信息
     *
     * @param source        数据源信息
     * @param status        任务状态, 不可为空, 状态 {@link YarnApplicationStatus}
     * @param applicationId 任务 appId, 可为空
     * @param taskName      任务名称, 只会查询以 _taskName 结尾的 application
     * @return yarn app list
     */
    List<YarnApplicationInfoDTO> listApplication(@RpcNodeSign("tenantId") ISourceDTO source, YarnApplicationStatus status,
                                                 String taskName, String applicationId);

    /**
     * 获取 yarn 资源信息
     *
     * @param source 数据源信息
     * @return yarn 资源信息
     */
    YarnResourceDTO getYarnResource(@RpcNodeSign("tenantId") ISourceDTO source);

    /**
     * 获取 yarn 资源队列信息
     *
     * @param source 数据源信息
     * @return yarn 资源队列信息
     */
    YarnResourceDescriptionDTO getYarnResourceDescription(@RpcNodeSign("tenantId") ISourceDTO source);
}
