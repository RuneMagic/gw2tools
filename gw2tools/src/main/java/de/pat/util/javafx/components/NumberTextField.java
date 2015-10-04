package de.pat.util.javafx.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;

public class NumberTextField extends TextField
{

    private Class<? extends Number> numberType;
    private SimpleObjectProperty<Number> valueProperty = new SimpleObjectProperty<>();

    private Number minValue;
    private Number maxValue;

    public NumberTextField(Class<? extends Number> numberType, Number minValue, Number maxValue)
    {
        super();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numberType = numberType;
        textProperty().addListener((e, oldVal, newVal) -> {
            if (newVal.equalsIgnoreCase("-")) valueProperty.set(0);
            else valueProperty.set(castValue(newVal));
        });
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        StringBuilder sb = new StringBuilder(getText());
        sb.replace(start, end, text);
        String fullText = sb.toString();
        if (fullText.equalsIgnoreCase("-") || validate(fullText))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        text = getText() + text;
        if (text.equalsIgnoreCase("-") || validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private Number castValue(String text)
    {
        if (text == null || text.equals(""))
        {
            return 0;
        }
        if (numberType == Integer.class)
        {
            return Integer.parseInt(text);
        } else if (numberType == Double.class)
        {
            return Double.parseDouble(text);
        } else if (numberType == Long.class)
        {
            return Long.parseLong(text);
        } else if (numberType == Float.class)
        {
            return Float.parseFloat(text);
        }
        return 0;
    }

    private boolean validate(String text)
    {
        if (text == null || text.equals("")) return true;
        try
        {
            if (numberType == Integer.class)
            {
                Integer num = Integer.parseInt(text);
                return !(num > maxValue.doubleValue() || num < minValue.doubleValue());
            }

            if (numberType == Double.class)
            {
                Double num = Double.parseDouble(text);
                return !(num > maxValue.doubleValue() || num < minValue.doubleValue());
            }

            if (numberType == Long.class)
            {
                Long num = Long.parseLong(text);
                return !(num > maxValue.longValue() || num < minValue.longValue());
            }

            if (numberType == Float.class)
            {
                Float num = Float.parseFloat(text);
                return !(num > maxValue.floatValue() || num < minValue.floatValue());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public SimpleObjectProperty<Number> getValueProperty()
    {
        return valueProperty;
    }

}