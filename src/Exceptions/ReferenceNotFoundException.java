/*
 * Copyright (C) 2017 emnga
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package Exceptions;

import java.util.Arrays;

/**
 *
 * @author emnga
 */
public class ReferenceNotFoundException extends RuntimeException{
    
    private Object source;
    private Object[] references;
    
    public ReferenceNotFoundException() {
    }
    
    public ReferenceNotFoundException(String str){
        super(str);
    }
    
    public ReferenceNotFoundException(Object source, Object... references) {
        super("References \""+Arrays.toString(references)+"\" not found in \""+source.toString()+"\"");
        this.source=source;
        this.references=references;
    }

    public Object getSource() {
        return source;
    }

    public Object[] getReferences() {
        return references;
    }
    
    
    
}
