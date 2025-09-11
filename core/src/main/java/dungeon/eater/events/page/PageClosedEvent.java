package dungeon.eater.events.page;

import dungeon.eater.managers.API;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.pages.core.APage;

public class PageClosedEvent extends APageEvent {

    public static void fire (Class<? extends APage> pageClass) {
        final PageClosedEvent event = API.get(EventModule.class).obtainEvent(PageClosedEvent.class);
        event.pageClass = pageClass;
        API.get(EventModule.class).fireEvent(event);
    }
}

