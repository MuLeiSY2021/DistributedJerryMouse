package pers.jerrymouse.utils;


import pers.jerrymouse.log.LOG;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class Utils {
    public static void writeStringToFile(String content, File file) throws IOException {
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                file.getParentFile().mkdirs();
            }

            file.createNewFile();
        }
        FileWriter writer = null;
        writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static File getFile(File dir, String fileName, boolean isDir) {
        if (!dir.isDirectory()) {
            return null;
        }
        File[] tmp = dir.listFiles((file, s) -> s.equals(fileName));
        if (tmp == null || tmp.length == 0) {
            return null;
        }
        File tgr = tmp[0];
        if (!isDir) {
            return tgr;
        } else if (tgr.isDirectory()) {
            return tgr;
        } else {
            return null;
        }
    }

    public static String readFileToString(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            // 构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            // 使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public static byte[] inputStreamToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        return out.toByteArray();
    }

    /**
     * 将本地文件传输到指定的 Socket 服务端
     *
     * @param target 目标 Socket 服务端
     * @param src    待传输的本地文件
     */
    public static void transferFrom(Socket target, File src) throws IOException {
        // 获取本地文件列表
        ArrayList<String> files = new ArrayList<>();
        String srcPath = src.getPath() + "/";
        getFiles(src, "", files);

        // 将文件列表发送到服务端
        OutputStream outputStream = target.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(files.size());
        for (String file : files) {
            dataOutputStream.writeUTF(file);
        }

        // 发送文件内容到服务端
        for (String file : files) {
            if (file.endsWith("/")) {
                continue; // 如果是目录，不需要发送内容
            }
            File localFile = new File(srcPath + file);
            FileInputStream fileInputStream = new FileInputStream(localFile);

            // 发送文件长度信息
            long fileSize = localFile.length();
            dataOutputStream.writeLong(fileSize);

            // 发送文件内容
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            fileInputStream.close();
        }
    }

    /**
     * 将 Socket 服务端接收到的文件保存到本地指定目录下
     *
     * @param target    目标 Socket 服务端
     * @param dstFolder 本地目标文件夹路径
     */
    public static void transferTo(Socket target, String dstFolder) throws IOException {
        // 读取服务端发送的文件列表
        InputStream inputStream = target.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int count = dataInputStream.readInt();
        ArrayList<String> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(dataInputStream.readUTF());
        }

        // 接收服务端发送的文件内容并保存到本地目录中
        for (String file : files) {
            String localPath = dstFolder + "/" + file;
            File localFile = new File(localPath);
            if (file.endsWith("/")) {
                localFile.mkdirs(); // 如果是目录，创建本地目录
            } else {
                localFile.getParentFile().mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(localFile);

                // 读取文件长度信息
                long fileSize = dataInputStream.readLong();
                byte[] buffer = new byte[1024];
                int length;
                LOG.debug("waiting " + file);
                while (fileSize > 0 && (length = inputStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                    fileOutputStream.write(buffer, 0, length);
                    fileSize -= length;
                }
                LOG.debug("file done");

                fileOutputStream.close();
            }
        }
    }

    /**
     * 递归获取文件和目录结构
     *
     * @param dir   当前目录
     * @param path  相对路径
     * @param files 文件列表
     */
    private static void getFiles(File dir, String path, ArrayList<String> files) {
        LOG.debug(dir.getPath() + " " + dir.isDirectory());
        for (File subFile : dir.listFiles()) {
            if (subFile.getName().equals("DashBoard")) {
                continue;
            }
            if (subFile.isDirectory()) {
                // 如果是目录，递归获取目录结构
                String subPath = path + subFile.getName() + "/";
                files.add(subPath);
                getFiles(subFile, subPath, files);
            } else {
                // 如果是文件，添加到文件列表中
                String filePath = path + subFile.getName();
                files.add(filePath);
            }
        }
    }

    public static void deleteAllFiles(File file) throws FileNotFoundException {
        LOG.info("Start deleting all files");
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            throw new FileNotFoundException("File " + file);
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //打印文件名
            String name = file.getName();
            LOG.info("Delete file:" + f.getPath());
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteAllFiles(f);
            } else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }


}
