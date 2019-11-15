package com.ljz.demo4.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.ljz.demo4.entity.Question;
import com.ljz.demo4.entity.Sscore;
import com.ljz.demo4.entity.Teacher;
import com.ljz.demo4.entity.Testpaper;
import com.ljz.demo4.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/tea")
public class TeacherController {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    TestpaperRepository testpaperRepository;
    @Autowired
    GradeRepo classesRepository;
@Autowired
TeacherRepository teacherRepository;
@Autowired
SscoreRepo sscoreRepo;


    @PostMapping("/commitquestions")
    String commitQuestion
            (
            Model model,
    @RequestParam("qname") String qname,
    @RequestParam("content") String content,
    @RequestParam("inputrule") String inputrule,
    @RequestParam("outputrule") String outputrule,
    @RequestParam("inputexample") String inputexample,
    @RequestParam("outputexample") String outputexample,
    @RequestParam("rating") String rating
                          )
    {
        Question question =new Question();
        question.setQname(qname);
        question.setContent(content);
        question.setInputrule(inputrule);
        question.setOutputrule(outputrule);
        question.setInputexample(inputexample);
        question.setOutputexample(outputexample);
        question.setRating(rating);
        questionRepository.save(question);
        model.addAttribute("msg","成功");
        model.addAttribute("questions", questionRepository.findAll());
        return "Teacher/questionStatus";
    }


