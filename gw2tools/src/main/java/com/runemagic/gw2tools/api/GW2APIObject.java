package com.runemagic.gw2tools.api;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyProperty;

public interface GW2APIObject
{

	/**
	 * Sets the API source to be used by this object and its children.
	 * @param source
	 */
	//void setAPISource(GW2APISource source);

	/**
	 * Returns the API source used by this object and its children.
	 * @return
	 */
	//GW2APISource getAPISource();

	ReadOnlyProperty<?> property(String name);

	Object get(String name);

	/**
	 * Forces the object ro re-fetch data from the GW2 API Source.<br>
	 * This method does nothing if there is already an update running.
	 */
	void update();

	/**
	 * Returns true if there is an update running, false otherwise.
	 * @return
	 */
	boolean isUpdating();

	/**
	 * Returns the update progress.
	 * @return
	 */
	float getUpdateProgress();

	ReadOnlyFloatProperty updateProgressProperty();

	ReadOnlyBooleanProperty updatingProperty();
}
