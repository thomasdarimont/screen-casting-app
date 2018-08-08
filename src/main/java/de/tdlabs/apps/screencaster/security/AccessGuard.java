package de.tdlabs.apps.screencaster.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AccessGuard {

  private final HttpServletRequest currentRequest;

  public boolean isStreamerRequest() {
    return currentRequest.getRemoteAddr().equals(currentRequest.getLocalAddr());
  }
}
