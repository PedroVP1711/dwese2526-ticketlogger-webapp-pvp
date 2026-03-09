package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;

import java.util.Map;

public interface AppUrlService {
    String buildResetUrl(String rawToken);
    String buildUrl(String path, Map<String, String> queryParams);
}
