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

package com.dtstack.dtcenter.loader.dto.tsdb;

import lombok.Builder;
import lombok.Data;

/**
 * 过滤器
 *
 * @author ：wangchuan
 * date：Created in 上午10:20 2021/6/24
 * company: www.dtstack.com
 */
@Data
@Builder
public class Filter {

    private FilterType type;

    private String tagk;

    private String filter;

    @Builder.Default
    private Boolean groupBy = false;
}
