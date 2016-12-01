package com.runemagic.gw2tools.gui.components;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import com.runemagic.gw2tools.raid.RaidBoss;
import com.runemagic.gw2tools.raid.RaidSession;

public class RaidSessionView extends BorderPane
{
	private final HBox compList;

	private final ObjectProperty<RaidSession> session=new SimpleObjectProperty<RaidSession>()
	{
		@Override protected void invalidated()
		{
			RaidSession session=get();
			List<Node> nodes=new ArrayList<>();
			for (RaidBoss boss:session.getBosses())
			{
				nodes.add(new RaidCompositionView(session.getComposition(boss)));
			}
			compList.getChildren().setAll(nodes);
		}
	};

	public RaidSessionView()
	{
		compList=new HBox();
	}
}
