package dungeon.eater.events.dialog;

import dungeon.eater.events.core.Event;
import dungeon.eater.dialogs.core.ADialog;
import lombok.Getter;

@Getter
public abstract class ADialogEvent extends Event {

    protected Class<? extends ADialog> dialogClass;

    @Override
    public void reset () {
        super.reset();
        dialogClass = null;
    }
}
