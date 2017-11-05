package de.tdlabs.apps.screencaster.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {

  private final NoteService noteService;

  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  ResponseEntity<Note> createNote(Note note, UriComponentsBuilder uriBuilder, HttpServletRequest request) {

    Note saved = noteService.save(note);
    URI location = uriBuilder.path("/notes/{id}").buildAndExpand(saved.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping("/{id}")
  ResponseEntity<Note> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(noteService.findById(id));
  }

  @GetMapping
  ResponseEntity<List<Note>> findAll() {
    return ResponseEntity.ok(noteService.findAll());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  ResponseEntity<Note> delete(@PathVariable("id") Long id, HttpServletRequest request) {

    Note note = noteService.findById(id);
    if (note == null) {
      return ResponseEntity.notFound().build();
    }

    noteService.delete(note);

    return ResponseEntity.noContent().build();
  }
}
