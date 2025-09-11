package dungeon.eater.events.example;

import dungeon.eater.events.core.EventHandler;
import dungeon.eater.events.core.EventListener;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.events.core.EventPriority;
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
