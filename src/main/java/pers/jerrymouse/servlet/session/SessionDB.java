package pers.jerrymouse.servlet.session;

import com.google.gson.Gson;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.utils.Utils;
import pers.jerrymouse.warpper.session.CustomSession;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SessionDB {

    private final HashMap<String, CustomSession> sessionJar = new HashMap<>();

    public static File getSessionDBDir(String rootUrl, String mapping) throws IOException {
        File workDir = Server.INSTANCE.workdir;
        File data = Utils.getFile(workDir, "data", true);
        if (data == null) {
            data = new File(workDir.getPath() + "/data");
        }
        File sessions = Utils.getFile(workDir, "sessions", true);
        if (sessions == null) {
            sessions = new File(data.getPath() + "/sessions");
        }
        File root = Utils.getFile(sessions, rootUrl.replace("/", "-"), true);
        if (root == null) {
            root = new File(sessions.getPath() + "/" + rootUrl.replace("/", "-"));
        }
        root.mkdirs();
        File path = Utils.getFile(workDir, mapping.replace("/", "-") + ".json", false);
        if (path == null) {
            path = new File(root.getPath() + "/" + mapping.replace("/", "-") + ".json");
        }
        return path;
    }

    public static SessionDB getSessionDB(String rootURL, String path) throws IOException {
        File dbDir = SessionDB.getSessionDBDir(rootURL, path);
        Gson gson = new Gson();
        if (!dbDir.exists()) {
            return new SessionDB();
        } else {
            SessionDB db = gson.fromJson(Utils.readFileToString(dbDir), SessionDB.class);
            if (db != null) {
                return db;
            } else {
                return new SessionDB();
            }
        }

    }

    public CustomSession get(String sessionId) {
        return this.sessionJar.get(sessionId);
    }

    public void put(String sessionId, CustomSession session, String rootUrl, String url) throws IOException {
        this.sessionJar.put(sessionId, session);
        save(rootUrl, url);
    }

    public void save(String rootUrl, String mapping) throws IOException {
        Gson gson = new Gson();
        File dir = getSessionDBDir(rootUrl, mapping);
        Utils.writeStringToFile(gson.toJson(this), dir);
    }
}
