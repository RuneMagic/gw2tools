package de.pat.fxsettings;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.runemagic.gw2tools.GW2Tools;
import de.pat.fxsettings.modules.*;
import de.pat.fxsettings.moduletypes.FXModuleType;
import de.pat.fxsettings.moduletypes.FXSettingsModule;
import de.pat.fxsettings.serializer.FXSettingsSerializer;
import de.pat.fxsettings.sheet.FXSettingsSheet;
import de.pat.fxsettings.types.FXListSetting;
import de.pat.fxsettings.types.FXSetting;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class FXSettingsManager
{

    private Map<FXSettingsSerializerType, FXSettingsSerializer> serrializers = new HashMap<>();
    private Map<FXModuleType, Class<? extends FXSettingsModule>> modules = new HashMap<>();

    public FXSettingsManager()
    {
        registerSettingsModules();
    }

    public void registerSerializer(FXSettingsSerializerType type, FXSettingsSerializer serializer)
    {
        serrializers.put(type, serializer);
    }

    public FXSettingsSheetPane buildFXSettingsSheet(FXSettingsSheet sheet)
    {
        return buildFXSettingsSheet(sheet, null);
    }

    public FXSettingsSheetPane buildFXSettingsSheet(FXSettingsSheet sheet, Label image)
    {
        ListMultimap<String, FXSettingsModule> categories = ArrayListMultimap.create();
        Map<String, TitledPane> categoryNodes = new HashMap<>();
        buildFXListModules(sheet, categories);
        buildFXModules(sheet, categories);
        FXSettingsSheetPane root = new FXSettingsSheetPane(sheet.getSettingsID(), image);

        Accordion settings = new Accordion();
        settings.setMaxWidth(Double.MAX_VALUE);
        settings.setMaxHeight(Double.MAX_VALUE);

        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(0, 0, 10, 0));

        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchField.textProperty().addListener((search, oldValue, searchText) -> {
            if (searchText == null || searchText.length() <= 0)
            {
                for (String category : categories.keySet())
                {
                    for (FXSettingsModule loop : categories.get(category))
                    {
                        loop.getContent().setVisible(true);
                    }
                    TitledPane cat = categoryNodes.get(category);

                    if (!settings.getPanes().contains(cat))
                    {
                        insertCategory(settings, cat);
                    }
                }
                selectCategory(settings, settings.getPanes().get(0));
            } else
            {
                for (String category : categories.keySet())
                {
                    for (FXSettingsModule loop : categories.get(category))
                    {
                        if (loop.getFieldName().toLowerCase().contains(searchText.toLowerCase()))
                        {
                            loop.getContent().setVisible(true);
                        } else
                        {
                            loop.getContent().setVisible(false);
                        }
                    }
                    TitledPane cat = categoryNodes.get(category);
                    if (isCategoryEmpty(cat))
                    {
                        if (settings.getPanes().contains(cat))
                        {
                            settings.getPanes().remove(cat);
                        }
                        if (cat.isExpanded()) cat.setExpanded(false);
                    } else
                    {
                        if (!settings.getPanes().contains(cat))
                        {
                            insertCategory(settings, cat);
                        }
                        selectCategory(settings, settings.getPanes().get(0));
                    }
                }
            }
        });

        searchBar.getChildren().addAll(searchField);

        final ObservableList<BooleanBinding> hasChangesList = FXCollections.observableArrayList();

        for (String category : categories.keySet())
        {
            for (FXSettingsModule loop : categories.get(category))
            {
                Node content = loop.getContent();
                loop.hasChangesProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue)
                    {
                        content.setStyle("-fx-background-color: rgba(255, 255, 0, 0.1)");
                    }else
                    {
                        content.setStyle("");
                    }
                });
                TitledPane categoryPane = categoryNodes.get(category);
                if (categoryPane == null)
                {
                    VBox catContent = new VBox(10);

                    ToolBar toolbar = new ToolBar();
                    sheet.initToolbar(category, toolbar);
                    if (toolbar.getItems().size() != 0) catContent.getChildren().add(toolbar);

                    ScrollPane scroll = new ScrollPane(catContent);
                    scroll.setFitToHeight(true);
                    scroll.setFitToWidth(true);
                    scroll.setPadding(new Insets(10));

                    categoryPane = new TitledPane(category, scroll);
                    categoryPane.setUserData(categoryNodes.values().size());
                    categoryNodes.put(category, categoryPane);
                }
                ScrollPane catScroll = (ScrollPane) categoryPane.getContent();
                VBox catContent = (VBox) catScroll.getContent();
                catContent.getChildren().add(content);
                content.managedProperty().bind(content.visibleProperty());
                hasChangesList.add(loop.hasChangesProperty());
            }
        }

        Button submit = new Button("Apply");
        submit.setPrefWidth(75);
        submit.setDisable(true);
        submit.getStyleClass().add("confirm-button");
        submit.setOnAction((e) -> {

            List<String> changedValues = new ArrayList<>();

            for(FXSettingsModule module : categories.values())
            {
                if(module.hasChangesProperty().getValue())
                {
                    changedValues.add(module.getFieldName());
                    module.onSubmit();
                }
            }

            saveFXSettingsSheet(sheet, changedValues);
        });

        Button cancel = new Button("Revert");
        cancel.setPrefWidth(75);
        cancel.setDisable(true);
        cancel.getStyleClass().add("cancel-button");
        cancel.setOnAction((e) -> categories.values().forEach(de.pat.fxsettings.moduletypes.FXSettingsModule::onCancel));

        settings.getPanes().addAll(categoryNodes.values());

        OrConstraintBinding changes = new OrConstraintBinding(hasChangesList);
        BooleanProperty hasChanges = changes.valueProperty();
        hasChanges.addListener((e, oldValue, newValue) -> {
            submit.setDisable(!newValue);
            cancel.setDisable(!newValue);
        });

        HBox toolbar = new HBox(10);
        toolbar.getChildren().addAll(cancel, submit);
        toolbar.setPadding(new Insets(10, 0, 0, 0));
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        root.setTop(searchBar);
        root.setCenter(settings);
        root.setBottom(toolbar);

        settings.setExpandedPane(settings.getPanes().get(0));

        return root;
    }

    public boolean loadFXSettingsSheet(FXSettingsSheet sheet)
    {
        if (sheet == null) return false;
        try
        {
            loadFXListSettings(sheet);
            loadFXSettings(sheet);
            sheet.onLoad();
        } catch (Exception e)
        {
//			GW2Tools.log().error("Failed to load FXSettingsSheet: " + sheet.getClass().getSimpleName());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveFXSettingsSheet(FXSettingsSheet sheet)
    {
        return saveFXSettingsSheet(sheet, null);
    }

    public boolean saveFXSettingsSheet(FXSettingsSheet sheet, List<String> changes)
    {
        if (sheet == null) return false;
        try
        {
            saveFXSettings(sheet, changes);
            saveFXListSettings(sheet, changes);
            sheet.onSave();
        } catch (Exception e)
        {
//			GW2Tools.log().error("Failed to save FXSettingsSheet: " + sheet.getClass().getSimpleName());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void registerSettingsModules()
    {
        modules.put(FXModuleType.CHECK_BOX, CheckBoxModule.class);
        modules.put(FXModuleType.COLOR_PICKER, ColorPickerModule.class);
        modules.put(FXModuleType.COMBO_BOX, ComboBoxModule.class);
        modules.put(FXModuleType.DATE_PICKER, DatePickerModule.class);
        modules.put(FXModuleType.DOUBLE_FIELD, DoubleFieldModule.class);
        modules.put(FXModuleType.INTEGER_FIELD, IntegerFieldModule.class);
        modules.put(FXModuleType.PASSWORD_FIELD, PasswordFieldModule.class);
        modules.put(FXModuleType.SLIDER, SliderModule.class);
        modules.put(FXModuleType.TEXT_AREA, TextAreaModule.class);
        modules.put(FXModuleType.TEXT_FIELD, TextFieldModule.class);
        modules.put(FXModuleType.TOGGLE_BUTTON, ToggleButtonModule.class);
        modules.put(FXModuleType.CHECKLIST_VIEW, CheckBoxListViewModule.class);
    }

    @SuppressWarnings({"rawtypes"})
    private void saveFXSettings(FXSettingsSheet sheet, List<String> changes) throws Exception
    {
        FXSettingsSerializerType serializerType = sheet.getSerializerType();
        FXSettingsSerializer serializer = serrializers.get(serializerType);
        if (serializer == null)
            throw new IllegalArgumentException("serializer " + serializerType.name() + " is not registered!");

        String settingsID = sheet.getSettingsID();
        Collection<Field> fxFields = sheet.getFXSettingFields(FXSetting.class);

        for (Field field : fxFields)
        {

            if (!WritableValue.class.isAssignableFrom(field.getType()))
                throw new InvalidFXSettingTypeException(field.getType().getName() + " is not assignable from " + WritableValue.class.getName());

            WritableValue val = (WritableValue) field.get(sheet);

            FXSetting ant = field.getAnnotation(FXSetting.class);
            if (ant == null) continue;

            if(!ant.saveSystemChanges())
            {
                System.out.println(field.getName());
                System.out.println(changes);
                if(changes == null || (!changes.contains(field.getName()) && !changes.contains(ant.displayName())))continue;
            }

            String fieldName = field.getName();

            if (serializer.saveSetting(settingsID, fieldName, val.getValue()))
            {
                GW2Tools.log().debug("Saved setting: " + fieldName);
            } else
            {
                GW2Tools.log().error("Failed to save setting: " + fieldName);
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    private void saveFXListSettings(FXSettingsSheet sheet, List<String> changes) throws Exception
    {
        FXSettingsSerializerType serializerType = sheet.getSerializerType();
        FXSettingsSerializer serializer = serrializers.get(serializerType);
        if (serializer == null)
            throw new IllegalArgumentException("serializer " + serializerType.name() + " is not registered!");

        String settingsID = sheet.getSettingsID();
        Collection<Field> fxFields = sheet.getFXSettingFields(FXListSetting.class);

        for (Field field : fxFields)
        {

            if (!ObservableList.class.isAssignableFrom(field.getType()))
                throw new InvalidFXSettingTypeException(field.getType().getName() + " is not assignable from " + WritableValue.class.getName());

            ObservableList val = (ObservableList) field.get(sheet);

            FXListSetting ant = field.getAnnotation(FXListSetting.class);
            if (ant == null) continue;

            if(ant.saveSystemChanges() || changes == null || !changes.contains(field.getName()))continue;

            String fieldName = field.getName();
            if (serializer.saveValues(settingsID, fieldName, val))
            {
                GW2Tools.log().debug("Saved list setting: " + fieldName);
            } else
            {
                GW2Tools.log().error("Failed to save list setting: " + fieldName);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadFXSettings(FXSettingsSheet sheet) throws Exception
    {

        FXSettingsSerializerType serializerType = sheet.getSerializerType();
        FXSettingsSerializer serializer = serrializers.get(serializerType);
        if (serializer == null)
            throw new IllegalArgumentException("serializer " + serializerType.name() + " is not registered!");

        String settingsID = sheet.getSettingsID();
        Collection<Field> fxFields = sheet.getFXSettingFields(FXSetting.class);

        for (Field field : fxFields)
        {
            if (!WritableValue.class.isAssignableFrom(field.getType()))
                throw new InvalidFXSettingTypeException(field.getType().getName() + " is not assignable from " + WritableValue.class.getName());

            FXSetting ant = field.getAnnotation(FXSetting.class);
            if (ant == null) continue;

            Class<?> valueType = ant.type();
            WritableValue property = (WritableValue) field.getType().newInstance();

            String fieldName = field.getName();
            Object val = serializer.loadSetting(valueType, settingsID, fieldName);
            if (val == null && ant.hasDefaultValue()) continue;
            property.setValue(val);
            field.set(sheet, property);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadFXListSettings(FXSettingsSheet sheet) throws Exception
    {

        FXSettingsSerializerType serializerType = sheet.getSerializerType();
        FXSettingsSerializer serializer = serrializers.get(serializerType);
        if (serializer == null)
            throw new IllegalArgumentException("serializer " + serializerType.name() + " is not registered!");

        String settingsID = sheet.getSettingsID();
        Collection<Field> fxFields = sheet.getFXSettingFields(FXListSetting.class);

        for (Field field : fxFields)
        {
            if (!ObservableList.class.isAssignableFrom(field.getType()))
                throw new InvalidFXSettingTypeException(field.getType().getName() + " is not assignable from " + ObservableList.class.getName());

            FXListSetting ant = field.getAnnotation(FXListSetting.class);
            if (ant == null) continue;

            Class<?> valueType = ant.type();
            ObservableList list = FXCollections.observableArrayList();

            String fieldName = field.getName();

            Collection val = serializer.loadValues(valueType, settingsID, fieldName);
            if (val == null && ant.hasDefaultValue()) continue;

            if (val != null) list.addAll(serializer.loadValues(valueType, settingsID, fieldName));
            field.set(sheet, list);
        }
    }

    private void insertCategory(Accordion accord, TitledPane pane)
    {
        int index = (int) pane.getUserData();
        if (accord.getPanes().size() == 0)
        {
            accord.getPanes().add(pane);
        } else if (index > accord.getPanes().size() - 1)
        {
            int newIndex = 0;
            for (TitledPane loop : accord.getPanes())
            {
                int exIndex = (int) loop.getUserData();
                if (exIndex > index) break;
                newIndex = exIndex;
            }
            accord.getPanes().add(newIndex, pane);
        } else
        {
            accord.getPanes().add(index, pane);
        }
    }

    private boolean isCategoryEmpty(TitledPane pane)
    {
        ScrollPane scroll = (ScrollPane) pane.getContent();
        for (Node item : ((VBox) scroll.getContent()).getChildren())
        {
            if (item.isVisible())
                return false;
        }
        return true;
    }

    private void selectCategory(Accordion pane, TitledPane category)
    {
        pane.getPanes().stream().filter(TitledPane::isExpanded).forEach(loop -> loop.setExpanded(false));
        pane.setExpandedPane(category);
    }

    private void buildFXModules(FXSettingsSheet sheet, ListMultimap<String, FXSettingsModule> categories)
    {
        try
        {
            for (Field field : sheet.getFXSettingFields(FXSetting.class))
            {
                FXSetting ant = field.getAnnotation(FXSetting.class);
                if (ant == null || !ant.userEditable()) continue;

                String category = ant.category();
                String valuesField = ant.valuesField();
                String fieldName = field.getName();

                double minValue = ant.minValue();
                double maxValue = ant.maxValue();

                FXModuleType moduleType = ant.moduleType();
                ObservableList<?> valuesList = null;
                Property<?> fxProperty;

                if (!(field.get(sheet) instanceof Property))
                    throw new IllegalArgumentException("FX field must be of type Property to attach a fx module!");
                fxProperty = (Property<?>) field.get(sheet);

                if (!"not_set".equals(valuesField))
                {
                    Field valField = sheet.getFXSettingField(valuesField);
                    if (valField == null)
                        throw new IllegalArgumentException("Values field " + valuesField + " does not exist in settings sheet!");
                    Object values = valField.get(sheet);
                    if (!(values instanceof ObservableList))
                        throw new IllegalArgumentException("Values field " + valuesField + "is invalid type! Expected ObservableList.");
                    valuesList = (ObservableList<?>) values;
                }

                FXSettingsModule module;
                Class<? extends FXSettingsModule> moduleClass = modules.get(moduleType);
                if(ant.displayName().length() > 0)fieldName = ant.displayName();
                if (valuesList != null)
                {
                    Constructor<? extends FXSettingsModule> constructor = moduleClass.getConstructor(String.class, Property.class, ObservableList.class);
                    constructor.setAccessible(true);
                    module = constructor.newInstance(fieldName, fxProperty, valuesList);
                } else
                {
                    Constructor<? extends FXSettingsModule> constructor;
                    try
                    {
                        constructor = moduleClass.getConstructor(String.class, Property.class, MinValue.class, MaxValue.class);
                        if (constructor != null)
                        {
                            constructor.setAccessible(true);
                            module = constructor.newInstance(fieldName, fxProperty, new MinValue(minValue), new MaxValue(maxValue));
                        } else throw new NoSuchMethodError();
                    } catch (Exception e)
                    {
                        constructor = moduleClass.getConstructor(String.class, Property.class);
                        constructor.setAccessible(true);
                        module = constructor.newInstance(fieldName, fxProperty);
                    }

                }
                //TODO add module
                module.buildFXNode();
                categories.put(category, module);

            }
        } catch (IllegalAccessException | SecurityException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot access FXSetting field to create sheet");
        } catch (NoSuchMethodException | InstantiationException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot initiate FXSettingsModule with given arguments!");
        }
    }

    private void buildFXListModules(FXSettingsSheet sheet, ListMultimap<String, FXSettingsModule> categories)
    {
        try
        {
            for (Field field : sheet.getFXSettingFields(FXListSetting.class))
            {
                FXListSetting ant = field.getAnnotation(FXListSetting.class);
                if (ant == null || !ant.userEditable()) continue;

                String category = ant.category();
                String valuesField = ant.valuesField();
                String fieldName = field.getName();

                FXModuleType moduleType = ant.moduleType();
                ObservableList<?> valuesList = null;
                ObservableList<?> fxProperty;

                if (!(field.get(sheet) instanceof ObservableList))
                    throw new IllegalArgumentException("FX field must be of type ObservableList to attach a fx module!");
                fxProperty = (ObservableList<?>) field.get(sheet);

                if (!"not_set".equals(valuesField))
                {
                    Field valField = sheet.getFXSettingField(valuesField);
                    if (valField == null)
                        throw new IllegalArgumentException("Values field " + valuesField + " does not exist in settings sheet!");

                    Object values = valField.get(sheet);
                    if (!(values instanceof ObservableList))
                        throw new IllegalArgumentException("Values field " + valuesField + "is invalid type! Expected ObservableList.");
                    valuesList = (ObservableList<?>) values;
                }

                FXSettingsModule module;
                Class<? extends FXSettingsModule> moduleClass = modules.get(moduleType);
                if(ant.displayName().length() > 0)fieldName = ant.displayName();
                if (valuesField != null)
                {
                    Constructor<? extends FXSettingsModule> constructor = moduleClass.getConstructor(String.class, ObservableList.class, ObservableList.class);
                    module = constructor.newInstance(fieldName, fxProperty, valuesList);
                } else
                {
                    Constructor<? extends FXSettingsModule> constructor;
                    if(moduleClass == CheckBoxListViewModule.class)
                    {
                        constructor = moduleClass.getConstructor(String.class, ObservableList.class, ObservableList.class);
                        module = constructor.newInstance(fieldName, fxProperty, valuesList);
                    }else
                    {
                        constructor = moduleClass.getConstructor(String.class, ObservableList.class);
                        module = constructor.newInstance(fieldName, fxProperty);
                    }
                }

                module.buildFXNode();
                categories.put(category, module);
            }
        } catch (IllegalAccessException | SecurityException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot access FXListSetting field to create sheet");
        } catch (NoSuchMethodException | InstantiationException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot initiate FXSettingsModule with given arguments!");
        }
    }

}
