package de.tdlabs.apps.screencaster.filestore;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;

interface FileStore {

  Path save(MultipartFile file);

  InputStream getContents(Path path);
}
