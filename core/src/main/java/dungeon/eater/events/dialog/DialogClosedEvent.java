package dungeon.eater.events.dialog;

import dungeon.eater.managers.API;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.dialogs.core.ADialog;

public class DialogClosedEvent extends ADialogEvent {

    public static void fire (Class<? extends ADialog> dialogClass) {
        final DialogClosedEvent event = API.get(EventModule.class).obtainEvent(DialogClosedEvent.class);
        event.dialogClass = dialogClass;
        API.get(EventModule.class).fireEvent(event);
    }
}
