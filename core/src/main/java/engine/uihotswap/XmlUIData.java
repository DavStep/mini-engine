package engine.uihotswap;

import com.badlogic.gdx.utils.*;
import lombok.Getter;

import java.util.Locale;

@Getter
public class XmlUIData implements IXmlUIData {

    @Getter
    private XmlKey key;
    public final ObjectMap<String, String> attributes = new ObjectMap<>();

    public final Array<XmlUIData> children = new Array<>();

    public XmlUIData (XmlReader.Element element) {
        load(element);
    }

    @Override
    public void load (XmlReader.Element element) {
        reset();

        // init class name
        key = XmlKey.getKey(element.getName());

        // init attributes
        final ObjectMap<String, String> attributes = element.getAttributes();
        if (attributes != null) {
            this.attributes.putAll(attributes);
        }

        // init children
        for (int i = 0; i < element.getChildCount(); i++) {
            final XmlReader.Element childElement = element.getChild(i);
            final XmlUIData childComponent = new XmlUIData(childElement);
            children.add(childComponent);
        }
    }

    private void reset () {
        attributes.clear();
        children.clear();
    }
}
