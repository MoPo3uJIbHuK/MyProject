package files.controllers;

import files.dao.FileDescriptorDao;
import files.models.FileDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.nio.charset.StandardCharsets;

@RestController()
@RequestMapping("/")
public class FileRestController {
    private final FileDescriptorDao fileDAO;
    private final String pathFolder;

    @Autowired
    public FileRestController(@Qualifier("filesDaoHiber") FileDescriptorDao fileDAO, @Value("${folder.path}") String pathFolder) {
        this.fileDAO = fileDAO;
        this.pathFolder = pathFolder;
    }

    @GetMapping
    public ModelAndView getFiles(Model model) {
        model.addAttribute("files", fileDAO.getFiles());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/{id}/descriptor")
    public ResponseEntity<FileDescriptor> getFile(@PathVariable int id) {
        FileDescriptor file = fileDAO.getFile(id);
        return file == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(file);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteFile(@PathVariable int id) {
        fileDAO.deleteFile(id);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/");
        return mav;
    }

    @PostMapping()
    public ModelAndView UploadFile(@RequestParam("file") MultipartFile file, Model model) {
        fileDAO.addFile(file);
        model.addAttribute("files", fileDAO.getFiles());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/");
        return mav;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable int id) {
        try {
            FileDescriptor fileDesc = fileDAO.getFile(id);
            String extension = fileDesc.getExtension().equals("none") ? "" : "." + fileDesc.getExtension();
            String fullName = pathFolder + fileDesc.getFileName() + extension;
            File file = new File(fullName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            MediaType mediaType = MediaType.MULTIPART_FORM_DATA;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileDesc.getFileName() + extension, StandardCharsets.UTF_8).build());
            return file == null ? ResponseEntity.notFound().build() : ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(mediaType)
                    .body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

}
