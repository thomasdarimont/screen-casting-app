package de.tdlabs.apps.screencaster.notes;

import de.tdlabs.apps.screencaster.config.WebsocketDestinations;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class SimpleNoteService implements NoteService {

  private final NoteRepository noteRepository;

  private final SimpMessagingTemplate messagingTemplate;

  private final MarkdownFormatter markdownFormatter;

  public Note save(Note note) {

    String rendered = markdownFormatter.format(note.getText());

    note.setText(rendered);

    Note saved = noteRepository.save(note);

    this.messagingTemplate.convertAndSend(WebsocketDestinations.TOPIC_NOTES, NoteEvent.created(saved));

    return saved;
  }


  public Note findById(Long id) {
    return noteRepository.findOne(id);
  }

  public List<Note> findAll() {
    return noteRepository.findAllByOrderByCreatedAtAsc();
  }

  public void delete(Note note) {
    noteRepository.delete(note);

    this.messagingTemplate.convertAndSend("/topic/notes", NoteEvent.deleted(note));
  }
}
