package com.runemagic.gw2tools.commerce;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.runemagic.gw2tools.api.items.GW2Item;
import com.runemagic.gw2tools.api.items.MaterialPair;
import com.runemagic.gw2tools.util.StringTools;

public class MaterialProcessingCalculator
{
	private static final float TP_MULT=0.85f;

	private ListProperty<MaterialPair> materialPairs=new SimpleListProperty<>(FXCollections.observableArrayList());
	private ObjectProperty<MaterialPair> selected=new SimpleObjectProperty<>();
	private ObjectProperty<TPAction> action=new SimpleObjectProperty<>(TPAction.SELL);
	private BooleanProperty rawBuy=new SimpleBooleanProperty();
	private BooleanProperty processedBuy=new SimpleBooleanProperty();
	private StringProperty rawPriceStr=new SimpleStringProperty();
	private StringProperty processedPriceStr=new SimpleStringProperty();
	private IntegerProperty resultValue=new SimpleIntegerProperty();
	private StringProperty resultValueStr=new SimpleStringProperty();
	private StringProperty resultText=new SimpleStringProperty();

	public MaterialProcessingCalculator()
	{
		loadMaterialPairs();

		selected.addListener((obs, ov, nv) -> {
			rawPriceStr.unbind();
			GW2Item raw = nv.getRawMaterial();
			GW2Item proc = nv.getProcessedMaterial();
			rawPriceStr.bind(Bindings.createStringBinding(() -> {
				int val = rawBuy.get() ? raw.getBuyUnitPrice() : raw.getSellUnitPrice();
				return StringTools.formatMoney(val);
			}, rawBuy, raw.buyUnitPriceProperty(), raw.sellUnitPriceProperty()));
			processedPriceStr.unbind();
			processedPriceStr.bind(Bindings.createStringBinding(() -> {
				int val = processedBuy.get() ? proc.getBuyUnitPrice() : proc.getSellUnitPrice();
				return StringTools.formatMoney(val);
			}, processedBuy, proc.buyUnitPriceProperty(), proc.sellUnitPriceProperty()));
			resultValue.unbind();
			resultValue.bind(Bindings.createIntegerBinding(() -> {
				int rawVal = rawBuy.get() ? raw.getBuyUnitPrice() : raw.getSellUnitPrice();
				int procVal = processedBuy.get() ? proc.getBuyUnitPrice() : proc.getSellUnitPrice();
				TPAction act = action.get();
				switch (act)
				{
				case BUY: return procVal - rawVal*nv.getAmount();
				case SELL: return (int) ((float) procVal*TP_MULT - (float) (rawVal*nv.getAmount())*TP_MULT);
				case FLIP: return (int) ((float) procVal*TP_MULT - rawVal*nv.getAmount());
				}
				return 0;
			}, rawBuy, processedBuy, action, raw.buyUnitPriceProperty(), raw.sellUnitPriceProperty(), proc.buyUnitPriceProperty(), proc.sellUnitPriceProperty()));
			resultValueStr.unbind();
			resultValueStr.bind(Bindings.createStringBinding(() -> StringTools.formatMoney(resultValue.get()), resultValue));
			resultText.unbind();
			resultText.bind(Bindings.createStringBinding(() -> {
				int val=resultValue.get();
				TPAction act = action.get();
				switch (act)
				{
				case BUY:
					if (val<=0) return "Buy "+nv.getProcessedMaterial().getName();
					if (val>0) return "Buy "+nv.getRawMaterial().getName();
				case SELL:
					if (val<=0) return "Sell "+nv.getRawMaterial().getName();
					if (val>0) return "Sell "+nv.getProcessedMaterial().getName();
				case FLIP:
					if (val<=0) return "Flipping is not profitable";
					if (val>0) return "Flipping is profitable";
				}
				return "ERROR";
			}, resultValue));
		});

	}

	private void loadMaterialPairs()
	{
		//t6
		addMaterialPair("Orichalcum", 2, 19701, 19685);
		addMaterialPair("Gossamer", 2, 19745, 19746);
		addMaterialPair("Ancient Wood", 3, 19725, 19712);
		addMaterialPair("Hardened Leather", 2, 19732, 19737);
		//t5
		addMaterialPair("Mithril", 2, 19700, 19684);
		addMaterialPair("Silk", 3, 19748, 19747);
		addMaterialPair("Elder Wood", 3, 19722, 19709);
		addMaterialPair("Thick Leather", 3, 19729, 19735);
		//TODO the rest
	}

	private void addMaterialPair(String name, int amount, int raw, int proc)
	{
		materialPairs.add(new MaterialPair(name, amount, raw, proc));
	}

	public void update()
	{
		MaterialPair sel=getSelected();
		if (sel!=null) sel.update();
	}

	public ObservableList<MaterialPair> getMaterialPairs()
	{
		return materialPairs;
	}

	public MaterialPair getSelected()
	{
		return selected.get();
	}

	public ObjectProperty<MaterialPair> selectedProperty()
	{
		return selected;
	}

	public void setSelected(MaterialPair selected)
	{
		this.selected.set(selected);
	}

	public TPAction getAction()
	{
		return action.get();
	}

	public ObjectProperty<TPAction> actionProperty()
	{
		return action;
	}

	public void setAction(TPAction action)
	{
		this.action.set(action);
	}

	public boolean getRawBuy()
	{
		return rawBuy.get();
	}

	public BooleanProperty rawBuyProperty()
	{
		return rawBuy;
	}

	public void setRawBuy(boolean rawBuy)
	{
		this.rawBuy.set(rawBuy);
	}

	public boolean getProcessedBuy()
	{
		return processedBuy.get();
	}

	public BooleanProperty processedBuyProperty()
	{
		return processedBuy;
	}

	public void setProcessedBuy(boolean processedBuy)
	{
		this.processedBuy.set(processedBuy);
	}

	public String getRawPriceStr()
	{
		return rawPriceStr.get();
	}

	public ReadOnlyStringProperty rawPriceStrProperty()
	{
		return rawPriceStr;
	}

	public String getProcessedPriceStr()
	{
		return processedPriceStr.get();
	}

	public ReadOnlyStringProperty processedPriceStrProperty()
	{
		return processedPriceStr;
	}

	public String getResultValueStr()
	{
		return resultValueStr.get();
	}

	public ReadOnlyStringProperty resultValueStrProperty()
	{
		return resultValueStr;
	}

	public String getResultText()
	{
		return resultText.get();
	}

	public ReadOnlyStringProperty resultTextProperty()
	{
		return resultText;
	}
}
