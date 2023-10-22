package pers.jerrymouse.mainjerry.distributed.linktoslave;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pers.jerrymouse.log.LOG;
import pers.jerrymouse.mainjerry.distributed.Slave;
import pers.jerrymouse.mainjerry.distributed.SlaveFactory;
import pers.jerrymouse.mainjerry.distributed.SlaveHash;
import pers.jerrymouse.mainjerry.main.MainServer;
import pers.jerrymouse.servlet.request.CustomServletServerRequest;
import pers.jerrymouse.utils.Utils;
import pers.jerrymouse.warpper.session.CustomCookie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MappingStrategy {
    public static final MappingStrategy INSTANCE = new MappingStrategy();

    public final Map<String, Slave> urlHashMap = new ConcurrentHashMap<>();
    public final Map<String, Slave> slaveMap = new HashMap<>();
    private final SlaveFactory slaveFactory = new SlaveFactory();
    public List<SlaveHash> slaveHashsMap = new LinkedList<>();
    public Queue<Slave> slaves = new ConcurrentLinkedQueue<>();
    CheckLink checkLink = new CheckLink();
    private Map<String, String> hashMapInfo = new ConcurrentHashMap<>();
    private int virtualNodeNumber;

    public void init() throws Exception {
        File workdir = MainServer.INSTANCE.workdir;
        File xmlConfigFile = Utils.getFile(workdir, "slaves.xml", false);
        SAXReader reader = new SAXReader();
        Document document = reader.read(xmlConfigFile);
        Element root = document.getRootElement();
        /*
        <slaves>
            <slave>
                <name>Host1</name>

                <host>localhost</host>
                <port>8090</port>
                <beat-port>8091</beat-port>
            </slave>
        </slaves>
         */
        LOG.info("Slaves read from " + xmlConfigFile.getPath());
        this.setVirtualNodeNumber(Integer.parseInt(root.elementText("virtual-node-number")));
        int time = 1;
        for (Object obj : root.elements("slave")) {
            boolean flg = true;
            while (flg) {
                Slave slave;
                try {
                    Element slave_e = null;
                    if (obj instanceof Element) {
                        slave_e = (Element) obj;
                    }

                    String ip = slave_e.elementText("host");
                    int port = Integer.parseInt(slave_e.elementText("port"));
                    String name = slave_e.elementText("name");
                    int beatPort = Integer.parseInt(slave_e.elementText("beat-port"));
                    LOG.info("Slave " + name + " " + ip + " " + port + " Connecting...");
                    slave = new Slave(ip, port, name, beatPort);
                    this.registerSlave(slave);
                } catch (ConnectException e) {
                    int waitTime = 1000 * time;
                    LOG.warn("Failed to connect slave,wait " + time + " seconds");
                    Thread.sleep(waitTime);
                    time = time << 1;
                    continue;
                }
                slave.setAlive();
                flg = false;
                time = 1;
                LOG.info("Success");
            }
        }
        Random random = new Random(System.currentTimeMillis());
        Map<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i < virtualNodeNumber; i++) {
            int hashCode = getNoRepeatHashCode(random.nextInt(), map);
            slaveHashsMap.add(new SlaveHash(i, hashCode));
        }
        link();

        LOG.info("Start scatter");
        Scatter.transportation(slaves);
        LOG.info("Scatter end");
        hashMapInit();
    }

    private void hashMapInit() throws FileNotFoundException {
        LOG.info("HashMapInit");
        Gson gson = new Gson();
        File hash = new File(MainServer.INSTANCE.workdir + "/data/hashmap/hashmap.json");
        if (hash.exists()) {
            hashMapInfo = gson.fromJson(new JsonReader(new FileReader(hash)), this.hashMapInfo.getClass());
            for (Map.Entry<String, String> entrySet : hashMapInfo.entrySet()) {
                Slave slave = getSlaveByName(entrySet.getValue());
                if (slave == null) {
                    LOG.error("The slave " + entrySet.getValue() + " in the record is not config now");
                    continue;
                }
                urlHashMap.put(entrySet.getKey(), slave);
            }
        }
        LOG.info("Hash map inited");
    }

    public Set<Map.Entry<String, Slave>> getHashMapEntrySet() {
        return urlHashMap.entrySet();
    }


    public void setHashMap(String cookie, Slave slave) throws IOException {
        this.urlHashMap.put(cookie, slave);
        this.hashMapInfo.put(cookie, slave.getName());
        hashMapSave();
    }

    private void hashMapSave() throws IOException {
        Gson gson = new Gson();
        Utils.writeStringToFile(gson.toJson(this.hashMapInfo), new File(MainServer.INSTANCE.workdir + "/data/hashmap/hashmap.json"));
    }

    public int getNoRepeatHashCode(int hash, Map<Integer, Boolean> map) {
        while (map.containsKey(hash)) {
            hash = hash << 1;
        }
        return hash;
    }

    public void link() {
        Thread checkLinkThread = new Thread(checkLink);
        checkLinkThread.start();
    }

    public void stop() {
        checkLink.stop();
    }


    public void setVirtualNodeNumber(int virtualNodeNumber) {
        this.virtualNodeNumber = virtualNodeNumber;
    }

    public String registerUser(CustomServletServerRequest request) throws IOException {
        return request.getCookie(CustomCookie.COOKIE_NAME).toString() + " " + request.getRequestPath();
    }

    private Slave getSlave(String hashValue) throws IOException {
        int hashCode = Math.abs(hashValue.hashCode());
        final SlaveHash[] tmp = {null};
        try {
            SlaveHash resSlaveHash = null;
            Optional<SlaveHash> optional = slaveHashsMap.stream().sorted(Comparator.comparing(SlaveHash::getHashCode)).filter((x) -> {
                if (x.getHashCode() == hashCode) {
                    return true;
                }
                if (tmp[0] == null) {
                    tmp[0] = x;
                    return false;
                } else {
                    boolean res = tmp[0].getHashCode() < hashCode && hashCode < x.getHashCode();
                    tmp[0] = x;
                    return res;
                }
            }).findFirst();
            resSlaveHash = optional.orElseGet(() -> slaveHashsMap.get(0));
            Slave slave = this.slaves.toArray(new Slave[0])[resSlaveHash.getPosition() % slaves.size()];
            this.setHashMap(hashValue, slave);
            return slave;
        } catch (NoSuchElementException e) {
            LOG.error("Hash Value is" + hashValue + " hash code is:" + hashValue.hashCode());
            LOG.error(slaves.toString());
            LOG.error(e);

        }
        return null;
    }

    public Socket getServer(CustomServletServerRequest request) throws IOException {
        Slave slave = getSlave(registerUser(request));
        LOG.debug("get distribution Server " + "ip is " + slave.getIp() + " port is " + slave.getPort());
        return new Socket(slave.getIp(), slave.getPort());
    }

    public void registerSlave(String ip, int port, String name, int beatPort) throws IOException {
        Slave slave = slaveFactory.newSlave(ip, port, name, beatPort);
        this.slaves.add(slave);
        this.slaveMap.put(slave.getName(), slave);
    }

    public void registerSlave(Slave slave) throws IOException {
        this.slaves.add(slave);
        this.slaveMap.put(slave.getName(), slave);
    }

    public Slave getSlaveByName(String name) {
        return this.slaveMap.get(name);
    }
}
