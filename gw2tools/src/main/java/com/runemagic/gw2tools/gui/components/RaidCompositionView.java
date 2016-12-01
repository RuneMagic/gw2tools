package com.runemagic.gw2tools.gui.components;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import com.runemagic.gw2tools.raid.RaidComposition;
import com.runemagic.gw2tools.raid.RaidSlot;

public class RaidCompositionView extends BorderPane
{
	private final VBox slotList;

	private final ObjectProperty<RaidComposition> comp=new SimpleObjectProperty<RaidComposition>()
	{
		@Override protected void invalidated()
		{
			RaidComposition comp=get();
			List<Node> nodes=new ArrayList<>();
			for (RaidSlot slot:comp.getOrderedSlots())
			{
				nodes.add(new RaidSlotView(slot));
			}
			slotList.getChildren().setAll(nodes);
		}
	};

	public RaidCompositionView()
	{
		slotList=new VBox();
	}

	public RaidCompositionView(RaidComposition comp)
	{
		slotList=new VBox();
		setComposition(comp);
	}

	public RaidComposition getComposition()
	{
		return comp.get();
	}

	public ObjectProperty<RaidComposition> compositionProperty()
	{
		return comp;
	}

	public void setComposition(RaidComposition comp)
	{
		this.comp.set(comp);
	}
}
