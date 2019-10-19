package com.a.platform.elasticsearch;

import com.a.platform.common.util.StringUtil;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


/**
 * 默认elasticsearch构建器
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:05
 */
public class DefaultEsTemplateBuilder implements  EsTemplateBuilder {

    private String clusterName;
    private String clusterNodes;
    private String userPass;

    public DefaultEsTemplateBuilder() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @Override
    public ElasticsearchTemplate build() {

        return new ElasticsearchTemplate(transportClient());
    }

    private TransportClient transportClient() {

        try {
            Settings.Builder settings = Settings.builder().put("cluster.name", clusterName);

            if(!StringUtil.isEmpty(userPass)&&!userPass.equals("''")){
                settings.put("xpack.security.user", userPass);
            }

            TransportClient client = new PreBuiltXPackTransportClient(settings.build());

            Map<String, Integer> nodeMap = parseNodeIpInfo();
            for (Map.Entry<String, Integer> entry : nodeMap.entrySet()) {

                client.addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName(entry.getKey()), entry.getValue()));
            }

            return client;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析节点IP信息,多个节点用逗号隔开,IP和端口用冒号隔开
     *
     * @return
     */
    private Map<String, Integer> parseNodeIpInfo() {
        String[] nodeIpInfoArr = clusterNodes.split(",");
        Map<String, Integer> map = new HashMap<>(nodeIpInfoArr.length);
        for (String ipInfo : nodeIpInfoArr) {
            String[] ipInfoArr = ipInfo.split(":");
            map.put(ipInfoArr[0], Integer.parseInt(ipInfoArr[1]));
        }
        return map;
    }



    public DefaultEsTemplateBuilder setClusterName(String clusterName) {
        this.clusterName = clusterName;
        return  this;
    }

    public DefaultEsTemplateBuilder setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
        return  this;
    }

    public DefaultEsTemplateBuilder setUserPass(String userPass) {
        this.userPass = userPass;
        return this;

    }
}
