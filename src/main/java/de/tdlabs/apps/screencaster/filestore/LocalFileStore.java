package de.tdlabs.apps.screencaster.filestore;

import de.tdlabs.apps.screencaster.ScreenCasterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
class LocalFileStore implements FileStore {

  private final String storeLocation;

  private final AtomicLong counter = new AtomicLong();

  public LocalFileStore(ScreenCasterProperties props) {
    this.storeLocation = props.getFileStore().getLocation();
  }

  @Override
  public Path save(MultipartFile file) {

    LocalDateTime now = LocalDateTime.now();

    String folderName = DateTimeFormatter.ofPattern("'upload/'yyyy-MM-dd").format(now);
    long differencer = counter.incrementAndGet() % Long.MAX_VALUE;
    String filename = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss-SSS").format(now);

    File folder = new File(storeLocation, folderName);

    if (!folder.mkdirs()) {
      log.error("Could not create folder {}", folder);
    }

    File destination = new File(folder, filename + "_" + differencer);

    try {
      Files.copy(file.getInputStream(), destination.toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return destination.toPath();
  }

  @Override
  public InputStream getContents(Path path) {

    try {
      return new BufferedInputStream(Files.newInputStream(path));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
