package project.generate_html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
 
public class GenerateHtml {

    public static void generate(String templateName, String targetFileName, Map<String, Object> params) {
        
        Writer out = null;

        Path currentRelativePath = Paths.get("");
        String base_path = currentRelativePath.toAbsolutePath().toString();
        // System.out.println(base_path);

        String output_folder = "target\\output";        //通過匹配路徑格式拼接完整生成路徑
        String outFile = base_path + File.separator + output_folder + File.separator + targetFileName;
        System.out.println("Output file: " + outFile);
        
        try {
            
            File file = new File(outFile); 

            if (!file.exists()) {           // 生成空 HTML文件
                file.createNewFile();
            }
            else{
                file.delete();
            }

            // 初始化 freemarker 設定
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
            cfg.setDefaultEncoding("UTF-8");
            
            String template_folder = base_path + File.separator + "src\\main\\resources\\templates";
            cfg.setDirectoryForTemplateLoading(new File(template_folder));   // 模板存放處

            // 根據模板名稱獲取模板
            Template template = cfg.getTemplate(templateName);
            
            // 設置輸出流
            out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

            // 模版數據插入參數，通過輸出流插入到HTML中
            template.process(params, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
    }

    public static String readAll(BufferedReader reader) {

        StringBuffer buffer = new StringBuffer();
        
        while (true) {
            String line;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                break;
            } else {
                buffer.append(line);
                buffer.append("\n");
            }
        }

        return buffer.toString();
    }

    public static void main(String[] args) throws IOException, TemplateException {
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("title", "Freemarker 模板引擎");
        params.put("author", "Stanley");
        params.put("publishTime", "2022-07-07");
        params.put("seeNum", "6");
        params.put("imgPath", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRABZYhX2FTcVfhbhN2y6QFHITZf1XgSy92eQ&usqp=CAU");
        params.put("content", "不秀恩愛，沒有傷害！！！<br>~~單身狗保護協會~~");

        Object arr[] = new Object[0];
        List<Object> array = new ArrayList<Object>(Arrays.asList(arr));

        try {     
            JSONParser parser = new JSONParser();

            Path currentRelativePath = Paths.get("");
            String base_path = currentRelativePath.toAbsolutePath().toString();
            String input_file_path = base_path + File.separator + "src\\main\\resources\\inputs\\comments.json";

            FileInputStream input_file = new FileInputStream(input_file_path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input_file, "UTF-8"));
            JSONObject jsonObject = (JSONObject) parser.parse(readAll(reader));

            Set<String> keys = jsonObject.keySet();
            // System.out.println(keys);
            
            for(String time : keys){    
                Map<String, Object> data = new HashMap<String, Object>();

                JSONObject commentObject = (JSONObject) jsonObject.get(time);
                String comment = (String) commentObject.get("comment");

                // System.out.println(time);
                // System.out.println(comment);

                data.put("time", time);
                data.put("detail", comment);

                array.add(data);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        params.put("commentList", array);
        
        GenerateHtml.generate("html_template.html", "html_test.html", params);
    }

}