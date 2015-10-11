package de.pat.fxsettings.modules;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;

import org.controlsfx.control.CheckListView;

import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;


public class CheckBoxListViewModule<T> extends AbstractFXSettingsModule<T>
{

    private CheckListView<T> checkListView;

    public CheckBoxListViewModule(String fieldName, ObservableList<T> setting, ObservableList<T> values)
    {
        super(fieldName, setting, values);
    }

    @Override
    public void buildNode(GridPane content)
    {
        checkListView = new CheckListView<>();
        checkListView.setItems(values);

        checkListView.setMaxWidth(Double.MAX_VALUE);
        settings.addListener((ListChangeListener<? super T>) (e) -> {
            checkListView.getCheckModel().clearChecks();
            for (T item : settings)
            {
                checkListView.getCheckModel().check(item);
            }
});
        content.getChildren().add(checkListView);
        GridPane.setConstraints(checkListView, 1, 0);

        hasChanges = Bindings.createBooleanBinding(() -> !settings.containsAll(checkListView.getCheckModel().getCheckedItems()) || !checkListView.getCheckModel().getCheckedItems().containsAll(settings), checkListView.getCheckModel().getCheckedItems(), settings);

        Platform.runLater(() -> {
            for (T item : settings)
            {
                checkListView.getCheckModel().check(item);
            }
        });
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        settings.setAll(checkListView.getCheckModel().getCheckedItems());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        checkListView.getCheckModel().clearChecks();
        for (T item : settings)
        {
            checkListView.getCheckModel().check(item);
        }

    }

}