    @PostMapping("/commitpapers")
    String commitPapers
            (
            Model model,
    @RequestParam("testname") String testname,
    @RequestParam("cno") Integer cno,
    @RequestParam("startdate") String startdate,
    @RequestParam("enddate") String enddate,
    @RequestParam("testtime") String testtime,


    @RequestParam("qno_0") Integer qno_0,
    @RequestParam("qscore_0") Double qscore_0,

                @RequestParam("qno_1") Integer qno_1,
    @RequestParam("qscore_1") Double qscore_1,

                @RequestParam("qno_2") Integer qno_2,
    @RequestParam("qscore_2") Double qscore_2,

            @RequestParam("qno_3") Integer qno_3,
    @RequestParam("qscore_3") Double qscore_3,

                @RequestParam("qno_4") Integer qno_4,
    @RequestParam("qscore_4") Double qscore_4,
            HttpSession session

                          )
    {

        if (classesRepository.findByGid(cno) == null){
            model.addAttribute("msg","班级号不存在或格式错误");
        }
        if (questionRepository.findByQno(qno_0) == null){
            model.addAttribute("msg","第一题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }
        if (questionRepository.findByQno(qno_1) == null){
            model.addAttribute("msg","第二题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }
        if (questionRepository.findByQno(qno_2) == null){
            model.addAttribute("msg","第三题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }
        if (questionRepository.findByQno(qno_3) == null){
            model.addAttribute("msg","第四题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }if (questionRepository.findByQno(qno_4) == null){
            model.addAttribute("msg","第五题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }


        Testpaper testpaper = new Testpaper();
        testpaper.setTestname(testname);
        Teacher teacher = (Teacher) session.getAttribute("Teacher");
        testpaper.setTno(teacher.getTno());
        testpaper.setGid(cno);
        testpaper.setStartdate(java.sql.Date.valueOf(startdate));
        testpaper.setEnddate(java.sql.Date.valueOf(enddate));
        testpaper.setTesttime(java.sql.Time.valueOf(testtime+":00"));

        testpaper.setQno_0(qno_0);
        testpaper.setQscore_0(qscore_0);

        testpaper.setQno_1(qno_1);
        testpaper.setQscore_1(qscore_1);

        testpaper.setQno_2(qno_2);
        testpaper.setQscore_2(qscore_2);

        testpaper.setQno_3(qno_3);
        testpaper.setQscore_3(qscore_3);

        testpaper.setQno_4(qno_4);
        testpaper.setQscore_4(qscore_4);

        Testpaper newtestpaper = testpaperRepository.save(testpaper);
//        System.out.println(newtestpaper);
        model.addAttribute("msg","成功");
//        model.addAttribute("testpaper", testpaperRepository.findByTno(newtestpaper.getTno()));
        return "Teacher/paperStatus";
    }



    //查看题目详细内容
    @GetMapping("/content/{qno}")
    String content(@PathVariable(value = "qno") Integer qno
            , Model model) {
        model.addAttribute("question", questionRepository.findByQno(qno));
        return "Teacher/question";
    }



    @PostMapping("/repwd")
    String repairpwd(Model model,
                     @RequestParam("opttype") String opttype,
                     @RequestParam("pwd1") String pwd1,
                     @RequestParam("pwd2") String pwd2,
                     HttpSession session
    ){
        if (pwd1.equals(pwd2)){
            Teacher tea = (Teacher) session.getAttribute("Teacher");
            String hashed = BCrypt.hashpw(pwd1, BCrypt.gensalt());
            tea.setPassword(hashed);
            teacherRepository.save(tea);
            model.addAttribute("msg","成功修改密码为"+pwd1);
        }else {

            model.addAttribute("msg","两次输入的密码不相同，请重新输入");
        }

        return "Teacher/repairpwd";
    }


    //查看学生考卷
    @GetMapping("/lookpaper")
    String lookpaper(Model model){

        List<Testpaper> testpapers = testpaperRepository.findAll();
        model.addAttribute("Testpaers",testpapers);
        return "Teacher/lookpaper";
    }

    @GetMapping("/Browse/{testno}")
    String browse(Model model,
                  @PathVariable(value = "testno") Integer testno
                  ){
        List<Sscore> sscores = sscoreRepo.findByTestno(testno);
        model.addAttribute("sscores",sscores);
        return "Teacher/browsepaper";
    }
    @GetMapping("/givesscore/{ssno}")
    String givesscore(Model model,
                  @PathVariable(value = "ssno") Integer ssno
                  ){
        Sscore sscore = sscoreRepo.findBySsno(ssno);
        model.addAttribute("sscore",sscore);
        return "Teacher/givesscore";
    }

    @PostMapping("/submitsscore/{ssno}")
    String submitsscore(Model model,
                        @PathVariable(value = "ssno") Integer ssno,
                        @RequestParam("score") Double score){

        Sscore s = sscoreRepo.findBySsno(ssno);
        s.setScore(score);
        sscoreRepo.save(s);
        return browse(model,s.getTestno());

    }

    @GetMapping("/scoreexport")
    String scoreexport(Model model){
        List<Testpaper> testpapers = testpaperRepository.findAll();
        model.addAttribute("Testpaers",testpapers);
        return "Teacher/scoreexport";
    }

//参考easyexcel 写法
    @GetMapping("/export/{testno}")
    String export(
            @PathVariable(value = "testno") Integer testno,
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename="+testno+".xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        String fileName = new String(("UserInfo " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .getBytes(), "UTF-8");
        Sheet sheet1 = new Sheet(1, 0);
        sheet1.setSheetName("第一个sheet");

        //设置列宽 设置每列的宽度
        Map columnWidth = new HashMap();
        columnWidth.put(0,1000);columnWidth.put(1,5000);columnWidth.put(2,1000);columnWidth.put(3,1000);
        sheet1.setColumnWidthMap(columnWidth);
        sheet1.setHead(createTestListStringHead());
        //or 设置自适应宽度
        //sheet1.setAutoWidth(Boolean.TRUE);
        writer.write1(createTestListObject(testno), sheet1);

        writer.finish();
        out.close();
        return "Teacher/scoreexport";
    }

    public static List<List<String>> createTestListStringHead(){
        //写sheet3  模型上没有注解，表头数据动态传入
        List<List<String>> head = new ArrayList<List<String>>();
        List<String> headCoulumn1 = new ArrayList<String>();
        List<String> headCoulumn2 = new ArrayList<String>();
        List<String> headCoulumn3 = new ArrayList<String>();

        headCoulumn1.add("考试号");
        headCoulumn2.add("学生学号");
        headCoulumn3.add("考试分数");

        head.add(headCoulumn1);
        head.add(headCoulumn2);
        head.add(headCoulumn3);
        return head;
    }
    public List<List<Object>> createTestListObject(Integer testno) {
        List<List<Object>> object = new ArrayList<List<Object>>();
        List<Sscore> sscores = sscoreRepo.findByTestno(testno);
        for (Sscore s:
                sscores) {
            List<Object> da = new ArrayList<Object>();
            da.add(s.getTestno());
            da.add(s.getSno());
            da.add(s.getScore());
            object.add(da);
        }

        return object;
    }
}

