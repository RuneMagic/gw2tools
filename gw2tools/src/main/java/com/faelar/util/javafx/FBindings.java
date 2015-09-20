package com.faelar.util.javafx;

import java.lang.ref.WeakReference;
import java.util.List;

import javafx.beans.WeakListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import com.faelar.util.collections.ContentAdapter;
import com.faelar.util.collections.FCollections;

public final class FBindings
{
	private FBindings() {}


	public static <F,T> void bindContent(List<T> list1, ObservableList<F> list2, ContentAdapter<T,F> adapter)
	{
		Bindings.bindContent(FCollections.convertList(list1, adapter), list2);
	}
	//TODO bindContentBidirectional

	@SuppressWarnings("unchecked")
	public static <F,T> void bindContentBidirectional(ObservableList<T> list1, ObservableList<F> list2, ContentAdapter<F,T> adapter)
	{
		checkParameters(list1, list2);
		ListContentAdapterBinding<F,T> localListContentBinding = new ListContentAdapterBinding<F,T>(list2, list1, adapter);
		list1.setAll(FCollections.convertList(list2, adapter));
		list1.addListener(localListContentBinding);
		list2.addListener(localListContentBinding);
	}

	private static void checkParameters(Object obj1, Object obj2)
	{
		if (obj1 == null || obj2 == null) throw new NullPointerException("Both parameters must be specified.");
		if (obj1 == obj2) throw new IllegalArgumentException("Cannot bind object to itself");
	}

	@SuppressWarnings("rawtypes")
	private static class ListContentAdapterBinding<F,T> implements ListChangeListener, WeakListener
	{
		private final WeakReference<ObservableList<F>> listRef1;
		private final WeakReference<ObservableList<T>> listRef2;
		private final ContentAdapter<F,T> adapter;
		private boolean updating = false;

		public ListContentAdapterBinding(ObservableList<F> list1, ObservableList<T> list2, ContentAdapter<F,T> adapter)
		{
			this.listRef1 = new WeakReference<ObservableList<F>>(list1);
			this.listRef2 = new WeakReference<ObservableList<T>>(list2);
			this.adapter=adapter;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onChanged(Change change)
		{
			if (!this.updating)
			{
				ObservableList list1 = this.listRef1.get();
				ObservableList list2 = this.listRef2.get();
				if ((list1 == null) || (list2 == null))
				{
					if (list1 != null) {
						list1.removeListener(this);
					}
					if (list2 != null) {
						list2.removeListener(this);
					}
				}
				else
				{
					try
					{
						this.updating = true;
						ObservableList list;
						ContentAdapter ca;
						if (change.getList()==list1)
						{
							list=list2;
							ca=adapter;
						}
						else
						{
							list=list1;
							ca=adapter.reverse();
						}
						while (change.next()) {
							if (change.wasPermutated())
							{
								list.remove(change.getFrom(), change.getTo());//TODO consider using a permanent list adapter instead of creating them on the fly
								list.addAll(change.getFrom(), FCollections.convertList(change.getList().subList(change.getFrom(), change.getTo()),ca));
							}
							else
							{
								if (change.wasRemoved()) {
									list.remove(change.getFrom(), change.getFrom() + change.getRemovedSize());
								}
								if (change.wasAdded()) {
									list.addAll(change.getFrom(),  FCollections.convertList(change.getAddedSubList(),ca));
								}
							}
						}
					}
					finally
					{
						this.updating = false;
					}
				}
			}
		}

		@Override
		public boolean wasGarbageCollected()
		{
			return (this.listRef1.get() == null) || (this.listRef2.get() == null);
		}

		@Override
		public int hashCode()
		{
			ObservableList localObservableList1 = this.listRef1.get();
			ObservableList localObservableList2 = this.listRef2.get();
			int i = localObservableList1 == null ? 0 : localObservableList1.hashCode();
			int j = localObservableList2 == null ? 0 : localObservableList2.hashCode();
			return i * j;
		}

		@Override
		public boolean equals(Object paramObject)
		{
			if (this == paramObject) {
				return true;
			}
			Object localObject1 = this.listRef1.get();
			Object localObject2 = this.listRef2.get();
			if ((localObject1 == null) || (localObject2 == null)) {
				return false;
			}
			if ((paramObject instanceof ListContentAdapterBinding))
			{
				ListContentAdapterBinding localListContentBinding = (ListContentAdapterBinding)paramObject;
				Object localObject3 = localListContentBinding.listRef1.get();
				Object localObject4 = localListContentBinding.listRef2.get();
				if ((localObject3 == null) || (localObject4 == null)) {
					return false;
				}
				if ((localObject1 == localObject3) && (localObject2 == localObject4)) {
					return true;
				}
				if ((localObject1 == localObject4) && (localObject2 == localObject3)) {
					return true;
				}
			}
			return false;
		}
	}
}
