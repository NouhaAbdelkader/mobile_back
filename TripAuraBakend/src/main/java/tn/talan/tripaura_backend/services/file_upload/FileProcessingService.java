package tn.talan.tripaura_backend.services.file_upload;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.util.List;

public interface FileProcessingService {

    List<String> fileList();

    String uploadFile(MultipartFile file);

    Resource downloadFile(String fileName);
}
