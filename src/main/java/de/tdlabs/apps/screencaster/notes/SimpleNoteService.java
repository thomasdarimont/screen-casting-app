package de.tdlabs.apps.screencaster.notes;

import de.tdlabs.apps.screencaster.config.WebsocketDestinations;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
class SimpleNoteService implements NoteService {

  private final NoteRepository noteRepository;

  private final SimpMessagingTemplate messagingTemplate;

  private final MarkdownFormatter markdownFormatter;

  public NoteEntity save(NoteEntity noteEntity) {

    boolean newNote = noteEntity.isNew();

    noteEntity.setHtml(markdownFormatter.format(noteEntity.getText()));
    NoteEntity saved = noteRepository.save(noteEntity);

    Note note = saved.toNote();

    NoteEvent noteEvent = newNote ? NoteEvent.created(note) : NoteEvent.updated(note);

    this.messagingTemplate.convertAndSend(WebsocketDestinations.TOPIC_NOTES, noteEvent);

    return saved;
  }

  public void delete(NoteEntity noteEntity) {

    noteRepository.delete(noteEntity);
    this.messagingTemplate.convertAndSend("/topic/notes", NoteEvent.deleted(noteEntity.toNote()));
  }

  public void deleteAll() {
    noteRepository.deleteAllInBatch();
  }

  @Transactional(readOnly = true)
  public NoteEntity findById(Long id) {
    return noteRepository.findById(id).map(this::renderNoteHtml).orElse(null);
  }

  @Transactional(readOnly = true)
  public List<NoteEntity> findAll() {

    Stream<NoteEntity> noteStream = noteRepository.findAllByOrderByCreatedAtAsc().stream();
    return noteStream.map(this::renderNoteHtml).collect(Collectors.toList());
  }

  NoteEntity renderNoteHtml(NoteEntity noteEntity) {

    String text = noteEntity.getText();
    String html = markdownFormatter.format(text);
    noteEntity.setHtml(html);

    return noteEntity;
  }
}
