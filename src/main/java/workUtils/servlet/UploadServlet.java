package workUtils.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import workUtils.utils.ImageUtils;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;

/**
 * @ClassName: BaseServlet
 * @Description: TODO(Base Servlet.)
 * @author juesu.chen
 * @date Jun 18, 2014 12:00:32 PM
 * @version 1.0
 */
public class UploadServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UploadServlet.class);

    private static final List<String> saveDirs = new ArrayList<String>();

    private static final String P_FILE_NAME = "fileName";

    private static final String P_OLD_FILE_NAME = "oldFileName";

    protected static final String SUCCESS = "success";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding(req.getCharacterEncoding());
        String act = req.getParameter("act");
        if ("cut".equals(act)) {
            cutImage(req, resp);
        } else {
            SmartUpload su = new SmartUpload();
            su.initialize(this.getServletConfig(), req, resp);
            int maxSize = 2;// 2M
            su.setMaxFileSize(maxSize * 1024 * 1024);
            JSONObject result = new JSONObject();
            result.put(SUCCESS, false);
            try {
                su.upload();
                File file = su.getFiles().getFile(0);
                String oldFileNamePath = su.getRequest().getParameter(P_OLD_FILE_NAME);
                String fileName = su.getRequest().getParameter(P_FILE_NAME);
                if (fileName == null) {
                    for (int i = 0; i < 5; i++) {
                        oldFileNamePath = su.getRequest().getParameter(P_OLD_FILE_NAME + "_" + i);
                        fileName = su.getRequest().getParameter(P_FILE_NAME + "_" + i);
                        if (fileName != null) {
                            break;
                        }
                    }
                }

                String dir = fileName.substring(0, fileName.lastIndexOf("/"));
                java.io.File dirFile = new java.io.File(req.getSession().getServletContext().getRealPath(dir));
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                if (!file.isMissing()) {
                    String photoAddr = fileName;
                    file.saveAs(photoAddr, File.SAVEAS_VIRTUAL);
                    String msg = checkFileOk(fileName, su.getRequest(), req, oldFileNamePath);
                    if (msg == null) {
                        if (StringUtils.isNotEmpty(oldFileNamePath)) {
                            oldFileNamePath = req.getSession().getServletContext().getRealPath(oldFileNamePath);
                            new java.io.File(oldFileNamePath).delete();
                        }
                        result.put(SUCCESS, true);
                        result.put("msg", "MSG_UPLOAD_PHOTO_SUCCESS");
                    } else {
                        result.put("msg", msg);
                    }
                }
            } catch(Exception e) {
                logger.error("upload error:", e);
                if (e.getMessage().contains("Size exceeded")) {
                    result.put("msg", "MSG_FILE_SIZE_EXCEEDED");
                } else {
                    result.put("msg", e.getMessage());
                }
            }
            try {
                resp.getWriter().write(result.toString());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String checkFileOk(String fileName, Request request, HttpServletRequest req, String oldFileNamePath)
            throws FileNotFoundException, IOException {
        // check file can be access

        if (!(isFileAccessable(fileName) && isFileAccessable(oldFileNamePath))) {
            return "The file path is not allowed to access.";
        }
        String sizeEq = request.getParameter("sizeEq");
        if (sizeEq != null) {
            String filePath = req.getSession().getServletContext().getRealPath(fileName);
            int[] wh = ImageUtils.getImageSize(filePath);
            if (!sizeEq.equalsIgnoreCase(wh[0] + "x" + wh[1])) {
                return MessageFormat.format("MSG_FILE_SIZE_WRONG", sizeEq);
            }
        }

        return null;
    }

    private boolean isFileAccessable(String fileName) {
        if (saveDirs.isEmpty()) {
            saveDirs.add("/cards");
            saveDirs.add("/images");
            saveDirs.add("/photos");
        }
        for (String dir : saveDirs) {
            if (fileName.startsWith(dir)) {
                return true;
            }
        }
        return StringUtils.isEmpty(fileName);
    }

    private void cutImage(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        result.put(SUCCESS, true);
        ServletContext scx = req.getSession().getServletContext();

        int x = new Float(req.getParameter("x")).intValue();
        int y = new Float(req.getParameter("y")).intValue();
        int w = new Float(req.getParameter("w")).intValue();
        int h = new Float(req.getParameter("h")).intValue();
        String src = scx.getRealPath(req.getParameter("src"));
        String dest = scx.getRealPath(req.getParameter("dest"));
        try {
            ImageUtils.cutImage(src, dest, x, y, w, h);
            result.put("msg", "MSG_UPLOAD_PHOTO_SUCCESS");
        } catch(IOException e) {
            logger.error("cutImage error:", e);
            result.put("msg", "MSG_UPLOAD_PHOTO_FAILED");
            result.put(SUCCESS, false);
        }
        try {
            resp.getWriter().write(result.toString());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
