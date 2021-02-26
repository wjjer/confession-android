package vip.ablog.confession.utils;

import java.util.HashMap;
import java.util.Map;

public class StrConvertUtil {

    private static   Map<String,String> convertMap = null;



    public static String convert(String k){
        if (convertMap == null){
            initData();
        }
        return convertMap.get(k);
    }

    private static void initData() {
        convertMap = new HashMap();
        convertMap.put("_title_1_","请您填写标题");
        convertMap.put("_title_2_","请您填写标题2");
        convertMap.put("_title_3_","请您填写标题3");
        convertMap.put("_subtitle_1_","请您填写副标题");
        convertMap.put("_subtitle_2_","请您填写副标题2");
        convertMap.put("_subtitle_3_","请您填写副标题3");
        convertMap.put("_speaker_name_","请输入您（告白者）的姓字");
        convertMap.put("_listener_name_","请输入她/他/它（被告白者）的名字");
        convertMap.put("_meet_time_","请输入你们的相识时间（格式：2020-12-15）");
        convertMap.put("_content_1_","填写告白的第一句话/句子/内容");
        convertMap.put("_content_2_","填写告白的第二句话/句子/内容");
        convertMap.put("_content_3_","填写告白的第三句话/句子/内容");
        convertMap.put("_content_4_","填写告白的第四句话/句子/内容");
        convertMap.put("_content_5_","填写告白的第五句话/句子/内容");
        convertMap.put("_content_6_","填写告白的第六句话/句子/内容");
        convertMap.put("_content_7_","填写告白的第七句话/句子/内容");
        convertMap.put("_content_8_","填写告白的第八句话/句子/内容");
        convertMap.put("_content_9_","填写告白的第九句话/句子/内容");
        convertMap.put("_content_10_","填写告白的第十句话/句子/内容");
    }

}
