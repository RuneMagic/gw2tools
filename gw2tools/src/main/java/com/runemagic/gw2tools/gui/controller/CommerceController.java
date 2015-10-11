package com.runemagic.gw2tools.gui.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.runemagic.gw2tools.api.items.MaterialPair;
import com.runemagic.gw2tools.commerce.MaterialProcessingCalculator;
import com.runemagic.gw2tools.commerce.TPAction;

public class CommerceController
{

	@FXML
	private Label lblRaw;

	@FXML
	private TextField txtProcessedPrice;

	@FXML
	private Label lblProcessed;

	@FXML
	private CheckBox chkRawBuy;

	@FXML
	private ComboBox<TPAction> cbAction;

	@FXML
	private ComboBox<MaterialPair> cbSelectMaterial;

	@FXML
	private TextField txtProcessingValue;

	@FXML
	private TextField txtResult;

	@FXML
	private TextField txtRawPrice;

	@FXML
	private CheckBox chkProcessedBuy;

	private MaterialProcessingCalculator calc=new MaterialProcessingCalculator();

	public void initialize()
	{
		cbSelectMaterial.setItems(calc.getMaterialPairs());
		cbAction.setItems(FXCollections.observableArrayList(TPAction.values()));

		cbSelectMaterial.valueProperty().bindBidirectional(calc.selectedProperty());
		cbAction.valueProperty().bindBidirectional(calc.actionProperty());
		chkRawBuy.selectedProperty().bindBidirectional(calc.rawBuyProperty());
		chkProcessedBuy.selectedProperty().bindBidirectional(calc.processedBuyProperty());

		txtRawPrice.textProperty().bind(calc.rawPriceStrProperty());
		txtProcessedPrice.textProperty().bind(calc.processedPriceStrProperty());
		txtProcessingValue.textProperty().bind(calc.resultValueStrProperty());
		txtResult.textProperty().bind(calc.resultTextProperty());

		lblRaw.textProperty().bind(Bindings.createStringBinding(() -> {
			if (calc.getSelected()==null) return "Raw material price:";
			return calc.getSelected().getRawMaterial().getName() + " price:";
		}, calc.selectedProperty()));
		lblProcessed.textProperty().bind(Bindings.createStringBinding(()->{
			if (calc.getSelected()==null) return "Processed material price:";
			return calc.getSelected().getProcessedMaterial().getName()+" price:";
		}, calc.selectedProperty()));
	}

	@FXML
	void updateItemData(ActionEvent event)
	{
		calc.update();
	}
}
