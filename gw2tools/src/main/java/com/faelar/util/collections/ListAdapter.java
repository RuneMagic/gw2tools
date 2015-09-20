package com.faelar.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

//TODO RandomAccess
public class ListAdapter<F,T> implements List<T>
{

	private final List<F> list;
	private final ContentAdapter<F,T> adapter;

	public ListAdapter(List<F> list, ContentAdapter<F,T> adapter)
	{
		this.list=list;
		this.adapter=adapter;
	}

	private F from(T e)
	{
		return adapter.convertFrom(e);
	}

	private T to(F t)
	{
		return adapter.convertTo(t);
	}

	private Collection<F> from(Collection<? extends T> e)//TODO more efficient method
	{
		Collection<F> ret=new LinkedList<F>();
		for (T loop:e) ret.add(from(loop));
		return ret;
	}

	@Override
	public boolean add(T e)
	{
		return list.add(from(e));
	}

	@Override
	public void add(int index, T element)
	{
		list.add(index, from(element));
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		return list.addAll(from(c));
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return list.addAll(index, from(c));
	}

	@Override
	public void clear()
	{
		list.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o)
	{
		return list.contains(from((T) o));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return list.containsAll(from((Collection<? extends T>)c));
	}

	@Override
	public void forEach(Consumer<? super T> action)
	{
		list.forEach((t)->action.accept(to(t)));
	}

	@Override
	public T get(int index)
	{
		return to(list.get(index));
	}

	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(Object o)
	{
		return list.indexOf(from((T) o));
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return new IteratorAdapter(list.iterator());
	}

	@SuppressWarnings("unchecked")
	@Override
	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(from((T) o));
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return new ListIteratorAdapter(list.listIterator());
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return new ListIteratorAdapter(list.listIterator(index));
	}

	@Override
	public T remove(int index)
	{
		return to(list.remove(index));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o)
	{
		return list.remove(from((T) o));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> c)
	{
		return list.removeAll(from((Collection<T>) c));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return list.retainAll(from((Collection<T>) c));
	}

	@Override
	public T set(int index, T element)
	{
		return to(list.set(index, from(element)));
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return new ListAdapter<F,T>(list.subList(fromIndex, toIndex),adapter);//TODO RandomAccess interface
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] toArray()
	{
		Object[] ret=new Object[list.size()];
		Object[] arr=list.toArray();
		for (int i=0;i<ret.length;i++) ret[i]=to((F) arr[i]);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K[] toArray(K[] a)
	{
		F[] tArr=list.toArray((F[]) new Object[a.length]);
		K[] ret=(K[]) new Object[a.length];
		for (int i=0;i<ret.length;i++) ret[i]=(K) to(tArr[i]);
		return ret;
	}

	private class IteratorAdapter implements Iterator<T>
	{
		private final Iterator<F> iter;

		private IteratorAdapter(Iterator<F> iter)
		{
			this.iter=iter;
		}

		@Override
		public boolean hasNext()
		{
			return iter.hasNext();
		}

		@Override
		public T next()
		{
			return to(iter.next());
		}
	}

	private class ListIteratorAdapter implements ListIterator<T>
	{
		private final ListIterator<F> iter;

		private ListIteratorAdapter(ListIterator<F> iter)
		{
			this.iter=iter;
		}

		@Override
		public boolean hasNext()
		{
			return iter.hasNext();
		}

		@Override
		public T next()
		{
			return to(iter.next());
		}

		@Override
		public boolean hasPrevious()
		{
			return iter.hasPrevious();
		}

		@Override
		public T previous()
		{
			return to(iter.previous());
		}

		@Override
		public int nextIndex()
		{
			return iter.nextIndex();
		}

		@Override
		public int previousIndex()
		{
			return iter.previousIndex();
		}

		@Override
		public void remove()
		{
			iter.remove();
		}

		@Override
		public void set(T e)
		{
			iter.set(from(e));
		}

		@Override
		public void add(T e)
		{
			iter.add(from(e));
		}

	}
}
