package de.tdlabs.apps.screencaster.filestore;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file")
@EntityListeners(AuditingEntityListener.class)
public class FileEntity {

  @Id
  String id;

  String path;

  String name;

  String contentType;

  @CreatedDate
  LocalDateTime createdAt;

  @LastModifiedDate
  LocalDateTime updatedAt;

  long sizeInBytes;

  public FileInfo toInfo() {
    return new FileInfo(id, name, contentType, sizeInBytes);
  }
}

