package com.faelar.util.collections;


public interface ContentAdapter<F,T>
{
	public boolean equals(F o1, T o2);

	public T convertTo(F obj);

	public F convertFrom(T obj);

	public default ContentAdapter<T,F> reverse()
	{
		return new ContentAdapter<T,F>(){
			@Override
			public boolean equals(T o1, F o2)
			{
				return ContentAdapter.this.equals(o2,o1);
			}

			@Override
			public F convertTo(T obj)
			{
				return ContentAdapter.this.convertFrom(obj);
			}

			@Override
			public T convertFrom(F obj)
			{
				return ContentAdapter.this.convertTo(obj);
			}

			@Override
			public ContentAdapter<F,T> reverse()
			{
				return ContentAdapter.this;
			}
		};
	}
}
