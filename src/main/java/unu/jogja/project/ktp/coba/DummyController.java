/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unu.jogja.project.ktp.coba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author user
 */
@Controller
public class DummyController {
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    public String getDummy(Model m){
    try {
        data = dummyController.findDummyEntities();
    }
    catch (Exception e){}
    m.addAttribute("data", data);
    return "dummy";
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    
    @PostMapping(value="/newdata", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView newDummy(@RequestParam("gambar") MultipartFile file, HttpServletRequest http) throws ParseException, Exception{      
        Dummy dumdata = new Dummy();
        int id = Integer.parseInt(http.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(http.getParameter("tanggal"));
        byte[] img = file.getBytes();
        dumdata.setId(id);
        dumdata.setTanggal(date);
        dumdata.setGambar(img);
        
        dummyController.create(dumdata);
        return new RedirectView("/read");
    }
    
    @RequestMapping(value = "/img", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
        Dummy dumdata = dummyController.findDummy(id);
        byte[] img = dumdata.getGambar();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
    }
    
    @GetMapping("/delete/{id}")
    public RedirectView deleteDummy(@PathVariable int id) throws Exception {
        dummyController.destroy(id);
        return new RedirectView("/read");
    }
    
    @RequestMapping("/edit/{id}")
    public String updateDummy(@PathVariable("id") int id, Model m) throws Exception {
        Dummy dumdata = dummyController.findDummy(id);
        m.addAttribute("data", dumdata);
        return "dummy/edit";
    }
    
    @PostMapping(value = "/perbarui/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView updateDummy(@RequestParam("gambar") MultipartFile file, HttpServletRequest http)
            throws ParseException, Exception {
        Dummy dumdata = new Dummy();

        int id = Integer.parseInt(http.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(http.getParameter("tanggal"));
        byte[] img = file.getBytes();
        dumdata.setId(id);
        dumdata.setTanggal(date);
        dumdata.setGambar(img);
        dummyController.edit(dumdata);
        return new RedirectView("/read");
    }
}