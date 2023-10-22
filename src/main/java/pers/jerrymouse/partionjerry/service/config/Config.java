package pers.jerrymouse.partionjerry.service.config;

import org.dom4j.Element;
import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.service.connect.inner.proecssor.ProcessorFactoryDao;
import pers.jerrymouse.partionjerry.service.container.inner.Engine;
import pers.jerrymouse.servlet.Servlet;
import pers.jerrymouse.servlet.ServletFactory;
import pers.jerrymouse.servlet.specialsevlet.FileProcessServlet;
import pers.jerrymouse.servlet.specialsevlet.IndexProcessServlet;
import pers.jerrymouse.servlet.specialsevlet.Status404Servlet;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Config {
    private final List<WebAppConfig> webAppConfigs = new LinkedList<>();
    List<String> servlet_rootUrl = new LinkedList<>();
    List<String> servlet_name = new LinkedList<>();
    List<String> servlet_class = new LinkedList<>();
    List<String> servlet_mapping = new LinkedList<>();
    private String description;
    private String coreDir;
    private int port;
    private String name;
    private Engine engine;
    private ProcessorFactoryDao processorFactory;
    private int outTime;
    private int beatPort = -1;

    public List<WebAppConfig> getWebApps() {
        return webAppConfigs;
    }

    public void readCoreConfig(Element xmlConfig) throws Exception {
    }

    public void add404WebAppConfig() throws Exception {
        WebAppConfig config = new WebAppConfig();
        config.setServletDir("");
        this.webAppConfigs.add(config);

        config.setRootURL("404");
        config.setDisplayName("404 Error Processor");
        config.setDescription("A 404 Error Response Page\nIt will return a 404 Error Response");

        config.addServlet(ServletFactory.INSTANCE.newServlet("404 Processor", Status404Servlet.TAG, Status404Servlet.class, ""));
    }

    public void readWebAppConfig(Element xmlConfig, String path) throws Exception {
        WebAppConfig config = new WebAppConfig();
        config.setServletDir(path);
        this.webAppConfigs.add(config);

        Element configElement = xmlConfig.element("config");
        String tmpRoot = configElement.elementText("root-url");
        config.setRootURL(tmpRoot);
        config.setDisplayName(configElement.elementText("display-name"));
        config.setDescription(configElement.elementText("description"));

        loadJar(config.getServletDir() + "/WEB-INF/lib");
        loadClass(config.getServletDir() + "/WEB-INF/classes");

        List<Element> servletsElement = xmlConfig.elements("servlet");
        for (Element servlet : servletsElement) {
            String name = servlet.elementText("servlet-name");
            Class<?> tmpClass = Class.forName(servlet.elementText("servlet-class"));

            if (!Servlet.class.isAssignableFrom(tmpClass)) {
                LOG.error("Not a Servlet Class");
            }

            Class<? extends Servlet> servletClass = (Class<? extends Servlet>) tmpClass;
            String servletPath = config.getRootURL() + servlet.elementText("servlet-mapping");
            config.addServlet(ServletFactory.INSTANCE.newServlet(name, servletPath, servletClass, path));

            this.servlet_rootUrl.add(path);
            this.servlet_name.add(name);
            this.servlet_class.add(servletClass.getName());
            this.servlet_mapping.add(servletPath);
        }
        config.addServlet(ServletFactory.INSTANCE.newServlet(config.getDisplayName() + " File Processor", config.getRootURL() + "/" + FileProcessServlet.TAG, FileProcessServlet.class, path));
        config.addServlet(ServletFactory.INSTANCE.newServlet(config.getDisplayName() + " Index Processor", config.getRootURL(), IndexProcessServlet.class, path));
        servletsElement = xmlConfig.elements("welcome-file-list");
        for (Element servlet : servletsElement) {
            String name = servlet.elementText("welcome-file");
            config.getWelcomeFiles().put(name, name);
        }

    }

    public void loadClass(String url) throws Exception {
        // 设置class文件所在根路径
        // 例如/usr/java/classes下有一个test.App类，则/usr/java/classes即这个类的根路径，而.class文件的实际位置是/usr/java/classes/test/App.class
        File clazzPath = new File(url);

        // 记录加载.class文件的数量
        int clazzCount = 0;

        if (clazzPath.exists() && clazzPath.isDirectory()) {
            // 获取路径长度
            int clazzPathLen = clazzPath.getAbsolutePath().length() + 1;

            Stack<File> stack = new Stack<>();
            stack.push(clazzPath);

            // 遍历类路径
            while (stack.isEmpty() == false) {
                File path = stack.pop();
                File[] classFiles = path.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(".class"));
                for (File subFile : classFiles) {
                    if (subFile.isDirectory()) {
                        stack.push(subFile);
                    } else {
                        if (clazzCount++ == 0) {
                            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                            method.setAccessible(true);

                            // 设置类加载器
                            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                            // 将当前类路径加入到类加载器中
                            method.invoke(classLoader, clazzPath.toURI().toURL());
                        }
                        // 文件名称
                        String className = subFile.getAbsolutePath();
                        className = className.substring(clazzPathLen, className.length() - 6);
                        className = className.replace(File.separatorChar, '.');
                        // 加载Class类
                        Class.forName(className);
                        //.debug("读取应用程序类文件[class={}]", className);
                    }
                }
            }
        }
    }

    public void loadJar(String url) throws Exception {
        // 系统类库路径
        File libPath = new File(url);

        // 获取所有的.jar和.zip文件
        File[] jarFiles = libPath.listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".zip"));

        if (jarFiles != null) {
            // 从URLClassLoader类中获取类所在文件夹的方法
            // 对于jar文件，可以理解为一个存放class文件的文件夹
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);     // 设置方法的访问权限

            // 获取系统类加载器
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            for (File file : jarFiles) {
                method.invoke(classLoader, file.toURI().toURL());
                //.debug("读取jar文件[name={}]", file.getName());
            }

        }
    }

    public Engine engine() {
        return engine;
    }

    public String coreDir() {
        return coreDir;
    }

    public ProcessorFactoryDao getProcessorFactory() {
        return processorFactory;
    }

    public void setProcessorFactory(ProcessorFactoryDao processorFactoryDao) {
        this.processorFactory = processorFactoryDao;
    }

    public int getOutTime() {
        return outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public int getBeatPort() {
        return this.beatPort;
    }

    public void setBeatPort(int beatPort) {
        this.beatPort = beatPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoreDir() {
        return coreDir;
    }

    public void setCoreDir(String coreDir) {
        this.coreDir = coreDir;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public List<String> getServlet_rootUrl() {
        return servlet_rootUrl;
    }

    public List<String> getServlet_name() {
        return servlet_name;
    }

    public List<String> getServlet_class() {
        return servlet_class;
    }

    public List<String> getServlet_mapping() {
        return servlet_mapping;
    }
}
