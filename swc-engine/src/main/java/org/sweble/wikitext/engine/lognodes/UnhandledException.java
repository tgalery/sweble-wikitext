/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.lognodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class UnhandledException
		extends
			LogLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public UnhandledException()
	{
	}
	
	public UnhandledException(Throwable exception, String dump)
	{
		setException(exception);
		setDump(dump);
	}
	
	// =========================================================================
	// Properties
	
	private Throwable exception;
	
	public final Throwable getException()
	{
		return this.exception;
	}
	
	public final Throwable setException(Throwable exception)
	{
		Throwable old = this.exception;
		this.exception = exception;
		return old;
	}
	
	private String dump;
	
	public final String getDump()
	{
		return this.dump;
	}
	
	public final String setDump(String dump)
	{
		String old = this.dump;
		this.dump = dump;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 2;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "exception";
					case 1:
						return "dump";
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index)
				{
					case 0:
						return UnhandledException.this.getException();
					case 1:
						return UnhandledException.this.getDump();
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index)
				{
					case 0:
						return UnhandledException.this.setException((Throwable) value);
					case 1:
						return UnhandledException.this.setDump((String) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
