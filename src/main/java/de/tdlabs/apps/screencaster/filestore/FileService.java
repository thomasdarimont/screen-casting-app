package de.tdlabs.apps.screencaster.filestore;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface FileService {

  FileInfo save(MultipartFile file);

  FileEntity loadFile(UUID id);

  InputStream getContents(FileEntity file);
}
