package de.tdlabs.apps.screencaster.filestore;

import lombok.Data;

@Data
public class FileInfo {

  private final String id;

  private final String name;

  private final String contentType;

  private final long sizeInBytes;
}
