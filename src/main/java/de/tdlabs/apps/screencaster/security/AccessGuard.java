package de.tdlabs.apps.screencaster.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessGuard {

  private final HttpServletRequest currentRequest;

  public boolean isStreamerRequest() {
    String remoteAddr = currentRequest.getRemoteAddr();
    String localAddr = currentRequest.getLocalAddr();

    if (remoteAddr.equals(localAddr)) {
      return true;
    }

    log.warn("Access denied. remoteAddr=%s localAddr=%s%n", remoteAddr, localAddr);
    return false;
  }
}
