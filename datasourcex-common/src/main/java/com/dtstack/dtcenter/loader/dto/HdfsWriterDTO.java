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

package com.dtstack.dtcenter.loader.dto;

import com.dtstack.rpc.annotation.FileUpload;
import lombok.Data;

import java.util.List;

/**
 *
 * 写入hdfs的参数列表
 *
 * @author ：wangchuan
 * date：Created in 下午2:38 2020/8/11
 * company: www.dtstack.com
 */
@Data
public class HdfsWriterDTO {

    private String hdfsDirPath;

    private String fromLineDelimiter;

    private String toLineDelimiter;

    @FileUpload
    private String fromFileName;

    private String oriCharSet;

    private Integer startLine;

    private Boolean topLineIsTitle;

    private List<ColumnMetaDTO> columnsList;

    private List<HDFSImportColumn> keyList;

    private String fileFormat;

    /**
     * 是否设置默认值
     */
    private Boolean setDefault;

}
