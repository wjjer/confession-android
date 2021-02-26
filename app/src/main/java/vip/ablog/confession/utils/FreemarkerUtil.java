package vip.ablog.confession.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class FreemarkerUtil {
    public static Configuration getConfiguration(String template, String modulePath) throws IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_25);
        config.setDirectoryForTemplateLoading(new File(modulePath));
        //这里要设置取消使用Local语言环境
        config.setLocalizedLookup(false);
        try {
            config.getTemplate(template);
        } catch (TemplateNotFoundException e) {
            System.out.println("模板文件未找到！");
        }
        return config;
    }
    /**
     * 使用模板生成HTML代码
     */
    public static String createHtmlFromModel(Context context, String ftlPath, String htmlPath,String ftlName, String htmlName, Map<String,String> map) {
        FileWriter out = null;
        String result = "0";
        try {
            // 通过FreeMarker的Confuguration读取相应的模板文件
            Configuration configuration = getConfiguration(ftlName, ftlPath);
            // 获取模板
            Template template = configuration.getTemplate(ftlName);
            //设置模型
            //User user = new User("tom", "hahahah", 28, "上海市");

            //设置输出文件
            String path = ftlPath;
            System.out.println("路径："+ path);
            File files = new File(path);
            if(!files.exists()) {
                files.mkdirs();
            }

            //设置输出流
            out = new FileWriter(htmlPath + File.separator + htmlName);
            //out.write("学而时习之，温故而知新");
            //fw.close();



           // SDUtils.saveFileToExternalFileDir(context,)
            //模板输出静态文件
            template.process(map, out);
        } catch (TemplateNotFoundException e) {
            result = "没有找到模板："+e.getTemplateName();
            e.printStackTrace();
        } catch (Exception e) {
            result = "生成异常："+e.getMessage();
            e.printStackTrace();
        } finally {
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 根据String模板生成HTML，模板中存在List循环
     */
    public static void createHtmlFromStringList() {
        BufferedInputStream in = null;
        FileWriter out = null;
        try {
            //模板文件
            File file = new File("D:/EclipseLearnSpace/ResearchSpace/Html2Pdf/src/main/resources/static/html/class.html");
            //构造输入流
            in = new BufferedInputStream(new FileInputStream(file));
            int len;
            byte[] bytes = new byte[1024];
            //模板内容
            StringBuilder content = new StringBuilder();
            while((len = in.read(bytes)) != -1) {
                content.append(new String(bytes, 0, len, "utf-8"));
            }

            //构造Configuration
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
            //构造StringTemplateLoader
            StringTemplateLoader loader = new StringTemplateLoader();
            //添加String模板
            loader.putTemplate("test", content.toString());
            //把StringTemplateLoader添加到Configuration中
            configuration.setTemplateLoader(loader);

            //构造Model
            /*Classes classes = new Classes();
            classes.setClassId("23");
            classes.setClassName("实验一班");
            List<User> users = new ArrayList<User>();
            User user = new User("tom", "kkkkk", 29, "北京");
            users.add(user);
            User user2 = new User("Jim", "hhhh", 22, "上海");
            users.add(user2);
            User user3 = new User("Jerry", "aaaa", 25, "南京");
            users.add(user3);
            classes.setUsers(users);*/
            //获取模板
            Template template = configuration.getTemplate("test");
            //构造输出路
            out = new FileWriter("e:/html/result.html");
            //生成HTML
            template.process(new Object(), out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
