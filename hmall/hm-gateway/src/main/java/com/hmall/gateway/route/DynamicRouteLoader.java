package com.hmall.gateway.route;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * ClassName：RouteDefinitionWriter
 *
 * @author: Devil
 * @Date: 2025/1/5
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicRouteLoader {
    private final NacosConfigManager nacosConfigManager;

    private final RouteDefinitionWriter writer;

    private final Set<String > routeIds = new HashSet<>();

    // 路由配置文件的id和分组
    private final String dataId = "gateway-routes.json";
    private final String group = "DEFAULT_GROUP";

    @PostConstruct
    public void initRouteConfigListener() throws NacosException {
        //1.项目启动时，先拉取一次配置，并且添加配置监听器
        String configInfo = nacosConfigManager.getConfigService()
                .getConfigAndSignListener(dataId, group, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                       //2.监听到配置变更，需要去更新路由表
                        updateConfigInfo(configInfo);
                    }
                });
        //3.第一次读取到配置，也需要更新路由表
        updateConfigInfo(configInfo);
    }
        private void updateConfigInfo(String configInfo){
        log.debug("监听到路由配置信息：{}",configInfo);
            //1.解析配置信息，转为RouteDefinition
            List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
            //2.删除旧的路由表
            for (String routeId: routeIds) {
                writer.delete(Mono.just(routeId)).subscribe();
            }
            routeIds.clear();

            //3.更新路由表
            for (RouteDefinition  routeDefinition:routeDefinitions) {
                writer.save(Mono.just(routeDefinition)).subscribe();
                //记录路由id.便于下一次删除
                routeIds.add(routeDefinition.getId());
            }
        }
}
