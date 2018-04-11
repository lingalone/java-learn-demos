package io.github.lingalone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * java 控制 windows 进程
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/4/11
 */

public class WindowsProgramControlUtil {


    /**
     * 获取程序的进程PID
     * cmd:
     *      tasklist /fi "imagename eq java*"  /fo list
     * result:
     *      映像名称:     java.exe
     *      PID:          3760
     *      会话名      : Console
     *      会话#   :     1
     *      内存使用 :    127,464 K
     *      .....foreach.....
     * @param name
     * @return list
     */
    public static List<String> getPid(String name) {
        BufferedReader bufferedReader = null;
        List<String> list = new ArrayList();
        try{
            Process process = Runtime.getRuntime().exec("tasklist /fi \"imagename eq " + name + "\" /fo list");
            System.out.println("tasklist /fi \"imagename eq " + name + "\" /fo list");

            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str;
            while ((str = bufferedReader.readLine()) != null){
                /**
                 * bufferedReader 数据结构为（参考WIN CMD 控制台展示）：
                 * 映像名称:Foxmail.exe
                 * PID:27716
                 * 会话名:Console
                 * 会话#:1
                 * 内存使用:171,056 K
                 *
                 */
                if (str.startsWith("PID")){
                    String[] array = str.split(":");
                    String pid = array[1].trim();
                    list.add(pid);
                }
                System.out.println(str);
            }
        }catch (IOException e){
            System.out.println(e.toString());
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }

        return list;
    }


    /**
     * url:
     *      https://blog.csdn.net/oscar999/article/details/14291757
     * cmd:
     *      TASKKILL [/S system [/U username [/P [password]]]] { [/FI filter] [/PID processid | /IM imagename] } [/F] [/T]
     *
     *
     * @param name
     */
    public static void kill(String name){

        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec("taskkill /F /IM " + name);
            System.out.println("taskkill /F /IM " + name);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str;
            while ((str = bufferedReader.readLine()) != null){
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String launch(String path){
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        StringBuilder build = new StringBuilder();
        try {
            process = runtime.exec("cmd /c start " + path);
            System.out.println("cmd /c start " + path);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                build.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return build.toString();
    }


    public static void main(String[] args) {

        getPid("Foxmail.exe");
        launch("C:\\Users\\Public\\Desktop\\Foxmail");
        kill("Foxmail.exe");
    }



}
