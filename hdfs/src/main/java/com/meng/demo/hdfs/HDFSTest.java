package com.meng.demo.hdfs;

import java.io.IOException;

public class HDFSTest {

    public static void main(String[] args) throws IOException {

        HDFSUtil.CreateFile("/user/duking/testfile2.txt", "hello,world");

        //Hdfs.CreateDir("/user/test");

        //Hdfs.RenameFile("/user/duking/testfile.txt", "/user/duking/test.txt");

        //Hdfs.DeleteFile("/user/duking/test.txt");

        //Hdfs.CheckFile("/user/hadoop/input/protocols");

        //Hdfs.GetLTime("/user/hadoop/input/protocols");

        //Hdfs.GetFileInfo("/user/hadoop/input/protocols");

        //Hdfs.GetAllFile("/user/hadoop/input/");

//        Hdfs.FindFilePost("/user/hadoop/input/protocols");

        System.out.println("Runing is over!!");
    }
}
