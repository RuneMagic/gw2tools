package de.pat.fxsettings;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

public class OrConstraintBinding
{

    private final ObservableList<BooleanBinding> binds;
    private final BooleanProperty value = new SimpleBooleanProperty(false);

    public OrConstraintBinding(ObservableList<BooleanBinding> binds)
    {
        this.binds = binds;
        this.binds.forEach((bind) -> bind.addListener((e, oldValue, newValue) -> {
            if (newValue)
            {
                value.set(true);
            } else
            {
                boolean hasHigh = false;
                for (BooleanBinding loop : this.binds)
                {
                    if (loop.getValue())
                    {
                        hasHigh = true;
                    }
                }
                value.set(hasHigh);
            }
        }));
    }

    public BooleanProperty valueProperty()
    {
        return value;
    }

}
