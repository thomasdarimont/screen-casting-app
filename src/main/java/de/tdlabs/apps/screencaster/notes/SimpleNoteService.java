package de.tdlabs.apps.screencaster.notes;

import de.tdlabs.apps.screencaster.config.WebsocketDestinations;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class SimpleNoteService implements NoteService {

  private final NoteRepository noteRepository;

  private final SimpMessagingTemplate messagingTemplate;

  private final MarkdownFormatter markdownFormatter;

  public NoteEntity save(NoteEntity note) {

    String rendered = markdownFormatter.format(note.getText());
    note.setText(rendered);
    NoteEntity saved = noteRepository.save(note);
    this.messagingTemplate.convertAndSend(WebsocketDestinations.TOPIC_NOTES, NoteEvent.created(saved));

    return saved;
  }

  public void delete(NoteEntity note) {

    noteRepository.delete(note);
    this.messagingTemplate.convertAndSend("/topic/notes", NoteEvent.deleted(note));
  }

  public void deleteAll() {
    noteRepository.deleteAll();
  }

  @Transactional(readOnly = true)
  public NoteEntity findById(Long id) {
    return noteRepository.findOne(id);
  }

  @Transactional(readOnly = true)
  public List<NoteEntity> findAll() {
    return noteRepository.findAllByOrderByCreatedAtAsc();
  }
}
