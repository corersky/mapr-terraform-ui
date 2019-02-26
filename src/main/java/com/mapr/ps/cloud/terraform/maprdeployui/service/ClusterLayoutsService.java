package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterLayoutDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusterLayoutsService {

    public List<ClusterLayoutDTO> getPredefindedClusterLayouts() {
        List<ClusterLayoutDTO> layouts = new ArrayList<>();
        layouts.add(createClusterLayoutNodes3());
        layouts.add(createClusterLayoutNodes4());
        layouts.add(createClusterLayoutNodes5());
        layouts.add(createClusterLayoutNodes6());
        layouts.add(createClusterLayoutNodes7());
        layouts.add(createClusterLayoutNodes8());
        layouts.add(createClusterLayoutNodes9());
        layouts.add(createClusterLayoutNodes10());
        return layouts;
    }

    private ClusterLayoutDTO createClusterLayoutNodes10() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 10;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3, 4, 5));
        layout.setCldb(nodes(6,7,8));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(1, 2, 3));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(10));
        layout.setMySQL(nodes(10));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(3, 4));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(8, 9, 10));
        layout.setGrafana(nodes(1, 2));
        return layout;
    }

    private ClusterLayoutDTO createClusterLayoutNodes9() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 9;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3, 4, 5));
        layout.setCldb(nodes(6,7,8));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(1, 2, 3));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(9));
        layout.setMySQL(nodes(9));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(3, 4));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(7, 8, 9));
        layout.setGrafana(nodes(1, 2));
        return layout;
    }

    private ClusterLayoutDTO createClusterLayoutNodes8() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 8;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3, 4, 5));
        layout.setCldb(nodes(6,7,8));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(1, 2, 3));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(8));
        layout.setMySQL(nodes(8));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(3, 4));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(6, 7, 8));
        layout.setGrafana(nodes(1, 2));
        return layout;
    }

    private ClusterLayoutDTO createClusterLayoutNodes7() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 7;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3));
        layout.setCldb(nodes(4,5,6));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(1, 2, 3));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(7));
        layout.setMySQL(nodes(7));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(3, 4));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(5, 6, 7));
        layout.setGrafana(nodes(1, 2));
        return layout;
    }


    private ClusterLayoutDTO createClusterLayoutNodes6() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 6;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3));
        layout.setCldb(nodes(4,5,6));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(1, 2, 3));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(6));
        layout.setMySQL(nodes(6));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(3, 4));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(5, 6));
        layout.setGrafana(nodes(1));
        return layout;
    }

    private ClusterLayoutDTO createClusterLayoutNodes5() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 5;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3));
        layout.setCldb(nodes(3,4,5));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(3, 4));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(5));
        layout.setMySQL(nodes(5));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(1, 2));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(3, 4));
        layout.setGrafana(nodes(5));
        return layout;
    }

    private ClusterLayoutDTO createClusterLayoutNodes4() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 4;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3));
        layout.setCldb(nodes(2, 3, 4));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(1, 2));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(4));
        layout.setMySQL(nodes(4));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(1, 2));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(3, 4));
        layout.setGrafana(nodes(4));
        return layout;
    }

    private ClusterLayoutDTO createClusterLayoutNodes3() {
        ClusterLayoutDTO layout = new ClusterLayoutDTO();
        int numberNodes = 3;
        layout.setNumberNodes(numberNodes);
        layout.setZookeeper(nodes(1, 2, 3));
        layout.setCldb(nodes(1, 2, 3));
        layout.setFileserver(all(numberNodes));
        layout.setKafkaClient(all(numberNodes));
        layout.setMcs(nodes(1, 2));
        layout.setResourceManager(nodes(1, 2));
        layout.setNodeManager(all(numberNodes));
        layout.setHistoryServer(nodes(3));
        layout.setMySQL(nodes(3));
        layout.setSpark(all(numberNodes));
        layout.setNfs(all(numberNodes));
        layout.setDrill(all(numberNodes));
        layout.setFlume(all(numberNodes));
        layout.setHbaseCli(all(numberNodes));
        layout.setHiveCli(all(numberNodes));
        layout.setHiveMetaStore(nodes(1, 2));
        layout.setHiveServer2(nodes(1, 2));
        layout.setCollectd(all(numberNodes));
        layout.setOpenTSDB(nodes(2, 3));
        layout.setGrafana(nodes(3));
        return layout;
    }

    private List<Integer> all(int numberNodes) {
        List<Integer> nodes = new ArrayList<>();
        for(int i = 1; i <= numberNodes; i++) {
            nodes.add(i);
        }
        return nodes;
    }

    private List<Integer> nodes(int ... nodesIdxs) {
        List<Integer> nodes = new ArrayList<>();
        for (int nodesIdx : nodesIdxs) {
            nodes.add(nodesIdx);
        }
        return nodes;
    }
}
