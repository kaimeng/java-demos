package com.meng.demo.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.util.Date;

public class HDFSUtil {

    /**
     * 创建一个文件
     * path:路径   txt：文件类容
     */
    public static void CreateFile(String path, String txt) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        byte[] buff = txt.getBytes();
        Path dfs = new Path(path);
        FSDataOutputStream outputStream = hdfs.create(dfs);
        outputStream.write(buff, 0, buff.length);
        outputStream.close();
        hdfs.close();
        System.out.println("Runing CreateFile over!!");
    }

    /**
     * 上传本地文件
     */
    public static void uploadFile(String src, String dst) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        Path srcPath = new Path(src); //原路径
        Path dstPath = new Path(dst); //目标路径

        //调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
        fs.copyFromLocalFile(false, srcPath, dstPath);

        //打印文件路径
        System.out.println("Upload to " + conf.get("fs.default.name"));
        System.out.println("------------list files------------" + "\n");

        FileStatus[] fileStatus = fs.listStatus(dstPath);
        for (FileStatus file : fileStatus) {
            System.out.println(file.getPath());
        }
        fs.close();
    }

    /**
     * 创建文件夹 *
     *
     * @param path *
     * @throws IOException
     */
    public static void CreateDir(String path) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path dfs = new Path(path);
        hdfs.mkdirs(dfs);
        hdfs.close();
    }

    /**
     * 重命名文件 *
     *
     * @param oldpath *
     * @param newpath *
     * @throws IOException
     */
    public static void RenameFile(String oldpath, String newpath) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path frpaht = new Path(oldpath); //旧的文件名
        Path topath = new Path(newpath); //新的文件名
        boolean isRename = hdfs.rename(frpaht, topath);
        String result = isRename ? "成功" : "失败";
        System.out.println("文件重命名结果为：" + result);
        hdfs.close();
    }

    /**
     * 删除文件 或者目录 *
     *
     * @param path *
     * @throws IOException
     */
    public static void DeleteFile(String path) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        hdfs.delete(new Path(path), true);
        hdfs.close();
    }

    /**
     * 判断文件或目录是否存在 *
     *
     * @param path *
     * @throws IOException
     */
    public static void CheckFile(String path) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path findf = new Path(path);
        boolean isExists = hdfs.exists(findf);
        System.out.println("Exist?" + isExists);
        hdfs.close();
    }

    /**
     * 查看HDFS文件的最后修改时间 *
     *
     * @param path *
     * @throws IOException
     */
    public static void GetLTime(String path) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path fpath = new Path(path);
        FileStatus fileStatus = hdfs.getFileStatus(fpath);
        long modiTime = fileStatus.getModificationTime();
        Date date = new Date(modiTime);
        System.out.println("file1.txt change time is" + date);
        hdfs.close();
    }

    /**
     * 获取文件信息 * @param path * @throws IOException
     */
    public static void GetFileInfo(String path) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path fpath = new Path(path);
        FileStatus fileStatus = hdfs.getFileStatus(fpath);
        System.out.println("文件路径：" + fileStatus.getPath());
        System.out.println("块的大小：" + fileStatus.getBlockSize());
        System.out.println("文件所有者：" + fileStatus.getOwner() + ":" + fileStatus.getGroup());
        System.out.println("文件权限：" + fileStatus.getPermission());
        System.out.println("文件长度：" + fileStatus.getLen());
        System.out.println("备份数：" + fileStatus.getReplication());
        System.out.println("修改时间：" + fileStatus.getModificationTime());
        hdfs.close();
    }

    /**
     * 获取目录下的所有文件 *
     *
     * @param path *
     * @throws IOException
     */
    public static void GetAllFile(String path) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path fpath = new Path(path);
        FileStatus stats[] = hdfs.listStatus(fpath);
        for (int i = 0; i < stats.length; ++i) {
            System.out.println(stats[i].getPath().toString());
        }
        hdfs.close();
    }

    /**
     * 查看文件所在集群的位置 *
     *
     * @param path *
     * @throws IOException
     */
    public static void FindFilePost(String path) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path fpath = new Path(path);
        FileStatus fileStatus = hdfs.getFileStatus(fpath);
        BlockLocation[] blkLocations = hdfs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        int blockLen = blkLocations.length;
        for (int i = 0; i < blockLen; i++) {
            String[] hosts = blkLocations[i].getHosts();
            System.out.println("block_" + i + "_location:" + hosts[0]);
        }
        hdfs.close();
    }
}
