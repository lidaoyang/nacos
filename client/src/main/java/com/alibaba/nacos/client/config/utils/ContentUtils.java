/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.client.config.utils;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigType;

import java.util.Properties;

import static com.alibaba.nacos.api.common.Constants.WORD_SEPARATOR;

/**
 * Content Util.
 *
 * @author Nacos
 */
public class ContentUtils {
    
    /**
     * Verify increment pub content.
     *
     * @param content content
     * @throws IllegalArgumentException if content is not valid
     */
    public static void verifyIncrementPubContent(String content) {
        
        if (content == null || content.length() == 0) {
            throw new IllegalArgumentException("发布/删除内容不能为空");
        }
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '\r' || c == '\n') {
                throw new IllegalArgumentException("发布/删除内容不能包含回车和换行");
            }
            if (c == Constants.WORD_SEPARATOR.charAt(0)) {
                throw new IllegalArgumentException("发布/删除内容不能包含(char)2");
            }
        }
    }
    
    public static String getContentIdentity(String content) {
        int index = content.indexOf(WORD_SEPARATOR);
        if (index == -1) {
            throw new IllegalArgumentException("内容没有包含分隔符");
        }
        return content.substring(0, index);
    }
    
    public static String getContent(String content) {
        int index = content.indexOf(WORD_SEPARATOR);
        if (index == -1) {
            throw new IllegalArgumentException("内容没有包含分隔符");
        }
        return content.substring(index + 1);
    }
    
    /**
     * Truncate content.
     *
     * @param content content
     * @return truncated content
     */
    public static String truncateContent(String content) {
        if (content == null) {
            return "";
        } else if (content.length() <= SHOW_CONTENT_SIZE) {
            return content;
        } else {
            return content.substring(0, SHOW_CONTENT_SIZE) + "...";
        }
    }
    
    private static final int SHOW_CONTENT_SIZE = 100;
    
    /**
     * configuration parsing to support different schemas.
     *
     * @param text config context
     * @return {@link Properties}
     */
    public static Properties toProperties(String text) {
        return toProperties(text, "properties");
    }
    
    public static Properties toProperties(String text, String type) {
        return toProperties("", "", text, type);
    }
    
    public static Properties toProperties(String dataId, String group, String text) {
        return toProperties(dataId, group, text, "properties");
    }
    
    /**
     * configuration parsing to support different schemas.
     *
     * @param dataId config dataId
     * @param group  config group
     * @param text   config context
     * @param type   config type
     * @return {@link Properties}
     */
    public static Properties toProperties(String dataId, String group, String text, String type) {
        type = type.toLowerCase();
        if (ConfigType.YML.getType().equals(type)) {
            type = ConfigType.YAML.getType();
        }
        return ConfigParseUtils.toProperties(dataId, group, text, type);
    }
}
