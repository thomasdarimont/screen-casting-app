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
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "note")
@EntityListeners(AuditingEntityListener.class)
public class NoteEntity {

  @Id
  @GeneratedValue
  Long id;

  @NotEmpty
  @Column(length = 64000)
  String text;

  @CreatedDate
  LocalDateTime createdAt;

  @LastModifiedDate
  LocalDateTime updatedAt;

  public static NoteEntity valueOf(Note note) {
    NoteEntity ne = new NoteEntity();
    ne.setText(note.getText());
    return ne;
  }

  public Note toNote() {

    Note n = new Note();
    n.setText(getText());
    n.setId(getId());
    n.setCreatedAt(getCreatedAt());
    n.setUpdatedAt(getUpdatedAt());

    return n;
  }
}
