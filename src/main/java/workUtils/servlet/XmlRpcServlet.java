package workUtils.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;

public class XmlRpcServlet extends HttpServlet {

    private XmlRpcServletServer server;

    public void init(ServletConfig pConfig) throws ServletException {

        super.init(pConfig);

        // create a new XmlRpcServletServer object
        server = new XmlRpcServletServer();

        // set up handler mapping of XmlRpcServletServer object
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        try {
            phm.addHandler("RS", RpcService.class);
            server.setHandlerMapping(phm);
            // more config of XmlRpcServletServer object
            XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) server.getConfig();
            serverConfig.setEnabledForExtensions(true);
            serverConfig.setContentLengthOptional(false);
            serverConfig.setBasicEncoding("UTF-8");
            serverConfig.setEncoding("UTF-8");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        server.execute(req, resp);
    }
}
