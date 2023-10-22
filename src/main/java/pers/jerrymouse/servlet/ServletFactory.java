package pers.jerrymouse.servlet;

public class ServletFactory {
    public static final ServletFactory INSTANCE = new ServletFactory();

    private ServletFactory() {
    }

    public Servlet newServlet(String name, String mapping, Class<? extends Servlet> servletClass, String workDir) throws Exception {
        Servlet servlet = servletClass.getConstructor().newInstance();
        servlet.setName(name);
        servlet.setMapping(mapping);
        servlet.setWorkdir(workDir);
        servlet.setClazz(servletClass.getName());
        return servlet;
    }

}
