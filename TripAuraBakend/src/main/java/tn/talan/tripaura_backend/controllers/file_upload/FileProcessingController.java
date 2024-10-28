package tn.talan.tripaura_backend.controllers.file_upload;

import com.nimbusds.jose.util.Resource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.services.file_upload.FileProcessingService;

@RestController
@RequestMapping("/file")

public class FileProcessingController {

    @Autowired
    private FileProcessingService fileProcessingService;

    @GetMapping("/list")
    public ResponseEntity<?> getFileList() {
        return new ResponseEntity<>(fileProcessingService.fileList(), HttpStatus.OK);
    }

    @GetMapping(value = "/download/{name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadFile(@PathVariable(value = "name") String fileName) {
        Resource file = (Resource) fileProcessingService.downloadFile(fileName);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
        }
    }

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String status = fileProcessingService.uploadFile(multipartFile);
        return "CREATED".equals(status) ? new ResponseEntity<>(HttpStatus.CREATED) :
                ("EXIST".equals(status) ? new ResponseEntity<>(HttpStatus.NOT_MODIFIED) : new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

}
