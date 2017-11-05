package de.tdlabs.apps.screencaster.notes;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Note {

  @Id
  @GeneratedValue
  Long id;

  @NotEmpty
  @Column(length = 16000)
  String text;

  @CreatedDate
  LocalDateTime createdAt;

  @LastModifiedDate
  LocalDateTime updatedAt;
}
