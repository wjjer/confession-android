package vip.ablog.confession.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vip.ablog.confession.global.Constant;
import vip.ablog.confession.ui.model.ModuleItemBean;

public class FileReaderUtil {


    public static List<String> read(String path) throws IOException {
        //读取一个bai文本的字符流
        BufferedReader in = new BufferedReader(new FileReader(path));
        String line = null;
        //定义一个空字符串来接受读到的字符串
        StringBuilder sb = new StringBuilder();
        //循环把读取到的字符赋给str
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }


        String data = sb.toString();

        boolean startx = true;
        boolean starty = true;
        int index = 0;
        int index1 = 0;
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        while (startx) {
            int i = data.indexOf("${_", index + 1);
            if (i != -1) {
                x.add(i);
                index = i;
                int j = data.indexOf("${_", index + 1);
                if (j != -1) {
                    index = j;
                    x.add(j);
                } else {
                    startx = false;
                }
            } else {
                startx = false;
            }
        }

        while (starty) {
            int i = data.indexOf("_}", index1 + 1);
            if (i != -1) {
                y.add(i);
                index1 = i;
                int j = data.indexOf("_}", index1 + 1);
                if (j != -1) {
                    index1 = j;
                    y.add(j);
                } else {
                    starty = false;
                }
            } else {
                starty = false;
            }
        }


        List<String> keyList = new ArrayList<>();

        Map<String,String> map = new HashMap<>();
        for (int i=0; i<x.size();i++) {
            //System.out.println("x = "+ x.get(i)+ "   y = " + y.get(i));
            String s = data.substring(x.get(i) + 2, y.get(i) + 1);
            if (!keyList.contains(s)){
                keyList.add(s);
                System.out.println(s);
            }
        }

        return keyList;
    }

    public static List<ModuleItemBean> getLocalStorageModules(){
        List<ModuleItemBean> list = new ArrayList<>();
        File dir = new File(Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) continue;
            ModuleItemBean itemBean = new ModuleItemBean();
            itemBean.setName(files[i].getName());
            itemBean.setCover(Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + files[i].getName() +File.separator+ "cover.jpg");
            itemBean.setPreviewPath("file://" + Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + files[i].getName() +File.separator+ "index.html");
            itemBean.setModulePath(Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + files[i].getName() +File.separator+ "index.ftl");
            list.add(itemBean);
        }

        return list;

    }

    public static List<ModuleItemBean> getLocalStorageGaoBai(){
        List<ModuleItemBean> list = new ArrayList<>();
        File dir = new File(Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) continue;
            ModuleItemBean itemBean = new ModuleItemBean();
            itemBean.setName(files[i].getName());
            itemBean.setCover(Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE + File.separator + files[i].getName() +File.separator+ "cover.jpg");
            itemBean.setPreviewPath("file://" + Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE + File.separator + files[i].getName() +File.separator+ "index.html");
            list.add(itemBean);
        }

        return list;

    }

    public static void main(String[] args) {
        try {
            System.out.println("START");
            read("E:\\MyProject\\OpenSourceProj\\Android\\GeekMarket\\app\\src\\main\\assets\\template\\confession\\index.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
