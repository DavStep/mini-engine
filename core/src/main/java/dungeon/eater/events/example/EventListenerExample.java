package dungeon.eater.events.example;

import engine.events.core.EventHandler;
import engine.events.core.EventListener;
import engine.events.core.EventModule;
import engine.events.core.EventPriority;
import dungeon.eater.managers.API;

public class EventListenerExample implements EventListener {

    public EventListenerExample () {
        API.get(EventModule.class).registerListener(this); // do not forget to register listener
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEventExample (EventExample event) {
        System.out.println(event.test);
    }
}
