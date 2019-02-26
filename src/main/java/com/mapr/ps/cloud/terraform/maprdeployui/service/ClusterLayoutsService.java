package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.mapr.ps.cloud.terraform.maprdeployui.model.DefaultClusterLayoutDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.NodeLayoutDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClusterLayoutsService {

    public List<DefaultClusterLayoutDTO> getPredefindedClusterLayouts() {
        List<DefaultClusterLayoutDTO> layouts = new ArrayList<>();
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

    private DefaultClusterLayoutDTO createClusterLayoutNodes10() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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

    private DefaultClusterLayoutDTO createClusterLayoutNodes9() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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

    private DefaultClusterLayoutDTO createClusterLayoutNodes8() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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

    private DefaultClusterLayoutDTO createClusterLayoutNodes7() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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


    private DefaultClusterLayoutDTO createClusterLayoutNodes6() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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

    private DefaultClusterLayoutDTO createClusterLayoutNodes5() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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

    private DefaultClusterLayoutDTO createClusterLayoutNodes4() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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

    private DefaultClusterLayoutDTO createClusterLayoutNodes3() {
        DefaultClusterLayoutDTO layout = new DefaultClusterLayoutDTO();
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

    public List<NodeLayoutDTO> createNodeLayoutList(DefaultClusterLayoutDTO defaultLayout) {
        List<NodeLayoutDTO> layouts = new ArrayList<>();
        for(int i = 1; i <= defaultLayout.getNumberNodes(); i++) {
            NodeLayoutDTO layout = new NodeLayoutDTO();
            layout.setNodeIndex(i);
            layout.setZookeeper(defaultLayout.getZookeeper().contains(i));
            layout.setCldb(defaultLayout.getCldb().contains(i));
            layout.setFileserver(defaultLayout.getFileserver().contains(i));
            layout.setKafkaClient(defaultLayout.getKafkaClient().contains(i));
            layout.setMcs(defaultLayout.getMcs().contains(i));
            layout.setResourceManager(defaultLayout.getResourceManager().contains(i));
            layout.setNodeManager(defaultLayout.getNodeManager().contains(i));
            layout.setHistoryServer(defaultLayout.getHistoryServer().contains(i));
            layout.setMySQL(defaultLayout.getMySQL().contains(i));
            layout.setSpark(defaultLayout.getSpark().contains(i));
            layout.setNfs(defaultLayout.getNfs().contains(i));
            layout.setDrill(defaultLayout.getDrill().contains(i));
            layout.setFlume(defaultLayout.getFlume().contains(i));
            layout.setHbaseCli(defaultLayout.getHbaseCli().contains(i));
            layout.setHiveCli(defaultLayout.getHiveCli().contains(i));
            layout.setHiveMetaStore(defaultLayout.getHiveMetaStore().contains(i));
            layout.setHiveServer2(defaultLayout.getHiveServer2().contains(i));
            layout.setCollectd(defaultLayout.getCollectd().contains(i));
            layout.setOpenTSDB(defaultLayout.getOpenTSDB().contains(i));
            layout.setGrafana(defaultLayout.getGrafana().contains(i));
            layouts.add(layout);
        }
        return layouts;
    }
}
