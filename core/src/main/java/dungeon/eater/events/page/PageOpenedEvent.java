package dungeon.eater.events.page;

import dungeon.eater.managers.API;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.pages.core.APage;

public class PageOpenedEvent extends APageEvent {

    public static void fire (Class<? extends APage> pageClass) {
        final PageOpenedEvent event = API.get(EventModule.class).obtainEvent(PageOpenedEvent.class);
        event.pageClass = pageClass;
        API.get(EventModule.class).fireEvent(event);
    }
}

