package com.a.platform.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:05
 */
public interface EsTemplateBuilder {

     ElasticsearchTemplate build();


}
