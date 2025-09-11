package dungeon.eater.events.dialog;

import dungeon.eater.managers.API;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.dialogs.core.ADialog;

public class DialogOpenedEvent extends ADialogEvent {

    public static void fire (Class<? extends ADialog> dialogClass) {
        final DialogOpenedEvent event = API.get(EventModule.class).obtainEvent(DialogOpenedEvent.class);
        event.dialogClass = dialogClass;
        API.get(EventModule.class).fireEvent(event);
    }
}
