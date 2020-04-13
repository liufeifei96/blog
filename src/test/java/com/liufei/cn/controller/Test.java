package com.liufei.cn.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class Test {
    @GetMapping("/testthy")
    public ModelAndView hello(Model model){
        model.addAttribute("hello","你好");
        return new ModelAndView("file","hModel",model);
    }
    @ResponseBody
    @PostMapping("/getMap")
    public Map<String,String> getMap(){
        System.out.println("come in ");
        Map<String,String> map = new HashMap<>();
        map.put("name","刘飞");
        map.put("sex","男");
        map.put("hobby","打篮球");
        return map;
    }

    /**
     * 接收文件demo
     * @return
     */
    @ResponseBody
    @PostMapping("multifileUpload")
    public String multifileUpload(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("fileName");
        //String path  = this.getClass().getResource("/")+"down";
       // String path2 = this.getClass().getResource("/").getPath();
        //文件夹创建
        String path = "D:/test" ;
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }

        for(MultipartFile file:files){
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            System.out.println(fileName + "-->" + size);

            if(file.isEmpty()){
                return "false";
            }else{
                File dest = new File(path + "/" + fileName);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "false";
                }
            }
        }

        return "true";

    }

    @ResponseBody
    @RequestMapping("/download")
    public String downLoad(HttpServletResponse response) throws UnsupportedEncodingException {
        String filename="2.xlsx";
        String filePath = "D:/download" ;
        File file = new File(filePath + "/" + filename);
        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(filename,"UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download---" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }


    @GetMapping("/fronted")
    public ModelAndView fronted(){
        return new ModelAndView("slicefile");
    }

    @ResponseBody
    @PostMapping("/slice")
    public String getfile(HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        System.out.println("files size:"+files.size());
        String sliceIndex = request.getParameter("sliceIndex");
        System.out.println("sliceIndex"+sliceIndex);
        String path  = "D:/test";
        File dir = new File(path);
        if(!dir.isDirectory()){
            dir.mkdir();
        }
        MultipartFile file = files.get(0);
        String fileName = file.getOriginalFilename();
        System.out.println("originName:"+fileName);
        System.out.println("name:"+file.getName());
        //保存的文件路径
        File saveFile = new File(path+"/"+sliceIndex);
        if(saveFile.exists()){
            return "已经存在";
        }
        file.transferTo(saveFile);


        //分片文件进行合并

        return "s";
    }
}
