package tn.talan.tripaura_backend.entities.file_upload;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "file_process")
public class FileProcess {

    @Indexed
    @NotEmpty(message = "fileName cannot be empty")
    private String fileName;

    @Indexed
    @NotEmpty(message = "fileContent  cannot be empty")
    private  String fileContent;

}
