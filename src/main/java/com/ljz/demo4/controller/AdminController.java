package com.ljz.demo4.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.ljz.demo4.entity.Admin;
import com.ljz.demo4.entity.Student;
import com.ljz.demo4.entity.Teacher;
import com.ljz.demo4.repository.AdminRepo;
import com.ljz.demo4.repository.StudentRepository;
import com.ljz.demo4.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AdminRepo adminRepository;
    @Autowired
    TeacherRepository teacherRepository;
    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads";


    @PostMapping("/upload")
    String batch(Model model,
                 @RequestParam("files") MultipartFile[] files) throws IOException {

        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file:files
        ) {
            Path fileNameAndPath = Paths.get(uploadDirectory,file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            try {
                Files.write(fileNameAndPath,file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }


            File convFile = new File(fileNameAndPath.toString());

            InputStream input = new FileInputStream(convFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
            List<Object> data = EasyExcelFactory.read(bufferedInputStream,new Sheet(1,0));
            input.close();

            for (Object a: data) {
                String row = a.toString();
                row = row.substring(row.indexOf('[')+1,row.indexOf(']'));
                String[] value = row.split(",");
                Student student = new Student();
                student.setSno(Integer.parseInt(value[0]));
                student.setSname(value[1].substring(1));
                String password = (value[0].substring(4));
                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
                student.setPassword(hashed);
                student.setGid(Integer.parseInt(value[2].substring(1)));


                //如果不存在，save Student
                if (studentRepository.findBySno(student.getSno()) == null)
                {
                    studentRepository.save(student);
                }

            }
            convFile.delete();

        }

        model.addAttribute("msg","学生数据成功导入  注意已存在的学生将不会导入");
        return "Admin/bath";
    }


    @PostMapping("/resetpwd")
    String resetpwd(Model model,
                    @RequestParam("sno") Integer sno){

        Student student = studentRepository.findBySno(sno);
        if (student == null){
            model.addAttribute("msg","输入的学生不存在");
        }else {
            String password = sno.toString();
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            student.setPassword(hashed);
            studentRepository.save(student);
            model.addAttribute("msg","成功修改密码为"+sno.toString());
        }
        return "Admin/reset";
    }

    @PostMapping("/repwd")
    String repairpwd(Model model,
                     @RequestParam("opttype") String opttype,
                     @RequestParam("pwd1") String pwd1,
                     @RequestParam("pwd2") String pwd2,
                     HttpSession session
    ){
        if (pwd1.equals(pwd2)){
            Admin admin = (Admin) session.getAttribute("Admin");
            String hashed = BCrypt.hashpw(pwd1, BCrypt.gensalt());
            admin.setPassword(hashed);
            adminRepository.save(admin);
            model.addAttribute("msg","成功修改密码为"+pwd1);
        }else {

            model.addAttribute("msg","两次输入的密码不相同，请重新输入");
        }

        return "Admin/repairpwd";
    }

    @PostMapping("/register")
    String register(Model model,
                    @RequestParam("tno") Integer tno,
                    @RequestParam("tname") String tname,
                    @RequestParam("newpwd") String newpwd,
                    @RequestParam("repwd") String repwd,
                    @RequestParam("phone") String phone
    ){
        if (teacherRepository.findByTno(tno) == null){
            model.addAttribute("msg","该账号已注册");
        }
        if (newpwd.equals(repwd)){
            Teacher teacher =  new Teacher();
            teacher.setTno(tno);
            teacher.setTname(tname);
            String hashed = BCrypt.hashpw(newpwd, BCrypt.gensalt());
            teacher.setPassword(hashed);
            teacher.setPhone(phone);
            teacherRepository.save(teacher);
            model.addAttribute("msg","成功  注册！");
        }else {
            model.addAttribute("msg","两次输入的密码不相同");
        }
//        System.out.println(tno+" "+tname+" "+newpwd+" "+repwd+" "+phone+" ");
//        model.addAttribute("msg","成功");
        return "Admin/registered";
    }
}
