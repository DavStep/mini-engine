package dungeon.eater.events.example;

import dungeon.eater.managers.API;
import dungeon.eater.events.core.Event;
import dungeon.eater.events.core.EventModule;

public class EventExample extends Event {
    public String test;

    public static void fire (String test) {
        final EventModule eventModule = API.get(EventModule.class);
        final EventExample event = eventModule.obtainEvent(EventExample.class);
        event.test = test;
        API.get(EventModule.class).fireEvent(event);
    }
}
