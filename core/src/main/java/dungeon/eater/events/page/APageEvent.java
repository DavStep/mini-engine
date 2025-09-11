package dungeon.eater.events.page;

import dungeon.eater.events.core.Event;
import dungeon.eater.pages.core.APage;
import lombok.Getter;

@Getter
public class APageEvent extends Event {

    protected Class<? extends APage> pageClass;

    @Override
    public void reset () {
        super.reset();
        pageClass = null;
    }
}
